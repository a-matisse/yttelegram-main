package cs.youtrade.telegram.buttons.sender.doc;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.MessageProcessor;
import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import cs.youtrade.telegram.buttons.sender.MessageInfoDto;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Supplier;

public abstract class BaseDocMessageSender<USER extends AbstractUserData>
        extends BaseMessageSender<USER, SendDocument, EditMessageMedia> {
    public BaseDocMessageSender(
            ISenderService sender
    ) {
        super(sender);
    }

    @Override
    public MessageInfoDto createEdit(TelegramClient bot, USER user, Supplier<EditMessageMedia> editSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.editMedia(bot, user.getChatId(), editSupplier, processor::processMessage);
    }

    @Override
    public MessageInfoDto createMessage(TelegramClient bot, USER user, Supplier<SendDocument> mesSupplier, MessageProcessor<USER> processor) {
        return MessageInfoDto.doc(bot, user.getChatId(), mesSupplier, processor::processMessage);
    }
}
