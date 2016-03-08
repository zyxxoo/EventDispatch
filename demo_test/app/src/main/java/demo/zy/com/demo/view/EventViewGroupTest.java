package demo.zy.com.demo.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/3/8.
 */
public class EventViewGroupTest extends FrameLayout{
    public static String TAG = "+========>EventViewGroupTest";
    public EventViewGroupTest(Context context) {
        super(context);
    }

    public EventViewGroupTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventViewGroupTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    PointF initPoint = new PointF();
    PointF lastPoint = new PointF();
    PointF curPoint = new PointF();
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "inter y="+ev.getY());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "inter_down");
                initPoint.set(ev.getX(), ev.getY());
                lastPoint.set(ev.getX(), ev.getY());
                curPoint.set(ev.getX(), ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "inter_move");
                curPoint.set(ev.getX(), ev.getY());
                if (curPoint.y - lastPoint.y > 0){//下滑
                    Log.d(TAG, "===============true");
                    lastPoint.set(ev.getX(), ev.getY());
                    return true;
                }else if (curPoint.y - lastPoint.y < 0){
                    Log.d(TAG, "===============false");
                    lastPoint.set(ev.getX(), ev.getY());
                    return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "inter_cancel");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "inter_up");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    float lasy = 0;
    float cury = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "y="+event.getY());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lasy = event.getY();
                cury = event.getY();
                Log.d(TAG, "touch_down");
                break;
            case MotionEvent.ACTION_MOVE:
                cury = event.getY();
                Log.d(TAG, "touch_move");
                if (cury - lasy < 0) { //下拉
                    Log.d(TAG, "touch========false");
                    lasy = event.getY();
                    return false;
                }else if (cury - lasy > 0){
                    Log.d(TAG, "touch========true");
                    lasy = event.getY();
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "touch_cancel");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "touch_up");
                break;
        }
        return true;
    }
}
