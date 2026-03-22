package cs.youtrade.telegram.buttons.sender.invoice;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseInvoiceMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendInvoice, EditMessageMedia> {
    public BaseInvoiceMessageSender(ISenderService sender) {
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
            SendInvoice mes,
            MessageProcessor<USER> processor
    ) {
        long chatId = user.getChatId();
        if (mes == null)
            processor.processError(bot, user);
        else
            sender.sendMessage(bot, chatId, mes, processor::processMessage);
    }
}
