package src;

public class Product {
    private String name;
    private double price;
    private String category;
    private String description;
    private int productID;

    public Product(String name, double price, String category, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        Utils.saveData_Products(name, category, price, description);
    }

    public String getName() {
        return name;
    }

    public void setProductId(int productID) {
        this.productID = productID;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductID(){
        if (this.productID != 0) {this.productID=Utils.getProductId(this.name ,this.price, this.category, this.description );}
        return this.productID;
    }
}
