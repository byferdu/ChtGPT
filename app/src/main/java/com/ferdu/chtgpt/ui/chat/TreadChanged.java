package com.ferdu.chtgpt.ui.chat;

import com.ferdu.chtgpt.models.data.ChatThread;

public interface TreadChanged {
    void onChanged(ChatThread thread);
}
