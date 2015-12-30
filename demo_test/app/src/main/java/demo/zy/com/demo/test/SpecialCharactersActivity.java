package demo.zy.com.demo.test;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2015/12/8.
 */
public class SpecialCharactersActivity extends BaseTitleActivity{
    LinearLayout bottom;
    RelativeLayout root;
    @Override
    protected void initData() {

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        getWindow().setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bottom = (LinearLayout) findViewById(R.id.bottom_linear);
        root = (RelativeLayout) findViewById(R.id.root);
        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                Log.i("onLayoutChange", "-------->"+bottom+"");
            }
        });

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int[] loc = new int[2];
                root.getLocationOnScreen(loc);
                int screenY = loc[1];
                int bottom = root.getBottom();
                Log.i("onglobal", "------->" + "screenY=" + screenY + " , bottom=" + bottom);
            }
        });

        Log.i("screen_display", "------->"+getResources().getDisplayMetrics().heightPixels);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_sepcialcharacters, null);
    }
}
