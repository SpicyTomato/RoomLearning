package com.spicytomato.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    LiveData<List<Word>> AllWords;
    WordDao wordDao;

    WordRepository(Context context){
        WordDataBase wordDataBase = WordDataBase.getInstance(context);
        wordDao = wordDataBase.getWordDao();
        AllWords = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return AllWords;
    }

    void insert(Word...words){
        new Insert(wordDao).execute(words);
    }

    void delete(Word...words){
        new Delete(wordDao).execute(words);
    }

    void deleteAll(Word...words){
        new DeleteAll(wordDao).execute();
    }

    void update(Word...words){
        new Update(wordDao).execute(words);
    }

    static class Insert extends AsyncTask<Word,Void,Void>{
        WordDao wordDao;

        Insert(WordDao wordDao){
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.Insert(words);
            return null;
        }
    }

    static class DeleteAll extends AsyncTask<Void,Void,Void>{
        WordDao wordDao;

        DeleteAll(WordDao wordDao){
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAll();
            return null;
        }
    }

    static class Delete extends AsyncTask<Word,Void,Void>{
        WordDao wordDao;

        Delete(WordDao wordDao){
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.delete(words);
            return null;
        }
    }

    static class Update extends AsyncTask<Word,Void,Void>{
        WordDao wordDao;
        Update(WordDao wordDao){
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.Update(words);
            return null;
        }
    }
}
