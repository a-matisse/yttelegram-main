package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface MessageProcessor<USER extends AbstractUserData> {
    default void processMessage(MessageSentData sentData) {
    }

    void processError(TelegramClient bot, USER user);
}
