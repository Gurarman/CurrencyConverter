package algonquin.cst2335.currencyconverter.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Conversion.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ConversionDao conversionDao();
}
