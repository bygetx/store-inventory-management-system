package src;

import java.time.LocalDateTime;

public class Tr {
    private int transactionId;
    private LocalDateTime transactionDate;
    private double totalPrice;
    private int quantity;
    private int productId;
    private int userId;
    private double unitPrice;

    public Tr(LocalDateTime transactionDate, int userId, int productId, int quantity, int transactionId,
            double unitPrice, double totalPrice) {
                this.transactionDate = transactionDate;             
                this.userId = userId;
                this.productId = productId;
                this.quantity = quantity;
                this.transactionId = transactionId;
                this.unitPrice = unitPrice;
                this.totalPrice = totalPrice;


    }

}