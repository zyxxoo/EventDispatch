package com.totoro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by zhangyan on 2017/3/21.
 */

public class PullZoomListView extends PullZoomBase<ListView, ImageView> {
    public PullZoomListView(Context context) {
        super(context);
    }

    public PullZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullZoomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean isReadyPull(float diffx, float diffy) {

        int y = (int) ViewHelper.getTranslationY(mainView);
        // 1. mainView 在顶部，diffy > 0, mainView 第一行数据已经显示
        // 2. mainView 不在顶部
        boolean ret = ((y <= -originMainRect.top && diffy > 0 && isFirstShow()) || y > -originMainRect.top);
        Log.d("isReadyPull", "diffy="+diffy+", ret="+ret);
        return ret;
    }

    // listview 第一条数据显示 或者 数据为空
    private boolean isFirstShow() {
        boolean ret = true;
        if (mainView != null && mainView.getAdapter() != null){
            if (mainView.getAdapter().getCount() > 0){
                ret = (mainView.getFirstVisiblePosition() == 0) && (mainView.getChildAt(0).getTop() >= 0);
            }
        }

        return ret;
    }

    @Override
    protected void smoothLayout(int futureTranslationY) {
        if (futureTranslationY < 0){

            ViewHelper.setTranslationY(scaleview, futureTranslationY / 2);
            scaleHeight(originScaleRect.height());
        }else if (futureTranslationY == 0){
            ViewHelper.setTranslationY(scaleview, 0);
            scaleHeight(originScaleRect.height());
        }else if (futureTranslationY > 0){
            ViewHelper.setTranslationY(scaleview, 0);
            int height = scaleview.getLayoutParams().height;
            scaleHeight(originScaleRect.height() + futureTranslationY);
        }

        ViewHelper.setTranslationY(mainView, futureTranslationY);
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
    public ListView getMainView() {
        return (ListView) getChildAt(1);
    }

    @Override
    public ImageView getScaleView() {
        return (ImageView) getChildAt(0);
    }
}
