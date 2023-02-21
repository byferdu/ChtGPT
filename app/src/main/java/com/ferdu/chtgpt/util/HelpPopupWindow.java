package com.ferdu.chtgpt.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ferdu.chtgpt.R;

public class HelpPopupWindow {
    private final Context context;
    private final View anchorView;
    private final String message;

    private PopupWindow popupWindow;

    public HelpPopupWindow(Context context, View anchorView, String message) {
        this.context = context;
        this.anchorView = anchorView;
        this.message = message;
    }
    @SuppressLint("MissingInflatedId")
    public void showHelp() {
        if (popupWindow == null) {
            // 创建 PopupWindow
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_help, null,false);
            TextView messageView = popupView.findViewById(R.id.popup_help_message);
            messageView.setText(message);
            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // 计算 PopupWindow 在屏幕上的位置
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int x = location[0] + anchorView.getWidth();
        int y = location[1] + anchorView.getHeight();

        // 显示 PopupWindow
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
    }

    public boolean isShowing() {
        if (popupWindow != null) {
            return popupWindow.isShowing();
        }
        return false;
    }

    public void hideHelp() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}

