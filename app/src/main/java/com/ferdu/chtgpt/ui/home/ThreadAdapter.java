package com.ferdu.chtgpt.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.ThreadCardItemBinding;
import com.ferdu.chtgpt.models.data.ChatThread;
import com.ferdu.chtgpt.util.interfces.MyItemClickListener;
import com.ferdu.chtgpt.util.interfces.MyItemLongClick;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder> {
    public void setClickListener(MyItemClickListener<ChatThread> clickListener) {
        ThreadAdapter.clickListener = clickListener;
    }

    private final List<ChatThread> chatThreads;
    private static MyItemClickListener<ChatThread> clickListener;

    //private SparseBooleanArray selectedItems = new SparseBooleanArray();
    public List<ChatThread> getChatThreads() {
        return chatThreads;
    }

    private static MyItemLongClick itemLongClick;
    public static boolean isLongClick = false;
    private static Boolean choose = false;
    static int selectAll = 0;

    public ThreadAdapter(List<ChatThread> chatThreads) {
        this.chatThreads = chatThreads;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThreadCardItemBinding binding = ThreadCardItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ThreadViewHolder holder = new ThreadViewHolder(binding);
        holder.setBinding1(chatThreads);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {

        holder.binding.textView14.setText(chatThreads.get(position).getTitle());
        holder.itemView.setTag(R.id.chat_threads_position, position);
        boolean equals=false;
        if (HomeFragment.recyclerViewOnClickData != null) {
            equals = Boolean.TRUE.equals(HomeFragment.recyclerViewOnClickData.get(position));
        }
        if (isLongClick && equals) {
              holder.binding.selectView.setVisibility(View.VISIBLE);
              choose = true;
        }else {
            holder.binding.selectView.setVisibility(View.GONE);
        }
        if (isLongClick) {
            if (selectAll == 1) {
                holder.binding.selectView.setVisibility(View.VISIBLE);//如果当前为长按状态则让长按选择图片显示
            } else if (selectAll == 2) {
                holder.binding.selectView.setVisibility(View.GONE);
            }//如果当前为长按状态则让长按选择图片显示
        } else {
            holder.binding.selectView.setVisibility(View.GONE);//否则让其消失
        }
    }

    @Override
    public int getItemCount() {
        return chatThreads.size();
    }

    static class ThreadViewHolder extends RecyclerView.ViewHolder {
        ThreadCardItemBinding binding;

        public ThreadViewHolder(@NonNull ThreadCardItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setBinding1(List<ChatThread> chatThread) {
            binding.imageView1.setOnClickListener(v -> {
                int position = (int) itemView.getTag(R.id.chat_threads_position);
                View viewById = v.findViewById(R.id.selectView);
                if ( isLongClick) {
                    choose = viewById.getVisibility() != View.VISIBLE;//将choose设置为未选中态
                }
                clickListener.onItemClicked(chatThread.get(position), choose, position);
            });
            binding.imageView1.setOnLongClickListener(v -> {
                isLongClick = true;
                int position = (int) itemView.getTag(R.id.chat_threads_position);
                itemLongClick.OnItemLongClick(position);
               //View viewById = v.findViewById(R.id.selectView);
               // viewById.setVisibility(View.VISIBLE);
                selectAll = 0;
                choose = true;
                clickListener.onItemClicked(chatThread.get(position), true, position);
                return true;
            });
        }

    }
    public void setOnItemLongClickListener(MyItemLongClick onItemClick) {
        itemLongClick = onItemClick;
    }
    //如何将RecylerView获取某个

    //删除Notes
    public void removeNotes(int position) {
        synchronized (chatThreads) {
             chatThreads.remove(position);
        }
        notifyItemRemoved(position);
    }

}
