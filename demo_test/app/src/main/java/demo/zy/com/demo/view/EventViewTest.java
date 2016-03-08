package demo.zy.com.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/1/9.
 */
public class EventViewTest extends View{
    public static String TAG = "+========>eventViewTest";
    public EventViewTest(Context context) {
        super(context);
    }

    public EventViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public EventViewTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        logEvent("old" , event);

        MotionEvent e = MotionEvent.obtain(event);
        e.offsetLocation(30, 40);

        logEvent("new" , e);

        MotionEvent o = MotionEvent.obtainNoHistory(event);
        o.setLocation(10, 10);
        logEvent("setLocation", o);

        Log.d(TAG, "y="+event.getY());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "touch_down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "touch_move");
                return false;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "touch_cancel");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "touch_up");
                break;
        }
        return true;
    }

    private void logEvent(String tag, MotionEvent event) {
        Log.d("ontouchEvent_"+tag, "event.getX()=" + event.getX() + ", event.getRawX()=" + event.getRawX());
    }
}
