package src;
import java.util.Scanner;

public class User extends Guest {
    private int userId;
    private String username;
    private String email;
    private boolean isLoggedIn;

    private static Scanner scanner = new Scanner(System.in);
    public User(String username, String email) {
        this.userId = Utils.getUserIdByEmail(email);
        this.username = username;
        this.email = email;
        this.isLoggedIn = true;
    }

    public int getUserId() {
        if(isLoggedIn) {
            return this.userId;
        } else {
            System.out.println("you're not logged in yet?");
            return -1;
        }
    }

    public String getUsername() {
        if(isLoggedIn) {
            return username;
        } else {
            System.out.println("you're not logged in yet?");
            return null;
        }
    }

    public void setUsername(String username) {
        if(isLoggedIn) {
            this.username = username;
        } else {
            System.out.println("You're in guest mode, please login");
        }
    }

    public String getEmail() {
        if(isLoggedIn) {
            return email;
        } else {
            System.out.println("you're not logged in yet?");
            return null;
        }
    }

    public void setEmail(String email) {
        if(isLoggedIn) {
            this.email = email;
        } else {
            System.out.println("You're in guest mode, please login");
        }
    }

    public void logout() {
        System.out.printf("\nSee u later %s!.",this.username);
        isLoggedIn = false;
    }

    public void changePassword(String email , String old_password) throws Exception {
        Object[] result = Utils.getUserIdAndPasswordByEmail(email);
        int index = (int) result[0];
        if (index < 0) {
            throw new Exception("User does not exist. Cannot change password.");
        }
        if (Utils.hashPassword(old_password).equals( (String) result[1] )) {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Re-enter your new password: ");
            String newPasswordReentered = scanner.nextLine();
            if (Utils.hashPassword(newPasswordReentered).equals(Utils.hashPassword(newPassword))) {
                newPasswordReentered = null;
                Utils.updateData_Accounts_Password(index, newPassword);
            }
        } else {
            System.out.printf("\nYour password is incorrect. Please re-enter the current password of %s's account", this.username);
        }
    }

    public void createReview(int productID , int ratingGiven, String comment) {
        new Review(this.userId, productID, ratingGiven, comment);
    }
    

    public double getAverageRatingOnReview(int reviewId){return Utils.getAverageRatingOnReview(reviewId);}

    public void createRatingOnReview(int reviewId,int ratingGiven){
        Utils.saveData_RatingsOnReviews(this.userId, reviewId, ratingGiven);
    }
}
