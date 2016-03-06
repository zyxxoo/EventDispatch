package demo.zy.com.demo.view.widget.PullZoom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.OverScroller;

import com.nineoldandroids.view.ViewHelper;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/2/3.
 */
public class PullToZoomListView extends PullToZoomBase<ListView>{

    //y = (k/2)*x*x + x;
    public PullToZoomListView(Context context) {
        this(context, null);
    }

    public PullToZoomListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToZoomListView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        }
    }

    int height = 0;
    float k = 0;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getMeasuredHeight();
        k = -2*rootViewTop/(height*height);

        Log.d("---->", "onSizeChanged, height=" + height + " ,k=" + k);

    }

    @Override
    protected boolean canPull(float translationY) {
          boolean ret = (rootViewRect.top + translationY) >= 0;
        return true;
    }

    @Override
    protected void pullEvent(float translationY) {
        if (translationY > 0) {
            ViewHelper.setTranslationY(rootView, translationY);
            if (zoomViewRect.bottom - (rootViewRect.top + translationY) <= 0.5) {
                toZoom(Math.max(translationY, 0));
            }
        }else{
            ViewHelper.setTranslationY(rootView, translationY);
            ViewHelper.setTranslationY(zoomView, translationY / 2.0f);
        }
    }

    private void toZoom(float v) {

        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
        layoutParams.height = Math.round(zoomViewRect.bottom - zoomViewRect.top + v);
        zoomView.setLayoutParams(layoutParams);
    }

    @Override
    public void setHeaderView(View headerView) {

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
    public void setZoomView(View zoomView) {
        this.zoomView = zoomView;
        addView(zoomView, 0);
    }

    @Override
    protected ListView createRootView(Context context, AttributeSet attrs) {
        ListView lv = new ListView(context, attrs);
        lv.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    @Override
    protected boolean smoothScrollToTop(OverScroller mScroller, int velocityY) {
        if (ViewHelper.getTranslationY(rootView) > 0) {
            mScroller.startScroll(0, (int) ViewHelper.getTranslationY(rootView), 0, -Math.round(ViewHelper.getTranslationY(rootView)), 200);
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected boolean isReadyForPullStart(float diffX, float diffY) {
        boolean ret = true;
        float baseTranslationY = ViewHelper.getTranslationY(rootView);
        if (Math.abs(diffY) < Math.abs(diffX)){
            return false;
        }
            if (diffY < 0) { //向上
                if (rootViewTop + baseTranslationY <= 0) {
                    ret = false;
                }
//                ret = isLastVisible();
            } else if (diffY > 0) {//向下
                ret = isFirstVisible();

//                if (baseTranslationY >= 0) {
//                    ret = isFirstVisible();
//                }else{
//                    ret = true;
//                }
            }

        Log.d("---->", "isReadyForPullStart, diffx=" + diffX + " ,diffy=" + diffY
                + " , baseTranslationY=" + baseTranslationY + " ,ret=" + ret);
        return ret;
    }

    private boolean isLastVisible() {
        boolean ret = true;
        if (rootView.getAdapter() != null && rootView.getAdapter().getCount() > 0 && rootView.getCount() > 0) {
            if (rootView.getLastVisiblePosition() != rootView.getAdapter().getCount() - 1 || rootView.getChildAt(rootView.getLastVisiblePosition() - rootView.getFirstVisiblePosition()).getBottom() > (rootView.getBottom() - rootView.getTop())){
                ret = false;
            }
        }
        return ret;
    }

    private boolean isFirstVisible() {
        boolean ret = true;
        if (rootView.getAdapter() != null && rootView.getAdapter().getCount() > 0 && rootView.getCount() > 0){
            if (rootView.getFirstVisiblePosition() != 0 || rootView.getChildAt(0).getTop() < 0){
                ret = false;
            }
        }
        return ret;
    }

}
