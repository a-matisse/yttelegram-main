package cs.youtrade.telegram.buttons.sender.text;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseTextMessageSender<USER extends AbstractUserData> extends BaseMessageSender<USER, SendMessage> {
    public BaseTextMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendMessage mes) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, chatId);
        else
            sender.sendMessage(bot, chatId, mes);
    }
}
