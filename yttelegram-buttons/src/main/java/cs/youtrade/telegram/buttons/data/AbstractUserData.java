package cs.youtrade.telegram.buttons.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public abstract class AbstractUserData {
    private final Long chatId;
    @Setter
    private Class<?> supportedEdit;
    @Setter
    private long lastMessageId = -1L;

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
