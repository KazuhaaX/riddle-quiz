import java.util.*;
import java.util.Scanner;

public class Leaderboard {
    private final Scanner scanner = new Scanner(System.in);

    public void showLeaderboard() {
        // Updated to use the getter method
        Map<String, Long> completionTimes = Main.getUserManager().getCompletionTimes();
        
        while (true) {
            System.out.println("\n\n                                Leaderboard                              ");
            completionTimes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    String username = entry.getKey();
                    long time = entry.getValue();
                    System.out.println(username + " - Fastest time: " + 
                        (time == Long.MAX_VALUE ? "N/A" : formatDuration(time)));
                });
    
            System.out.println("\n1. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    return;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number (1).");
                scanner.nextLine();
            }
        }
    }

    private String formatDuration(long durationMillis) {
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long hours = (durationMillis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}