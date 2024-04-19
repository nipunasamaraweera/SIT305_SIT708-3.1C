package com.example.quiz;

// This class represents a quiz question
public class Question {

    // Fields to store question text, options, and correct answer index
    private String questionText;
    private String[] options;
    private String correctAnswerIndex;

    // Constructor to initialize a Question object with question text, options, and correct answer index
    public Question(String questionText, String[] options, String correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Method to get the question text
    public String getQuestionText() {
        return questionText;
    }

    // Method to get the options array
    public String[] getOptions() {
        return options;
    }

    // Method to get the correct answer index
    public String getCorrectAnswer() {
        return correctAnswerIndex;
    }
}
