package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class BaseMessageSender<USER extends AbstractUserData, MESSAGE, EDIT>
        implements IMessageSender<USER, MESSAGE, EDIT> {
    protected final ISenderService sender;

    public void sendEdit(
            TelegramClient bot,
            USER user,
            Supplier<EDIT> editSupplier,
            MessageProcessor<USER> processor
    ) {
        var editInfo = createEdit(bot, user, editSupplier, processor);
        send(editInfo);
    }

    public void sendMessage(
            TelegramClient bot,
            USER user,
            Supplier<MESSAGE> messageSupplier,
            MessageProcessor<USER> processor
    ) {
        var messageInfo = createMessage(bot, user, messageSupplier, processor);
        send(messageInfo);
    }

    public void send(
            MessageInfoDto messageInfo
    ) {
        sender.sendMessage(messageInfo);
    }

    @Override
    public void sendTextMes(
            TelegramClient bot,
            USER user,
            String text
    ) {
        sendTextMes(bot, user, text, data ->
                user.setLastMessageId(data.getMessageId()));
    }

    @Override
    public void sendTextMes(
            TelegramClient bot,
            USER user,
            String text,
            Consumer<MessageSentData> onMessage
    ) {
        sender.sendMessage(bot, user.getChatId(), text, onMessage);
    }

    @Override
    public void replyCallback(
            TelegramClient bot,
            USER userData,
            Update update,
            Consumer<MessageSentData> onMessage
    ) {
        if (!update.hasCallbackQuery())
            return;

        var messageInfo = MessageInfoDto.ack(bot, userData.getChatId(), () -> generateAck(update), onMessage);
        sender.sendMessage(messageInfo);
    }

    public void deleteMes(
            TelegramClient bot,
            USER user,
            Supplier<Integer> messageIdSupplier,
            Consumer<MessageSentData> onMessage
    ) {
        sender.deleteMes(bot, user.getChatId(), messageIdSupplier, onMessage);
    }

    // --- Assistive methods
    private AnswerCallbackQuery generateAck(
            Update update
    ) {
        String callbackId = update.getCallbackQuery().getId();
        return AnswerCallbackQuery
                .builder()
                .callbackQueryId(callbackId)
                .build();
    }

    public abstract MessageInfoDto createEdit(TelegramClient bot, USER user, Supplier<EDIT> editSupplier, MessageProcessor<USER> processor);

    public abstract MessageInfoDto createMessage(TelegramClient bot, USER user, Supplier<MESSAGE> mesSupplier, MessageProcessor<USER> processor);
}
