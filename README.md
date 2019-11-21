# RoomLearning

出现的问题❓

在ViewModel中
                
                //为什么不能使用 判断总个数是否增加 来调用 notifyDataSetChanged
                //主要表现为 按键有反应 但是无法更新视图（应该）
                myAdapter_normal.notifyDataSetChanged();
                myAdapter_card.notifyDataSetChanged();



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





















