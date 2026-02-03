package cs.youtrade.telegram.buttons.sender.doc;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseDocMessageSender<USER extends AbstractUserData> extends BaseMessageSender<USER, SendDocument> {
    public BaseDocMessageSender(
            ISenderService sender
    ) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendDocument mes) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, chatId);
        else
            sender.sendMessage(bot, chatId, mes);
    }
}
