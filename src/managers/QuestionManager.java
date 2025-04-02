import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionManager {
    private final List<Question> questions = new ArrayList<>();
    private final File questionsFile;

    public QuestionManager(String filePath) {
        this.questionsFile = new File(filePath);
        validateFile();
    }

    /**
     * Loads questions from the specified file
     * @throws IllegalStateException if file is invalid
     */
    public void loadQuestions() throws IllegalStateException {
        try (Scanner fileScanner = new Scanner(questionsFile)) {
            int questionNumber = 1;
            while (fileScanner.hasNextLine()) {
                processQuestionLine(fileScanner.nextLine(), questionNumber++);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Questions file not found at: " + questionsFile.getAbsolutePath());
        }
    }

    /**
     * Gets a defensive copy of all questions
     * @return List of all questions
     */
    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    /**
     * Gets questions grouped by difficulty level
     * @return Map where key is difficulty, value is list of questions
     */
    public Map<String, List<Question>> getQuestionsByDifficulty() {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        Question::getDifficulty,
                        () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER),
                        Collectors.toList()
                ));
    }

    /**
     * Gets count of questions by difficulty
     * @return Map with difficulty levels and question counts
     */
    public Map<String, Long> getQuestionCountsByDifficulty() {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        Question::getDifficulty,
                        () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER),
                        Collectors.counting()
                ));
    }

    private void validateFile() {
        if (!questionsFile.exists()) {
            throw new IllegalArgumentException("File does not exist: " + questionsFile.getAbsolutePath());
        }
        if (!questionsFile.canRead()) {
            throw new IllegalArgumentException("Cannot read file: " + questionsFile.getAbsolutePath());
        }
    }

    private void processQuestionLine(String line, int questionNumber) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String[] parts = parseCsvLine(line);
        if (isValidQuestionData(parts)) {
            questions.add(createQuestion(parts, questionNumber));
        }
    }

    private String[] parseCsvLine(String line) {
        // Split on commas not inside quotes
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private boolean isValidQuestionData(String[] parts) {
        return parts != null && parts.length >= 6;
    }

    private Question createQuestion(String[] parts, int questionNumber) {
        // Clean and trim all parts
        String[] cleanedParts = Arrays.stream(parts)
                .map(part -> part.trim().replaceAll("^\"|\"$", ""))
                .toArray(String[]::new);

        return new Question(
                questionNumber,
                cleanedParts[0], // difficulty
                cleanedParts[1], // text
                Arrays.copyOfRange(cleanedParts, 2, cleanedParts.length - 1), // options
                cleanedParts[cleanedParts.length - 1].trim().toUpperCase().charAt(0) // correct answer
        );
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\JavaProjects\\RiddleQuiz\\questions.txt";
            QuestionManager manager = new QuestionManager(filePath);
            manager.loadQuestions();
            
            System.out.println("Total questions loaded: " + manager.getQuestions().size());
            
            Map<String, Long> counts = manager.getQuestionCountsByDifficulty();
            counts.forEach((difficulty, count) -> 
                System.out.printf("%s questions: %d%n", difficulty, count));
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}