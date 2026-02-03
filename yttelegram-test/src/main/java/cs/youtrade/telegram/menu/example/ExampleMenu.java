package cs.youtrade.telegram.menu.example;

import cs.youtrade.telegram.buttons.IMenuEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExampleMenu implements IMenuEnum {
    BUTTON_1("First button", 0),
    BUTTON_2("Second button", 1),
    BUTTON_3("Third button", 2);

    private final String buttonName;
    private final int rowNum;
}
