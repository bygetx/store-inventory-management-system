package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private User user;
    private Map<Integer, Integer> products;

    public Cart(User user) {
        this.user = user;
        this.products = new HashMap<>();
    }

    public void addToCart(int productId, int quantity) {
        if (products.containsKey(productId)) {
            int currentQuantity = products.get(productId);
            products.put(productId, currentQuantity + quantity);
        } else {
            products.put(productId, quantity);
        }
    }

    public void removeFromCart(int productId) {
        products.remove(productId);
    }

    public List<Transaction> pay() {
        List<Transaction> transactions = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Transaction transaction = new Transaction(this.user.getUserId(), productId, quantity);
            transactions.add(transaction);
            //update the inventory manager
        }
         
        return transactions;
    }

    // Get cart user and content
    public Map<Integer, Integer> getProducts() {
        return this.products;
    }

    public int getUserId() {
        return this.user.getUserId();
    }

    public void clearProducts() {// Clear the cart after creating transactions
        products.clear();
    }
}
