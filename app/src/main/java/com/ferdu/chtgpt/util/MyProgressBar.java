package com.ferdu.chtgpt.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;

import java.util.Timer;
import java.util.TimerTask;

public class MyProgressBar extends ProgressBar {

    public MyProgressBar(Context context) {
        super(context);
    }

    public static final String TAG = "ProgressDialog";
    private long mTimeOut = 0;  // 默认timeOut为0即无限大
    private OnTimeOutListener mTimeOutListener = null;  // timeOut后的处理器
    private Timer mTimer = null;// 定时器
    /*
        超时后才调用该Handler
     */
    private Handler mHandler = new Handler(Looper.myLooper()){

        @Override
        public void handleMessage(Message msg) {
            if(mTimeOutListener != null){
                mTimeOutListener.onTimeOut(MyProgressBar.this);
                setVisibility(GONE);    //释放掉ProgressDialog
            }
        }
    };
//    public static MyProgressBar createProgressBar(Context context,
//                                                        long time, MyProgressBar.OnTimeOutListener listener) {
//        MyProgressBar progressDialog = new MyProgressBar(context);
//        if (time != 0) {
//            progressDialog.setTimeOut(time, listener);
//        }
//        return progressDialog;
//    }

    private void setTimeOut(long time, OnTimeOutListener listener) {
        mTimeOut = time;
        if (listener != null) {
            this.mTimeOutListener = listener;
        }
    }

    public void cancel(AppCompatImageButton button) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        setVisibility(GONE);
        button.setEnabled(true);
        button.setAlpha(1f);
    }
    public void show(long time, AppCompatImageButton button) {
        setVisibility(VISIBLE);
        if (!button.isEnabled())
        button.setAlpha(0.5f);

        if (time != 0) {
            setTimeOut(time, dialog -> {
                dialog.cancel(button);
                Toast.makeText(getContext(), "请求超时😟", Toast.LENGTH_SHORT).show();

            });
        }
        if (mTimeOut > 0) {
            mTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            };
            mTimer.schedule(timerTask, mTimeOut);  //设定mTimeout时间后调用TimerTask任务
        }
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface OnTimeOutListener {

        /**
         * 当progressDialog超时时调用此方法
         */
        void onTimeOut(MyProgressBar dialog);
    }
}
