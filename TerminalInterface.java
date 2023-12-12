import src.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TerminalInterface {
    private static final PDFont PDType1Font = null;
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static Cart userCart = null;
    public static List<Product> productList = Utils.getAllProducts();
    public static InventoryManager inventoryManager = new InventoryManager();
    public static int productId;    
    public static int productQuantity;
    public static LocalDateTime date_range =LocalDateTime.parse("2023-12-09T14:04:44.618655800") ;


    public static void main(String[] args) {

        while (true) {
            showMainMenu();

            int choice = getUserChoice(1, 9);

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
                    supply();
                    break;
                case 8:
                    generate_product_sales_report(date_range);
                    break;
                case 9:
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
        System.out.println("7. Supply");
        System.out.println("8. generate Sales Report");
        System.out.println("9. Exit");
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

    public static void supply(){
        System.out.println("\n=== Supply ===");
        if(currentUser != null && currentUser instanceof Admin){
            System.out.println("choose 1 to add a product , 2 to add product inventory");
            int check =Integer.parseInt(scanner.nextLine());
            switch (check){
                case 1:
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Product price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter Product category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter Product description: ");
                    String description = scanner.nextLine();
                    ((Admin) currentUser).add_product(name, price, category, description);
                    System.out.print("product added successfully");
                    break;
                case 2:
                    System.out.print("Enter Product Id: ");
                    int productId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Product Quantity to add: ");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    ((Admin) currentUser).add_supplies(productId, quantity);
                    System.out.print("Quantity added successfully");
                    break;

            }
        }else{System.out.println("please login as an Admin account");}

    }

    public static void generate_product_sales_report(LocalDateTime date_range){
        Object[][] transactions = Utils.readTransactionTable();
        productList = Utils.getAllProducts();
        Map<Integer, Integer> productSales = new HashMap<>(); // ProductId -> totalQuantitySold
        Map<Integer, Double> productRevenue = new HashMap<>();
        List<Tr> filteredTransactions = new ArrayList<>();
        for (Object[] transaction : transactions) {
            LocalDateTime transactionDate = LocalDateTime.parse((String) transaction[1]);
            if (transactionDate.isAfter(date_range)) {
                int transactionId = (int) transaction[0];
                double totalPrice = (double) transaction[2];
                int quantity = (int) transaction[3];
                int productId = (int) transaction[4];
                int userId = (int) transaction[5];      
                double unitPrice = (double) transaction[6];
                Tr newTransaction = new Tr(transactionDate, userId, productId, quantity, transactionId, unitPrice, totalPrice);
                filteredTransactions.add(newTransaction);
                productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
                productRevenue.put(productId, productRevenue.getOrDefault(productId, 0.0) + totalPrice);
        }
        int totalProductsSold = productSales.values().stream().mapToInt(Integer::intValue).sum();
        double totalRevenue = productRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
        generatedocsReport(totalProductsSold, totalRevenue,productSales,productRevenue,filteredTransactions);
    
    }      
    }
    private static void generatedocsReport(int totalProductsSold, double totalRevenue, Map<Integer, Integer> productSales, Map<Integer, Double> productRevenue, List<Tr> filteredTransactions) {
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for adding text to the PDF
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add total products sold and total revenue to the PDF
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Total Products Sold: " + totalProductsSold);
            contentStream.newLine();
            contentStream.showText("Total Revenue: $" + totalRevenue);
            contentStream.endText();

            // Convert filteredTransactions array to a string and add it to the PDF
            String transactionsString = Arrays.toString(filteredTransactions.toArray());
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 600);
            contentStream.setFont(PDType1Font, 12);
            contentStream.showText("Filtered Transactions: " + transactionsString);
            contentStream.endText();

            // Save the document
            contentStream.close();
            document.save("docsReport.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}