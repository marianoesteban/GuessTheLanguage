package marianoesteban.guessthelanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int numQuestions = getIntent().getIntExtra("numQuestions", 0);
        String scoreText = String.format(getResources().getString(R.string.score),
                                         correctAnswers, numQuestions);
        scoreTextView.setText(scoreText);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
