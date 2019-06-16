package emschenn.csie.ncku.webparsetest;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface cardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(cardData card);

    @Query("UPDATE card_table SET episode = :episode WHERE card = :title")
    void update(String title, String episode);

    @Query("DELETE FROM card_table")
    void deleteAll();

    @Delete
    void deleteCard(cardData card);

    @Query("SELECT * from card_table LIMIT 1")
    cardData[] getAnyCard();

    @Query("SELECT * from card_table ORDER BY card ASC")
    LiveData<List<cardData>> getAllCards();

}
