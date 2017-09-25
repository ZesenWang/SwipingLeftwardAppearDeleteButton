package com.example.swipingleftwardappeardeletebutton;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by wangz on 2016/8/28.
 */
public class LinearButton extends LinearLayout {

    private int mTouchSlop;
    private float mInitialX;
    private float mInitialY;
    private float mLastX;
    private float mLastY;
    private int totalX;
    private int X_SPAN = 300;
    private Scroller scroller;
    private boolean isButtonHide = true;

    public LinearButton(Context context) {
        super(context);
        init(context);
    }

    public LinearButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInitialX = ev.getX();
                mLastX = mInitialX;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                int deltaX = (int)Math.abs(x - mInitialX);
                if(x > mTouchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        Log.i("coordinate","x: "+x+" mLastX: "+mLastX);
        int deltaX = (int)(mLastX - x);
        mLastX = x;

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(isButtonHide){
                    if(deltaX <= 0)
                        return true;
                }else{
                    if(deltaX >= 0)
                        return true;
                }

                int mScrollX = getScrollX() + deltaX;

                if((Math.abs(deltaX) + totalX) <= X_SPAN && totalX <= X_SPAN)
                    scrollTo(mScrollX, 0);
                else if((Math.abs(deltaX) + totalX) >= X_SPAN && totalX <= X_SPAN){
                    if(isButtonHide) {
                        scrollTo(X_SPAN, 0);
                        isButtonHide = false;
                        Log.i("totalX","x_span");
                    }
                    else {
                        scrollTo(0, 0);
                        isButtonHide = true;
                        Log.i("totalX","0");
                    }

                }
                totalX += Math.abs(deltaX);
                Log.i("totalX","MOVE: totalX: "+totalX);
                break;
            case MotionEvent.ACTION_UP:
                totalX += Math.abs(deltaX);
                Log.i("totalX","UP: totalX: "+totalX);
                if(totalX <= X_SPAN/2 ){
                    if(isButtonHide){
                        scroller.startScroll(getScrollX(),0,- getScrollX(),0);
                        Log.i("totalX","scroll");
                        invalidate();
                    }
                    else{
                        scroller.startScroll(getScrollX(),0,X_SPAN - getScrollX(),0);
                        invalidate();

                    }
                }
                else if(totalX >= X_SPAN/2 && totalX <= X_SPAN){
                    if(isButtonHide){
                        scroller.startScroll(getScrollX(),0,X_SPAN - getScrollX(),0);
                        invalidate();
                        isButtonHide = false;
                    }
                    else{
                        scroller.startScroll(getScrollX(),0,- getScrollX(),0);
                        invalidate();
                        isButtonHide = true;
                    }
                }
                totalX = 0;
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            int x = scroller.getCurrX();
            scrollTo(x, 0);
            Log.i("totalX","CurrX: "+ x );
            postInvalidate();
        }
    }
}
