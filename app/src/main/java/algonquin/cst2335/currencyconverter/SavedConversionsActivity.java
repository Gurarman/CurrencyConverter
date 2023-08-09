package algonquin.cst2335.currencyconverter;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import algonquin.cst2335.currencyconverter.Data.Conversion;
import algonquin.cst2335.currencyconverter.Data.ConversionAdapter;
import algonquin.cst2335.currencyconverter.Data.ConversionViewModel;

/**
 * Activity to display and manage saved currency conversions.
 * <p>
 * Allows users to view their saved conversions, update conversion rates, and clear all saved conversions.
 * </p>
 */
public class SavedConversionsActivity extends AppCompatActivity {

    /** RecyclerView to display the list of saved conversions. */
    private RecyclerView savedConversionsRecyclerView;

    /** Adapter to bind conversions data to the RecyclerView. */
    private ConversionAdapter adapter;

    /** ViewModel to manage the conversions data. */
    private ConversionViewModel viewModel;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState Bundle containing saved instance state, if any.
     */
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

        adapter = new ConversionAdapter(conversion -> {
            ConversionDetailFragment detailFragment = new ConversionDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("sourceCurrency", conversion.getSourceCurrency());
            bundle.putString("destinationCurrency", conversion.getDestinationCurrency());
            bundle.putString("exchangeRate", conversion.getExchangeRate());
            bundle.putString("conversionDate", conversion.getDate());
            detailFragment.setArguments(bundle);

            // Safeguard against showing fragment when activity is finishing or destroyed
            if (!isFinishing() && !isDestroyed()) {
                detailFragment.show(getSupportFragmentManager(), "ConversionDetail");
            }
        });


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

        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(SavedConversionsActivity.this)
                    .setTitle("Clear All Conversions")
                    .setMessage("Are you sure you want to delete all saved conversions?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Calls a method to delete all conversions from the database
                        viewModel.clearAllConversions();
                        Snackbar.make(v, "All conversions cleared", Snackbar.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null) // No action on clicking 'NO' option
                    .show();
        });

    }
    /**
     * Updates the conversion rate for a saved conversion.
     *
     * @param conversion The saved conversion to be updated.
     */
    private void updateConversionWithCurrentRate(Conversion conversion) {
        String apiKey = "901a2e7a9bc5c1f9c07442d7a095e3196d7e8c7f";
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
