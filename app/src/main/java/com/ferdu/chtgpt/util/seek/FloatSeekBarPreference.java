package com.ferdu.chtgpt.util.seek;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SeekBarPreference;

import com.ferdu.chtgpt.R;

public class FloatSeekBarPreference extends SeekBarPreference {
    public float mMinValue;
    public float mMaxValue;


    public float getMinValue() {
        return mMinValue;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    private int progress;

    public FloatSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatSeekBarPreference);
        mMaxValue = typedArray.getFloat(R.styleable.FloatSeekBarPreference_maxValue, 0);
        mMinValue = typedArray.getFloat(R.styleable.FloatSeekBarPreference_minValue, 0);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getFloat(index, 0.5f);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        float def = defaultValue != null ? (float) defaultValue : 0.5f;
        float value = getPersistedFloat(def);
        progress = (int) ((value - mMinValue) / (mMaxValue - mMinValue) * getMax());
        setDefaultValue(progress);
    }

    public void setMinValue(float minValue) {
        mMinValue = minValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        SeekBar seekBar = (SeekBar) holder.findViewById(androidx.preference.R.id.seekbar);
        float value = mMinValue + (mMaxValue - mMinValue) * progress / getMax();
        seekBar.setProgress(progress);
        TextView textView = (TextView) holder.findViewById(androidx.preference.R.id.seekbar_value);
        textView.setText(String.valueOf(value));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = mMinValue + (mMaxValue - mMinValue) * progress / getMax();
                if (callChangeListener(value)) {
                    persistFloat(value);
                    textView.setText(String.valueOf(value));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}

