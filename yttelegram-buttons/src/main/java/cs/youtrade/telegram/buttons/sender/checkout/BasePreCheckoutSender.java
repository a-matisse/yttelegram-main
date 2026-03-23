package cs.youtrade.telegram.buttons.sender.checkout;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BasePreCheckoutSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, AnswerPreCheckoutQuery, EditMessageText> {
    public BasePreCheckoutSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendEdit(
            TelegramClient bot,
            USER user,
            EditMessageText edit,
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
            AnswerPreCheckoutQuery mes,
            MessageProcessor<USER> processor
    ) {
        long chatId = user.getChatId();
        if (mes == null)
            processor.processError(bot, user);
        else
            sender.sendMessage(bot, chatId, mes, processor::processMessage);
    }
}
