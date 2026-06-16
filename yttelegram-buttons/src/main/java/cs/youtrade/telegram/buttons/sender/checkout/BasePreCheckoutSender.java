package cs.youtrade.telegram.buttons.sender.checkout;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.sender.MessageInfoDto;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Supplier;

public abstract class BasePreCheckoutSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, AnswerPreCheckoutQuery, EditMessageText> {

    public BasePreCheckoutSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public MessageInfoDto createEdit(TelegramClient bot, USER user, Supplier<EditMessageText> editSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.editText(bot, user.getChatId(), editSupplier, processor::processMessage);
    }

    @Override
    public MessageInfoDto createMessage(TelegramClient bot, USER user, Supplier<AnswerPreCheckoutQuery> mesSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.apcq(bot, user.getChatId(), mesSupplier, processor::processMessage);
    }
}
