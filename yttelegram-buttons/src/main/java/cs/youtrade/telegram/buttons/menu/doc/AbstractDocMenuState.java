package cs.youtrade.telegram.buttons.menu.doc;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.TelegramFileDownloader;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.menu.AbstractMenuState;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.io.IOException;

public abstract class AbstractDocMenuState<C, USER extends AbstractUserData, STATE extends Enum<STATE>, MENU_TYPE extends IMenuEnum>
        extends AbstractMenuState<USER, MENU_TYPE, STATE, SendDocument> {
    public AbstractDocMenuState(
            IMessageSender<USER, SendDocument> sender
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
                .replyMarkup(buildMarkup())
                .parseMode(ParseMode.HTML)
                .build();
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
