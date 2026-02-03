package cs.youtrade.telegram.buttons.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractUserData {
    private final Long chatId;
}
