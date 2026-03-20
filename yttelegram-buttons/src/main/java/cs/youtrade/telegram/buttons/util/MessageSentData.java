package cs.youtrade.telegram.buttons.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageSentData {
    private final String username;
    private final long chatId;
    private final long messageId;
    private final boolean isEdit;
}
