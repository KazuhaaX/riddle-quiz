import java.util.Scanner;

public class Main {
    // Create scanner for user input
    private static final Scanner input = new Scanner(System.in);
    // Create managers for users, quizzes, and leaderboard
    private static final UserManager userManager = new UserManager();
    private static final QuizManager quizManager = new QuizManager();
    private static final Leaderboard leaderboard = new Leaderboard();

    public static void main(String[] args) {
        // Load saved data when program starts
        userManager.loadData();
        quizManager.loadQuestions();
        
        // Show the welcome screen
        showWelcomeScreen();
    }

    // Method to show welcome screen and login/signup options
    private static void showWelcomeScreen() {
        System.out.println("---------- Welcome to Riddle Quiz Game! ----------");
        System.out.println("  \"Solve fun riddles at different difficulty levels!\"");
        
        while (true) {
            System.out.print("\nDo you have an account? (yes/no): ");
            String answer = input.nextLine().trim().toLowerCase();
            
            if (answer.equals("yes") || answer.equals("y")) {
                // Try to login
                String username = userManager.login();
                if (username != null) {
                    // If login works, show main menu
                    showMainMenu(username);
                    break;
                } else {
                    System.out.println("Login failed. Try again.");
                }
            } 
            else if (answer.equals("no") || answer.equals("n")) {
                // Create new account
                if (userManager.signUp()) {
                    // After signing up, show welcome screen again
                    showWelcomeScreen();
                    break;
                }
            } 
            else {
                System.out.println("Please type 'yes' or 'no'.");
            }
        }
    }

    // Method to show the main menu options
    private static void showMainMenu(String username) {
        while (true) {
            System.out.println("\n\n---------- Main Menu ----------");
            System.out.println("Hello, " + username + "!");
            System.out.println("Enter 'a' to edit your profile");
            System.out.println("Enter 'l' to see the leaderboard");
            System.out.println("Enter 'p' to start the game");
            System.out.println("Enter 'q' to quit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    // Edit user profile
                    userManager.editProfile(username);
                    break;
                case "l":
                    // Show high scores
                    leaderboard.showLeaderboard();
                    break;
                case "p":
                    // Start the quiz game
                    quizManager.playQuiz(username);
                    break;
                case "q":
                    // Exit the program
                    System.out.println("Goodbye, " + username + "!");
                    showWelcomeScreen(); // Go back to welcome screen
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Methods to get the managers (used by other classes)
    public static UserManager getUserManager() {
        return userManager;
    }

    public static QuizManager getQuizManager() {
        return quizManager;
    }
}
