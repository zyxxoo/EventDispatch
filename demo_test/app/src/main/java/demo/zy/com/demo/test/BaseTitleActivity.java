package demo.zy.com.demo.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import demo.zy.com.demo.R;
import demo.zy.com.demo.view.TitleView;


/**
 * Created by tubban on 2015/11/9.
 */
public abstract class BaseTitleActivity extends  BaseActivity{

    private LinearLayout title;
    private FrameLayout content;
    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_title);

        title = (LinearLayout) findViewById(R.id.base_title);
        content = (FrameLayout) findViewById(R.id.base_content);
        View contentview = getContentView();
        if (contentview != null) {
            content.addView(contentview);
        }
        View titleView = getTitleView();
        if (titleView != null){
            title.addView(titleView);
        }
        initData();
        initListener();
    }


    abstract protected void initData();

    abstract protected void initListener();

    abstract protected View getContentView();


    protected View getTitleView(){
        TitleView titleView = new TitleView(this){
            @Override
            public Activity getActivity() {

                return BaseTitleActivity.this;
            }
        };
        return titleView;
    }

    protected View getContentView(int resId){

        return LayoutInflater.from(this).inflate(resId, content, false);
    }
}