package cs.youtrade.telegram.buttons.sender.text;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

public abstract class BaseTextMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendMessage, EditMessageText> {
    public BaseTextMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendMessage mes, Consumer<MessageSentData> onMessage) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, user);
        else
            sender.sendMessage(bot, chatId, mes, onMessage);
    }
}
