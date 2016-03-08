package demo.zy.com.demo.view.widget.PullZoom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.view.ViewHelper;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/2/3.
 */
public abstract class PullToZoomBase<T extends View> extends com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout implements IPullToZoom<T>, TouchInterceptionFrameLayout.TouchInterceptionListener {
    public static final String TAG = "PullToZoomBase";
    private static final float FRICTION = 2.0f;
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final int INVALID_POINTER = -1;
    View zoomView;
    T rootView;

    private float factor = 2.0f;

    protected float rootViewTop;

    protected int mMaximumVelocity;
    protected int mSlop;

    protected VelocityTracker mVelocityTracker;
    protected OverScroller mScroller;

    private boolean isZoomEnabled = true;
    private boolean isZooming = false;

    private int mActivePointerId = INVALID_POINTER;
    private float mBaseTranslationY;

    Rect rootViewRect;
    Rect zoomViewRect;

    public PullToZoomBase(Context context) {
        this(context, null);
    }

    public PullToZoomBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToZoomBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setScrollInterceptionListener(this);
        ViewConfiguration vc = ViewConfiguration.get(context);
        mSlop = vc.getScaledTouchSlop();
        mMaximumVelocity = vc.getScaledMaximumFlingVelocity();
        mScroller = new OverScroller(context.getApplicationContext());
        rootView = createRootView(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyPullToZoom, defStyleAttr, 0);
        Resources res = context.getResources();
        rootViewTop = a.getDimension(R.styleable.MyPullToZoom_rootview_top, res.getDimension(R.dimen.default_pullzoom_top));
        a.recycle();
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = (int) rootViewTop;
        rootView.setLayoutParams(params);
        addView(rootView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (rootViewRect == null) {
            rootViewRect = new Rect();
            rootView.getHitRect(rootViewRect);
        }
        if (zoomView != null && zoomViewRect == null){
            zoomViewRect = new Rect();
            zoomView.getHitRect(zoomViewRect);
        }

        Log.d("---->", "onLayout, rootViewRect=" + rootViewRect + " ,zoomViewRect=" + zoomViewRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("---->", "onMeasure, rootViewTop=" + rootViewTop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d(TAG, "touch" + ev.getY());
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
        if (zoomView == null) {
            return false;
        }
        return isReadyForPullStart(diffX, diffY);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        if (zoomView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (ViewHelper.getTranslationY(zoomView) <= -rootViewTop){
                          onUpOrCancelMotionEvent(ev);
                    }
                        break;
            }
        }
        return ret;
    }

    @Override
    public void onDownMotionEvent(MotionEvent ev) {
        if (zoomView == null) return;
        mActivePointerId = ev.getPointerId(0);
        mScroller.forceFinished(true);
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
        mBaseTranslationY = ViewHelper.getTranslationY(rootView);
        mVelocityTracker.addMovement(ev);
    }

    @Override
    public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
        if (zoomView == null) return;
//        float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(rootView) + diffY, -flexibleSpace, 0);
        if (Math.abs(diffY) > 1 && Math.abs(diffY) >= mSlop) {
            float translationY = mBaseTranslationY;
            translationY += diffY;
//            MotionEvent e = MotionEvent.obtainNoHistory(ev);
//            e.offsetLocation(0, translationY - mBaseTranslationY);
            mVelocityTracker.addMovement(ev);
//            if (canPull(translationY)) {
//                pullEvent(translationY);
//            }
            Log.d(TAG, translationY+" diffy=" + diffY);
            if (isReadyForPullStart(diffX, diffY)){
                if (canPull(translationY)) {
                    pullEvent(translationY);
                }
            }
            Log.d("move", diffY+"");
        }
    }

    @Override
    public void onUpOrCancelMotionEvent(MotionEvent ev) {
        if (zoomView == null) return;
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        int velocityY = (int) mVelocityTracker.getYVelocity(mActivePointerId);
        mActivePointerId = INVALID_POINTER;
        mScroller.forceFinished(true);
        int baseTranslationY = (int) ViewHelper.getTranslationY(rootView);
        if (smoothScrollToTop(mScroller, velocityY)) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateLayout();
                }
            });
        }
    }

    private void updateLayout() {
        boolean needsUpdate = false;
        float translationY = 0;
        if (mScroller.computeScrollOffset()){
            translationY = mScroller.getCurrY();
            if (rootView.getTop() + translationY != rootViewRect.top){
                needsUpdate = true;
            }
        }

        if (needsUpdate){
            pullEvent(translationY);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                 updateLayout();
                }
            });
        }
    }

    protected abstract boolean canPull(float translationY);

    @Override
    public View getZoomView() {
        return zoomView;
    }

    @Override
    public boolean isPullToZoomEnabled() {
        return isZoomEnabled;
    }

    @Override
    public boolean isZooming() {
        return isZooming;
    }

    protected abstract void pullEvent(float translationY);

    public void setZoomView(View zoomView){this.zoomView = zoomView; requestLayout();};

    protected abstract T createRootView(Context context, AttributeSet attrs);

    /**
     *
     * @param mScroller
     * @param velocityY
     * @return true 调用update方法，要求canpull重写
     */
    protected abstract boolean smoothScrollToTop(OverScroller mScroller, int velocityY);

    protected abstract boolean isReadyForPullStart(float diffX, float diffY);

    public boolean isZoomEnabled() {
        return isZoomEnabled;
    }

    public void setIsZoomEnabled(boolean isZoomEnabled) {
        this.isZoomEnabled = isZoomEnabled;
    }

    public void setIsZooming(boolean isZooming) {
        this.isZooming = isZooming;
    }

    @Override
    public T getRootView() {
        return rootView;
    }

    public void setRootView(T rootView) {
        this.rootView = rootView;
    }


}
