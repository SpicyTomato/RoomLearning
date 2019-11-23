package com.spicytomato.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyViewModel myViewModell;
    MyAdapter myAdapter_normal,myAdapter_card;
    Button buttonInsert,buttonDelete;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = findViewById(R.id.switchCard);

        myViewModell = ViewModelProviders.of(this).get(MyViewModel.class);


        recyclerView = findViewById(R.id.reclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter_normal = new MyAdapter(false,myViewModell);
        myAdapter_card = new MyAdapter(true,myViewModell);
        recyclerView.setAdapter(myAdapter_normal);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    recyclerView.setAdapter(myAdapter_card);
                }else {
                    recyclerView.setAdapter(myAdapter_normal);
                }
            }
        });


        buttonInsert = findViewById(R.id.buttonInsert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] english = {
                        "Hello",
                        "World",
                        "Android",
                        "Google",
                        "Studio",
                        "Project",
                        "Database",
                        "Recycler",
                        "View",
                        "String",
                        "Value",
                        "Integer"
                };
                String[] chinese = {
                        "你好",
                        "世界",
                        "安卓系统",
                        "谷歌公司",
                        "工作室",
                        "项目",
                        "数据库",
                        "回收站",
                        "视图",
                        "字符串",
                        "价值",
                        "整数类型"
                };
                for (int i = 0; i<english.length;i++){
                    myViewModell.insert(new Word(english[i],chinese[i]));
                }
            }
        });

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModell.deleteAll();
            }
        });

        myViewModell.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                myAdapter_normal.setAllWords(words);
                myAdapter_card.setAllWords(words);
                //为什么不能使用 判断总个数是否增加 来调用 notifyDataSetChanged
                //主要表现为 按键有反应 但是无法更新视图（应该）
                myAdapter_normal.notifyDataSetChanged();
                myAdapter_card.notifyDataSetChanged();
//                if (myAdapter_normal.getItemCount() != words.size()){
//                    myAdapter_normal.notifyDataSetChanged();
//                    myAdapter_card.notifyDataSetChanged();
//                }
//            }
            }
        });




//        buttonInsert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] english = {
//                        "Hello",
//                        "World",
//                        "Android",
//                        "Google",
//                        "Studio",
//                        "Project",
//                        "Database",
//                        "Recycler",
//                        "View",
//                        "String",
//                        "Value",
//                        "Integer"
//                };
//                String[] chinese = {
//                        "你好",
//                        "世界",
//                        "安卓系统",
//                        "谷歌公司",
//                        "工作室",
//                        "项目",
//                        "数据库",
//                        "回收站",
//                        "视图",
//                        "字符串",
//                        "价值",
//                        "整数类型"
//                };
//                for (int i = 0; i<english.length;i++){
//                    myViewModell.insert(new Word(english[i],chinese[i]));
//                }
//            }
//        });

//        buttonDelete.findViewById(R.id.buttonDelete);
//
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });


    }


}
