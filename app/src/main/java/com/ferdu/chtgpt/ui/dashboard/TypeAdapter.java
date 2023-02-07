package com.ferdu.chtgpt.ui.dashboard;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.ferdu.chtgpt.models.data.Types;

public class TypeAdapter extends ArrayAdapter<Types> {
    public TypeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
