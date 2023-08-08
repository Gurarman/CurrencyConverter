package algonquin.cst2335.currencyconverter;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

        import algonquin.cst2335.currencyconverter.Data.Conversion;
        import algonquin.cst2335.currencyconverter.Data.ConversionAdapter;
        import algonquin.cst2335.currencyconverter.Data.ConversionViewModel;

public class MainActivity extends AppCompatActivity {

    private EditText amountEditText, sourceCurrencyEditText, destinationCurrencyEditText;
    private TextView convertedAmountText;
    private RecyclerView conversionRecyclerView;
    private ConversionAdapter adapter;
    private List<Conversion> conversionList = new ArrayList<>();

    private ConversionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountEditText = findViewById(R.id.amountEditText);
        sourceCurrencyEditText = findViewById(R.id.sourceCurrencyEditText);
        destinationCurrencyEditText = findViewById(R.id.destinationCurrencyEditText);
        convertedAmountText = findViewById(R.id.convertedAmountText);
        conversionRecyclerView = findViewById(R.id.conversionRecyclerView);
        ImageButton swapImageButton = findViewById(R.id.swapImageButton);
        Button convertButton = findViewById(R.id.convertButton);
        Button btnShowSavedConversions = findViewById(R.id.btnShowSavedConversions);

        viewModel = new ViewModelProvider(this).get(ConversionViewModel.class);


        adapter = new ConversionAdapter(conversion -> {
            // Insert logic to save the conversion to the database here
            viewModel.insert(conversion);
        });

        viewModel.getConversions().observe(this, newConversions -> {
            conversionList.clear();
            conversionList.addAll(newConversions);
            adapter.notifyDataSetChanged();
        });

        adapter.setConversions(conversionList);
        conversionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        conversionRecyclerView.setAdapter(adapter);

        // Set up listeners
        swapImageButton.setOnClickListener(v -> swapCurrencies());
        convertButton.setOnClickListener(v -> performConversion());
        btnShowSavedConversions.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SavedConversionsActivity.class)));
    }

    private void swapCurrencies() {
        String sourceCurrency = sourceCurrencyEditText.getText().toString();
        String destinationCurrency = destinationCurrencyEditText.getText().toString();

        sourceCurrencyEditText.setText(destinationCurrency);
        destinationCurrencyEditText.setText(sourceCurrency);
    }

    private void performConversion() {
        String apiKey = "901a2e7a9bc5c1f9c07442d7a095e3196d7e8c7f";  // Hide this key or retrieve it securely
        String baseCurrency = sourceCurrencyEditText.getText().toString();
        String targetCurrency = destinationCurrencyEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());


        String url = "https://api.getgeoapi.com/v2/currency/convert" +
                "?api_key=" + apiKey +
                "&from=" + baseCurrency +
                "&to=" + targetCurrency +
                "&amount=" + amount +
                "&format=json";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                // Convert string response to JSONObject
                JSONObject jsonObject = new JSONObject(response);

                // Navigate through the JSON structure to get the rate_for_amount for the target currency
                JSONObject ratesObject = jsonObject.getJSONObject("rates");
                JSONObject targetCurrencyObject = ratesObject.getJSONObject(targetCurrency); // assuming targetCurrency is "GBP" in this case
                double result = targetCurrencyObject.getDouble("rate_for_amount");

                convertedAmountText.setText(String.valueOf(result));

                Conversion conversion = new Conversion(baseCurrency, targetCurrency, amount, result);
                viewModel.insert(conversion);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            // Handle error
            Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }
}
