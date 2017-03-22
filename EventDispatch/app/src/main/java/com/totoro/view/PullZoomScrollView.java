package com.totoro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by zhangyan on 2017/3/21.
 */

public class PullZoomScrollView extends PullZoomBase<ScrollView, ImageView> {
    public PullZoomScrollView(Context context) {
        super(context);
    }

    public PullZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean isReadyPull(float diffx, float diffy) {

        float y =  ViewHelper.getTranslationY(mainView);
        // 1. mainView 在顶部，diffy > 0, mainView 第一行数据已经显示
        // 2. mainView 不在顶部
        boolean ret = (y <= -originMainRect.top && diffy > 0 && mainView.getScrollY() <= 0) || y > -originMainRect.top;
        Log.d("isReadyPull", "diffy="+diffy+", scrollY="+mainView.getScrollY()+", y="+y+", ret="+ret);
        return ret;
    }

    @Override
    protected void smoothLayout(int futureTranslationY) {
        int acceptTranslationY = futureTranslationY;
        if (futureTranslationY < 0){

            ViewHelper.setTranslationY(scaleview, acceptTranslationY / 2); //放大的view 的移动的比 mainView 慢 1／2
            scaleHeight(originScaleRect.height());
        }else if (futureTranslationY == 0){
            ViewHelper.setTranslationY(scaleview, 0);
            scaleHeight(originScaleRect.height());
        }else if (futureTranslationY > 0){
            // 除 2 是为了让下拉的时候有阻碍的感觉，用户可以将 2 换成关于 futureTranslationY 的函数来改进这个效果
            acceptTranslationY = futureTranslationY / 2;
            ViewHelper.setTranslationY(scaleview, 0);
            scaleHeight(originScaleRect.height() + acceptTranslationY);
        }

        ViewHelper.setTranslationY(mainView, acceptTranslationY);
    }

    private void scaleHeight(int height) {
        scaleview.getLayoutParams().height = height;
        requestLayout();
    }

    @Override
    public boolean shouldSmooth() {
        return true;
    }

    @Override
    public ScrollView getMainView() {
        return (ScrollView) getChildAt(1);
    }

    @Override
    public ImageView getScaleView() {
        return (ImageView) getChildAt(0);
    }
}
