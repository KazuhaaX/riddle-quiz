import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class QuizManager {
    private static final String QUESTIONS_FILE = "questions.txt";
    private final List<Question> questions = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    // Load questions from file
    public void loadQuestions() {
        try (Scanner fileScanner = new Scanner(new File(QUESTIONS_FILE))) {
            int questionNum = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue; // Skip empty lines
    
                // Split by commas BUT ignore commas inside quotes
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // Clean each part (remove outer quotes and trim)
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].replaceAll("^\"|\"$", "").trim();
                }
    
                // Validate structure: [Difficulty, Question, OptionA, OptionB, OptionC, Answer]
                if (parts.length == 6) {
                    String difficulty = parts[0];
                    String text = parts[1];
                    String[] options = Arrays.copyOfRange(parts, 2, 5); // Options A/B/C
                    char correctAnswer = parts[5].toUpperCase().charAt(0); // A/B/C
    
                    questions.add(new Question(questionNum++, difficulty, text, options, correctAnswer));
                } else {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Questions file not found at " + QUESTIONS_FILE);
        }
    }
    

    // Main quiz playing method
    public void playQuiz(String username) {
        if (questions.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }   

        System.out.println("\nGames Start Now!");
        
        // Organize questions by difficulty
        Map<String, List<Question>> questionsByLevel = new HashMap<>();
        questionsByLevel.put("Easy", new ArrayList<>());
        questionsByLevel.put("Medium", new ArrayList<>());
        questionsByLevel.put("Hard", new ArrayList<>());

        for (Question q : questions) {
            questionsByLevel.get(q.getDifficulty()).add(q);
        }

        long startTime = System.currentTimeMillis();
        int levelNum = 1;

        // Play through each difficulty level
        for (String difficulty : new String[]{"Easy", "Medium", "Hard"}) {
            List<Question> levelQuestions = questionsByLevel.get(difficulty);
            if (levelQuestions.isEmpty()) continue;
        
            // Show level header
            System.out.println("\n----- Level " + levelNum++ + " : " + difficulty + " -----");
            
            // Ask each question
            for (Question q : levelQuestions) {
                askQuestion(q);
            }
        }

        // Calculate and show final time
        long endTime = System.currentTimeMillis();
        double minutesTaken = (endTime - startTime) / 60000.0;
        System.out.println("\nYou did it in " + String.format("%.4f", minutesTaken) + " minutes.");
        
        // Wait for user to return to menu
        waitForMenuInput();
        
        // Save completion time
        Main.getUserManager().updateCompletionTime(username, endTime - startTime);
    }

    // Helper method to ask a single question
    private void askQuestion(Question q) {
        boolean correct = false;
        while (!correct) {
            // Print question and options
            System.out.println("\n" + q.getNumber() + "." + q.getText());
            char optionLetter = 'a';
            for (String option : q.getOptions()) {
                System.out.println(optionLetter + ". " + option);
                optionLetter++;
            }
            
            // Get and validate answer
            System.out.print("Enter the correct answer as (a, b or c): ");
            try {
                char answer = Character.toLowerCase(scanner.nextLine().charAt(0));
                
                if (answer < 'a' || answer > 'c') {
                    System.out.println("Invalid input. Please enter a, b, or c.");
                    continue;
                }

                if (answer == Character.toLowerCase(q.getCorrectAnswer())) {
                    System.out.println("Correct!");
                    correct = true;
                } else {
                    System.out.println("Wrong! Try again.");
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please enter a valid answer (a, b, or c).");
            }
        }
    }

    // Helper method to wait for user to return to menu
    private void waitForMenuInput() {
        System.out.print("\nEnter 'm' to go to menu: ");
        while (!scanner.nextLine().trim().equalsIgnoreCase("m")) {
            System.out.print("Please enter 'm' to go to menu: ");
        }
    }
}
