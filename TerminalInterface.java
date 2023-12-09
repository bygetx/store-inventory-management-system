import src.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TerminalInterface {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static Cart userCart = null;
    public static List<Product> productList = Utils.getAllProducts();
    public static InventoryManager inventoryManager = new InventoryManager();
    public static int productId;    
    public static int productQuantity;


    public static void main(String[] args) {

        while (true) {
            showMainMenu();

            int choice = getUserChoice(1, 7);

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    viewProducts();
                    break;
                case 4:
                    addToCart();
                    break;
                case 5:
                    viewCart();
                    break;
                case 6:
                    checkout();
                    break;
                case 7:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== E-Commerce System ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. View Products");
        System.out.println("4. Add to Cart");
        System.out.println("5. View Cart");
        System.out.println("6. Checkout");
        System.out.println("7. Exit");
    }

    private static int getUserChoice(int min, int max) {
        int choice ;
        choice = 0;
        do {
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice < min || choice > max) {
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
            }

        } while (choice < min || choice > max);

        return choice;
    }


    private static void handleLogin() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        currentUser = Guest.login(username, email, password);
        if (currentUser != null) {
            System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
            userCart = new Cart(currentUser);
        } else {
            System.out.println("Login failed. Please check your email and password.");
        }
    }

    private static void handleRegister() {
        System.out.println("\n=== Register ===");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        currentUser = Guest.register(username, email, password);
        if (currentUser != null) {
            System.out.println("Registration successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Registration failed. Please choose a different username or check your email.");
        }
    }



   public static void viewProducts() {
        System.out.println("\n=== View Products ===");

        if (productList.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("Available Products:");
            for (Product product : productList) {
                System.out.println(product.getProductID() + ". " + product.getName() +
                        " - $" + product.getPrice() + " - " + product.getCategory());
            }
        }
    }

    public static void addToCart() {
        System.out.println("\n=== Add to Cart ===");
        viewProducts();

        // Assume user is logged in
        if (currentUser != null) {
            while(true){
                System.out.print("Enter the product ID to add to the cart: ");
                int productId = scanner.nextInt(); 
                System.out.print("Enter the quantity: ");
                int quantity = scanner.nextInt();
                boolean cond = false;
                for (Product product : productList){
                    if (productId == product.getProductID()){
                        cond = true;
                        break;
                    }
                }
                if (quantity > 0 && cond) {
                        userCart.addToCart(productId, quantity);
                        System.out.println("Product added to the cart successfully.");
                        break;
                    } else {
                        System.out.println("Invalid quantity or product Id. Please enter a valid number.");
                    }
                }
                }
        else {
            System.out.println("Please login to add products to the cart.");
        }
    }

    public static void viewCart() {
        System.out.println("\n=== View Cart ===");
        
        if (currentUser != null && userCart != null && (currentUser.getUserId() == userCart.getUserId())) {
            Map<Integer, Integer> cartContents = userCart.getProducts();

            if (cartContents.isEmpty()) {
                System.out.println("Your cart is empty.");
            } else {
                System.out.println("Cart Contents:");
                for (Map.Entry<Integer, Integer> entry : cartContents.entrySet()) {
                    int productId = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = Utils.getProductById(productId); //to do later ////////////////////////////

                    if (product != null) {
                        System.out.println(product.getName() + " - Quantity: " + quantity);
                    }
                }
            }
        } else {
            System.out.println("Please login to view the cart.");
        }
    }

    public static void checkout() {
        System.out.println("\n=== Checkout ===");

        if (currentUser != null && userCart != null) {
            List<Transaction> transactions = userCart.pay();

            if (transactions.isEmpty()) {
                System.out.println("No items in the cart to checkout.");
            } else {
                for (Map.Entry<Integer, Integer> product : userCart.getProducts().entrySet()) {
                    productId = product.getKey();
                    productQuantity = product.getValue();
                    InventoryManager.updateProductQuantityById(productId, productQuantity, false);}
                userCart.clearProducts();
                System.out.print("\n\nCheckout successful. Thank you for your purchase!");
            }
        } else {
            System.out.println("Please login and add items to the cart before checkout.");
        }
    }
}