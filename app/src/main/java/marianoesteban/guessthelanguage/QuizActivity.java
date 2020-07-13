package marianoesteban.guessthelanguage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import marianoesteban.guessthelanguage.quiz.Question;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;
    private TextView questionNumberTextView;
    private TextView questionTextView;
    private Button[] optionButtons;
    private Button nextButton;
    private boolean answered = false; // true if the current question was answered
    private int correctAnswers = 0;
    private String lastAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
            correctAnswers = savedInstanceState.getInt("correctAnswers");
            answered = savedInstanceState.getBoolean("answered");
            if (answered)
                lastAnswer = savedInstanceState.getString("lastAnswer");
        }

        questionNumberTextView = findViewById(R.id.questionNumberTextView);

        questionTextView = findViewById(R.id.questionTextView);

        optionButtons = new Button[] { findViewById(R.id.option1Button), findViewById(R.id.option2Button),
                                       findViewById(R.id.option3Button), findViewById(R.id.option4Button) };

        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedOptionButton = (Button) v;
                checkAnswer(clickedOptionButton.getText().toString());
            }
        };
        for (Button optionButton : optionButtons)
            optionButton.setOnClickListener(optionClickListener);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionIndex++;
                answered = false;
                nextQuestion();
            }
        });

        questions = getIntent().getParcelableArrayListExtra("questions");
        nextQuestion();

        if (answered) {
            markAnswer(lastAnswer);
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("currentQuestionIndex", currentQuestionIndex);
        savedInstanceState.putInt("correctAnswers", correctAnswers);
        savedInstanceState.putBoolean("answered", answered);
        if (answered)
            savedInstanceState.putString("lastAnswer", lastAnswer);

        super.onSaveInstanceState(savedInstanceState);
    }

    private void checkAnswer(String givenAnswer) {
        if (answered)
            return;
        answered = true;

        if (givenAnswer.equals(currentQuestion.getCorrectAnswer()))
            correctAnswers++;

        Log.v(TAG, "Current score: " + correctAnswers + "/" + (currentQuestionIndex+1));

        markAnswer(givenAnswer);

        lastAnswer = givenAnswer;

        // show next button
        nextButton.setVisibility(View.VISIBLE);
    }

    private void markAnswer(String givenAnswer) {
        for (Button optionButton : optionButtons) {
            if (currentQuestion.getCorrectAnswer().contentEquals(optionButton.getText()))
                optionButton.setBackgroundColor(Color.GREEN);
            else if (givenAnswer.contentEquals(optionButton.getText()))
                optionButton.setBackgroundColor(Color.RED);
        }
    }

    private void nextQuestion() {
        currentQuestion = questions.get(currentQuestionIndex);

        // hide next button
        nextButton.setVisibility(View.INVISIBLE);

        // show the current question number
        String questionNumberText = String.format(getString(R.string.question_number),
                currentQuestionIndex + 1, questions.size());
        questionNumberTextView.setText(questionNumberText);

        // show the current question text
        questionTextView.setText(currentQuestion.getText());

        int optionIndex = 0;
        for (String option : currentQuestion.getOptions()) {
            Button optionButton = optionButtons[optionIndex++];
            optionButton.setText(option);
            optionButton.setBackground(new Button(this).getBackground());
        }

        // if it is the last question, show "finish" instead of "next"
        if (currentQuestionIndex == questions.size() - 1) {
            nextButton.setText(R.string.finish);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishQuiz();
                }
            });
        }
    }

    private void finishQuiz() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("numQuestions", questions.size());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing App")
                .setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
