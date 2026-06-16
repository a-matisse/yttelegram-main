package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IMessageSender<USER extends AbstractUserData, MESSAGE, EDIT> {
    /**
     * Message edit method
     *
     * @param bot          TelegramClient
     * @param user         Inherited user
     * @param editSupplier MessageEdit supplier
     * @param processor    MessageProcessor
     */
    void sendEdit(TelegramClient bot, USER user, Supplier<EDIT> editSupplier, MessageProcessor<USER> processor);

    /**
     * @param bot       TelegramClient
     * @param user      Inherited user
     * @param mes       SendMessage supplier
     * @param processor MessageProcessor
     */
    void sendMessage(TelegramClient bot, USER user, Supplier<MESSAGE> mes, MessageProcessor<USER> processor);

    void sendTextMes(TelegramClient bot, USER user, String textSupplier);

    void sendTextMes(TelegramClient bot, USER user, String textSupplier, Consumer<MessageSentData> onMessage);

    void replyCallback(TelegramClient bot, USER user, Update update, Consumer<MessageSentData> onMessage);

    void deleteMes(TelegramClient bot, USER user, Supplier<Integer> messageIdSupplier, Consumer<MessageSentData> onMessage);
}
