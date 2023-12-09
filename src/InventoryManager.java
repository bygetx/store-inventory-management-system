package src;

import java.util.Map;

public class InventoryManager {
    private static Map<Integer, Integer> productQuantityMap;

    public InventoryManager() {
        productQuantityMap = Utils.readInventoryAsMap();
    }

    public static int getProductQuantity(Product product) {
        return productQuantityMap.getOrDefault(product.getProductID(), 0);
    }
    public static int getProductQuantityById(int productId) {
        return productQuantityMap.getOrDefault(productId, 0);
    }

    public static Map<Integer, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    public static void removeProduct(Product product) {
        productQuantityMap.remove(product.getProductID());
        Utils.removeProductQuantity(product.getProductID());
    }
    public static void removeProductById(int productId) {
        productQuantityMap.remove(productId);
        Utils.removeProductQuantity(productId);
    }

    public static void setProductQuantity(Product product, int quantity) {
        productQuantityMap.put(product.getProductID(), quantity);
        Utils.insertProductQuantity(product.getProductID(),quantity);
    }
    public static void setProductQuantityById(int productId, int quantity) {
        productQuantityMap.put(productId, quantity);
        Utils.insertProductQuantity(productId,quantity);
    }

    
    public static void updateProductQuantity(Product product, int quantity, boolean add) {
        int originalQuantity = InventoryManager.getProductQuantity(product);
        
        if (originalQuantity == 0) {
            System.out.printf("\nThe product %s | id: %d was not in stock \n", product.getName(), product.getProductID());
        }

        if (!Utils.checkProductIdExists(product.getProductID()) && quantity > 0) {
            setProductQuantityById(product.getProductID(), quantity);
        } else if (quantity == 0) {
            System.out.println("Cannot have quantity = 0");
        } else {
            if (add) {
                int updatedQuantity = originalQuantity + quantity;
                productQuantityMap.put(product.getProductID(), updatedQuantity);
                Utils.updateProductQuantityById(product.getProductID(), updatedQuantity);
            } else {
                if (originalQuantity < quantity) {
                    System.out.printf("Cannot have quantity less than 0. Product original quantity = %d", originalQuantity);
                } else {
                    int updatedQuantity = originalQuantity - quantity;
                    productQuantityMap.put(product.getProductID(), updatedQuantity);
                    Utils.updateProductQuantityById(product.getProductID(), updatedQuantity);
                }
            }
        }
    }
    public static void updateProductQuantityById(int productId, int quantity, boolean add) {
    int originalQuantity = InventoryManager.getProductQuantityById(productId);
    
    if (originalQuantity == 0) {
        System.out.printf("\nThe product with ID: %d was not in stock \n", productId);
    }

    if (!Utils.checkProductIdExists(productId) && quantity > 0) {
        setProductQuantityById(productId, quantity);
    } else if (quantity == 0) {
        System.out.println("Cannot have quantity = 0");
    } else {
        if (add) {
            int updatedQuantity = originalQuantity + quantity;
            productQuantityMap.put(productId, updatedQuantity);
            Utils.updateProductQuantityById(productId, updatedQuantity);
        } else {
            if (originalQuantity < quantity) {
                System.out.printf("Cannot have quantity less than 0. Product original quantity = %d", originalQuantity);
            } else {
                int updatedQuantity = originalQuantity - quantity;
                productQuantityMap.put(productId, updatedQuantity);
                Utils.updateProductQuantityById(productId, updatedQuantity);
            }
        }
    }
}


}


