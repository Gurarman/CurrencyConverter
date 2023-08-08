package algonquin.cst2335.currencyconverter.Data;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import java.util.List;

public class ConversionViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Conversion>> conversions = new MutableLiveData<>();
    private final AppDatabase db;

    public ConversionViewModel(Application application) {
        super(application);
        db = Room.databaseBuilder(application, AppDatabase.class, "conversion-database").fallbackToDestructiveMigration().build();
        loadConversions();
    }

    public LiveData<List<Conversion>> getConversions() {
        return conversions;
    }

    private void loadConversions() {
        new Thread(() -> {
            try {
                List<Conversion> conversionList = db.conversionDao().getAll();
                conversions.postValue(conversionList);
            } catch (Exception e) {
                    e.printStackTrace();  // add this
            }
        }).start();
    }

    public void clearAllConversions() {
        new Thread(() -> {
            try {
                db.conversionDao().deleteAll();
                loadConversions();
            } catch (Exception e) {
                // Handle or log the exception as per your need.
            }
        }).start();
    }

    public void insert(Conversion conversion) {
        new Thread(() -> {
            try {
                db.conversionDao().insert(conversion);
                loadConversions();
            } catch (Exception e) {
                // Handle or log the exception as per your need.
            }
        }).start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Close the database
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void update(Conversion conversion) {
        new Thread(() -> {
            try {
                db.conversionDao().update(conversion);
                loadConversions();  // Reload conversions after update
            } catch (Exception e) {
                // Handle or log the exception as per your need.
                e.printStackTrace();
            }
        }).start();
    }

}
