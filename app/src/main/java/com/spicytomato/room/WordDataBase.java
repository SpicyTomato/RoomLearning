package com.spicytomato.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class},version = 2,exportSchema = false)
public abstract class WordDataBase extends RoomDatabase {

    private static WordDataBase INSTANCE;

    public static WordDataBase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (WordDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),WordDataBase.class,"word_dataBase")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();


    static final Migration MIGRATION_1_2  = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 0");
        }
    };
}
