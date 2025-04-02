import java.util.*;
import java.util.Scanner;

public class Leaderboard {
    private final Scanner scanner = new Scanner(System.in);

    // Shows the leaderboard with fastest completion times
    public void showLeaderboard() {
        // Get all users' completion times
        Map<String, Long> times = Main.getUserManager().getCompletionTimes();
        
        while (true) {
            // Print the leaderboard header
            System.out.println("\n\n---------- Leaderboard ----------\n");
            
            // Sort and display times from fastest to slowest
            times.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    String user = entry.getKey();
                    long time = entry.getValue();
                    // Format the time display
                    String timeDisplay = (time == Long.MAX_VALUE) ? "N/A" : formatTime(time);
                    System.out.println(user + " - Fastest time: " + timeDisplay);
                });
    
            // Show menu options
            System.out.println("\n1. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            // Handle user input
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the input buffer
                
                if (choice == 1) {
                    return; // Go back to main menu
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number (1).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    // Helper method to format milliseconds into HH:MM:SS
    private String formatTime(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
