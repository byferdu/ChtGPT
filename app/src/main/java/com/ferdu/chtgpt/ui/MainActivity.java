package com.ferdu.chtgpt.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.ui.home.HistoryFragment;

import java.util.Objects;

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


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main2);
        NavController navController;

        navController = Objects.requireNonNull(navHostFragment).getNavController();

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