package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndQuiz extends AppCompatActivity {

    private TextView congratsTextView, scoreTextView;
    private Button retakeQuizButton, finishQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_quiz);

        // Initialize UI components
        congratsTextView = findViewById(R.id.congratsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        retakeQuizButton = findViewById(R.id.retakeQuiz);
        finishQuizButton = findViewById(R.id.finishQuiz);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String userName = sharedPreferences.getString("USERNAME", "");

        // Display congratulatory message
        congratsTextView.setText("Congratulations " + userName.trim() + "!");

        // Retrieve quiz results from Intent
        Intent intent = getIntent();
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);
        int score = intent.getIntExtra("score", 0);

        // Display the user's score
        scoreTextView.setText(score + "/" + totalQuestions);

        // Set OnClickListener for "Retake Quiz" button
        retakeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new instance of the QuizQuestions activity
                Intent quizIntent = new Intent(EndQuiz.this, QuizQuestions.class);
                startActivity(quizIntent);
                finish();
            }
        });

        // Set OnClickListener for "Finish Quiz" button
        finishQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the app
                finishAffinity();
            }
        });
    }
}