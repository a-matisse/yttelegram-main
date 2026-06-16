package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.TelegramFileDownloader;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import cs.youtrade.telegram.buttons.util.TelegramMessageEmptyException;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Log4j2
@Builder
public class BaseSendMessageService implements ISenderService, Runnable {
    @Builder.Default
    private final BlockingQueue<MessageInfoDto> messageQueue = new LinkedBlockingQueue<>();
    @Builder.Default
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    @Builder.Default
    private final Map<Long, Long> lastTimeSentMessages = new HashMap<>();
    @Builder.Default
    private final Duration messageDelay = Duration.ofMillis(35);
    @Builder.Default
    private final int maxMessageLength = 4096;

    @Override
    public void run() {
        long delayMillis = messageDelay.toMillis();
        scheduledThreadPoolExecutor.scheduleAtFixedRate(
                this::sendMessageFromQueue, 0,
                delayMillis, TimeUnit.MILLISECONDS
        );
    }

    private void sendMessageFromQueue() {
        long chatId = -1;
        long now = System.currentTimeMillis();
        try {
            // Getting the message from queue
            MessageInfoDto messageInfo = messageQueue.take();
            if (messageInfo.getMessage() == null)
                throw new TelegramMessageEmptyException("Message is empty");
            chatId = messageInfo.getChatId();
            long lastTime = lastTimeSentMessages.getOrDefault(chatId, 0L);
            // Checking the message send interval
            if (now - lastTime < 1020) {
                messageQueue.offer(messageInfo);
                return;
            }
            // Executing the message
            TelegramClient bot = messageInfo.getBot();
            MessageSentData data = switch (messageInfo.getMessageType()) {
                case TEXT -> sendText(bot, messageInfo);
                case DOCUMENT -> sendDoc(bot, messageInfo);
                case PHOTO -> sendPhoto(bot, messageInfo);
                case EDIT_TEXT -> sendEditText(bot, messageInfo);
                case EDIT_MEDIA -> sendEditMedia(bot, messageInfo);
                case EDIT_MARKUP -> sendEditMarkup(bot, messageInfo);
                case ANSWER_CALLBACK -> sendAck(bot, messageInfo);
                case ANSWER_PRE_CHECKOUT -> sendApcq(bot, messageInfo);
                case DELETE -> sendDelete(bot, messageInfo);
                case INVOICE -> sendInvoice(bot, messageInfo);
            };
            // Consuming if accessible and not null
            var onMessage = messageInfo.getOnMessage();
            if (data.isAccessible() && onMessage != null) {
                try {
                    messageInfo.getOnMessage().accept(data);
                } catch (Exception e) {
                    log.error("Couldn't invoke onMessage", e);
                }
            }
            // Putting the timestamp
            lastTimeSentMessages.put(chatId, now);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Поток Rate Limiter был прерван {}", e.getMessage());
        } catch (TelegramApiException e) {
            if (chatId != -1002332618563L)
                log.error("Ошибка при отправке сообщения по id={}: {}", chatId, e.getMessage(), e);
        } catch (TelegramMessageEmptyException e) {
            log.error(e);
        }
    }

    @Override
    public InputStream downloadFile(TelegramClient bot, Document doc) {
        try {
            return TelegramFileDownloader.downloadFileAsStream(bot, doc);
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException("Cannot download file from Telegram", e);
        }
    }

    @Override
    public SendMessage createMessage(Long chatId, String text) {
        if (text.length() > maxMessageLength)
            throw new RuntimeException(String.format("Too long message length (%s)", text.length()));

        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    @Override
    public void deleteMes(TelegramClient bot, Long chatId, Supplier<Integer> messageIdSupplier, Consumer<MessageSentData> onMessage) {
        // Creating the supplier
        Supplier<DeleteMessage> deleteSupplier = () -> generateDeleteMessage(chatId, messageIdSupplier.get());
        // Queueing the message
        messageQueue.add(MessageInfoDto.delete(bot, chatId, deleteSupplier, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, String text, Consumer<MessageSentData> onMessage) {
        SendMessage message;
        int startIndex = 0;
        do {
            int endIndex = Math.min(startIndex + maxMessageLength, text.length());
            String chunk = text.substring(startIndex, endIndex);
            // Creating the supplier
            Supplier<SendMessage> messageSupplier = () -> generateTextMessage(chatId, chunk);
            // Queueing the message
            messageQueue.add(MessageInfoDto.text(bot, chatId, messageSupplier, onMessage));
            startIndex = endIndex;
        } while (startIndex < text.length());
    }

    public void sendMessage(MessageInfoDto messageInfo) {
        messageQueue.add(messageInfo);
    }

    // --- Inner send methods
    private MessageSentData sendText(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        SendMessage mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendDoc(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        SendDocument mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendPhoto(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        SendPhoto mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendInvoice(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        SendInvoice mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendEditText(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        EditMessageText mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendEditMedia(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        EditMessageMedia mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }


    private MessageSentData sendEditMarkup(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        EditMessageReplyMarkup mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendAck(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        AnswerCallbackQuery mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendApcq(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        AnswerPreCheckoutQuery mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    private MessageSentData sendDelete(TelegramClient bot, MessageInfoDto info) throws TelegramApiException {
        DeleteMessage mes = info.getMessage();
        return new MessageSentData(bot.execute(mes));
    }

    // --- Assistive methods
    private SendMessage generateTextMessage(long chatId, String chunk) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(chunk)
                .parseMode(ParseMode.HTML)
                .build();
    }

    private DeleteMessage generateDeleteMessage(long chatId, int messageId) {
        return DeleteMessage
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }
}
