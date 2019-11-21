package com.spicytomato.room;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Word> AllWords = new ArrayList<>();
    private boolean isCard = false;

    MyAdapter(boolean isCard){
        this.isCard = isCard;
    }

    void setAllWords(List<Word> allWords) {
        AllWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (isCard == false) {
            itemView = layoutInflater.inflate(R.layout.cell_normal, parent, false);
        }else {
            itemView = layoutInflater.inflate(R.layout.cell_cardview,parent,false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Word word = AllWords.get(position);

        holder.textViewNumber.setText(String.valueOf(position+1));
        holder.textViewChineseWord.setText(word.getChineseWord());
        holder.textViewEnglishWord.setText(word.getEnglishWord());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://fanyi.baidu.com/translate?aldtype=16047&query=&keyfrom=baidu&smartresult=dict&lang=auto2zh#zh/en/"+holder.textViewEnglishWord.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return AllWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber,textViewEnglishWord,textViewChineseWord;
        ImageButton imageButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglishWord = itemView.findViewById(R.id.textViewEnglish);
            textViewChineseWord = itemView.findViewById(R.id.textViewChinese);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
    }
}
