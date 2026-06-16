package cs.youtrade.telegram.buttons.sender.doc;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseDocMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendDocument, EditMessageMedia> {
    public BaseDocMessageSender(
            ISenderService sender
    ) {
        super(sender);
    }

    @Override
    public void sendEdit(
            TelegramClient bot,
            USER user,
            EditMessageMedia edit,
            MessageProcessor<USER> processor
    ) {
        long chatId = user.getChatId();
        if (edit == null)
            processor.processError(bot, user);
        else
            sender.sendMessage(bot, chatId, edit, processor::processMessage);
    }

    @Override
    public void sendMessage(
            TelegramClient bot,
            USER user,
            SendDocument mes,
            MessageProcessor<USER> processor
    ) {
        long chatId = user.getChatId();
        if (mes == null)
            processor.processError(bot, user);
        else
            sender.sendMessage(bot, chatId, mes, processor::processMessage);
    }
}
