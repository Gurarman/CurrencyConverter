package algonquin.cst2335.currencyconverter;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import algonquin.cst2335.currencyconverter.Data.Conversion;
import algonquin.cst2335.currencyconverter.Data.ConversionAdapter;
import algonquin.cst2335.currencyconverter.Data.ConversionViewModel;

public class SavedConversionsActivity extends AppCompatActivity {

    // UI components for displaying the saved conversions and managing user actions
    private RecyclerView savedConversionsRecyclerView;
    private ConversionAdapter adapter;
    private ConversionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_conversions);

        // Initialize button for updating conversions
        MaterialButton btnUpdateConversions = findViewById(R.id.btnUpdateConversions);

        // Initialize button for clearing all conversions
        MaterialButton btnClearAll = findViewById(R.id.btnClearAll);

        // Set up the RecyclerView to display the saved conversions
        savedConversionsRecyclerView = findViewById(R.id.savedConversionsRecyclerView);

        // Initialize adapter for the RecyclerView.
        // No action is defined for item clicks in this context.
        adapter = new ConversionAdapter(conversion -> {});

        savedConversionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedConversionsRecyclerView.setAdapter(adapter);

        // Initialize the ViewModel responsible for retrieving and managing data
        viewModel = new ViewModelProvider(this).get(ConversionViewModel.class);

        // Observe any changes to the conversions data and update the UI accordingly
        viewModel.getConversions().observe(this, conversions -> {
            adapter.setConversions(conversions);
        });

        // Set up the button to handle updates to the conversion rates
        btnUpdateConversions.setOnClickListener(v -> {
            // Fetch the list of saved conversions
            List<Conversion> savedConversions = viewModel.getConversions().getValue();

            // Update each saved conversion with the current exchange rate
            for (Conversion conversion : savedConversions) {
                updateConversionWithCurrentRate(conversion);
            }
        });

        // Set up the button to clear all saved conversions from the database
        btnClearAll.setOnClickListener(v -> {
            viewModel.clearAllConversions(); // Calls a method to delete all conversions from the database
        });
    }
    private void updateConversionWithCurrentRate(Conversion conversion) {
        String apiKey = "YOUR_API_KEY";  // Your API key (should be stored securely)
        String baseCurrency = conversion.getSourceCurrency();
        String targetCurrency = conversion.getDestinationCurrency();

        String url = "https://api.getgeoapi.com/v2/currency/convert" +
                "?api_key=" + apiKey +
                "&from=" + baseCurrency +
                "&to=" + targetCurrency +
                "&amount=1" +  // Get rate for 1 unit to calculate new rate
                "&format=json";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                // Convert string response to JSONObject
                JSONObject jsonObject = new JSONObject(response);

                // Navigate through the JSON structure to get the rate_for_amount for the target currency
                JSONObject ratesObject = jsonObject.getJSONObject("rates");
                JSONObject targetCurrencyObject = ratesObject.getJSONObject(targetCurrency);
                double newRate = targetCurrencyObject.getDouble("rate_for_amount");

                // Update the saved conversion with the new rate
                conversion.setConvertedAmount(conversion.getOriginalAmount() * newRate);
                viewModel.update(conversion);  // Assuming you have an "update" method in your ViewModel

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SavedConversionsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            // Handle error
            Toast.makeText(SavedConversionsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }

}
