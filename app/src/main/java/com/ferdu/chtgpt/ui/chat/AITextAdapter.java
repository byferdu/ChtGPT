package com.ferdu.chtgpt.ui.chat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.models.data.ChatModel;
import com.ferdu.chtgpt.ui.home.HistoryFragment;
import com.ferdu.chtgpt.util.MyUtil;
import com.ferdu.chtgpt.util.interfces.MyItemClickListener;
import com.ferdu.chtgpt.util.interfces.MyItemLongClick;
import com.xiaoguang.selecttext.SelectTextHelper;

import java.util.List;

import io.noties.markwon.Markwon;

public class AITextAdapter extends RecyclerView.Adapter<AITextAdapter.MyViewHolder> {
    private final Context context;
    private final List<ChatModel> list;
    private static MyItemLongClick itemLongClick;
    public static boolean isMultiSelectMode = false;
    private  MyItemClickListener<ChatModel> clickListener;
    public static int selectAll = 0;

    public List<ChatModel> getList() {
        return list;
    }
    public AITextAdapter(Context context, List<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public AITextAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 根据布局类型返回不同的 ViewHolder
        final String[] selectText = {""};
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ai_text_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);

        @SuppressLint("NotifyDataSetChanged") SelectTextHelper mSelectableTextHelper = new SelectTextHelper
                .Builder(myViewHolder.textView)// 放你的textView到这里！！
                .setCursorHandleColor(ContextCompat.getColor(context, R.color.gradient))// 游标颜色 default 0xFF1379D6
                .setCursorHandleSizeInDp(24)// 游标大小 单位dp default 24
                .setSelectedColor(ContextCompat.getColor(context, R.color.message))// 选中文本的颜色 default 0xFFAFE1F4
                .setMagnifierShow(true)// 放大镜 default true
                .setPopSpanCount(5)// 设置操作弹窗每行个数 default 5
                .setPopStyle(R.drawable.copy_back/*操作弹窗背景*/, com.xiaoguang.selecttext.R.drawable.ic_arrow/*箭头图片*/)// 设置操作弹窗背景色、箭头图片
                .addItem(0, "复制"/*item的描述*/,
                        () -> {
                            Log.i("SelectTextHelper", "复制");
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(ClipData.newPlainText("text", selectText[0]));//text也可以是"null"
                            if (cm.hasPrimaryClip()) {
                                cm.getPrimaryClip().getItemAt(0).getText();
                            }
                            /*item的回调*/
                        })// 操作弹窗的每个item
                .addItem(0, "多选", () -> {
                    isMultiSelectMode = true;
                    itemLongClick.OnItemLongClick(myViewHolder.getAdapterPosition());
                    myViewHolder.aiCheckBox.setChecked(true);
                    notifyDataSetChanged();
                })
                .build();
        @SuppressLint("NotifyDataSetChanged") SelectTextHelper mSelectableTextHelper2 = new SelectTextHelper
                .Builder(myViewHolder.textView2)// 放你的textView到这里！！
                .setCursorHandleColor(ContextCompat.getColor(parent.getContext(), R.color.gradient))// 游标颜色 default 0xFF1379D6
                .setCursorHandleSizeInDp(24)// 游标大小 单位dp default 24
                .setSelectedColor(ContextCompat.getColor(context, R.color.message))// 选中文本的颜色 default 0xFFAFE1F4
                .setSelectAll(true)// 初次选中是否全选 default true
                // .setScrollShow(false)// 滚动时是否继续显示 default true
                // 已经全选无弹窗，设置了true在监听会回调 onSelectAllShowCustomPop 方法 default false
                .setMagnifierShow(true)// 放大镜 default true
                .setPopSpanCount(5)// 设置操作弹窗每行个数 default 5
                .setPopStyle(R.drawable.copy_back/*操作弹窗背景*/, com.xiaoguang.selecttext.R.drawable.ic_arrow/*箭头图片*/)// 设置操作弹窗背景色、箭头图片
                //.setSelectTextLength(2)// 首次选中文本的长度，需要设置setSelectAll(false) default 2
                //.setPopSpanCount(1)// 弹窗延迟时间 default 100毫秒
                .addItem(0/*item的图标*/, "复制"/*item的描述*/,
                        () -> {
                            Log.i("SelectTextHelper", "复制");
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(ClipData.newPlainText("text", selectText[0]));//text也可以是"null"
                            if (cm.hasPrimaryClip()) {
                                cm.getPrimaryClip().getItemAt(0).getText();
                            }
                            /*item的回调*/
                        })// 操作弹窗的每个item
                .addItem(0, "多选", () -> {
                    // myViewHolder.checkBox.setVisibility(View.VISIBLE);
                    isMultiSelectMode = true;
                    itemLongClick.OnItemLongClick(myViewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                })
                .build();

        mSelectableTextHelper.setSelectListener(new SelectTextHelper.OnSelectListener() {
            @Override
            public void onClick(View v) {
                mSelectableTextHelper2.reset();
            }

            @Override
            public void onLongClick(View v) {
                // postShowCustomPop(SHOW_DELAY);
            }

            @Override
            public void onTextSelected(CharSequence content) {
                selectText[0] = (String) content;
            }

            @Override
            public void onDismiss() {
                selectText[0] = "";
                mSelectableTextHelper.reset();
            }

            @Override
            public void onClickUrl(String url) {

            }

            @Override
            public void onSelectAllShowCustomPop() {
                mSelectableTextHelper.selectAll();
            }

            @Override
            public void onReset() {

            }

            @Override
            public void onDismissCustomPop() {

            }

            @Override
            public void onScrolling() {

            }
        });
        mSelectableTextHelper2.setSelectListener(new SelectTextHelper.OnSelectListener() {
            @Override
            public void onClick(View v) {
                mSelectableTextHelper2.reset();
            }

            @Override
            public void onLongClick(View v) {
                // postShowCustomPop(SHOW_DELAY);
            }

            @Override
            public void onTextSelected(CharSequence content) {
                selectText[0] = (String) content;
            }

            @Override
            public void onDismiss() {
                selectText[0] = "";
            }

            @Override
            public void onClickUrl(String url) {

            }

            @Override
            public void onSelectAllShowCustomPop() {

            }

            @Override
            public void onReset() {

            }

            @Override
            public void onDismissCustomPop() {

            }

            @Override
            public void onScrolling() {

            }
        });
            getRecyclerView().setOnClickListener(v -> {
                mSelectableTextHelper.destroy();
                mSelectableTextHelper2.destroy();
            });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Markwon markwon = MyUtil.getMarkDown(context);
        holder.itemView.setTag(R.id.chat_position, position);
        holder.onBind(list);
        if (list.get(position).getMeText().equals("Something wrong! Please try again later!")) {
            holder.textView2.setTextColor(0x00ff00);
        }
        if (isMultiSelectMode) {
            holder.aiCheckBox.setVisibility(View.VISIBLE);
        } else holder.aiCheckBox.setVisibility(View.GONE);
        boolean equals=false;
        if (HistoryFragment.recyclerViewOnClickData != null) {
            equals = Boolean.TRUE.equals(HistoryFragment.recyclerViewOnClickData.get(position));
        }
        holder.aiCheckBox.setChecked(isMultiSelectMode && equals);
        if (isMultiSelectMode && selectAll != 0) {
            if (selectAll == 1)
                holder.aiCheckBox.setChecked(true);//如果当前为长按状态则让长按选择图片显示
            else if (selectAll == 2) holder.aiCheckBox.setChecked(false);
        }
        markwon.setMarkdown(holder.textView2, list.get(position).getAIText());
        holder.textView.setText(list.get(position).getMeText());
        holder.textView3.setText("提问词元: " + list.get(position).getPrompt_tokens());
        holder.textView4.setText("完成词元: " + list.get(position).getCompletion_tokens());
        holder.textView5.setText("总共词元: " + list.get(position).getTotal_tokens());
        holder.textView30.setText(list.get(position).getModel());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemLongClick(MyItemLongClick itemLongClick) {
        AITextAdapter.itemLongClick = itemLongClick;
    }

    public void setClickListener(MyItemClickListener<ChatModel> clickListener) {
        this.clickListener = clickListener;
    }

    //删除Notes
    public void removeNotes(int position) {
        synchronized (list) {
            list.remove(position);
        }
        notifyItemRemoved(position);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView30;
        CheckBox aiCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView2);
            textView2 = itemView.findViewById(R.id.textView3);
            textView3 = itemView.findViewById(R.id.textView7);
            textView4 = itemView.findViewById(R.id.textView8);
            textView5 = itemView.findViewById(R.id.textView9);
            textView30 = itemView.findViewById(R.id.textView30);
            aiCheckBox = itemView.findViewById(R.id.aiCheckBox);
        }

        public void onBind(List<ChatModel> chatModel) {
            int position = (int) itemView.getTag(R.id.chat_position);
            aiCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                clickListener.onItemClicked(chatModel.get(position), isChecked, position);
            });
        }

    }


}
