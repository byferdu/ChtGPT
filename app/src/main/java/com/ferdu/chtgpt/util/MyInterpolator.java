package com.ferdu.chtgpt.util;

import android.view.animation.Interpolator;

public class MyInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return (float)(1-Math.pow((1-input),2));
    }

}
