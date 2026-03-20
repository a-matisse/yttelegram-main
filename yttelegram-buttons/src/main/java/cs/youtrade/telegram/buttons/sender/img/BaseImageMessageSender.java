package cs.youtrade.telegram.buttons.sender.img;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

public abstract class BaseImageMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendPhoto, EditMessageMedia> {
    public BaseImageMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendEdit(TelegramClient bot, USER user, EditMessageMedia edit, Consumer<MessageSentData> onMessage) {
        long chatId = user.getChatId();
        if (edit == null)
            sendDefErrMes(bot, user);
        else
            sender.sendMessage(bot, chatId, edit, onMessage);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendPhoto mes, Consumer<MessageSentData> onMessage) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, user);
        else
            sender.sendMessage(bot, chatId, mes, onMessage);
    }
}
