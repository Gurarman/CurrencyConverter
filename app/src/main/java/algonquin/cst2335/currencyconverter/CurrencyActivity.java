package algonquin.cst2335.currencyconverter;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.lifecycle.ViewModelProvider;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
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

        import java.text.SimpleDateFormat;
        import java.util.Date;

        import algonquin.cst2335.currencyconverter.Data.Conversion;
        import algonquin.cst2335.currencyconverter.Data.ConversionViewModel;

/**
 * CurrencyActivity is the main activity for performing and viewing currency conversions.
 * <p>
 * It provides UI and functionality for users to input amounts and currency codes,
 * perform conversions via an external API, and view the results.
 * </p>
 */
public class CurrencyActivity extends AppCompatActivity {

    private EditText amountEditText, sourceCurrencyEditText, destinationCurrencyEditText;
    private TextView convertedAmountText;
    private ConversionViewModel viewModel;

    /**
     * Initializes the options menu.
     *
     * @param menu The options menu in which items are placed.
     * @return boolean - Return 'true' for the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }

    /**
     * Callback invoked when a menu item is selected.
     *
     * @param item The selected item.
     * @return boolean - Return 'false' to allow normal menu processing to proceed, 'true' to consume it.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.helpMenuItem) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the activity is starting.
     * Initializes the activity view and sets up event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        amountEditText = findViewById(R.id.amountEditText);
        sourceCurrencyEditText = findViewById(R.id.sourceCurrencyEditText);
        destinationCurrencyEditText = findViewById(R.id.destinationCurrencyEditText);
        convertedAmountText = findViewById(R.id.convertedAmountText);

        ImageButton swapImageButton = findViewById(R.id.swapImageButton);
        Button convertButton = findViewById(R.id.convertButton);
        Button btnShowSavedConversions = findViewById(R.id.btnShowSavedConversions);

        viewModel = new ViewModelProvider(this).get(ConversionViewModel.class);

        retrieveAmountFromPreferences();

        // Set up listeners
        swapImageButton.setOnClickListener(v -> swapCurrencies());
        convertButton.setOnClickListener(v -> performConversion());
        btnShowSavedConversions.setOnClickListener(v -> startActivity(new Intent(CurrencyActivity.this, SavedConversionsActivity.class)));
    }

    /**
     * Swaps the source and destination currencies in their respective EditTexts.
     */
    private void swapCurrencies() {
        String sourceCurrency = sourceCurrencyEditText.getText().toString();
        String destinationCurrency = destinationCurrencyEditText.getText().toString();

        sourceCurrencyEditText.setText(destinationCurrency);
        destinationCurrencyEditText.setText(sourceCurrency);
    }

    /**
     * Performs the currency conversion using an API call,
     * parses the result, and updates the UI.
     */
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
                JSONObject jsonObject = new JSONObject(response);
                JSONObject ratesObject = jsonObject.getJSONObject("rates");
                JSONObject targetCurrencyObject = ratesObject.getJSONObject(targetCurrency);
                double result = targetCurrencyObject.getDouble("rate_for_amount");

                String exchangeRate = String.valueOf(targetCurrencyObject.getDouble("rate"));
                String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                convertedAmountText.setText(String.valueOf(result));

                Conversion conversion = new Conversion(baseCurrency, targetCurrency, amount, result, exchangeRate, currentDate);

                viewModel.insert(conversion);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(CurrencyActivity.this, "Error parsing conversion data", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            // Handle error
            Toast.makeText(CurrencyActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }

    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the activity.
     * Saves the current amount entered by the user into shared preferences.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveAmountToPreferences();
    }
    /**
     * Shows a help dialog to guide the user on how to use the application.
     */
    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.help_title)
                .setMessage(R.string.help_message)
                .setPositiveButton("Got it", null)
                .show();
    }
    /**
     * Saves the entered amount to SharedPreferences.
     */
    private void saveAmountToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String amount = amountEditText.getText().toString();
        editor.putString("savedAmount", amount);

        editor.apply();
    }

    /**
     * Retrieves the saved amount from SharedPreferences
     * and sets it to the amount EditText.
     */
    private void retrieveAmountFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        String savedAmount = sharedPreferences.getString("savedAmount", "");

        amountEditText.setText(savedAmount);
    }
}
