package cs.youtrade.telegram.buttons.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public abstract class AbstractUserData {
    private final Long chatId;
    @Setter
    private Class<?> supportedEdit;
    @Setter
    private int lastMessageId = -1;
    @Setter
    private boolean updated = false;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUserData that = (AbstractUserData) o;
        return Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chatId);
    }
}
