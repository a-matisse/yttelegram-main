package cs.youtrade.telegram.buttons;

public interface WebPagePreviewable {
    default boolean disableWebPagePreview() {
        return true;
    }
}
