package src;


public class Review {
    private int userID;
    private int ratingGiven;
    private int productID;
    private String comment;

    public Review(int userID, int productID, int ratingGiven, String comment) {
        this.userID = userID;
        this.productID = productID;
        this.ratingGiven = ratingGiven;
        this.comment = comment;
        Utils.saveData_Reviews(productID, userID, ratingGiven, comment);
    }

}
