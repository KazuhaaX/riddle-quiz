import java.io.*;
import java.util.*;

public class UserManager {
    // File names for storing data
    private static final String USERS_FILE = "users.txt";
    private static final String TIMES_FILE = "completion_times.txt";
    
    // Maps to store user data
    private final Map<String, String> users = new HashMap<>(); // username -> password
    private final Map<String, Long> bestTimes = new HashMap<>(); // username -> best time
    private final Scanner scanner = new Scanner(System.in);

    // Load all user data when program starts
    public void loadData() {
        loadUsers();
        loadBestTimes();
    }

    // Handle user sign up
    public boolean signUp() {
        System.out.println("\n\n---------- Sign-Up Menu ----------");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        // Check if username exists
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Try another one.");
            return false;
        }

        // Get password and save new user
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        users.put(username, password);
        bestTimes.put(username, Long.MAX_VALUE); // No time recorded yet
        
        saveUsers();
        saveBestTimes();
        
        System.out.println("Sign-up successful! You can now log in.");
        return true;
    }

    // Handle user login
    public String login() {
        System.out.println("\n\n---------- Login Menu ----------");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Check credentials
        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful! Welcome, " + username + "!");
            return username;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    // Show profile editing options
    public void editProfile(String username) {
        System.out.println("\n\n---------- Edit Profile ----------");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an option: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear input buffer

            switch (choice) {
                case 1:
                    updateUsername(username);
                    break;
                case 2:
                    updatePassword(username);
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

    // Helper method to change username
    private void updateUsername(String oldUsername) {
        System.out.print("\nEnter new username: ");
        String newUsername = scanner.nextLine();

        if (users.containsKey(newUsername)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        // Move user data to new username
        String password = users.get(oldUsername);
        long time = bestTimes.getOrDefault(oldUsername, Long.MAX_VALUE);

        users.remove(oldUsername);
        bestTimes.remove(oldUsername);

        users.put(newUsername, password);
        bestTimes.put(newUsername, time);

        saveUsers();
        saveBestTimes();

        System.out.println("Username changed successfully to: " + newUsername);
        System.out.println("Please login again with your new username.");
    }

    // Helper method to change password
    private void updatePassword(String username) {
        System.out.print("\nEnter current password: ");
        String currentPass = scanner.nextLine();

        if (!users.get(username).equals(currentPass)) {
            System.out.println("Incorrect current password.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirmPass = scanner.nextLine();

        if (!newPass.equals(confirmPass)) {
            System.out.println("Passwords do not match.");
            return;
        }

        users.put(username, newPass);
        saveUsers();
        System.out.println("Password changed successfully!");
    }

    // File operations
    private void loadUsers() {
        try (Scanner fileScanner = new Scanner(new File(USERS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length >= 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous user data found. Starting fresh.");
        }
    }

    private void loadBestTimes() {
        try (Scanner fileScanner = new Scanner(new File(TIMES_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length >= 2) {
                    try {
                        bestTimes.put(parts[0], Long.parseLong(parts[1]));
                    } catch (NumberFormatException e) {
                        bestTimes.put(parts[0], Long.MAX_VALUE);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous completion times data found. Starting fresh.");
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(USERS_FILE)) {
            users.forEach((user, pass) -> writer.println(user + "," + pass));
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    private void saveBestTimes() {
        try (PrintWriter writer = new PrintWriter(TIMES_FILE)) {
            bestTimes.forEach((user, time) -> 
                writer.println(user + "," + (time == Long.MAX_VALUE ? "" : time)));
        } catch (IOException e) {
            System.out.println("Error saving completion times.");
        }
    }

    // Update user's best completion time
    public void updateCompletionTime(String username, long time) {
        if (!bestTimes.containsKey(username) || time < bestTimes.get(username)) {
            bestTimes.put(username, time);
            saveBestTimes();
        }
    }

    // Get all completion times for leaderboard
    public Map<String, Long> getCompletionTimes() {
        return bestTimes;
    }
}
