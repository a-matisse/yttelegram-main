package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class BaseMessageSender<USER extends AbstractUserData, MESSAGE, EDIT>
        implements IMessageSender<USER, MESSAGE, EDIT> {
    protected final ISenderService sender;

    public void sendMessage(
            TelegramClient bot,
            USER user,
            MESSAGE mes
    ) {
        sendMessage(bot, user, mes, null);
    }

    @Override
    public void sendDefErrMes(
            TelegramClient bot,
            USER user
    ) {
        sender.sendMessage(bot, user.getChatId(), getDefaultErrorText(), data ->
                user.setLastMessageId(data.getMessageId()));
    }

    @Override
    public void sendTextMes(
            TelegramClient bot,
            USER user,
            String text
    ) {
        sender.sendMessage(bot, user.getChatId(), text, data ->
                user.setLastMessageId(data.getMessageId()));
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

        String callbackId = update.getCallbackQuery().getId();
        AnswerCallbackQuery ack = AnswerCallbackQuery
                .builder()
                .callbackQueryId(callbackId)
                .build();
        sender.sendMessage(bot, userData.getChatId(), ack, onMessage);
    }

    @Override
    public void deleteMes(
            TelegramClient bot,
            USER user,
            Long messageId,
            Consumer<MessageSentData> onMessage
    ) {
        sender.deleteMes(bot, user.getChatId(), messageId, onMessage);
    }

    public String getDefaultErrorText() {
        return "🚫 Server is not available. Try again in a couple of minutes...";
    }
}
