package cs.youtrade.telegram.buttons.sender.invoice;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseInvoiceMessageSender<USER extends AbstractUserData> extends BaseMessageSender<USER, SendInvoice> {
    public BaseInvoiceMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendInvoice mes) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, chatId);
        else
            sender.sendMessage(bot, chatId, mes);
    }
}
