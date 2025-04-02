public class Question {
    private int number;
    private String difficulty;
    private String text;
    private String[] options;
    private char correctAnswer;

    public Question(int number, String difficulty, String text, String[] options, char correctAnswer) {
        this.number = number;
        this.difficulty = difficulty;
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters
    public int getNumber() { return number; }
    public String getDifficulty() { return difficulty; }
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public char getCorrectAnswer() { return correctAnswer; }
}