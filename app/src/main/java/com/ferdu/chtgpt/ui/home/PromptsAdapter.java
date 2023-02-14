package com.ferdu.chtgpt.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.PromptItemBinding;
import com.ferdu.chtgpt.util.interfces.MyItemClickListener;

import java.util.List;

public class PromptsAdapter extends RecyclerView.Adapter<PromptsAdapter.PromptViewHolder> {
    List<PromptModel> prompts;
    private MyItemClickListener<PromptModel> clickListener;

    public PromptsAdapter(List<PromptModel> prompts) {
        this.prompts = prompts;
    }

    public List<PromptModel> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<PromptModel> prompts) {
        this.prompts = prompts;
    }

    public MyItemClickListener<PromptModel> getClickListener() {
        return clickListener;
    }

    public void setClickListener(MyItemClickListener<PromptModel> clickListener) {
        this.clickListener = clickListener;
    }

    public void updateData(List<PromptModel> dataList, boolean isQuery) {
        //  this.prompts.clear();
        if (isQuery) {
            this.prompts = dataList;
            notifyDataSetChanged();
            return;
        }
        notifyItemRangeRemoved(0, dataList.size());
        this.prompts = dataList;
        notifyItemRangeChanged(0, dataList.size());
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PromptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PromptItemBinding binding = PromptItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new PromptViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PromptViewHolder holder, int position) {
        holder.itemView.setTag(R.id.chat_position, position);
        holder.binding.textView1.setText(prompts.get(position).getAct());
        holder.onBind(prompts,position);
    }

    @Override
    public int getItemCount() {
        return prompts.size();
    }

    class PromptViewHolder extends RecyclerView.ViewHolder {
        PromptItemBinding binding;

        public PromptViewHolder(@NonNull PromptItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(List<PromptModel> model,int position) {
            itemView.setOnClickListener(v -> {
                int position2 = (int) itemView.getTag(R.id.chat_position);
                if (model.size() > 0)
                    clickListener.onItemClicked(model.get(position), false, position);
            });

        }
    }

}
