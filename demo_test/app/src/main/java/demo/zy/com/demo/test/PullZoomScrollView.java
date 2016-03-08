package demo.zy.com.demo.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import demo.zy.com.demo.R;
import demo.zy.com.demo.view.widget.PullZoom.PullToZoomScrollView;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/3/8.
 */
public class PullZoomScrollView extends Activity{
    ScrollView scrollView;
    PullToZoomScrollView pullZoomScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullscrollview);
        pullZoomScrollView = (PullToZoomScrollView) findViewById(R.id.scrollview);
        scrollView = pullZoomScrollView.getRootView();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(getLayoutInflater().inflate(R.layout.item_textview, linearLayout, false));
        scrollView.addView(linearLayout);
        scrollView.setBackgroundColor(Color.WHITE);
    }
}
