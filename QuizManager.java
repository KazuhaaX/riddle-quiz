import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QuizManager {
    private static final String QUESTIONS_FILE = "questions.txt";
    private final List<Question> questions = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public void loadQuestions() {
        try (Scanner fileScanner = new Scanner(new File(QUESTIONS_FILE))) {
            int questionNumber = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
                }
                
                if (parts.length >= 6) {
                    String difficulty = parts[0];
                    String text = parts[1];
                    String[] options = Arrays.copyOfRange(parts, 2, parts.length - 1);
                    char correctAnswer = parts[parts.length - 1].trim().charAt(0);
                    questions.add(new Question(questionNumber++, difficulty, text, options, correctAnswer));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No questions found. Please add questions to " + QUESTIONS_FILE);
        }
    }

    public void playQuiz(String username) {
        if (questions.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }   

        System.out.println("\nGames Start Now!");
        
        Map<String, List<Question>> questionsByDifficulty = new HashMap<>();
        questionsByDifficulty.put("Easy", new ArrayList<Question>());
        questionsByDifficulty.put("Medium", new ArrayList<Question>());
        questionsByDifficulty.put("Hard", new ArrayList<Question>());

        for (Question q : questions) {
            questionsByDifficulty.get(q.getDifficulty()).add(q);
        }

        long startTime = System.currentTimeMillis();
        int level = 1;

        for (String difficulty : new String[]{"Easy", "Medium", "Hard"}) {
            List<Question> difficultyQuestions = questionsByDifficulty.get(difficulty);
            if (difficultyQuestions.isEmpty()) continue;
        
            System.out.println("\n----- Level " + level++ + " : " + difficulty + " -----");
            
            for (Question q : difficultyQuestions) {
                boolean answeredCorrectly = false;
                while (!answeredCorrectly) {
                    System.out.println("\n" + q.getNumber() + "." + q.getText());
                    char optionChar = 'a';
                    for (String option : q.getOptions()) {
                        System.out.println(optionChar + ". " + option);
                        optionChar++;
                    }
                    System.out.print("Enter the correct answer as (a, b or c): ");
                    char answer;
                    try {
                        answer = Character.toLowerCase(scanner.nextLine().charAt(0));
                        if (answer < 'a' || answer > 'c') {
                            System.out.println("Invalid input. Please enter a, b, or c.");
                            continue;
                        }

                        if (answer == Character.toLowerCase(q.getCorrectAnswer())) {
                            System.out.println("Correct!");
                            answeredCorrectly = true;
                        } else {
                            System.out.println("Wrong! Try again.");
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("Please enter a valid answer (a, b, or c).");
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        double durationMinutes = (endTime - startTime) / 60000.0;
        System.out.println("\nYou did it in " + String.format("%.4f", durationMinutes) + " minutes.");
        System.out.print("\nEnter 'm' to go to menu: ");
        
        while (!scanner.nextLine().trim().equalsIgnoreCase("m")) {
            System.out.print("Please enter 'm' to go to menu: ");
        }

        Main.getUserManager().updateCompletionTime(username, endTime - startTime);
    }
}