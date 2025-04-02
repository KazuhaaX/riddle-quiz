import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserManager userManager = new UserManager();
    private static final QuizManager quizManager = new QuizManager();
    private static final Leaderboard leaderboard = new Leaderboard();

    public static void main(String[] args) {
        userManager.loadData();
        quizManager.loadQuestions();
        showWelcomeScreen();
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static QuizManager getQuizManager() {
        return quizManager;
    }

    private static void showWelcomeScreen() {
        System.out.println("---------------------- Welcome to Riddle Quiz Game! ---------------------- ");
        System.out.println(" \"Test your wits with challenging riddles across different difficulty levels.\" ");
        
        while (true) {
            System.out.print("\nDo you have an account yet?\nEnter (Yes/No): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("yes") || response.equals("y")) {
                String username = userManager.login();
                if (username != null) {
                    showMainMenu(username);
                } else {
                    // If login fails, show welcome screen again
                    System.out.println("Login failed. Please try again.");
                    continue;  // This will restart the loop
                }
                break;
            } else if (response.equals("no") || response.equals("n")) {
                if (userManager.signUp()) {
                    showWelcomeScreen();
                }
                break;
            } else {
                System.out.println("Invalid input. Please enter 'Yes' or 'No'.");
            }
        }
    }

    private static void showMainMenu(String username) {
        while (true) {
            System.out.println("\n\n                                Main Menu                                ");
            System.out.println("Enter 'a' to edit your profile");
            System.out.println("Enter 'l' to see the leaderboard");
            System.out.println("Enter 'p' to start the game");
            System.out.println("Enter 'q' to quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    userManager.editProfile(username);
                    break;
                case "l":
                    leaderboard.showLeaderboard();
                    break;
                case "p":
                    quizManager.playQuiz(username);
                    break;
                case "q":
                    System.out.println("Logging out... Goodbye, " + username + "!");
                    showWelcomeScreen();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}