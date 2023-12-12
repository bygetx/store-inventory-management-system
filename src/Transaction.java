package src;
import java.time.LocalDateTime;

public class Transaction {

    private LocalDateTime currentDate;
    private double totalPrice;
    private int quantity;
    private int productId;
    private int userId;
    private double price;

    public Transaction(int userId, int productId, int quantity) {
        this.currentDate = LocalDateTime.now();
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = Utils.getPriceByProductId(productId);
        this.totalPrice = this.price * this.quantity;
        Utils.saveData_Transactions(this.currentDate, this.totalPrice, this.quantity, this.productId, this.userId, this.price);
    }
}
