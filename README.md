# RoomLearning

出现的问题❓

(已解决⭕️)在ViewModel中
                
                //为什么不能使用 判断总个数是否增加 来调用 notifyDataSetChanged
                //主要表现为 按键有反应 但是无法更新视图（应该）
                myAdapter_normal.notifyDataSetChanged();
                myAdapter_card.notifyDataSetChanged();
                
因为                   
     （1）首先获取没有改变的 myAdapter 中的 Word 对象数目
     （2）再对 Adapter 们进行修改
     （3）比较改变前后 Word 对象数目是否发生改变
                        int temp = myAdapter_normal.getItemCount();
                        myAdapter_normal.setAllWords(words);
                        myAdapter_card.setAllWords(words);
                        if (temp != words.size()){
                            myAdapter_normal.notifyDataSetChanged();
                            myAdapter_card.notifyDataSetChanged();
                        }




注意⚠️

//在 cell 视图中 不应该将 height 设置为 Wrap_content 
//因为每次使用 View.GONE 时 都会改变 高度 



在Adapter中
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

Adapter 的改进
            原因：
                （1）因为在 onBindViewHolder 中 每次都会更新 itemView 所以将 itemView 的监听器设置在这里 会很浪费性能 
                （因为每次都会创造一个新的监听器对象）
                所以 将监听器的创建 放在 onCreateViewHolder （每当创建一个 itemView 时运行该块代码）
                （2）传递 position 
                1⃣️可以用 holder.getAdapterPosition();
                2⃣️可以用 
                        setTag 可以传递任何一个对象
                        
                        final Word word = AllWords.get(position);
                        holder.itemView.setTag(R.id.word_view_holed,word);//参数类型 第一个放key 保证唯一性 创建id来保证
                        //setTag 可以放任意一个对象
                        //其他地方getTag就能调用
                        
                        用getTag来接收
                        
                        Word word = (Word) holder.itemView.getTag(R.id.word_view_holed);
                
             
            
Search 
            用LiveData<List<Word>> filiterWord 来设置 Observe 从而实现即使刷新 reyclerView
  
            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                //一个对象不能同时出现两个 Observe 不然会出现 BUG
                filiterWord.removeObservers(requireActivity());
                
                🌟filiterWord = myViewModel.getPatternWord(pattern);🌟
                
                filiterWord.observe(requireActivity(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = myAdapter_normal.getItemCount();
                        myAdapter_normal.setAllWords(words);
                        myAdapter_card.setAllWords(words);
                        if (temp != words.size()){
                            myAdapter_normal.notifyDataSetChanged();
                            myAdapter_card.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });
        
        
WordFragment 问题改进：
                    Observe 中的
                    
     * @param owner    The LifecycleOwner which controls the observer
     * @param observer The observer that will receive the events
     */
    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) { 
    
  
              第一个参数应该是 LifecycOwner 
              而 requireActivity 在程序中不会被销毁 所以当每次切换视图创建 Observe 时 会导致多个重叠
              所以这里应该传递 WordsFragment 的LifecyclerOwner 
              用getViewLifecyclerOwner()；





















