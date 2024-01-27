package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {

    private EditText etAmount;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private Button btnConvert;
    private TextView tvResult;

    private String[] currencyCodes = {"RON", "AED", "AUD", "BGN", "BRL", "CAD" ,"CHF", "CNY", "CZK", "DKK", "EGP", "EUR",
        "GBP", "HUF", "INR", "JPY", "KRW", "MDL", "MXN", "NOK", "NZD", "PLN", "RSD", "RUB", "SEK", "THB", "TRY", "UAH",
            "USD", "XAU", "XDR", "ZAR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        etAmount = findViewById(R.id.etAmount);
        spinnerFromCurrency = findViewById(R.id.spinnerFromCurrency);
        spinnerToCurrency = findViewById(R.id.spinnerToCurrency);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.currencies,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFromCurrency.setAdapter(adapter);
        spinnerToCurrency.setAdapter(adapter);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchExchangeRatesTask().execute();
            }
        });
    }

    private class FetchExchangeRatesTask extends AsyncTask<Void, Void, Double[]> {
        @Override
        protected Double[] doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.bnr.ro/nbrfxrates.xml")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        InputStream in = response.body().byteStream();
                        return parseXml(in);
                    } else {
                        // Handle the error response
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Double[] exchangeRates) {
            if (exchangeRates != null ) {
                convertCurrency(exchangeRates);
            } else {
                tvResult.setText("Failed to fetch exchange rates");
            }
        }

        private Double[] parseXml(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser parser = xmlFactoryObject.newPullParser();
                parser.setInput(new InputStreamReader(in));

                int eventType = parser.getEventType();
                int i = 1;
                Double[] exchangeRates = new Double[currencyCodes.length+1];
                exchangeRates[0]=1.0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && parser.getName().equals("Rate")) {
                        parser.next();
                        if (i < currencyCodes.length) {
                            exchangeRates[i] = Double.parseDouble(parser.getText());
                            i++;
                        }
                    }
                    eventType = parser.next();
                }

                return exchangeRates;
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void convertCurrency(Double[] exchangeRates) {
        String fromCurrency = spinnerFromCurrency.getSelectedItem().toString();
        String toCurrency = spinnerToCurrency.getSelectedItem().toString();

        int fromCurrencyIndex = Arrays.asList(currencyCodes).indexOf(fromCurrency);
        int toCurrencyIndex = Arrays.asList(currencyCodes).indexOf(toCurrency);

        double amount = Double.parseDouble(etAmount.getText().toString());
        double convertedAmount = amount * (exchangeRates[fromCurrencyIndex]/ exchangeRates[toCurrencyIndex]);

        tvResult.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency));
    }
}
