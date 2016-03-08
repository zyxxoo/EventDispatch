package demo.zy.com.demo.view.widget.PullZoom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewHelper;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/3/8.
 */
public class PullToZoomScrollView extends PullToZoomBase<ScrollView>{
    public PullToZoomScrollView(Context context) {
        this(context, null);
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count >= 2 && zoomView == null){
            this.zoomView = getChildAt(1);
            bringChildToFront(rootView);
            rejustRootHeight();
        }
    }

    private void rejustRootHeight() {
        if (zoomView != null){
            measureZoomView();
            int height = zoomView.getLayoutParams().height;
            MarginLayoutParams params = (MarginLayoutParams) rootView.getLayoutParams();
            if (params == null){
                params = generateDefaultLayoutParams();
            }
            params.topMargin = Math.round(height);
            rootView.setLayoutParams(params);
        }
    }

    private void measureZoomView() {
        ViewGroup.LayoutParams params = zoomView.getLayoutParams();
        int height = 0;
        int width = 0;
        if (params.height > 0){
            height = params.height;
        }else{
            height = getContext().getResources().getDimensionPixelOffset(R.dimen.default_pullzoom_height);
        }
        params.height = height;
        zoomView.setLayoutParams(params);
    }


    @Override
    protected boolean canPull(float translationY) {
        boolean ret = (rootViewRect.top + translationY) >= 0;
        return true;
    }

    @Override
    protected void pullEvent(float translationY) {
        if (translationY > 0) {
            ViewHelper.setTranslationY(rootView, translationY / 2.0f);
            if (zoomViewRect.bottom - (rootViewRect.top + translationY / 2.0f) <= 0.5) {
                toZoom(Math.max(translationY, 0));
            }
        }else{
            ViewHelper.setTranslationY(rootView, translationY);
            float zoomTranslationY = translationY;
            ViewHelper.setTranslationY(zoomView, zoomTranslationY / 2.0f);

        }
    }

    private void toZoom(float v) {

        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
        layoutParams.height = Math.round(zoomViewRect.bottom - zoomViewRect.top + v);
        zoomView.setLayoutParams(layoutParams);
    }

    @Override
    protected ScrollView createRootView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new ScrollView(context);
        return scrollView;
    }

    @Override
    protected boolean smoothScrollToTop(OverScroller mScroller, int velocityY) {
        if (ViewHelper.getTranslationY(rootView) > 0) {
            mScroller.startScroll(0, (int) ViewHelper.getTranslationY(rootView), 0, -Math.round(ViewHelper.getTranslationY(rootView)), 200);
            return true;
        }else{

            if (velocityY > 0) {
                mScroller.fling(0, Math.round(ViewHelper.getTranslationY(rootView)), 0, velocityY, 0, 0, Math.round(ViewHelper.getTranslationY(rootView)), 0);
            }else{
                mScroller.fling(0, Math.round(ViewHelper.getTranslationY(rootView)), 0, velocityY, 0, 0, Math.round(-zoomViewRect.height()), Math.round(ViewHelper.getTranslationY(rootView)));
            }
            return true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rootView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected boolean isReadyForPullStart(float diffX, float diffY) {
        boolean ret = true;
        if (diffY < 0){//向上
            if (ViewHelper.getTranslationY(rootView) <= -rootViewRect.top){
                ret = false;
            }
        }else if (diffY > 0){
            if (rootView.getScrollY() > 0){
                ret = false;
            }
        }
        return ret;
    }
}
