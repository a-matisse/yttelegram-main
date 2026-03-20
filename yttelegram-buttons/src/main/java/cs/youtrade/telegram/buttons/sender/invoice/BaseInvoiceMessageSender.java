package cs.youtrade.telegram.buttons.sender.invoice;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

public abstract class BaseInvoiceMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendInvoice, EditMessageMedia> {
    public BaseInvoiceMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, USER user, SendInvoice mes, Consumer<MessageSentData> onMessage) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, user);
        else
            sender.sendMessage(bot, chatId, mes, onMessage);
    }
}
