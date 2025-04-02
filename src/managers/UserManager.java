import java.util.*;
import java.io.*;

public class UserManager {
    private final Map<String, User> users = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private static final String USERS_FILE = "users.txt";

    public User login() {
        System.out.println("\n\n------------------------------------ Login Menu ------------------------------------\n");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            System.out.println("Login successful! Welcome, " + username + "!");
            return users.get(username);
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return null;
        }
    }

    public User signUp() {
        System.out.println("\n\n------------------------------------ Sign-Up Menu ------------------------------------\n");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Try another one.");
            return null;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        
        User newUser = new User(username, password);
        users.put(username, newUser);
        saveUsers();
        System.out.println("Sign-up successful! You can now log in.");
        return newUser;
    }

    public void editProfile(User currentUser) {
        System.out.println("\n\n------------------------------------ Edit Profile ------------------------------------");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an option: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    changeUsername(currentUser);
                    break;
                case 2:
                    changePassword(currentUser);
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number (1-3).");
            scanner.nextLine();
        }
    }

    private void changeUsername(User currentUser) {
        System.out.print("\nEnter new username: ");
        String newUsername = scanner.nextLine();

        if (users.containsKey(newUsername)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        String oldUsername = currentUser.getUsername();
        users.remove(oldUsername);
        currentUser.setUsername(newUsername);
        users.put(newUsername, currentUser);
        saveUsers();

        System.out.println("Username changed successfully to: " + newUsername);
    }

    private void changePassword(User user) {
        System.out.print("\nEnter current password: ");
        String currentPassword = scanner.nextLine();

        if (!user.getPassword().equals(currentPassword)) {
            System.out.println("Incorrect current password.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        user.setPassword(newPassword);
        users.put(user.getUsername(), user);
        saveUsers();
        System.out.println("Password changed successfully!");
    }

    public void loadUsers() {
        try (Scanner fileScanner = new Scanner(new File(USERS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) {
                    users.put(data[0], new User(data[0], data[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous user data found. Starting fresh.");
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            users.forEach((username, user) -> 
                writer.println(username + "," + user.getPassword()));
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }
}
