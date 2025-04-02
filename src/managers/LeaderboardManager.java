import java.util.*;
import java.io.*;

public class LeaderboardManager {
    private final Map<String, Integer> scores = new HashMap<>();
    private final Map<String, Long> completionTimes = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    public void loadLeaderboard() {
        try (Scanner fileScanner = new Scanner(new File(LEADERBOARD_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) {
                    try {
                        scores.put(data[0], Integer.parseInt(data[1]));
                        if (data.length >= 3) {
                            try {
                                completionTimes.put(data[0], Long.parseLong(data[2]));
                            } catch (NumberFormatException e) {
                                completionTimes.put(data[0], Long.MAX_VALUE);
                            }
                        } else {
                            completionTimes.put(data[0], Long.MAX_VALUE);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format in leaderboard file.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous leaderboard data found. Starting fresh.");
        }
    }

    public void showLeaderboard() {
        while (true) {
            System.out.println("\n\n---------------------- Leaderboard ----------------------\n");
            scores.entrySet().stream()
                .sorted((a, b) -> {
                    int scoreCompare = b.getValue().compareTo(a.getValue());
                    if (scoreCompare != 0) return scoreCompare;
                    return Long.compare(
                        completionTimes.getOrDefault(a.getKey(), Long.MAX_VALUE),
                        completionTimes.getOrDefault(b.getKey(), Long.MAX_VALUE)
                    );
                })
                .forEach(entry -> {
                    String username = entry.getKey();
                    int score = entry.getValue();
                    long time = completionTimes.getOrDefault(username, Long.MAX_VALUE);
                    System.out.println(username + " - " + score + " points - Fastest time: " + 
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

    public void updateScore(String username, int score, long time) {
        scores.put(username, score);
        if (!completionTimes.containsKey(username) || time < completionTimes.get(username)) {
            completionTimes.put(username, time);
        }
        saveLeaderboard();
    }

    private void saveLeaderboard() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARD_FILE))) {
            scores.forEach((user, score) -> {
                Long time = completionTimes.get(user);
                writer.println(user + "," + score + "," + (time == Long.MAX_VALUE ? "" : time));
            });
        } catch (IOException e) {
            System.out.println("Error saving leaderboard.");
        }
    }

    private String formatDuration(long durationMillis) {
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long hours = (durationMillis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}


