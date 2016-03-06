package demo.zy.com.demo.test.pull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import demo.zy.com.demo.R;

/**
 * Created by kai.wang on 3/18/14.
 */
public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Point point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
//        display.getRealSize(point);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Log.d("screen_params", "density=" + displayMetrics.density +
                "densitydpi" + displayMetrics.densityDpi + "scaledensity" + displayMetrics.scaledDensity + "def" + point);

        getScreenSizeOfDevice2();
    }
    private void getScreenSizeOfDevice2() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        int ppi = (int) Math.sqrt((Math.pow(point.x, 2) + Math.pow(point.y, 2))/(x + y));

        Log.d("screen_params", "Screen inches : " + screenInches+"denityPPI=" + ppi);
    }
    public void zoomLayout(View view){
        Intent intent = new Intent(MainActivity.this,ZoomLayoutActivity.class);
        startActivity(intent);
    }


    public void zoomList(View view){
        Intent intent = new Intent(MainActivity.this,ZoomListActivity.class);
        startActivity(intent);
    }

}