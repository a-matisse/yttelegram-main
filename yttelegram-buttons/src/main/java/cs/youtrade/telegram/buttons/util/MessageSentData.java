package cs.youtrade.telegram.buttons.util;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.Serializable;

@Getter
public class MessageSentData {
    private final String username;
    private final long chatId;
    private final int messageId;
    private final boolean isAccessible;

    public MessageSentData(Message message) {
        this.username = message.getFrom().getUserName();
        this.chatId = message.getChatId();
        this.messageId = message.getMessageId();
        this.isAccessible = true;
    }

    public MessageSentData(Serializable serializable) {
        this.username = null;
        this.chatId = 0;
        this.messageId = -1;
        this.isAccessible = false;
    }
}
