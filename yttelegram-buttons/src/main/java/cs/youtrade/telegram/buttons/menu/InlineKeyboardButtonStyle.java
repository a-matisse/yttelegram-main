package cs.youtrade.telegram.buttons.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InlineKeyboardButtonStyle {
    DANGER("danger"),
    SUCCESS("success"),
    PRIMARY("primary");

    private final String style;
}
