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
        return true;
    }

    private void logEvent(String tag, MotionEvent event) {
        Log.d("ontouchEvent_"+tag, "event.getX()=" + event.getX() + ", event.getRawX()=" + event.getRawX());
    }
}
