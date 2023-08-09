package algonquin.cst2335.currencyconverter.Data;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import java.util.List;

/**
 * ViewModel class for managing and interacting with the data related to currency conversions.
 * Provides methods for accessing and modifying the conversion data stored in the database.
 * <p>
 * This class encapsulates the logic for database operations such as insert, update, load, and delete conversions.
 * </p>
 */
public class ConversionViewModel extends AndroidViewModel {

    /** LiveData wrapper around the list of conversions. Allows observing changes in the data. */
    private final MutableLiveData<List<Conversion>> conversions = new MutableLiveData<>();

    /** Reference to the database for performing CRUD operations. */
    private final AppDatabase db;

    /**
     * Constructs the ViewModel with a reference to the application.
     * Initializes the database and loads conversions into memory.
     *
     * @param application The application context.
     */
    public ConversionViewModel(Application application) {
        super(application);
        db = Room.databaseBuilder(application, AppDatabase.class, "conversion-database")
                .fallbackToDestructiveMigration()
                .build();
        loadConversions();
    }

    /**
     * Returns the list of conversions wrapped in LiveData.
     *
     * @return LiveData containing the list of conversions.
     */
    public LiveData<List<Conversion>> getConversions() {
        return conversions;
    }

    /**
     * Loads the conversions from the database asynchronously.
     * Once loaded, the data is posted to the MutableLiveData object to notify any observers.
     */
    private void loadConversions() {
        new Thread(() -> {
            try {
                List<Conversion> conversionList = db.conversionDao().getAll();
                conversions.postValue(conversionList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Clears all conversions from the database asynchronously.
     * After deletion, the list of conversions is reloaded to update any observers.
     */
    public void clearAllConversions() {
        new Thread(() -> {
            try {
                db.conversionDao().deleteAll();
                loadConversions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Inserts a new conversion into the database asynchronously.
     *
     * @param conversion The conversion object to be inserted.
     */
    public void insert(Conversion conversion) {
        new Thread(() -> {
            try {
                db.conversionDao().insert(conversion);
                loadConversions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Lifecycle method that's called when the ViewModel is being cleared.
     * Used to close the database connection.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * Updates an existing conversion in the database asynchronously.
     *
     * @param conversion The conversion object with updated values.
     */
    public void update(Conversion conversion) {
        new Thread(() -> {
            try {
                db.conversionDao().update(conversion);
                loadConversions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
