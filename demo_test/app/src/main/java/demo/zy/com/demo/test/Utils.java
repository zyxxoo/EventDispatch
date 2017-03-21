package demo.zy.com.demo.test;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zhangyan on 2017/3/20.
 */

public class Utils {
    public static void logEvent(String tag, MotionEvent mv){
        switch (mv.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d(tag, "down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(tag, "move");

                break;
            case MotionEvent.ACTION_UP:
                Log.d(tag, "up");
                break;
        }

    }
}
