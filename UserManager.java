import java.io.*;
import java.util.*;

public class UserManager {
    private static final String USERS_FILE = "users.txt";
    private static final String COMPLETION_TIMES_FILE = "completion_times.txt";
    private final Map<String, String> users = new HashMap<>();
    private final Map<String, Long> completionTimes = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public void loadData() {
        loadUsers();
        loadCompletionTimes();
    }

    public boolean signUp() {
        System.out.println("\n\n                                Sign-Up Menu                               ");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Try another one.");
            return false;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        users.put(username, password);
        completionTimes.put(username, Long.MAX_VALUE);
        saveUsers();
        saveCompletionTimes();
        System.out.println("Sign-up successful! You can now log in.");
        return true;
    }

    public String login() {
        System.out.println("\n\n                                Login Menu                                ");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful! Welcome, " + username + "!");
            return username;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    public void editProfile(String currentUsername) {
        System.out.println("\n\n                                Edit Profile                                ");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an option: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    changeUsername(currentUsername);
                    break;
                case 2:
                    changePassword(currentUsername);
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

    private void changeUsername(String currentUsername) {
        System.out.print("\nEnter new username: ");
        String newUsername = scanner.nextLine();

        if (users.containsKey(newUsername)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        String password = users.get(currentUsername);
        long time = completionTimes.getOrDefault(currentUsername, Long.MAX_VALUE);

        users.remove(currentUsername);
        completionTimes.remove(currentUsername);

        users.put(newUsername, password);
        completionTimes.put(newUsername, time);

        saveUsers();
        saveCompletionTimes();

        System.out.println("Username changed successfully to: " + newUsername);
        System.out.println("Please login again with your new username.");
    }

    private void changePassword(String username) {
        System.out.print("\nEnter current password: ");
        String currentPassword = scanner.nextLine();

        if (!users.get(username).equals(currentPassword)) {
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

        users.put(username, newPassword);
        saveUsers();
        System.out.println("Password changed successfully!");
    }

    private void loadUsers() {
        try (Scanner fileScanner = new Scanner(new File(USERS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) {
                    users.put(data[0], data[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous user data found. Starting fresh.");
        }
    }

    private void loadCompletionTimes() {
        try (Scanner fileScanner = new Scanner(new File(COMPLETION_TIMES_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) {
                    try {
                        completionTimes.put(data[0], Long.parseLong(data[1]));
                    } catch (NumberFormatException e) {
                        completionTimes.put(data[0], Long.MAX_VALUE);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous completion times data found. Starting fresh.");
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            users.forEach((user, pass) -> writer.println(user + "," + pass));
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    private void saveCompletionTimes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COMPLETION_TIMES_FILE))) {
            completionTimes.forEach((user, time) -> 
                writer.println(user + "," + (time == Long.MAX_VALUE ? "" : time)));
        } catch (IOException e) {
            System.out.println("Error saving completion times.");
        }
    }

    public void updateCompletionTime(String username, long time) {
        if (!completionTimes.containsKey(username) || time < completionTimes.get(username)) {
            completionTimes.put(username, time);
            saveCompletionTimes();
        }
    }

    public Map<String, Long> getCompletionTimes() {
        return completionTimes;
    }
}
