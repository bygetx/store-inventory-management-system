package src;


public class Guest {
    private static int maxAttempts = 3;

    public Guest() {System.out.println("welcome to out app Sir!");}
    
    public static User login(String username, String email, String password) {
        Object[] result = Utils.getUserIdAndPasswordByEmail(email);
        int index = (int) result[0];
        int attemptCount = 0;
        while (attemptCount < maxAttempts) {
            if (index >= 0) {
                // User exists, attempt login
                String storedPassword = (String) result[1];

                if (Utils.hashPassword(password).equals(storedPassword)) {
                    System.out.println("Login successful for " + username);
                    if (Utils.isAdmin(username)) {
                        return new Admin(username, email);
                    } else {
                        return new User(username, email);
                    }
                } else {
                    System.out.println("Login failed for " + username + ". Incorrect password.");
                    attemptCount++;
                }
            } else {
                System.out.println("User does not exist.");
                break;
            }
        }

        // Check if max attempts reached without successful login
        if (attemptCount == maxAttempts) {
            System.out.println("Maximum login attempts reached. Please try again later.");
        }
        return null;
    }

    public static User register(String username, String email, String password) {
        
        if (Utils.isAdmin(username)){System.out.println("you cant use a name that starts with ADMIN use another username");}
        else{
            Object[] result = Utils.getUserIdAndPasswordByEmail(email);
            int index = (int) result[0];
            if (index < 0) {
                Utils.saveData_Accounts(username, email, Utils.hashPassword(password));
                return new User(username, email);
            } else {
                System.out.printf("\nemail: %s already exists", email);
            }
        }
        return null;
    }

}
