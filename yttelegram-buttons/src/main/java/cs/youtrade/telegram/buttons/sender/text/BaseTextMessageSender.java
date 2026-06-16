package cs.youtrade.telegram.buttons.sender.text;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.sender.MessageInfoDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Supplier;

public abstract class BaseTextMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendMessage, EditMessageText> {
    public BaseTextMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public MessageInfoDto createEdit(TelegramClient bot, USER user, Supplier<EditMessageText> editSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.editText(bot, user.getChatId(), editSupplier, processor::processMessage);
    }

    @Override
    public MessageInfoDto createMessage(TelegramClient bot, USER user, Supplier<SendMessage> mesSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.text(bot, user.getChatId(), mesSupplier, processor::processMessage);
    }
}
