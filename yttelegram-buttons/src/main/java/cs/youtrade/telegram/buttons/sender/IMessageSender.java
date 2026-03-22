package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.MessageProcessor;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

public interface IMessageSender<USER extends AbstractUserData, MESSAGE, EDIT> {
    /**
     * Message edit method
     * @param bot TelegramClient
     * @param user Inherited user
     * @param edit MessageEdit (or similar)
     * @param processor MessageProcessor
     */
    void sendEdit(TelegramClient bot, USER user, EDIT edit, MessageProcessor<USER> processor);

    /**
     *
     * @param bot TelegramClient
     * @param user Inherited user
     * @param mes SendMessage (or similar)
     * @param processor MessageProcessor
     */
    void sendMessage(TelegramClient bot, USER user, MESSAGE mes, MessageProcessor<USER> processor);

    void sendTextMes(TelegramClient bot, USER user, String text);

    void sendTextMes(TelegramClient bot, USER user, String text, Consumer<MessageSentData> onMessage);

    void replyCallback(TelegramClient bot, USER user, Update update, Consumer<MessageSentData> onMessage);

    void deleteMes(TelegramClient bot, USER user, int messageId, Consumer<MessageSentData> onMessage);
}
