package com.spicytomato.room;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
            itemView = layoutInflater.inflate(R.layout.cell_cardview_2,parent,false);
        }

        final MyViewHolder holder = new MyViewHolder(itemView);

        holder.itemView.findViewById(R.id.constraintLayoutWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://fanyi.baidu.com/translate?aldtype=16047&query=&keyfrom=baidu&smartresult=dict&lang=auto2zh#zh/en/"+holder.textViewEnglishWord.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.aSwitchInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //因为getTag获取的是广义的Object对象
                //所以需要强制转换
                Word word = (Word) holder.itemView.getTag(R.id.word_view_holed);
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


        return holder;
    }

    @Override
    //经常被呼叫的地方
    //如果把监听器 Listener 放到这里面会导致性能降低
    //放到onCreate中比较好
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = AllWords.get(position);

        holder.itemView.setTag(R.id.word_view_holed,word);//参数类型 第一个放key 保证唯一性 创建id来保证
        //setTag 可以放任意一个对象
        //其他地方getTag就能调用

        holder.textViewNumber.setText(String.valueOf(position+1));
        holder.textViewChineseWord.setText(word.getChineseWord());
        holder.textViewEnglishWord.setText(word.getEnglishWord());
//        holder.itemView.findViewById(R.id.constraintLayoutWord).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("https://fanyi.baidu.com/translate?aldtype=16047&query=&keyfrom=baidu&smartresult=dict&lang=auto2zh#zh/en/"+holder.textViewEnglishWord.getText());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(uri);
//                holder.itemView.getContext().startActivity(intent);
//            }
//        });

        //因为每次调用 setChecked 都会调用 setOnCheckedChangeListener
        //这样会导致Bug
        //所以现将 setOnCheckedChangeListener 设置为空
        //这样就意味着 仅仅只是改变 switch 的 开关状态 不调用事件
        //能减少 Bug 出现
//        holder.aSwitchInvisible.setOnCheckedChangeListener(null);

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
//        holder.aSwitchInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    holder.textViewChineseWord.setVisibility(View.GONE);
//                    word.setChineseInvisible(true);
//                    myViewModel.update(word);
//                }else {
//                    holder.textViewChineseWord.setVisibility(View.VISIBLE);
//                    word.setChineseInvisible(false);
//                    myViewModel.update(word);
//                }
//            }
//        });

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
            textViewEnglishWord = itemView.findViewById(R.id.textViewEnglishWord);
            textViewChineseWord = itemView.findViewById(R.id.textViewChineseWord);
            //imageButton = itemView.findViewById(R.id.imageButton);
            aSwitchInvisible = itemView.findViewById(R.id.switchInvisible);
        }
    }
}
