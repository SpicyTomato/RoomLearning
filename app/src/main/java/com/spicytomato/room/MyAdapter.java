package com.spicytomato.room;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Word> AllWords = new ArrayList<>();
    private boolean isCard = false;
    private MyViewModel myViewModel;

    MyAdapter(boolean isCard,MyViewModel myViewModel){
        this.isCard = isCard;
        this.myViewModel = myViewModel;
    }

    void setAllWords(List<Word> allWords) {
        AllWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (!isCard) {
            itemView = layoutInflater.inflate(R.layout.cell_normal_2, parent, false);
        }else {
            itemView = layoutInflater.inflate(R.layout.cell_cardview,parent,false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = AllWords.get(position);

        holder.textViewNumber.setText(String.valueOf(position+1));
        holder.textViewChineseWord.setText(word.getChineseWord());
        holder.textViewEnglishWord.setText(word.getEnglishWord());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://fanyi.baidu.com/translate?aldtype=16047&query=&keyfrom=baidu&smartresult=dict&lang=auto2zh#zh/en/"+holder.textViewEnglishWord.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        //因为每次调用 setChecked 都会调用 setOnCheckedChangeListener
        //这样会导致Bug
        //所以现将 setOnCheckedChangeListener 设置为空
        //这样就意味着 仅仅只是改变 switch 的 开关状态 不调用事件
        //能减少 Bug 出现
        holder.aSwitchInvisible.setOnCheckedChangeListener(null);

        //当加载页面的时候
        //首先看对象中的 isChineseInvisible 从而设置页面的按钮状态
        if (word.isChineseInvisible()){
            holder.textViewChineseWord.setVisibility(View.GONE);
            holder.aSwitchInvisible.setChecked(true);
        }else {
            holder.textViewChineseWord.setVisibility(View.VISIBLE);
            holder.aSwitchInvisible.setChecked(false);
        }

        //当按下按钮的时候发生的事件处理
        holder.aSwitchInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.textViewChineseWord.setVisibility(View.GONE);
                    word.setChineseInvisible(true);
                    myViewModel.update(word);
                }else {
                    holder.textViewChineseWord.setVisibility(View.VISIBLE);
                    word.setChineseInvisible(false);
                    myViewModel.update(word);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return AllWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber,textViewEnglishWord,textViewChineseWord;
        //ImageButton imageButton;
        Switch aSwitchInvisible;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglishWord = itemView.findViewById(R.id.textViewEnglish);
            textViewChineseWord = itemView.findViewById(R.id.textViewChinese);
            //imageButton = itemView.findViewById(R.id.imageButton);
            aSwitchInvisible = itemView.findViewById(R.id.switchInvisible);
        }
    }
}
