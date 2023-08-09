package algonquin.cst2335.currencyconverter.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) for the Conversion entity.
 * Provides methods for performing CRUD operations on the conversion table in the database.
 * <p>
 * This interface is used by Room to create an implementation for accessing the conversion database.
 * Each method is annotated with a SQL query that represents the desired database operation.
 * </p>
 */
@Dao
public interface ConversionDao {

    /**
     * Retrieves all conversions stored in the database.
     *
     * @return A list of all Conversion objects.
     */
    @Query("SELECT * FROM conversion")
    List<Conversion> getAll();

    /**
     * Retrieves a specific conversion by its unique ID.
     *
     * @param conversionId The unique ID of the desired Conversion.
     * @return The Conversion object with the specified ID.
     */
    @Query("SELECT * FROM conversion WHERE id = :conversionId")
    Conversion getById(int conversionId);

    /**
     * Inserts a new conversion into the database.
     * If the conversion already exists, it will not overwrite it.
     *
     * @param conversion The Conversion object to be inserted.
     */
    @Insert
    void insert(Conversion conversion);

    /**
     * Updates an existing conversion in the database.
     * The existing record with the same primary key as the provided Conversion object gets updated.
     *
     * @param conversion The Conversion object with updated values.
     */
    @Update
    void update(Conversion conversion);

    /**
     * Deletes a specific conversion from the database.
     *
     * @param conversion The Conversion object to be deleted.
     */
    @Delete
    void delete(Conversion conversion);

    /**
     * Deletes all conversions stored in the database.
     */
    @Query("DELETE FROM Conversion")
    void deleteAll();
}
