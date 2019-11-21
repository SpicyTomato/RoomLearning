package com.spicytomato.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class},version = 1,exportSchema = false)
public abstract class WordDataBase extends RoomDatabase {

    private static WordDataBase INSTANCE;

    public static WordDataBase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (WordDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),WordDataBase.class,"word_dataBase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();

}
