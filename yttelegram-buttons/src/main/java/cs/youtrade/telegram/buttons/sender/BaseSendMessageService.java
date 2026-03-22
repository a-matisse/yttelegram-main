package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.TelegramFileDownloader;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Log4j2
public abstract class BaseSendMessageService implements ISenderService, Runnable {
    protected static final BlockingQueue<MessageInfoDto> messageQueue = new LinkedBlockingQueue<>();

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    private final Map<Long, Long> lastTimeSentMessages = new HashMap<>();

    private final Duration messageDelay;
    private final int maxMessageLength;

    public BaseSendMessageService(Duration messageDelay, int maxMessageLength) {
        this.messageDelay = messageDelay != null ? messageDelay : Duration.ofMillis(35);
        this.maxMessageLength = maxMessageLength;
    }

    public BaseSendMessageService(long messageDelayMillis, int maxMessageLength) {
        this(Duration.ofMillis(messageDelayMillis), maxMessageLength);
    }

    public BaseSendMessageService(long messagePeriod, TemporalUnit temporalUnit, int maxMessageLength) {
        this(Duration.of(messagePeriod, temporalUnit), maxMessageLength);
    }

    public BaseSendMessageService(int maxMessageLength) {
        this(Duration.ofMillis(35), maxMessageLength);
    }

    public BaseSendMessageService() {
        this(Duration.ofMillis(35), 4096);
    }

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
                case TEXT -> new MessageSentData(bot.execute(messageInfo.getMessage()));
                case DOCUMENT -> new MessageSentData(bot.execute(messageInfo.getDoc()));
                case PHOTO -> new MessageSentData(bot.execute(messageInfo.getPhoto()));
                case EDIT_TEXT -> new MessageSentData(bot.execute(messageInfo.getEditText()));
                case EDIT_MEDIA -> new MessageSentData(bot.execute(messageInfo.getEditMedia()));
                case EDIT_MARKUP -> new MessageSentData(bot.execute(messageInfo.getEditMarkup()));
                case ANSWER_CALLBACK -> new MessageSentData(bot.execute(messageInfo.getAck()));
                case DELETE -> new MessageSentData(bot.execute(messageInfo.getDelete()));
                case INVOICE -> new MessageSentData(bot.execute(messageInfo.getInvoice()));
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
    public void deleteMes(TelegramClient bot, Long chatId, int messageId, Consumer<MessageSentData> onMessage) {
        DeleteMessage delete = DeleteMessage
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        messageQueue.add(new MessageInfoDto(bot, delete, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, String text, Consumer<MessageSentData> onMessage) {
        SendMessage message;
        int startIndex = 0;

        do {
            int endIndex = Math.min(startIndex + maxMessageLength, text.length());
            String chunk = text.substring(startIndex, endIndex);
            message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(chunk)
                    .parseMode(ParseMode.HTML)
                    .build();

            messageQueue.add(new MessageInfoDto(bot, message, chatId, onMessage));
            startIndex = endIndex;
        } while (startIndex < text.length());
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, SendMessage message, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, message, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, SendDocument doc, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, doc, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, SendPhoto sendPhoto, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, sendPhoto, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, SendInvoice invoice, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, invoice, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, EditMessageText edit, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, edit, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, EditMessageMedia edit, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, edit, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, EditMessageReplyMarkup edit, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, edit, chatId, onMessage));
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, AnswerCallbackQuery ack, Consumer<MessageSentData> onMessage) {
        messageQueue.add(new MessageInfoDto(bot, ack, chatId, onMessage));
    }
}
