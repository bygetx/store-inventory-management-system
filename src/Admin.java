package src;

import java.util.Scanner;

public class Admin extends User {
    // anyone having username start with "ADMIN"
    private boolean isLoggedIn;
    public Admin(String username, String email) {
        super(username, email);
        this.isLoggedIn = true;
    }

    public void removeUser(int targetUserID) {
        if (this.isLoggedIn) {
            if (targetUserID < 0) {
                System.out.println("User ID not found");
            } else {
                String[] result =  Utils.getUsernameAndEmailByUserID(targetUserID);
                System.out.printf("\nare u sure u want to remove user [%s] :: [%s] ? [y/n]", result[0], result[1]);
                try (Scanner scanner = new Scanner(System.in)) {
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("y")) {
                        Utils.removeUser(targetUserID);
                    } else {System.out.println("User removal canceled.");}
                }        
            }
        }
    }

    public void setAdmin(int targetUserId) {
        if (this.isLoggedIn) {
            String[] result =  Utils.getUsernameAndEmailByUserID(targetUserId);
            System.out.printf("\nare u sure u want to promote this user to admin [%s] :: [%s] ? [y/n]", result[0], result[1]);
            try (Scanner scanner = new Scanner(System.in)) {
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("y")) {
                        Utils.promoteToAdmin(targetUserId);
                    }else {System.out.println("User promotion canceled.");}
                }
        }
    }
    public void add_product(String name, double price, String category, String description){
        new Product(name,price,category,description);
    }
    public void add_supplies(int productId , int quantity){
        InventoryManager.updateProductQuantityById(productId,quantity,true);
    }
    
}
