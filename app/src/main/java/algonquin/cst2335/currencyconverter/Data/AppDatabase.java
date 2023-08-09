package algonquin.cst2335.currencyconverter.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Abstract representation of the application's SQLite database.
 * <p>
 * This class represents the application's Room database and serves as the main access point
 * for the underlying connection to your app's persisted relational data.
 * </p>
 *
 * <p>
 * The database class provides DAO (Data Access Object) methods for performing database operations.
 * This class is annotated with {@code @Database}, marking it as a Room database.
 * </p>
 *
 * <p>
 * The list of entities included in the database is defined by the {@code entities} attribute
 * of the {@code @Database} annotation. In this case, only {@link Conversion} is listed.
 * </p>
 *
 * @see Conversion
 * @see ConversionDao
 */
@Database(entities = {Conversion.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Retrieves the DAO (Data Access Object) associated with the {@link Conversion} entity.
     *
     * @return An instance of the {@link ConversionDao} to perform CRUD operations on the {@link Conversion} entity.
     */
    public abstract ConversionDao conversionDao();
}
