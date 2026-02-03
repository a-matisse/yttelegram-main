package cs.youtrade.telegram.prototype.text;

import cs.youtrade.telegram.buttons.IMenuEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TerminalMenu implements IMenuEnum {
    RETURN("Return", 0);

    private final String buttonName;
    private final int rowNum;
}
