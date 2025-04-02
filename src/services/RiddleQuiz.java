import java.util.Scanner;

public class RiddleQuiz {
    private final Scanner scanner;
    private final UserManager userManager;
    private final QuestionManager questionManager;
    private final LeaderboardManager leaderboardManager;
    private final QuizManager quizManager;

    public RiddleQuiz() {
        this.scanner = new Scanner(System.in);
        this.userManager = new UserManager();
        this.questionManager = new QuestionManager("questions.txt");
        this.leaderboardManager = new LeaderboardManager();
        this.quizManager = new QuizManager(questionManager, leaderboardManager);
    }

    public void start() {
        loadData();
        runApplication();
        shutdown();
    }

    private void loadData() {
        userManager.loadUsers();
        leaderboardManager.loadLeaderboard();
        questionManager.loadQuestions();
    }

    private void shutdown() {
        scanner.close();
        // Add any other cleanup logic here
    }

    private void runApplication() {
        while (true) {
            User currentUser = showWelcomeScreen();
            if (currentUser == null) break; // Exit condition
            
            boolean shouldContinue = showMainMenu(currentUser);
            if (!shouldContinue) break;
        }
    }

    private User showWelcomeScreen() {
        System.out.println("---------------------- Welcome to Riddle Quiz Game! ---------------------- \n");
        System.out.println(" \"Test your wits with challenging riddles across different difficulty levels.\" ");
        
        while (true) {
            System.out.print("\nDo you have an account? (Yes/No): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("yes")) {
                return handleLogin();
            } else if (response.equals("no")) {
                return handleSignup();
            } else {
                System.out.println("Please enter 'Yes' or 'No'.");
            }
        }
    }

    private User handleLogin() {
        User user = userManager.login();
        while (user == null) {
            System.out.print("Login failed. Try again? (Yes/No): ");
            String tryAgain = scanner.nextLine().trim().toLowerCase();
            if (!tryAgain.equals("yes")) {
                return null;
            }
            user = userManager.login();
        }
        return user;
    }

    private User handleSignup() {
        User newUser = userManager.signUp();
        while (newUser == null) {
            System.out.print("Signup failed. Try again? (Yes/No): ");
            String tryAgain = scanner.nextLine().trim().toLowerCase();
            if (!tryAgain.equals("yes")) {
                return null;
            }
            newUser = userManager.signUp();
        }
        return newUser;
    }

    private boolean showMainMenu(User user) {
        while (true) {
            System.out.println("\n\n---------------------- Main Menu ----------------------\n");
            System.out.println("Enter 'a' to edit your profile");
            System.out.println("Enter 'l' to see the leaderboard");
            System.out.println("Enter 'p' to start the game");
            System.out.println("Enter 'q' to logout");
            System.out.println("Enter 'x' to exit program");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    userManager.editProfile(user);
                    break;
                case "l":
                    leaderboardManager.showLeaderboard();
                    break;
                case "p":
                    quizManager.playQuiz(user);
                    break;
                case "q":
                    System.out.println("\nLogging out... Goodbye, " + user.getUsername() + "!");
                    return true; // Return to welcome screen
                case "x":
                    System.out.println("Exiting program...");
                    return false; // Exit application
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}import java.util.Scanner;

public class RiddleQuiz {
    private final Scanner scanner;
    private final UserManager userManager;
    private final QuestionManager questionManager;
    private final LeaderboardManager leaderboardManager;
    private final QuizManager quizManager;

    public RiddleQuiz() {
        this.scanner = new Scanner(System.in);
        this.userManager = new UserManager();
        this.questionManager = new QuestionManager("questions.txt");
        this.leaderboardManager = new LeaderboardManager();
        this.quizManager = new QuizManager(questionManager, leaderboardManager);
    }

    public void start() {
        loadData();
        runApplication();
        shutdown();
    }

    private void loadData() {
        userManager.loadUsers();
        leaderboardManager.loadLeaderboard();
        questionManager.loadQuestions();
    }

    private void shutdown() {
        scanner.close();
        // Add any other cleanup logic here
    }

    private void runApplication() {
        while (true) {
            User currentUser = showWelcomeScreen();
            if (currentUser == null) break; // Exit condition
            
            boolean shouldContinue = showMainMenu(currentUser);
            if (!shouldContinue) break;
        }
    }

    private User showWelcomeScreen() {
        System.out.println("---------------------- Welcome to Riddle Quiz Game! ---------------------- \n");
        System.out.println(" \"Test your wits with challenging riddles across different difficulty levels.\" ");
        
        while (true) {
            System.out.print("\nDo you have an account? (Yes/No): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("yes")) {
                return handleLogin();
            } else if (response.equals("no")) {
                return handleSignup();
            } else {
                System.out.println("Please enter 'Yes' or 'No'.");
            }
        }
    }

    private User handleLogin() {
        User user = userManager.login();
        while (user == null) {
            System.out.print("Login failed. Try again? (Yes/No): ");
            String tryAgain = scanner.nextLine().trim().toLowerCase();
            if (!tryAgain.equals("yes")) {
                return null;
            }
            user = userManager.login();
        }
        return user;
    }

    private User handleSignup() {
        User newUser = userManager.signUp();
        while (newUser == null) {
            System.out.print("Signup failed. Try again? (Yes/No): ");
            String tryAgain = scanner.nextLine().trim().toLowerCase();
            if (!tryAgain.equals("yes")) {
                return null;
            }
            newUser = userManager.signUp();
        }
        return newUser;
    }

    private boolean showMainMenu(User user) {
        while (true) {
            System.out.println("\n\n---------------------- Main Menu ----------------------\n");
            System.out.println("Enter 'a' to edit your profile");
            System.out.println("Enter 'l' to see the leaderboard");
            System.out.println("Enter 'p' to start the game");
            System.out.println("Enter 'q' to logout");
            System.out.println("Enter 'x' to exit program");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    userManager.editProfile(user);
                    break;
                case "l":
                    leaderboardManager.showLeaderboard();
                    break;
                case "p":
                    quizManager.playQuiz(user);
                    break;
                case "q":
                    System.out.println("\nLogging out... Goodbye, " + user.getUsername() + "!");
                    return true; // Return to welcome screen
                case "x":
                    System.out.println("Exiting program...");
                    return false; // Exit application
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
