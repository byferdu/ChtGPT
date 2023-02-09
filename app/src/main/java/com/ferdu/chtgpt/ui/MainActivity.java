package com.ferdu.chtgpt.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.ui.home.HistoryFragment;
import com.ferdu.chtgpt.util.MyUtil;

import java.util.Objects;

import cn.leancloud.LCInstallation;
import cn.leancloud.LCObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private com.ferdu.chtgpt.databinding.ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MyViewModel.class);
        binding = com.ferdu.chtgpt.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         // BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
      //HistoryFragment homeFragment = new HistoryFragment();

      SharedPreferences shared = MyUtil.getShared(this);
        if (shared.getBoolean("isFirstInstall", true)) {
            SharedPreferences.Editor edit = shared.edit();
            edit.putLong("versionDate", System.currentTimeMillis());
            edit.putBoolean("isFirstInstall", false);
            edit.apply();
            LCInstallation.getCurrentInstallation().saveInBackground().subscribe(new Observer<LCObject>() {
                @Override
                public void onSubscribe(Disposable d) {
                }
                @Override
                public void onNext(LCObject avObject) {
                    // 关联 installationId 到用户表等操作。
                    String installationId = LCInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println("保存成功：" + installationId );
                }
                @Override
                public void onError(Throwable e) {
                    System.out.println("保存失败，错误信息：" + e.getMessage());
                }
                @Override
                public void onComplete() {
                }
            });
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main2);
        NavController navController;

        navController = Objects.requireNonNull(navHostFragment).getNavController();
        int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
        HistoryFragment.pressedListener = null;
    }
    @Override
    public void onBackPressed() {
        Log.d("NOACTIVITY", "onBackPressed: "+HistoryFragment.pressedListener);
        if (HistoryFragment.pressedListener != null) {
            if (!HistoryFragment.pressedListener.handleBackPressed()) {
                if (binding.navView.isShown()) {
                    finish();
                }else super.onBackPressed();
            }
        }else {
            if (binding.navView.isShown()) {
                finish();
            } else super.onBackPressed();
        }
    }
}