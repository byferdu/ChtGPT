package com.ferdu.chtgpt.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

    static TimerUtil timerUtil;

    public synchronized static TimerUtil getInstance() {
        if (timerUtil == null) {
            timerUtil = new TimerUtil();
        }
        return timerUtil;
    }

    public TimerUtil() {
    }


    private Timer timer;
    private TimerTask task;

    //五分钟
    public void memberTimerStart(){
        timerCancel();
        timer = new Timer(true);
        task  = new TimerTask() {
            @Override
            public void run() {
                //doSomething

            }
        };
        timer.schedule(task, 0,  1000*5*60);
    }


    public void timerCancel(){
        if(task!=null){
            task.cancel();
            task = null;
        }
        if (timer!=null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

    }


}
