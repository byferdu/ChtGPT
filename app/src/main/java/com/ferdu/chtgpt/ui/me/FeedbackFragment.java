package com.ferdu.chtgpt.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.FeedbackAdviceBinding;
import com.ferdu.chtgpt.databinding.FragmentFeedbackBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View viewById;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentFeedbackBinding binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        //

        viewById = requireActivity().findViewById(R.id.nav_view);
        viewById.setVisibility(View.GONE);
        binding.feedViewPager.setAdapter(new RecyclerView.Adapter<MvViewHolder>() {

            @NonNull
            @Override
            public MvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                FeedbackAdviceBinding inflate = FeedbackAdviceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new MvViewHolder(inflate);
            }

            @Override
            public void onBindViewHolder(@NonNull MvViewHolder holder, int position) {
                if (position == 0) holder.inflate.inpuLayout.setHint("填写反馈内容，如果bug反馈,步骤尽量详细");
                else holder.inflate.inpuLayout.setHint("填写您的意见");
                holder.listener(holder.inflate.button2, holder.inflate.inpuLayout, position);
            }

            @Override
            public int getItemCount() {
                return binding.tabLayout.getTabCount();
            }
        });
        binding.meMaterialToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.feedViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.feedViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabLayout.getTabAt(position)).select();
            }
        });
        return binding.getRoot();
    }

   static class MvViewHolder extends RecyclerView.ViewHolder {
        FeedbackAdviceBinding inflate;

        public MvViewHolder(@NonNull FeedbackAdviceBinding itemView) {
            super(itemView.getRoot());
            inflate = itemView;
        }
        public void listener(Button button, TextInputLayout inputLayout, int position) {
            button.setOnClickListener(v -> {
                LCObject todo = new LCObject("Feedback");
                // 为属性赋值
                String string = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
                if (string.isEmpty()) {
                    inputLayout.getEditText().setError("输入框不能为空");
                    return;
                }

                if (position == 0) todo.put("feedBack", string);
                else todo.put("advice", string);
                todo.put("feedBackOrNot", position);
                todo.put("userId", LCUser.getCurrentUser().getUsername());
                // 将对象保存到云端
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    public void onSubscribe(Disposable disposable) {

                    }

                    public void onNext(LCObject todo) {
                        // 成功保存之后，执行其他逻辑
                        System.out.println("保存成功。objectId：" + todo.getObjectId());
                        Toast.makeText(inputLayout.getContext(), "反馈成功🤗", Toast.LENGTH_SHORT).show();
                    }

                    public void onError(Throwable throwable) {
                        // 异常处理
                        Toast.makeText(inputLayout.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete() {
                    }
                });

            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewById.setVisibility(View.VISIBLE);
    }
}