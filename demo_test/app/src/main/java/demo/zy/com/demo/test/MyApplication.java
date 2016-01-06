package demo.zy.com.demo.test;

import android.app.Application;
import android.util.DisplayMetrics;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/1/6.
 */
public class MyApplication extends Application{
    public static DisplayMetrics mDisplayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        mDisplayMetrics = getResources().getDisplayMetrics();
    }
}
