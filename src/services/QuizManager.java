import java.util.*;

public class QuizManager {
    private final Scanner scanner = new Scanner(System.in);
    private final QuestionManager questionManager;
    
    public QuizManager(QuestionManager questionManager, LeaderboardManager leaderboardManager) {
        this.questionManager = questionManager;
    }

    public void playQuiz(User user) {
        List<Question> questions = questionManager.getQuestions();
        if (questions.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }
    
        System.out.println("\n\nGames Start Now!");
        
        Map<String, List<Question>> questionsByDifficulty = questionManager.getQuestionsByDifficulty();
        int level = 1;
        long startTime = System.currentTimeMillis();
    
        for (String difficulty : new String[]{"Easy", "Medium", "Hard"}) {
            List<Question> difficultyQuestions = questionsByDifficulty.get(difficulty);
            if (difficultyQuestions == null || difficultyQuestions.isEmpty()) continue;
        
            System.out.println("\n----- Level " + level++ + " : " + difficulty + " -----");
            
            for (Question q : difficultyQuestions) {
                boolean answeredCorrectly = false;
                
                while (!answeredCorrectly) {
                    System.out.println("\n" + q.getNumber() + ". " + q.getText());
                    char optionChar = 'a';
                    for (String option : q.getOptions()) {
                        System.out.println(optionChar + ". " + option);
                        optionChar++;
                    }
                    
                    System.out.print("Enter your answer (a, b, or c): ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    
                    if (input.length() != 1 || input.charAt(0) < 'a' || input.charAt(0) > 'c') {
                        System.out.println("Invalid input. Please enter a, b, or c.");
                        continue;
                    }
                    
                    char answer = input.charAt(0);
                    if (answer == Character.toLowerCase(q.getCorrectAnswer())) {
                        System.out.println("Correct! Moving to next question.");
                        answeredCorrectly = true;
                    } else {
                        System.out.println("Incorrect. Try again!");
                    }
                }
            }
            
            System.out.println("\nLevel completed! Moving to next difficulty.");
        }
    
        long endTime = System.currentTimeMillis();
        double minutesTaken = (endTime - startTime) / 60000.0;
        
        System.out.println("\n\nRiddle Quiz Game Completed!");
        System.out.printf("You did it in %.4f minutes.\n", minutesTaken);
        System.out.println("\nEnter 'm' to go to menu");
        
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("m")) {
                return;
            }
            System.out.println("Invalid input. Please enter 'm' to go to menu.");
        }
    }
}

