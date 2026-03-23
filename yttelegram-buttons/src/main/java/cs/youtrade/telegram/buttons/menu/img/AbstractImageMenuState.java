package cs.youtrade.telegram.buttons.menu.img;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.menu.AbstractMenuState;
import cs.youtrade.telegram.buttons.sender.img.BaseImageMessageSender;
import cs.youtrade.telegram.buttons.util.NoContentException;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;

public abstract class AbstractImageMenuState<USER extends AbstractUserData, MENU_TYPE extends IMenuEnum, STATE extends Enum<STATE>>
        extends AbstractMenuState<USER, MENU_TYPE, STATE, SendPhoto, EditMessageMedia> {
    public AbstractImageMenuState(
            BaseImageMessageSender<USER> sender
    ) {
        super(sender);
    }

    @Override
    public SendPhoto buildMessage(TelegramClient bot, Update update, USER user) {
        // Initializing the SendPhoto builder for given user
        SendPhoto.SendPhotoBuilder<?, ?> builder = SendPhoto
                .builder()
                .chatId(user.getChatId());
        // Trying to get picture from method
        var picture = getPicture(bot, user);
        if (picture == null) {
            // If not - throwing exception (cannot create SendPhoto w/o photo)
            sendDefErrMes(bot, user);
            throw new NoContentException("No picture found for " + user.getChatId());
        }
        // Setting the picture for message
        builder.photo(new InputFile(picture));
        // Trying to create an answer text
        String ans = "";
        try {
            String header = getHeaderText(bot, user);
            if (header != null)
                ans = header;
        } catch (Exception ignored) {
        }
        // Setting the caption if it is not empty
        if (!ans.isEmpty())
            builder.caption(ans);
        // finalizing menu build
        return builder
                .replyMarkup(buildMarkup(user))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public EditMessageMedia buildEdit(TelegramClient bot, USER user) {
        // Initializing the EditMessageMediaBuilder
        EditMessageMedia.EditMessageMediaBuilder<?, ?> builder = EditMessageMedia
                .builder()
                .chatId(user.getChatId())
                .messageId(user.getLastMessageId());
        // Trying to get picture from method
        var picture = getPicture(bot, user);
        if (picture == null) {
            sendDefErrMes(bot, user);
            throw new NoContentException("No picture found for " + user.getChatId());
        }
        // Working only through inputMediaBuilder
        var inputMediaBuilder = InputMediaPhoto
                .builder()
                .media(picture, picture.getName());
        // Trying to create an answer text
        String ans = "";
        try {
            String header = getHeaderText(bot, user);
            if (header != null && !header.isEmpty())
                ans = header;
        } catch (Exception ignored) {
        }
        // Setting the caption if exists
        if (!ans.isEmpty()) {
            inputMediaBuilder.caption(ans);
            inputMediaBuilder.parseMode(ParseMode.HTML);
        }
        // Setting the new replyMarkup
        builder.replyMarkup(buildMarkup(user));
        // Completing the media build
        var inputMedia = inputMediaBuilder.build();
        builder.media(inputMedia);
        // Completing the overall build
        return builder.build();
    }

    @Override
    public Class<EditMessageMedia> supportedEdit() {
        return EditMessageMedia.class;
    }

    public abstract File getPicture(TelegramClient bot, USER user);
}
