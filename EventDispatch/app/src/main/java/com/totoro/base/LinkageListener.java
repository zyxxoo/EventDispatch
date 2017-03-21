package com.totoro.base;

import android.view.MotionEvent;

/**
 * Created by zhangyan on 2017/3/20.
 */

public interface LinkageListener {
    public boolean shouldIntercept(MotionEvent event, boolean isDown, float diffx, float diffy);
    public void eventDown(MotionEvent event);
    public void eventMove(MotionEvent event, float diffx, float diffy);
    public void eventCancelOrUp(MotionEvent event);
}
