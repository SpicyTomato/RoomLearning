package com.spicytomato.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyViewModel extends AndroidViewModel {
    WordRepository wordRepository;


    public MyViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getAllWords() {
        return wordRepository.AllWords;
    }

    public void insert(Word...words){
        wordRepository.insert(words);
    }

    public void delete(Word...words){
        wordRepository.delete(words);
    }

    public void deleteAll(){
        wordRepository.deleteAll();
    }
}
