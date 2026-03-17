package cs.youtrade.telegram.buttons.sender.img;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseImageMessageSender<USER extends AbstractUserData> extends BaseMessageSender<USER, SendPhoto> {
    public BaseImageMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendPhoto mes) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, chatId);
        else
            sender.sendMessage(bot, chatId, mes);
    }
}
