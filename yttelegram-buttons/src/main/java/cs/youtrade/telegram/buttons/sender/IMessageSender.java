package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

public interface IMessageSender<USER extends AbstractUserData, MESSAGE, EDIT> {
    void sendEdit(TelegramClient bot, USER user, EDIT edit, Consumer<MessageSentData> onMessage);

    /**
     * Message send method
     *
     * @param bot Telegram client
     * @param user The user with Telegram-chatID
     * @param mes Message that should be sent
     */
    void sendMessage(TelegramClient bot, USER user, MESSAGE mes, Consumer<MessageSentData> onMessage);

    void sendDefErrMes(TelegramClient bot, USER user);

    void sendTextMes(TelegramClient bot, USER user, String text);

    void replyCallback(TelegramClient bot, USER user, Update update, Consumer<MessageSentData> onMessage);

    void deleteMes(TelegramClient bot, USER user, int messageId, Consumer<MessageSentData> onMessage);
}
