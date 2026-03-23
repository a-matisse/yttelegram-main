package cs.youtrade.telegram.buttons.menu.checkout;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.AbstractDefState;
import cs.youtrade.telegram.buttons.sender.checkout.BasePreCheckoutSender;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class AbstractPreCheckoutState<USER extends AbstractUserData, STATE extends Enum<STATE>>
        extends AbstractDefState<USER, STATE, AnswerPreCheckoutQuery, EditMessageText> {
    public AbstractPreCheckoutState(
            BasePreCheckoutSender<USER> sender
    ) {
        super(sender);
    }

    @Override
    public AnswerPreCheckoutQuery buildMessage(TelegramClient bot, Update update, USER user) {
        if (!update.hasPreCheckoutQuery())
            return null;

        PreCheckoutQuery query = update.getPreCheckoutQuery();
        boolean ok = preCheckoutCheck(bot, update, user);
        if (ok) sendSuccess(bot, update, user);
        else sendFailed(bot, update, user);

        return AnswerPreCheckoutQuery
                .builder()
                .preCheckoutQueryId(query.getId())
                .ok(true)
                .build();
    }

    public void sendSuccess(TelegramClient bot, Update update, USER user) {
        sender.sendTextMes(bot, user, getSuccessText(bot, update, user));
    }

    public void sendFailed(TelegramClient bot, Update update, USER user) {
        sender.sendTextMes(bot, user, getFailedText(bot, update, user));
    }

    public abstract String getSuccessText(TelegramClient bot, Update update, USER user);

    public abstract String getFailedText(TelegramClient bot, Update update, USER user);

    public abstract boolean preCheckoutCheck(TelegramClient bot, Update update, USER user);
}
