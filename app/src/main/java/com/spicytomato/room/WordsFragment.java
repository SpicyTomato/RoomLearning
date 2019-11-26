package com.spicytomato.room;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends Fragment {
    MyViewModel myViewModel;
    RecyclerView recyclerView;
    MyAdapter myAdapter_normal,myAdapter_card;
    FloatingActionButton floatingActionButton;
    LiveData<List<Word>> filiterWord;
    private static final String VIEW_TYPE_SHP = "view_type_shp";
    private static final String VIEW_TYPE_CARD = "view_type_card";
    private List<Word> nowWords;
    boolean isUndo;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemClear:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("清空列表");
                builder.setPositiveButton("我想好了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myViewModel.deleteAll();
                    }
                });
                builder.setNegativeButton("还是算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.itemSwitch:
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP,Context.MODE_PRIVATE);
                boolean viewType = sharedPreferences.getBoolean(VIEW_TYPE_CARD,false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (recyclerView.getAdapter() == myAdapter_normal){
                    recyclerView.setAdapter(myAdapter_card);
                    editor.putBoolean(VIEW_TYPE_CARD,true);
                }else {
                    recyclerView.setAdapter(myAdapter_normal);
                    editor.putBoolean(VIEW_TYPE_CARD,false);
                }
                editor.apply();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(700);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                //一个对象不能同时出现两个 Observe 不然会出现 BUG
                filiterWord.removeObservers(requireActivity());
                filiterWord = myViewModel.getPatternWord(pattern);
                filiterWord.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = myAdapter_normal.getItemCount();
                        nowWords = words;
//                        myAdapter_normal.setAllWords(words);
//                        myAdapter_card.setAllWords(words);
                        if (temp != words.size()){
                            myAdapter_normal.submitList(words);
                            myAdapter_card.submitList(words);
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = requireActivity();

        myViewModel = ViewModelProviders.of(activity).get(MyViewModel.class);

        recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //更新序列号
        //在动画完成之后设置
        recyclerView.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();

                    for (int i = firstPosition;i<=lastPosition;i++) {
                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder != null) {
                            holder.textViewNumber.setText(String.valueOf(i + 1));
                        }
                    }
                    if (!isUndo) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        });
        //recyclerView.smoothScrollToPosition(0);

        myAdapter_normal = new MyAdapter(false,myViewModel);
        myAdapter_card = new MyAdapter(true,myViewModel);

//        recyclerView.setAdapter(myAdapter_normal);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP,Context.MODE_PRIVATE);
        boolean viewType = sharedPreferences.getBoolean(VIEW_TYPE_CARD,false);
        if (viewType){
            recyclerView.setAdapter(myAdapter_card);
        }else {
            recyclerView.setAdapter(myAdapter_normal);
        }

        floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });

        filiterWord = myViewModel.getAllWords();
        filiterWord.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter_normal.getItemCount();
                nowWords = words;
//                myAdapter_normal.setAllWords(words);
//                myAdapter_card.setAllWords(words);
                if (temp != words.size()){
//                    myAdapter_normal.notifyDataSetChanged();
//                    myAdapter_card.notifyDataSetChanged();
//                    myAdapter_normal.notifyItemInserted(0);
                    if (temp < words.size()){
                        isUndo = false;
                    }else {
                        isUndo = true;
                    }

                    myAdapter_normal.submitList(words);//提交的数据列表会在后台进行差异比较 根据比较结果来刷新页面
                    myAdapter_card.submitList(words);
                }
//                if (temp < words.size() && !isUndo){
//                    recyclerView.smoothScrollToPosition(0);
//                }
//                isUndo = false;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word word = nowWords.get(viewHolder.getAdapterPosition());
                myViewModel.delete(word);
                Snackbar.make(requireActivity().findViewById(R.id.fragment_words),"我还不想走",Snackbar.LENGTH_LONG)
                        .setAction("回来吧", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isUndo = true;
                                myViewModel.insert(word);
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getView() != null) {
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            super.onResume();
        }
    }
}
