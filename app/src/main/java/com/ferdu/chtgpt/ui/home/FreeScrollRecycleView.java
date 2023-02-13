package com.ferdu.chtgpt.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FreeScrollRecycleView extends RecyclerView {

    /** 记录父View的滚动状态 */
    int mFatherScrollState = -1;

    /** */
    private OnScrollListener mOnScrollListener;

    /** 父View*/
    private FreeScrollRecycleView mFatherScrollableView;

    /** 子View*/
    private FreeScrollRecycleView mJustStopScrollingChildView;

    public FreeScrollRecycleView(@NonNull Context context) {
        super(context);
    }

    public FreeScrollRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FreeScrollRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //监听父View的滚动
        setFatherScrollableView(mFatherScrollableView);
    }

    protected void setFatherScrollableView(FreeScrollRecycleView fatherScrollableView) {
        if(fatherScrollableView != null){
            mFatherScrollableView = fatherScrollableView;
            if(mOnScrollListener == null){
                mOnScrollListener = new OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        //记录父view滚动状态
                        mFatherScrollState = newState;
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                };
            }
            mFatherScrollableView.removeOnScrollListener(mOnScrollListener);
            mFatherScrollableView.addOnScrollListener(mOnScrollListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mFatherScrollableView != null){
            mFatherScrollableView.removeOnScrollListener(mOnScrollListener);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean ret = super.onInterceptTouchEvent(e);

        /*
        不拦截父View的Touch事件.
        当前是子View时, 在开始滚动时, 会在super.onInterceptTouchEvent()中调阻止父View滚动,
        所以在执行完onInterceptTouchEvent()后, 需要重置父View, 允许其接受事件:
         */
        getParent().requestDisallowInterceptTouchEvent(false);

        Log.d("FreeScrollRecycleView", getResources().getResourceName(getId())+": onInterceptTouchEvent: "+ret+" event:"+e.getAction()+"|"+e.hashCode());
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction()==MotionEvent.ACTION_CANCEL
                && mFatherScrollState ==SCROLL_STATE_DRAGGING){
            Log.d("FreeScrollRecycleView", getResources().getResourceName(getId())+": onTouchEvent: event:"+e.getAction()+"|"+e.hashCode());

            /*
            当前是子View时, 若父View可以开始滚动时, 则会向子View发送CANCEL事件, 阻止子View滚动.
            但是此时并不想子View停止滚动, 所以强行设置ACTION_MOVE
             */
            e.setAction(MotionEvent.ACTION_MOVE);
        }

        boolean ret = super.onTouchEvent(e);

        /*
        滚动时, 也会要求父布局不接受事件, 所以onTouchEvent()执行完毕后, 重新允许父View接受事件:
         */
        getParent().requestDisallowInterceptTouchEvent(false);
        Log.d("FreeScrollRecycleView", getResources().getResourceName(getId())+": onTouchEvent: "+ret+" event:"+e.getAction()+"|"+e.hashCode()+" mfatherScrollState:"+ mFatherScrollState);
        return ret;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_CANCEL
                && mFatherScrollState ==SCROLL_STATE_DRAGGING){
            Log.d("FreeScrollRecycleView", getResources().getResourceName(getId())+": dispatchTouchEvent: event:"+ev.getAction()+"|"+ev.hashCode());

            /*
            这个CANCEL事件, 是当父View从发送过来的,
            因为父View开始滚动了(father.onInterceptTouchEvent()==true),它要阻止子View滚动
            但此时子View也想滚动, 所以强制设置为 ACTION_MOVE
             */
            ev.setAction(MotionEvent.ACTION_MOVE);

        }

        boolean ret = super.dispatchTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(mFatherScrollableView != null) {
                    //当前是子View, 告知父View, 需要手动发送Touch事件的对象是this
                    mFatherScrollableView.mJustStopScrollingChildView = this;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                if(mFatherScrollableView != null) {
                    //当前是子View, 告知父View, 清空手动发送Touch事件的对象
                    mFatherScrollableView.mJustStopScrollingChildView = null;
                }
                //当前是父View. 清空需要手动发送Touch事件的子View
                mJustStopScrollingChildView = null;
        }

        if(mJustStopScrollingChildView != null){

            //向子View分发事件
            mJustStopScrollingChildView.dispatchTouchEvent(ev);
        }
        Log.d("FreeScrollRecycleView", getResources().getResourceName(getId())+": dispatchTouchEvent: "+ret+" event:"+ev.getAction()+"|"+ev.hashCode());
        return ret;
    }
}


