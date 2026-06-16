package cs.youtrade.telegram.buttons.sender.img;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.sender.MessageInfoDto;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Supplier;

public abstract class BaseImageMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendPhoto, EditMessageMedia> {
    public BaseImageMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public MessageInfoDto createEdit(TelegramClient bot, USER user, Supplier<EditMessageMedia> editSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.editMedia(bot, user.getChatId(), editSupplier, processor::processMessage);
    }

    @Override
    public MessageInfoDto createMessage(TelegramClient bot, USER user, Supplier<SendPhoto> mesSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.photo(bot, user.getChatId(), mesSupplier, processor::processMessage);
    }
}
