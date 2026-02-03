package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class AbstractDocState<C, USER extends AbstractUserData, STATE extends Enum<STATE>>
        extends AbstractDefState<USER, STATE, SendDocument> {
    public AbstractDocState(
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

        String header = getHeader(user, content);
        if (header == null)
            return null;

        return SendDocument
                .builder()
                .chatId(user.getChatId())
                .caption(header)
                .document(doc)
                .parseMode(ParseMode.HTML)
                .build();
    }

    private String getHeader(USER user, C content) {
        String mainHeader = getHeaderText(user);
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
    }

    public abstract String getHeaderText(USER user);

    public abstract C getContent(USER user);

    public abstract InputFile getHeaderDoc(USER user, C content);

    public String getHeaderDocText(USER user, C content) {
        return null;
    }
}
