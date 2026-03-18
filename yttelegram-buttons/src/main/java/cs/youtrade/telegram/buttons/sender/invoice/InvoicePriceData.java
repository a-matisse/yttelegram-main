package cs.youtrade.telegram.buttons.sender.invoice;

public interface InvoicePriceData {
    String getName();
    // Calculated from minimal currency amount
    Integer getPrice();
}
