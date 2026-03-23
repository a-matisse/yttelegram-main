package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;

@Data
public class MessageInfoDto {
    private final MessageType messageType;
    private final TelegramClient bot;
    private final long chatId;
    private final Consumer<MessageSentData> onMessage;
    // Send types
    private SendMessage message;
    private SendPhoto photo;
    private SendDocument doc;
    private SendInvoice invoice;
    // Edit types
    private EditMessageMedia editMedia;
    private EditMessageText editText;
    private EditMessageReplyMarkup editMarkup;
    // Other types
    private AnswerCallbackQuery ack;
    private AnswerPreCheckoutQuery apcq;
    private DeleteMessage delete;

    public MessageInfoDto(
            TelegramClient bot,
            SendMessage message,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.message = message;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.TEXT;
    }

    public MessageInfoDto(
            TelegramClient bot,
            SendPhoto photo,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.photo = photo;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.PHOTO;
    }

    public MessageInfoDto(
            TelegramClient bot,
            SendDocument doc,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.doc = doc;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.DOCUMENT;
    }

    public MessageInfoDto(
            TelegramClient bot,
            SendInvoice invoice,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.invoice = invoice;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.INVOICE;
    }

    public MessageInfoDto(
            TelegramClient bot,
            EditMessageText edit,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.editText = edit;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.EDIT_TEXT;
    }

    public MessageInfoDto(
            TelegramClient bot,
            EditMessageMedia edit,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.editMedia = edit;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.EDIT_MEDIA;
    }

    public MessageInfoDto(
            TelegramClient bot,
            EditMessageReplyMarkup edit,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.editMarkup = edit;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.EDIT_MARKUP;
    }

    public MessageInfoDto(
            TelegramClient bot,
            AnswerCallbackQuery ack,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.ack = ack;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.ANSWER_CALLBACK;
    }

    public MessageInfoDto(
            TelegramClient bot,
            AnswerPreCheckoutQuery apcq,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.apcq = apcq;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.ANSWER_PRE_CHECKOUT;
    }

    public MessageInfoDto(
            TelegramClient bot,
            DeleteMessage delete,
            long chatId,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.delete = delete;
        this.chatId = chatId;
        this.onMessage = onMessage;
        this.messageType = MessageType.DELETE;
    }
}
