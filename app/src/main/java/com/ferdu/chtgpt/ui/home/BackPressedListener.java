package com.ferdu.chtgpt.ui.home;

public interface BackPressedListener {
    /**
     * @return true代表响应back键点击，false代表不响应
     */
    boolean handleBackPressed();
}
