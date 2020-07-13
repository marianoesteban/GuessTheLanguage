package marianoesteban.guessthelanguage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;

import marianoesteban.guessthelanguage.quiz.Question;
import marianoesteban.guessthelanguage.wikipedia.PageSummary;
import marianoesteban.guessthelanguage.wikipedia.Wikipedia;
import marianoesteban.guessthelanguage.wikipedia.WikipediaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int NUM_QUESTIONS = 10;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(NUM_QUESTIONS);

        final Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                playButton.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                Log.v(TAG, "Loading questions...");
                loadQuestions(1);
            }
        });
    }

    private void loadQuestions(final int currentQuestionNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Wikipedia.getRandomApiBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WikipediaService service = retrofit.create(WikipediaService.class);

        service.getRandomPageSummary().enqueue(new Callback<PageSummary>() {
            @Override
            public void onResponse(Call<PageSummary> call, Response<PageSummary> response) {
                Log.v(TAG, "Loaded question " + currentQuestionNumber);

                questions.add(Wikipedia.createQuestion(response.body()));

                progressBar.setProgress(currentQuestionNumber);

                if (currentQuestionNumber < NUM_QUESTIONS) {
                    loadQuestions(currentQuestionNumber + 1);
                } else {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putParcelableArrayListExtra("questions", questions);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PageSummary> call, Throwable t) {
                Log.v(TAG, "Couldn't get page summary");
                new AlertDialog.Builder(HomeActivity.this)
                        .setMessage(getString(R.string.loading_questions_error))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.setProgress(0); // reset progress
                                HomeActivity.super.recreate();
                            }
                        })
                        .show();
            }
        });
    }
}
