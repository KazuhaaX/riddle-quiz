public class Question {
    // Instance variables to store question details
    private int number;  // Question number
    private String difficulty;  // Difficulty level (easy, medium, hard)
    private String text;  // The question text
    private String[] options;  // Multiple-choice options
    private char correctAnswer;  // Correct answer (a,b, or c)

    // Constructor to initialize question details
    public Question(int number, String difficulty, String text, String[] options, char correctAnswer) {
        this.number = number;
        this.difficulty = difficulty;
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getter methods to access private variables
    public int getNumber() { 
        return number; 
    }
    
    public String getDifficulty() { 
        return difficulty; 
    }
    
    public String getText() { 
        return text; 
    }
    
    public String[] getOptions() { 
        return options; 
    }
    
    public char getCorrectAnswer() { 
        return correctAnswer; 
    }
}
