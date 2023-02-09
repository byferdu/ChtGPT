package com.ferdu.chtgpt.ui.me;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.FragmentMeBinding;
import com.ferdu.chtgpt.ui.AboutActivity;
import com.ferdu.chtgpt.ui.MainActivity;
import com.ferdu.chtgpt.ui.SettingsActivity;
import com.ferdu.chtgpt.ui.login.LoginActivity;
import com.ferdu.chtgpt.viewmodel.MyViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cn.leancloud.LCUser;


public class MeFragment extends Fragment {

    private FragmentMeBinding binding;
    public static MeFragment instance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyViewModel meViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);

        binding = FragmentMeBinding.inflate(inflater, container, false);
        if (LCUser.getCurrentUser() == null) {
            binding.userName.setText("未登录");
            // 显示注册或登录页面
        }
        View.OnClickListener listener = v -> {
            if (LCUser.getCurrentUser() == null) {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
            }
        };
        if (requireActivity() instanceof MainActivity) {
            binding.meMaterialToolbar.setNavigationIcon(null);
        }
        binding.meMaterialToolbar.setNavigationOnClickListener(v -> {

        });

        binding.avatarMe.setOnClickListener(listener);
        binding.userName.setOnClickListener(listener);
        binding.textView24.setOnClickListener(listener);
        //  if (LCUser.getCurrentUser() != null) {
        // 跳到首页
        //binding.logoutButton.setVisibility(View.VISIBLE);
        //  } else {
        //      binding.logoutButton.setVisibility(View.INVISIBLE);
        //      binding.userName.setText("未登录");
        //      binding.textView23.setText("0.00");
        // 显示注册或登录页面
        //  }
        Handler handler = new Handler(Looper.getMainLooper());
        binding.aboutButton.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AboutActivity.class)));
        binding.settingBtn.setOnClickListener(v -> startActivity(new Intent(requireActivity(), SettingsActivity.class)));
        meViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                double doubleValue = BigDecimal.valueOf(user.getMoney()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                binding.textView23.setText(String.valueOf(doubleValue));
                binding.textView23.setTooltipText(String.valueOf(user.getMoney()));
                if (LCUser.currentUser() != null) {
                    binding.userName.setText(String.valueOf(LCUser.currentUser().getUsername()));
                }
            }
        });
        binding.historyButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_about_to_historyFragment));
        binding.feedbackButton.setOnClickListener(v -> {
            if (LCUser.currentUser() != null) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_about_to_feedbackFragment);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                        .setTitle("提示")
                        .setMessage("未登录状态")
                        .setPositiveButton("先登录", (dialog, which) -> startActivity(new Intent(requireActivity(), LoginActivity.class))).setNegativeButton("发送邮件", (dialog, which) -> {
                            Intent data = new Intent(Intent.ACTION_SENDTO);
                            data.setData(Uri.parse("mailto:by_ferdu@163.com"));
                            data.putExtra(Intent.EXTRA_SUBJECT, "来自ChtGPT 的反馈");
                            startActivity(data);
                        }).setNeutralButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LCUser.getCurrentUser() == null) {
            binding.userName.setText("未登录");
            // 显示注册或登录页面
        }
    }
}