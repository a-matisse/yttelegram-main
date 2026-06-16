package cs.youtrade.telegram.buttons.util;

public class TelegramMessageEmptyException extends RuntimeException {
    public TelegramMessageEmptyException(String message) {
        super(message);
    }
}
