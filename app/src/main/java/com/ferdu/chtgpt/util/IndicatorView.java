package com.ferdu.chtgpt.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class IndicatorView extends View {
    private final Paint paint = new Paint();
    private final Path path = new Path();  // 初始化 Path 对象
    private int backgroundColor = Color.RED;
    private int indicatorColor = Color.RED;
    private int indicatorSize = 40;
    private final RectF rectF = new RectF();

    private float cornerRadius = 10;
    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = width;
        rectF.bottom = height - indicatorSize;


        // 绘制背景矩形
        canvas.drawRect(0, 0, width, height - indicatorSize, paint);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        // 绘制指示三角形
        path.reset();  // 重置 Path 对象
        path.moveTo(width / 2f - indicatorSize / 2f, height - indicatorSize);
        path.lineTo(width / 2f, height);
        path.lineTo(width / 2f + indicatorSize / 2f, height - indicatorSize);
        path.close();
        paint.setColor(indicatorColor);
        canvas.drawPath(path, paint);
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        invalidate();
    }

    public void setIndicatorColor(int color) {
        indicatorColor = color;
        invalidate();
    }

    public void setIndicatorSize(int size) {
        indicatorSize = size;
        invalidate();
    }
    public void setCornerRadius(float radius) {
        cornerRadius = radius;
        invalidate();
    }
}


