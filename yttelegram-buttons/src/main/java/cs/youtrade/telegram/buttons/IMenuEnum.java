package cs.youtrade.telegram.buttons;

public interface IMenuEnum {
    String getButtonName();

    String getOptionName();

    int getRowNum();

    default boolean isPay() {
        return false;
    }
}
