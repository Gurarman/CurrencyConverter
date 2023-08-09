package algonquin.cst2335.currencyconverter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * A DialogFragment subclass that displays detailed information about a currency conversion.
 * <p>
 * Presents the source currency, converted destination currency, the exchange rate used, and the date of conversion.
 * </p>
 */
public class ConversionDetailFragment extends DialogFragment {

    /** View to display the source currency amount. */
    private TextView sourceCurrencyAmount;

    /** View to display the converted destination currency amount. */
    private TextView destinationCurrencyAmount;

    /** View to display the exchange rate used. */
    private TextView exchangeRate;

    /** View to display the date of conversion. */
    private TextView conversionDate;

    /**
     * Factory method to create a new instance of ConversionDetailFragment with provided arguments.
     *
     * @param source      The original currency value.
     * @param destination The converted currency value.
     * @param rate        The exchange rate used for the conversion.
     * @param date        The date when the conversion was made.
     * @return A new instance of {@link ConversionDetailFragment} with the given arguments.
     */
    public static ConversionDetailFragment newInstance(String source, String destination, String rate, String date) {
        ConversionDetailFragment fragment = new ConversionDetailFragment();

        Bundle args = new Bundle();
        args.putString("sourceCurrency", source);
        args.putString("destinationCurrency", destination);
        args.putString("exchangeRate", rate);
        args.putString("conversionDate", date);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
     * Used to perform one-time initialization.
     *
     * @param savedInstanceState If non-null, this is the state this fragment is being re-constructed from.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view the fragment's UI will be attached to.
     * @param savedInstanceState If non-null, this fragment is being reconstructed from a previous saved state.
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversion_detail, container, false);
    }

    /**
     * Set up views and populate them with data from the arguments provided during fragment instantiation.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being reconstructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sourceCurrencyAmount = view.findViewById(R.id.sourceCurrencyAmount);
        destinationCurrencyAmount = view.findViewById(R.id.destinationCurrencyAmount);
        exchangeRate = view.findViewById(R.id.exchangeRate);
        conversionDate = view.findViewById(R.id.conversionDate);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String source = arguments.getString("sourceCurrency", "N/A");
            String destination = arguments.getString("destinationCurrency", "N/A");
            String rate = arguments.getString("exchangeRate", "N/A");
            String date = arguments.getString("conversionDate", "N/A");

            sourceCurrencyAmount.setText("Source Currency: " + source);
            destinationCurrencyAmount.setText("Converted Amount: " + destination);
            exchangeRate.setText("Exchange Rate: " + rate);
            conversionDate.setText("Date: " + date);
        }
    }
}
