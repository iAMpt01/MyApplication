package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResult = findViewById(R.id.tvResult);

        boolean wordFound = getIntent().getBooleanExtra("wordFound", false);

        if (wordFound) {
            tvResult.setText("Cuvântul a fost identificat.");
        } else {
            tvResult.setText("Cuvântul nu a fost identificat.");
        }
    }
}
