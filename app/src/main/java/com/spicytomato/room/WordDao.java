package com.spicytomato.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Delete
    void delete(Word...words);

    @Insert
    void Insert(Word...words);

    @Query("SELECT *FROM word ORDER BY id DESC")
    LiveData<List<Word>> getAllWords();

    @Query("DELETE FROM word")
    void deleteAll();
}
