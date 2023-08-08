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

public class ConversionAdapter extends RecyclerView.Adapter<ConversionAdapter.ViewHolder> {
    private List<Conversion> conversionList = new ArrayList<>();
    private static OnSaveClickListener onSaveClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversion conversion = conversionList.get(position);
        holder.sourceTextView.setText(conversion.getSourceCurrency() + ": " + conversion.getOriginalAmount());
        holder.destinationTextView.setText(conversion.getDestinationCurrency() + ": " + conversion.getConvertedAmount());

        holder.saveButton.setTag(position);
    }

    @Override
    public int getItemCount() {
        return conversionList.size();
    }

    public void setConversions(List<Conversion> conversions) {
        this.conversionList = conversions;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView, destinationTextView;
        Button saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            saveButton = itemView.findViewById(R.id.saveButton);

            saveButton.setOnClickListener(v -> {
                // Get the position from the tag
                int position = (int) v.getTag();

                if(onSaveClickListener != null && position != RecyclerView.NO_POSITION) {
                    Conversion conversion = conversionList.get(position);
                    onSaveClickListener.onSaveClick(conversion);
                }
            });
        }
    }

    public interface OnSaveClickListener {
        void onSaveClick(Conversion conversion);
    }

    // Constructor modification or addition
    public ConversionAdapter(OnSaveClickListener listener) {
        this.onSaveClickListener = listener;
    }
}
