package com.ferdu.chtgpt.util.seek;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class FloatSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private float mStepSize;

    public FloatSeekBar(Context context) {
        super(context);
    }

    public FloatSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    public void setStepSize(float stepSize) {
        mStepSize = stepSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOnSeekBarChangeListener == null) {
            return false;
        }

        int width = getWidth();
        int progress = getMax() / width;
        float value = event.getX() * progress;
        value = Math.round(value / mStepSize) * mStepSize;
        setProgress((int) value);
        mOnSeekBarChangeListener.onProgressChanged(this, (int) value, true);
        return true;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    }
}

