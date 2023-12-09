package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Utils {
    static String ACCOUNTS_FILE_PATH = "src/accounts.csv";
    static String DATABASE_FILE_PATH = "jdbc:sqlite:C:/Users/mehre/OneDrive/Bureau/VSC/java project/database.db";

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Log the exception or handle it as needed
            e.printStackTrace();
            return null;
        }
    }

    public static int searchStringInMatrixColumn(String[][] accounts, int column, String specificString) {
        for (int i = 0; i < accounts.length; i++) {
            String[] account = accounts[i];
            if (account != null && account.length >= 4 && account[column] != null && account[column].equals(specificString)) {
                return i;
            }
        }
        return -1;
    }

    public static int generateRandomId() {
        // Generate a random 6-digit number as the new user ID
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    public static String[][] readAccountsFromFile() {
        String[][] accounts = new String[100][4];
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE_PATH))) {
            reader.readLine();
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < 100) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    for (int j = 0; j < 4; j++) {
                        accounts[i][j] = parts[j];
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static void replaceAnElementInMatrix(String[][] accounts, int index, int col, String value) {
        try {
            accounts[index][col] = value;

            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(ACCOUNTS_FILE_PATH));
            for (String[] row : accounts) {
                csvWriter.write(String.join(",", row) + "\n");
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeLineFromCsv(String[][] accounts, int col, String value) {
        try {
            List<String[]> updatedRows = new ArrayList<>();
            BufferedReader csvReader = new BufferedReader(new FileReader(ACCOUNTS_FILE_PATH));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length > col && data[col].equals(value)) {
                    continue; // Skip line if value found in specified column
                }
                updatedRows.add(data);
            }
            csvReader.close();

            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(ACCOUNTS_FILE_PATH));
            for (String[] updatedRow : updatedRows) {
                csvWriter.write(String.join(",", updatedRow) + "\n");
            }
            csvWriter.close();
            accounts = Utils.readAccountsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean isAdmin(String username) {
        return username.startsWith("ADMIN");
    }

    public static double calculateAverageOfArrayListInt(ArrayList<Integer> ratings) {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public static void saveData_Accounts(String username, String email,String password){
        String sql = "INSERT INTO Accounts(username, email, password) VALUES(?, ?, ?)";
        String[] values = {username, email, password};
        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {pstmt.setString(i + 1, values[i]);}
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully into Accounts.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
    }




    }

    public static void saveData_InventoryManager(int productId, int quantity) {
    String sql = "INSERT INTO InventoryManager(productId, quantity) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.setInt(2, quantity);

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully into InventoryManager.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}
    
    public static void saveData_Products(String name, String category, double price, String description) {
    String sql = "INSERT INTO Products(name, category, price, description) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setDouble(3, price);
            pstmt.setString(4, description);

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully into Products.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}

    public static void saveData_RatingsOnReviews(int userId, int reviewID, int ratingonreview) {
        String sql = "INSERT INTO RatingsOnReviews(userId, reviewID, ratingonreview) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, reviewID);
            pstmt.setInt(3, ratingonreview);

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully into RatingsOnReviews.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}

    public static void saveData_Reviews(int productID, int userId, int ratingGiven, String comment) {
        String sql = "INSERT INTO Reviews(productID, userId, ratingGiven, comment, averageRatingTaken) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productID);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, ratingGiven);
            pstmt.setString(4, comment);
            pstmt.setInt(5, 0); // Initializing averageRatingTaken to 0

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully into Reviews.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}

    public static void saveData_Transactions(LocalDateTime currentDate, double totalPrice, int quantity, int productID, int userId, double price) {
        String sql = "INSERT INTO Transactions(currentDate, totalPrice, quantity, productID, accountId, price) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Convert LocalDateTime to a formatted string
            String formattedDate = currentDate.toString(); // Adjust as per the required format

            pstmt.setString(1, formattedDate);
            pstmt.setDouble(2, totalPrice);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, productID);
            pstmt.setInt(5, userId);
            pstmt.setDouble(6, price);

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully into Transactions.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Object[] getUserIdAndPasswordByEmail(String email) {
        Object[] result = {-1, null};
        String sql = "SELECT userId, password FROM Accounts WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("userId");
                String password = rs.getString("password");
                result[0] = userId;
                result[1] = password;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static int getUserIdByEmail(String email) {
        int userId = -1;
        String sql = "SELECT userId FROM Accounts WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("userId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userId;
    }

    public static void updateData_Accounts_Password(int userId, String newpassword) {
        String sql = "UPDATE Accounts SET password = ? WHERE userId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newpassword);
            pstmt.setInt(2, userId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password updated successfully for userId: " + userId);
            } else {
                System.out.println("No user found with userId: " + userId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static double getAverageRatingOnReview(int reviewId) {
        double averageRating = 0.0;
        String sql = "SELECT AVG(ratingonreview) AS average FROM RatingsOnReviews WHERE reviewId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                averageRating = rs.getDouble("average");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return averageRating;
    }

    public static String[] getUsernameAndEmailByUserID(int userId) {
        String[] result = {null, null};
        String sql = "SELECT username, email FROM Accounts WHERE userId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String email = rs.getString("email");
                result[0] = username;
                result[1] = email;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static void removeUser(int userID) {
        String sql = "DELETE FROM Accounts WHERE userId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("User with userID: " + userID + " deleted successfully.");
            } else {
                System.out.println("No user found with userID: " + userID);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
   
    public static void promoteToAdmin(int userId) {
        String sql = "UPDATE Accounts SET username = 'ADMIN_' || username WHERE userId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User with userId: " + userId + " promoted to admin successfully.");
            } else {
                System.out.println("No user found with userId: " + userId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}

    public static int getProductId(String name, double price, String category, String description) {
        int productId = -1;
        String sql = "SELECT productId FROM Products WHERE name = ? AND price = ? AND category = ? AND description = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, category);
            pstmt.setString(4, description);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                productId = rs.getInt("productId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productId;
    }

    public static void removeProductQuantity(int productId ) {
        String sql = "DELETE FROM InventoryManager WHERE productId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId );
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("User with productId: " + productId + " deleted successfully.");
            } else {
                System.out.println("No product found with productId : " + productId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertProductQuantity(int productId , int quantity ) {
        String sql = "INSERT INTO InventoryManager (productId, quantity) VALUES(? , ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId );
            pstmt.executeUpdate(); 
            System.out.printf("\nProduct quantity set successfully %d:%d",productId,quantity);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } 

    public static boolean checkProductIdExists(int productId) {
        String sql = "SELECT COUNT(*) AS count FROM InventoryManager WHERE productId = ?";
        boolean productIdExists = false;

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            // Check if any row with the productId exists
            if (rs.next()) {
                int count = rs.getInt("count");
                productIdExists = count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return productIdExists;
    }

    public static void updateProductQuantityById(int productId, int updatedQuantity) {
        String sql = "UPDATE InventoryManager SET quantity = ? WHERE productId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, updatedQuantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();

            System.out.printf("\nProduct quantity updated successfully for productId %d to %d", productId, updatedQuantity);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Map<Integer, Integer> readInventoryAsMap() {
        Map<Integer, Integer> inventoryMap = new HashMap<>();
        String sql = "SELECT productId, quantity FROM InventoryManager";
        
        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            // Check if the result set has entries
            boolean hasEntries = rs.isBeforeFirst();

            if (hasEntries) {
                while (rs.next()) {
                    int productId = rs.getInt("productId");
                    int quantity = rs.getInt("quantity");
                    inventoryMap.put(productId, quantity);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return inventoryMap;
    }

    public static double getPriceByProductId(int productId) {
        double price = 0;
        String sql = "SELECT price FROM Products WHERE productId = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
               
                price = rs.getInt("price");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return price;
    }

    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");
                String description = resultSet.getString("description");

                // Create Product object using retrieved data and add it to the list
                Product product = new Product(name, price, category, description);
                product.setProductId(productId);
                productList.add(product);
            }
            
            }catch (SQLException e) {System.out.println(e.getMessage());}

        return productList;
    }
    public static Product getProductById(int productId) {
        String sql = "SELECT * FROM Products where productId =?";
        try (Connection conn = DriverManager.getConnection(DATABASE_FILE_PATH);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String category = rs.getString("category");
                String description = rs.getString("description");

                // Create Product object using retrieved data and add it to the list
                Product product = new Product(name, price, category, description);
                return product;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    
}
