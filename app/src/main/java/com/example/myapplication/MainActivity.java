package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private StringBuilder enteredPin = new StringBuilder();
    private String correctPin = "1234"; // PIN


    private int[] currencyImages = {
            R.drawable.currency_usd,
            R.drawable.currency_eu,
            R.drawable.currency_gbp,
            R.drawable.currency_aed,
            R.drawable.currency_aud,
            R.drawable.currency_bgn,
            R.drawable.currency_brl,
            R.drawable.currency_cad,
            R.drawable.currency_chf,
            R.drawable.currency_cny,
    };

    private int currentCurrencyIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("1");
                checkPin();
            }
        });
        //ascultători pentru butoane
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("2");
                checkPin();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("3");
                checkPin();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("4");
                checkPin();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("5");
                checkPin();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("6");
                checkPin();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("7");
                checkPin();
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("8");
                checkPin();
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPin.append("9");
                checkPin();
            }
        });

        // initializare ImageView pentru cursurile valutare
        ImageView currencyImageView = findViewById(R.id.currencyImageView);

        // Creare și pornire Thread-ului pentru actualizarea imaginiilor curs valutar
        Thread currencyThread = new Thread(new CurrencyUpdateRunnable(currencyImageView));
        currencyThread.start();
    }

    private void checkPin() {
        if (enteredPin.length() == 4) {
            if (enteredPin.toString().equals(correctPin)) {
                // PIN-ul este corect, trecem la următoarea activitate
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
            } else {
                enteredPin.setLength(0);
            }
        }
    }

    private class CurrencyUpdateRunnable implements Runnable {
        private final ImageView imageView;

        CurrencyUpdateRunnable(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Actualizare imaginea cu cursul valutar pe firul principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateCurrencyImage(imageView);
                        }
                    });

                    // 1 secunde înainte de următoarea actualizare
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void updateCurrencyImage(ImageView currencyImageView) {
        // Actualizare imaginea cu cursul valutar
        currencyImageView.setImageResource(currencyImages[currentCurrencyIndex]);

        // Incrementare index pentru următoarea imagine a cursului valutar
        currentCurrencyIndex = (currentCurrencyIndex + 1) % currencyImages.length;
    }
}
