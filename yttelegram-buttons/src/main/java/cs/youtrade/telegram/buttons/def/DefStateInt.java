package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface DefStateInt<USER extends AbstractUserData, STATE extends Enum<STATE>, MESSAGE, EDIT> {
    /**
     * Returns that STATE of a command for state-machine
     */
    STATE supportedState();

    /**
     * Executes the command using the Update for USER data
     */
    STATE execute(TelegramClient bot, Update update, USER user);

    /**
     * The message that will be sent to user on STATE change
     */
    void executeOnState(TelegramClient bot, Update update, USER e);

    /**
     * Message creation method
     */
    MESSAGE buildMessage(TelegramClient bot, Update update, USER e);

    /**
     * Message edit creation method
     *
     * @param bot TelegramClient
     * @param e User in Telegram
     * @return (By default) null. If is changed - the message will be editable
     */
    default EDIT buildEdit(TelegramClient bot, USER e) {
        return null;
    }

    default Class<EDIT> supportedEdit() {
        return null;
    }
}
