package com.totoro.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;
import com.totoro.base.LinkageBaseFrameLayout;
import com.totoro.base.LinkageListener;

/**
 * Created by zhangyan on 2017/3/20.
 */

public abstract class PullZoomBase<MAINVIEW extends View, SCALEVIEW extends View> extends LinkageBaseFrameLayout implements LinkageListener {
    MAINVIEW mainView;
    SCALEVIEW scaleview;
    ViewConfiguration viewConfiguration;
    public PullZoomBase(Context context) {
        this(context, null);
    }

    public PullZoomBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullZoomBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        viewConfiguration = ViewConfiguration.get(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.mainView = getMainView();
        this.scaleview = getScaleView();
        if (mainView != null){
            bringChildToFront(mainView);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mainView != null && scaleview != null && originMainRect == null){
            originMainRect = new Rect();
            originScaleRect = new Rect();
            mainView.getHitRect(originMainRect);
            scaleview.getHitRect(originScaleRect);
        }
    }

    @Override
    public LinkageListener getLinkageListener() {
        return this;
    }

    @Override
    public boolean shouldIntercept(MotionEvent event, boolean isDown, float diffx, float diffy) {
        return isReadyPull(diffx, diffy);
    }

    protected abstract boolean isReadyPull(float diffx, float diffy);


    Scroller mScroller;
    VelocityTracker velocityTracker;
    float mainBaseTranslationY;
    Rect originMainRect = null;
    Rect originScaleRect = null;
    int actionPointId = -1;
    @Override
    public void eventDown(MotionEvent event) {
        Log.d("isReadyPull", "eventDown");

        mScroller.abortAnimation();
        mScroller.forceFinished(true);
        if (velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }else{
            velocityTracker.clear();
        }

        actionPointId = event.getPointerId(0);
        velocityTracker.addMovement(event);

        mainBaseTranslationY = mainView.getTranslationY();

    }

    @Override
    public void eventMove(MotionEvent event, float diffx, float diffy) {
        Log.d("isReadyPull", "eventMove");

        velocityTracker.addMovement(event);

        smoothLayout((int) (mainBaseTranslationY + diffy));
    }

    @Override
    public void eventCancelOrUp(MotionEvent event) {
        Log.d("isReadyPull", "eventCancel");
        velocityTracker.addMovement(event);
        mScroller.forceFinished(true);
        velocityTracker.computeCurrentVelocity(1000, viewConfiguration.getScaledMaximumFlingVelocity());
        if (shouldSmooth()){
             updateFinalLayout(mScroller, velocityTracker);
        }
    }

    public void updateFinalLayout(final Scroller scroller, final VelocityTracker tracker) {
        int dy = (int) ViewHelper.getTranslationY(mainView);
        if (dy > 0){
            scroller.startScroll(0, dy, 0, -dy, 200);
        }else if (dy < 0){

            int yVelocity = (int)tracker.getYVelocity(actionPointId);
            if (yVelocity > 0){
                scroller.fling(0, dy, 0, yVelocity, 0, 0, dy, 0);

            }else if (yVelocity < 0){
                scroller.fling(0, dy, 0, yVelocity, 0, 0, -originMainRect.top, dy);
            }

        }

        final Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (scroller.computeScrollOffset()){
                    smoothLayout(scroller.getCurrY());
                    mHandler.post(this);
                }
            }
        });
    }

    protected abstract void smoothLayout(int futureTranslationY);

    public abstract boolean shouldSmooth();


    public abstract MAINVIEW getMainView();
    public abstract SCALEVIEW getScaleView();
}
