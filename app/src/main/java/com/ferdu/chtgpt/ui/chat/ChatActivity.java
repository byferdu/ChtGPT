package com.ferdu.chtgpt.ui.chat;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.ferdu.chtgpt.R;
import com.ferdu.chtgpt.databinding.ActivityChatBinding;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {


    private com.ferdu.chtgpt.databinding.ActivityChatBinding binding;

    public static DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("QIGUAI", "onCreate: ");
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController;
        navController = Objects.requireNonNull(navHostFragment).getNavController();
        Navigation.setViewNavController(binding.getRoot(), navController);
    }
    @Override
    public void onBackPressed() {
        if (ChatFragment.getBackPressedListener() != null) {
            if (!ChatFragment.getBackPressedListener().handleBackPressed()) {
                super.onBackPressed();
            }
        }else super.onBackPressed();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawerLayout = null;
        binding = null;
    }
}