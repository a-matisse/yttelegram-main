package cs.youtrade.telegram.buttons.menu.doc;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.TelegramFileDownloader;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.menu.AbstractMenuState;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import cs.youtrade.telegram.buttons.sender.doc.BaseDocMessageSender;
import cs.youtrade.telegram.buttons.util.NoContentException;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.io.IOException;

public abstract class AbstractDocMenuState<C, USER extends AbstractUserData, STATE extends Enum<STATE>, MENU_TYPE extends IMenuEnum>
        extends AbstractMenuState<USER, MENU_TYPE, STATE, SendDocument, EditMessageMedia> {
    public AbstractDocMenuState(
            BaseDocMessageSender<USER> sender
    ) {
        super(sender);
    }

    @Override
    public SendDocument buildMessage(TelegramClient bot, USER user) {
        C content = getContent(user);
        if (content == null)
            return null;

        InputFile doc = getHeaderDoc(user, content);
        if (doc == null)
            return null;

        String header = getHeader(bot, user, content);
        if (header == null)
            return null;

        return SendDocument
                .builder()
                .chatId(user.getChatId())
                .caption(header)
                .document(doc)
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
        // Trying to get document from method
        C content = getContent(user);
        if (content == null) {
            sendDefErrMes(bot, user);
            throw new NoContentException("No document found for " + user.getChatId());
        }
        InputFile doc = getHeaderDoc(user, content);
        if (doc == null) {
            sendDefErrMes(bot, user);
            throw new NoContentException("No inputFile found for " + user.getChatId());
        }
        // Working only through inputMediaBuilder
        var inputMediaBuilder = InputMediaDocument
                .builder()
                .media(doc.getNewMediaFile(), doc.getMediaName());
        // Trying to create an answer text
        String ans = "";
        try {
            String header = getHeaderDocText(user, content);
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

    private String getHeader(TelegramClient bot, USER user, C content) {
        try {
            String mainHeader = getHeaderText(bot, user);
            if (mainHeader == null)
                return null;

            String docHeader = getHeaderDocText(user, content);
            if (docHeader == null)
                return mainHeader;

            return String.format("""
                            %s
                            
                            %s
                            """,
                    mainHeader,
                    docHeader
            );
        } catch (Exception e) {
            return null;
        }
    }

    public File downloadFile(TelegramClient bot, Document doc) throws TelegramApiException, IOException {
        return TelegramFileDownloader.downloadFile(bot, doc);
    }

    public abstract C getContent(USER user);

    public abstract InputFile getHeaderDoc(USER user, C content);

    public String getHeaderDocText(USER user, C content) {
        return null;
    }
}
