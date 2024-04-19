package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    private String userName;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView questionTextView, questionProgressTextView;
    private Button option1Button, option2Button, option3Button, option4Button, submitButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        // Initialize UI components
        progressBar = findViewById(R.id.progressBar);
        questionProgressTextView = findViewById(R.id.questionProgressTextView);
        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        submitButton = findViewById(R.id.submitButton);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        userName = sharedPreferences.getString("USERNAME", "");

        // Load questions from JSON file
        loadQuestionsFromJson();

        // Display the first question
        setQuestion(questionList.get(currentQuestionIndex));
        updateProgress(currentQuestionIndex);

        // Set OnClickListener for option buttons
        option1Button.setOnClickListener(optionClickListener);
        option2Button.setOnClickListener(optionClickListener);
        option3Button.setOnClickListener(optionClickListener);
        option4Button.setOnClickListener(optionClickListener);

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });
    }

    // OnClickListener for option buttons
    private View.OnClickListener optionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectOption((Button) v);
        }
    };

    // Load questions from JSON file
    private void loadQuestionsFromJson() {
        questionList = new ArrayList<>();
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject rootObject = new JSONObject(json);
            JSONArray jsonArray = rootObject.getJSONArray("questions");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String questionText = jsonObject.getString("question");
                String correctAnswer = jsonObject.getString("correct_answer");
                JSONArray optionsArray = jsonObject.getJSONArray("options");
                String[] options = new String[optionsArray.length()];
                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }
                Question question = new Question(questionText, options, correctAnswer);
                questionList.add(question);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    // Update progress bar and display current question
    private void updateProgress(int index) {
        int totalQuestions = questionList.size();
        questionProgressTextView.setText((index + 1) + "/" + totalQuestions);
        progressBar.setProgress((index + 1) * 100 / totalQuestions);
    }

    // Display current question and options
    private void setQuestion(Question question) {
        questionTextView.setText(question.getQuestionText());
        String[] options = question.getOptions();
        option1Button.setText(options[0]);
        option2Button.setText(options[1]);
        option3Button.setText(options[2]);
        option4Button.setText(options[3]);
    }

    // Select option button and highlight it
    private void selectOption(Button button) {
        clearOptionButtons();
        button.setBackgroundColor(getResources().getColor(R.color.selectedOption));
    }

    // Clear highlighting from all option buttons
    private void clearOptionButtons() {
        option1Button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        option2Button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        option3Button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        option4Button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    // Submit the user's answer
    private void submitAnswer() {
        String selectedAnswer = getSelectedAnswer();
        if (TextUtils.isEmpty(selectedAnswer)) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }

        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        if (selectedAnswer.equals(correctAnswer)) {
            score++;
        }
        moveToNextQuestion();
    }

    // Get the selected answer
    private String getSelectedAnswer() {
        if (option1Button.getBackground().getConstantState().equals(getResources().getDrawable(R.color.selectedOption).getConstantState())) {
            return option1Button.getText().toString();
        } else if (option2Button.getBackground().getConstantState().equals(getResources().getDrawable(R.color.selectedOption).getConstantState())) {
            return option2Button.getText().toString();
        } else if (option3Button.getBackground().getConstantState().equals(getResources().getDrawable(R.color.selectedOption).getConstantState())) {
            return option3Button.getText().toString();
        } else if (option4Button.getBackground().getConstantState().equals(getResources().getDrawable(R.color.selectedOption).getConstantState())) {
            return option4Button.getText().toString();
        }
        return "";
    }

    // Move to the next question or end the quiz
    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            setQuestion(questionList.get(currentQuestionIndex));
            updateProgress(currentQuestionIndex);
            clearOptionButtons();
        } else {
            Intent intent = new Intent(QuizQuestions.this, EndQuiz.class);
            intent.putExtra("totalQuestions", questionList.size());
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }
}