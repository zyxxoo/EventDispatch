package com.totoro.base;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 *
 * Created by zhangyan on 2017/3/20.
 */

public abstract class LinkageBaseFrameLayout extends FrameLayout {
    public LinkageBaseFrameLayout(Context context) {
        super(context);
    }

    public LinkageBaseFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkageBaseFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean intercepting;
    PointF firstPoint;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getLinkageListener() != null){
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_DOWN://如果拦截，那么后续事件不会传递给 子view，并且不会子啊调用这个方法
                    firstPoint = new PointF(ev.getX(), getY());
                    intercepting = getLinkageListener().shouldIntercept(ev, true, 0, 0);
                    dispatchChildDownEvent = !intercepting;
                    dispatchThisDownEvent = intercepting;
                    cancelChildEvent = false;
                    cancelThisEvent = false;
                    firstPoint = new PointF(ev.getX(), ev.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (firstPoint == null){// 防御代码, 可能父容器是自定义的 View,比如本框架（QAQ） down 事件别拦截，其他事件被分发过来了
                        firstPoint = new PointF(ev.getX(), ev.getY());
                    }
                    float diffx = ev.getX() - firstPoint.x; //这里 diff 采用上一次和这一次的偏移或者到起始点的偏移, 这里第二种
                    float diffy = ev.getY() - firstPoint.y;
                    intercepting = getLinkageListener().shouldIntercept(ev, false, diffx, diffy);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }

            return intercepting;
        }
        return super.onInterceptTouchEvent(ev);
    }


    boolean dispatchChildDownEvent;
    boolean dispatchThisDownEvent;
    boolean cancelChildEvent;
    boolean cancelThisEvent;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getLinkageListener() != null){
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    if (intercepting){// 事件只给 viewGroup 处理
                        getLinkageListener().eventDown(ev);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (firstPoint == null){// 防御代码
                        firstPoint = new PointF(ev.getX(), ev.getY());
                    }
                    float diffx = ev.getX() - firstPoint.x;// 这里是事件开始，到当前事件坐标的偏移，当然也可以改成上一个事件到当前事件的偏移
                    float diffy = ev.getY() - firstPoint.y;
                    intercepting = getLinkageListener().shouldIntercept(ev, false, diffx, diffy);
                    if (intercepting){ // 拦截，说明接下来的事件给本 viewgrop 处理
                        // 1. 给前面分发给子 View 的事件创建 cancel 事件，使事件完整
                        if (!cancelChildEvent && dispatchChildDownEvent){ // 上一个事件是给子 View 处理，并且没有传递结束事件
                            MotionEvent cancelEvent = obtainMotionEvent(ev, MotionEvent.ACTION_CANCEL);
                            dispathEvent(ev);
                            dispathEvent(cancelEvent);
                            cancelChildEvent = true;
                        }
                        dispatchChildDownEvent = false;
                        cancelThisEvent = false;


                        //2. 当前分配给 ViewGroup 的事件，必须要 down 事件开头（注意偏移是从 down 事件开始）
                        if (!dispatchThisDownEvent){
                            dispatchThisDownEvent = true;
                            MotionEvent downEvent = obtainMotionEvent(ev, MotionEvent.ACTION_DOWN);
                            getLinkageListener().eventDown(downEvent);
                            firstPoint.set(ev.getX(), ev.getY());
                            diffx = 0;
                            diffy = 0;

                        }else {

                            getLinkageListener().eventMove(ev, diffx, diffy);
                        }


                    }else{// 不拦截，事件分发给子 View
                        //1. 取消 ViewGroup 事件， 可以思考不要这段代码怎么样
                        if (!cancelThisEvent && dispatchThisDownEvent){
                            MotionEvent cancelEvent = obtainMotionEvent(ev, MotionEvent.ACTION_CANCEL);
                            getLinkageListener().eventMove(ev, diffx, diffy);
                            getLinkageListener().eventCancelOrUp(cancelEvent);
                            cancelThisEvent = true;
                        }

                        cancelChildEvent = false;
                        dispatchThisDownEvent = false;
                        //2. 子 View 没有分发 down 事件，要补充 down 事件
                        if (!dispatchChildDownEvent){//没有分发 down 事件
                            dispatchChildDownEvent = true;
                            MotionEvent downEvent = obtainMotionEvent(ev, MotionEvent.ACTION_DOWN);
                            dispathEvent(downEvent);
                        }else {

                            dispathEvent(ev);
                        }

                    }

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                     if (!cancelChildEvent && dispatchChildDownEvent){
                         dispathEvent(ev);
                         cancelChildEvent = true;
                     }

                    if (intercepting){
                        if (!dispatchThisDownEvent){
                            dispatchThisDownEvent = true;
                            MotionEvent downEvent = obtainMotionEvent(ev, MotionEvent.ACTION_DOWN);
                            getLinkageListener().eventDown(downEvent);
                        }
                        getLinkageListener().eventCancelOrUp(ev);
                    }

                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private MotionEvent obtainMotionEvent(MotionEvent ev, int action) {
        MotionEvent downEvent = MotionEvent.obtain(ev);
        downEvent.setAction(action);
        return downEvent;
    }

    Rect hintRect = new Rect();

    /**
     * 向子 View 分发事件，子 View 不一定能收到完整的事件流，因此最好手指滑动的时候，要在同一个 View 上
     *
     * @param event
     */
    protected void dispathEvent(MotionEvent event){
        View child = null;
        boolean consume = false;
        MotionEvent temp = null;
        for (int i = 0; i < getChildCount(); i++){
            child = getChildAt(i);
            child.getHitRect(hintRect);
            if (hintRect.contains((int)event.getX(), (int)event.getY())){ // 判断点击点的坐标是否在该 view 上
                temp = MotionEvent.obtain(event);
                temp.offsetLocation(-hintRect.left, -hintRect.top); // event 坐标修改为相对 子 view
                consume |= child.dispatchTouchEvent(temp); //分发事件
                if (consume) break;
            };
        }
    };


    /**
     * 本 viewgroup 处理的事件
     *
     * @return
     */
    public abstract LinkageListener getLinkageListener();
}
