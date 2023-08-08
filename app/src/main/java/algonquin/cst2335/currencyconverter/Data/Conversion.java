package algonquin.cst2335.currencyconverter.Data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Conversion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sourceCurrency;
    private String destinationCurrency;
    private double originalAmount;
    private double convertedAmount;

    public Conversion(String sourceCurrency, String destinationCurrency, double originalAmount, double convertedAmount) {
        this.sourceCurrency = sourceCurrency;
        this.destinationCurrency = destinationCurrency;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public void setDestinationCurrency(String destinationCurrency) {
        this.destinationCurrency = destinationCurrency;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
