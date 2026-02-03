package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class AbstractTextState<USER extends AbstractUserData, STATE extends Enum<STATE>>
        extends AbstractDefState<USER, STATE, SendMessage> {
    public AbstractTextState(
            IMessageSender<USER, SendMessage> sender
    ) {
        super(sender);
    }

    @Override
    public SendMessage buildMessage(TelegramClient bot, USER e) {
        return SendMessage
                .builder()
                .chatId(e.getChatId())
                .text(getMessage(bot, e))
                .parseMode(ParseMode.HTML)
                .build();
    }

    protected abstract String getMessage(TelegramClient bot, USER user);
}
