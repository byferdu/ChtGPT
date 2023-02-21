package com.ferdu.chtgpt.ui.chat;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
    public  boolean isError = false;
    public static boolean isMultiSelectMode = false;
    private MyItemClickListener<ChatModel> clickListener;
    public static int selectAll = 0;
    private AnimatorSet mAnimatorSet;
    private AnimationSet animationSet;
    public CountDownTimer mTimer;
    public boolean isTimerFinished = true;
    private int setAlphaInt;

    public List<ChatModel> getList() {
        return list;
    }

    public AITextAdapter(Context context, List<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    private View recyclerView;

    public View getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(View recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public AITextAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 根据布局类型返回不同的 ViewHolder

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ai_text_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        SelectTextHelper selectTextHelper = setSelectAndPop(holder, holder.textView);
        SelectTextHelper selectTextHelper1 = setSelectAndPop(holder, holder.textView2);
        getRecyclerView().clearAnimation();

        //Log.d("RECTAG", "onTick: "+millisUntilFinished);
        // 创建 AlphaAnimation 对象，并设置动画持续时间和重复次数



        getRecyclerView().setOnClickListener(v -> {
            Log.d("RECTAG", "onCreateViewHolder: Click");
            selectTextHelper.destroy();
            selectTextHelper1.destroy();
        });
        itemView.setOnClickListener(v -> {
            Log.d("RECTAG", "onCreateViewHolder: Click");
            selectTextHelper.reset();
            selectTextHelper.destroy();
            selectTextHelper1.reset();
            selectTextHelper1.destroy();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Markwon markwon = MyUtil.getMarkDown(context);
        holder.itemView.setTag(R.id.chat_position, position);
        holder.onBind(list, position);
        isTimerFinished = false;
        mTimer = new CountDownTimer(Long.MAX_VALUE, 400) {
            @Override
            public void onTick(long millisUntilFinished) {
                holder.mTypingDot3.setAlpha(alphaFloat());
                holder.mTypingDot2.setAlpha(alphaFloat());
                holder.mTypingDot1.setAlpha(alphaFloat());
                isTimerFinished = false;
                Log.d("RECTAG", "onTick: " + millisUntilFinished);
                alphaFloat();
            }

            @Override
            public void onFinish() {
                if (isError) {
                    holder.textView2.setTextColor(Color.RED);
                    isError = false;
                }else holder.textView2.setTextColor(context.getResources().getColor(R.color.black_white));
                holder.typingContainer.setVisibility(View.GONE);
                holder.aiTextContainer.setVisibility(View.VISIBLE);
                markwon.setMarkdown(holder.textView2, "Error");
                isTimerFinished = true;
            }
        };
        // 开始动画
        // animationSet.start();
        //  mAnimatorSet.resume();
        if (isMultiSelectMode) {
            holder.aiCheckBox.setVisibility(View.VISIBLE);
        } else holder.aiCheckBox.setVisibility(View.GONE);
        boolean equals = false;
        if (HistoryFragment.recyclerViewOnClickData != null) {
            equals = Boolean.TRUE.equals(HistoryFragment.recyclerViewOnClickData.get(position));
        }
        holder.aiCheckBox.setChecked(isMultiSelectMode && equals);
        if (isMultiSelectMode && selectAll != 0) {
            if (selectAll == 1) holder.aiCheckBox.setChecked(true);//如果当前为长按状态则让长按选择图片显示
            else if (selectAll == 2) holder.aiCheckBox.setChecked(false);
        }


        holder.textView.setText(list.get(position).getMeText());
        if (list.get(list.size() - 1).getAIText() == null) {
            holder.typingContainer.setVisibility(View.VISIBLE);
            holder.aiTextContainer.setVisibility(View.GONE);
            mTimer.start();

            return;
        }
        holder.typingContainer.setVisibility(View.GONE);
        holder.aiTextContainer.setVisibility(View.VISIBLE);
        mTimer.cancel();
        isTimerFinished = true;
        // animationSet.cancel();
        setAlphaInt=0;
        if (isError) {
            holder.textView2.setTextColor(Color.RED);
            isError = false;
        } else holder.textView2.setTextColor(context.getResources().getColor(R.color.black_white));
        markwon.setMarkdown(holder.textView2, list.get(position).getAIText());
        holder.textView3.setText("提问: " + list.get(position).getPrompt_tokens());
        holder.textView4.setText("完成: " + list.get(position).getCompletion_tokens());
        holder.textView5.setText("总共: " + list.get(position).getTotal_tokens());
        holder.textView30.setText(list.get(position).getModel());
    }

    public void onDestroy() {
        setAlphaInt=0;
        if (mTimer != null) {
            mTimer.cancel();
        }
        isError = false;
        mTimer = null;
        isTimerFinished = true;
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

    public void update(ChatModel chatModels) {
        int size = list.size();
        list.add(chatModels);
        notifyItemRangeInserted(size, 1);
    }



    public void updateList2(ChatModel chatModels) {
        int size = list.size();
        if (size < 1) {
            return;
        }
        list.remove(size - 1);
        notifyItemRangeRemoved(size - 1, 1);
        list.add(chatModels);
        notifyItemRangeInserted(list.size() - 1, 1);
    }

    //删除Notes
    public void removeNotes(int position) {
        synchronized (list) {
            list.remove(position);
        }
        notifyItemRemoved(position);
    }

    private float alphaFloat() {
        float alphaFloat;

        if (setAlphaInt % 3==0) {
            alphaFloat = 0.7f;
        } else if (setAlphaInt % 2 == 0) {
            alphaFloat = 0.3f;
        } else {
            alphaFloat = 0.3f;
        }
        setAlphaInt++;
        return alphaFloat;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView30;
        CheckBox aiCheckBox;
        View aiTextContainer;
        TextView hintText;
        LinearLayout typingContainer;
        View mTypingDot1;
        View mTypingDot2;
        View mTypingDot3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView2);
            textView2 = itemView.findViewById(R.id.textView3);
            textView3 = itemView.findViewById(R.id.textView7);
            textView4 = itemView.findViewById(R.id.textView8);
            textView5 = itemView.findViewById(R.id.textView9);
            textView30 = itemView.findViewById(R.id.textView30);
            hintText = itemView.findViewById(R.id.hintText);
            typingContainer = itemView.findViewById(R.id.typing_indicator_container);
            aiCheckBox = itemView.findViewById(R.id.aiCheckBox);
            aiTextContainer = itemView.findViewById(R.id.ai_text_container);
            mTypingDot1 = itemView.findViewById(R.id.typing_dot_1);
            mTypingDot2 = itemView.findViewById(R.id.typing_dot_2);
            mTypingDot3 = itemView.findViewById(R.id.typing_dot_3);
        }

        public void onBind(List<ChatModel> chatModel, int position) {
            int position2 = (int) itemView.getTag(R.id.chat_position);
            aiCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                clickListener.onItemClicked(chatModel.get(position), isChecked, position);
            });
        }

    }

    private SelectTextHelper setSelectAndPop(MyViewHolder myViewHolder, TextView textView) {
        // 开始动画
        final String[] selectText = {""};
        @SuppressLint("NotifyDataSetChanged") SelectTextHelper mSelectableTextHelper = new SelectTextHelper.Builder(textView).setCursorHandleColor(ContextCompat.getColor(context, R.color.gradient))// 游标颜色 default 0xFF1379D6
                .setCursorHandleSizeInDp(24)// 游标大小 单位dp default 24
                .setSelectedColor(ContextCompat.getColor(context, R.color.message))// 选中文本的颜色 default 0xFFAFE1F4
                .setMagnifierShow(true)// 放大镜 default true
                .setPopSpanCount(5)// 设置操作弹窗每行个数 default 5
                .setPopStyle(R.drawable.copy_back/*操作弹窗背景*/, com.xiaoguang.selecttext.R.drawable.ic_arrow/*箭头图片*/)// 设置操作弹窗背景色、箭头图片
                .addItem(0, "复制"/*item的描述*/, () -> {
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
                }).build();

        mSelectableTextHelper.setSelectListener(new SelectTextHelper.OnSelectListener() {
            @Override
            public void onClick(View v) {
                mSelectableTextHelper.destroy();
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

        return mSelectableTextHelper;
    }
}
