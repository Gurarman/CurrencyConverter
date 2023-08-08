package algonquin.cst2335.currencyconverter.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ConversionDao {

    @Query("SELECT * FROM conversion")
    List<Conversion> getAll();

    @Query("SELECT * FROM conversion WHERE id = :conversionId")
    Conversion getById(int conversionId);

    @Insert
    void insert(Conversion conversion);

    @Update
    void update(Conversion conversion);

    @Delete
    void delete(Conversion conversion);

    @Query("DELETE FROM Conversion")
    void deleteAll();
}
