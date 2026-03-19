package cs.youtrade.telegram.buttons.menu.invoice;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.menu.AbstractMenuState;
import cs.youtrade.telegram.buttons.sender.invoice.BaseInvoiceMessageSender;
import cs.youtrade.telegram.buttons.sender.invoice.InvoicePriceData;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public abstract class AbstractInvoiceState<USER extends AbstractUserData, MENU extends IMenuEnum, STATE extends Enum<STATE>>
        extends AbstractMenuState<USER, MENU, STATE, SendInvoice> {
    public AbstractInvoiceState(
            BaseInvoiceMessageSender<USER> sender
    ) {
        super(sender);
    }


    @Override
    public SendInvoice buildMessage(TelegramClient bot, USER e) {
        // Getting the prices for goods
        var labeledPrices = getInvoicePrices(bot, e)
                .stream()
                .map(pd -> new LabeledPrice(pd.getName(), pd.getPrice()))
                .toList();
        // Building the base invoice model
        var builder = SendInvoice
                .builder()
                .providerToken(getProviderToken(bot, e))
                .chatId(e.getChatId())
                .title(getInvoiceTitle(bot, e))
                .description(getInvoiceDescription(bot, e))
                .payload(getInvoicePayload(bot, e))
                .currency(getInvoiceCurrency(bot, e))
                .prices(labeledPrices);
        // Adding markup if not empty
        var markup = buildMarkup(e);
        if (markup != null && !markup.getKeyboard().isEmpty())
            builder.replyMarkup(markup);
        // Finishing the build
        return builder.build();
    }

    public abstract String getProviderToken(TelegramClient bot, USER e);

    public abstract String getInvoiceTitle(TelegramClient bot, USER e);

    public abstract String getInvoiceDescription(TelegramClient bot, USER e);

    public abstract String getInvoicePayload(TelegramClient bot, USER e);

    public abstract String getInvoiceCurrency(TelegramClient bot, USER e);

    public abstract List<InvoicePriceData> getInvoicePrices(TelegramClient bot, USER e);
}
