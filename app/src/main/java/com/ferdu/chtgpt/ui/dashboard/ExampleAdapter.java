package com.ferdu.chtgpt.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.ExampleViewItemBinding;
import com.ferdu.chtgpt.models.data.Example2;
import com.ferdu.chtgpt.util.interfces.MyItemClickListener;

import java.util.Objects;

public class ExampleAdapter extends ListAdapter<Example2, ExampleAdapter.ExampleViewHolder> {
   static MyItemClickListener<Example2> myItemClickListener;
    int p = 0;

    protected ExampleAdapter(MyItemClickListener<Example2> myItemClickListener) {
        super(new DiffUtil.ItemCallback<Example2>() {
            @Override
            public boolean areItemsTheSame(@NonNull Example2 oldItem, @NonNull Example2 newItem) {
                return oldItem.getId()== newItem.getId();
            }
            @Override
            public boolean areContentsTheSame(@NonNull Example2 oldItem, @NonNull Example2 newItem) {
                return Objects.equals(oldItem.getTitle(), newItem.getTitle())
                        &&  Objects.equals(oldItem.getSummary(), newItem.getSummary());
            }
        });
        ExampleAdapter.myItemClickListener = myItemClickListener;
    }



    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExampleViewItemBinding bind = ExampleViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ExampleViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleViewItemBinding bind = ExampleViewItemBinding.bind(holder.itemView);
        Context context = holder.binding.getRoot().getContext();
        int[] colors = {R.color.yellow, R.color.green, R.color.purple,
                R.color.pink, R.color.red, R.color.gradient};
        if (p > colors.length - 1) {
            p = 0;
        }
        holder.binding.examCard.setCardBackgroundColor(context.getResources().getColor(colors[p]));
        int drawableId = context.getResources().getIdentifier(getItem(position).getImage(), "drawable", context.getPackageName());
        Glide.with(bind.getRoot())
                .load(drawableId)
                .into(bind.imageView4);
        holder.binding.textView1.setText(getItem(position).getTitle());
        holder.binding.textView2.setText(getItem(position).getSummary());
        Example2 item = getItem(position);
        holder.onBind(item,false,position);
        p++;
    }

    static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ExampleViewItemBinding binding;
        public ExampleViewHolder(@NonNull ExampleViewItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    public void onBind(Example2 example2, boolean adr, int position){
        binding.getRoot().setOnClickListener(view->myItemClickListener.onItemClicked(example2,adr,position));
    }

    }
}
