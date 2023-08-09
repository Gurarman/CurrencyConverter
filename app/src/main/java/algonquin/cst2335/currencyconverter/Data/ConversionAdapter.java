package algonquin.cst2335.currencyconverter.Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.currencyconverter.R;

/**
 * An adapter class for displaying a list of currency conversions within a RecyclerView.
 * <p>
 * The adapter binds the data from a list of {@link Conversion} objects to the UI components
 * defined in the 'item_conversion' layout resource. It also handles click events for each conversion item.
 * </p>
 */
public class ConversionAdapter extends RecyclerView.Adapter<ConversionAdapter.ViewHolder> {

    // List of Conversion objects to be displayed in the RecyclerView.
    private List<Conversion> conversionList = new ArrayList<>();

    // Listener to handle click events on the 'Save' button within each item.
    private OnSaveClickListener onSaveClickListener;

    /**
     * Inflates the layout resource for each conversion item and creates a ViewHolder to hold it.
     *
     * @param parent The parent ViewGroup that the returned ViewHolder will be a child of.
     * @param viewType View type, typically used when RecyclerView contains multiple item types.
     *                 In this case, it's not utilized.
     * @return ViewHolder to hold the inflated item view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversion, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds data from a Conversion object to the UI components within a ViewHolder.
     *
     * @param holder The ViewHolder that should have its content updated based on the item at the given position.
     * @param position Position of the item within the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversion conversion = conversionList.get(position);
        holder.sourceTextView.setText(conversion.getSourceCurrency() + ": " + conversion.getOriginalAmount());
        holder.destinationTextView.setText(conversion.getDestinationCurrency() + ": " + conversion.getConvertedAmount());

        holder.displayButton.setTag(position);
    }

    /**
     * @return The total number of conversion items held by the adapter.
     */
    @Override
    public int getItemCount() {
        return conversionList.size();
    }

    /**
     * Replaces the current dataset held by the adapter with a new one and refreshes the UI.
     *
     * @param conversions The new list of Conversion objects.
     */
    public void setConversions(List<Conversion> conversions) {
        this.conversionList = conversions;
        notifyDataSetChanged();
    }

    /**
     * Represents the UI components within each conversion item in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView, destinationTextView;
        Button displayButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            displayButton = itemView.findViewById(R.id.displayButton);

            displayButton.setOnClickListener(v -> {
                // Get the position from the tag
                int position = (int) v.getTag();

                // Call the onSaveClick callback if the item was clicked and position is valid
                if(onSaveClickListener != null && position != RecyclerView.NO_POSITION) {
                    Conversion conversion = conversionList.get(position);
                    onSaveClickListener.onSaveClick(conversion);
                }
            });
        }
    }

    /**
     * Interface defining the callback for handling click events on the 'Save' button within each item.
     */
    public interface OnSaveClickListener {
        void onSaveClick(Conversion conversion);
    }

    /**
     * Constructor for the ConversionAdapter.
     *
     * @param listener Callback listener for handling click events on the 'Save' button within each item.
     */
    public ConversionAdapter(OnSaveClickListener listener) {
        this.onSaveClickListener = listener;
    }
}
