package demo.zy.com.demo.test;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.Iterator;
import java.util.Map;

import demo.zy.com.demo.R;
import demo.zy.com.demo.utils.AbsParams;
import demo.zy.com.demo.utils.CommonUtil;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/1/6.
 */
public class WebViewWidthJsActivity extends BaseTitleActivity {
    WebView webView;
    TouchInterceptionFrameLayout touchFrameLayout;
    FrameLayout content_frame;
    LinearLayout bottom;
    WebViewHelper helper;
    String baseUrl;
    AbsParams absParams;
    UiProxy uiProxy;
    @Override
    protected void initData() {
        webView = (WebView) findViewById(R.id.webview);
        touchFrameLayout = (TouchInterceptionFrameLayout) findViewById(R.id.bottom_frame);
        bottom = (LinearLayout) findViewById(R.id.bottom_linear);
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        helper = new WebViewHelper(webView, opt);
        uiProxy = new UiProxy(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_wiewwithjs, null);
    }




    UrlOpt opt = new UrlOpt() {
        @Override
        public String getUrl() {
            return baseUrl+getParams(absParams);
        }

        @Override
        public void urlChange(String newUrl, String oldUrl) {

        }

        private String getParams(AbsParams params){
            String ret = "";
            Map<String, Object> map = null;
            StringBuilder sb = new StringBuilder();
            if (params != null && (map = params.toMap()) != null) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if (CommonUtil.isEmpty(entry.getValue())){
                        continue;
                    }

                    String value = (String) entry.getValue();
                    sb.append("&")
                      .append(entry.getKey())
                      .append("=")
                      .append(value);
                }

                if (sb.length() > 0){
                    sb.replace(0, 0, "?");
                    ret = sb.toString();
                }
            }
            return ret;
        }
    };

    public int getContentHeight(){
        return findViewById(R.id.root).getHeight();
    }

    public class UiProxy implements ValueAnimator.AnimatorUpdateListener {
        private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
        private static final int INVALID_POINTER = -1;
        private int mMaximumVelocity;
        private int mSlop;
        private boolean mScrolled;
        private int mActivePointerId = INVALID_POINTER;
        private float mBaseTranslationY;

        private int bottom_h = 0;

        private int bottom_content = 0;

        private final static float bottom_content_ratio = 1 / 3f;

        private VelocityTracker mVelocityTracker;
        private OverScroller mScroller;
        public UiProxy(Context context){
            ViewConfiguration vc = ViewConfiguration.get(context);
            mSlop = vc.getScaledTouchSlop();
            mMaximumVelocity = vc.getScaledMaximumFlingVelocity();
            mScroller = new OverScroller(context.getApplicationContext());
            bottom_h = context.getResources().getDimensionPixelOffset(R.dimen.bottom_height);
            ScrollUtils.addOnGlobalLayoutListener(touchFrameLayout, new Runnable() {
                @Override
                public void run() {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) touchFrameLayout.getLayoutParams();
                    int ch = getContentHeight() - bottom_h;

                    lp.topMargin = ch;
                    touchFrameLayout.setLayoutParams(lp);

                    bottom_content = (int) (getContentHeight() * UiProxy.bottom_content_ratio - bottom_h);

//                    FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) touchFrameLayout.getLayoutParams();
//                    p.height = (int) (getContentHeight()*2/3f - bottom_h);
//                    touchFrameLayout.setLayoutParams(p);
                    updateFlexibleSpace();
                }
            });
            touchFrameLayout.setScrollInterceptionListener(mInterceptionListener);
        }

        private void updateFlexibleSpace() {
            updateFlexibleSpace(ViewHelper.getTranslationY(touchFrameLayout));
        }

        private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
            @Override
            public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
                if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                    // Horizontal scroll is maybe handled by ViewPager
                    return false;
                }

                int flexibleSpace = getScollRange();
                int translationY = (int) ViewHelper.getTranslationY(touchFrameLayout);
                boolean scrollingUp = 0 < diffY;
                boolean scrollingDown = diffY < 0;
                if (scrollingUp) {
                    if (translationY < 0) {
                        mScrolled = true;
                        return true;
                    }
                } else if (scrollingDown) {
                    if (-flexibleSpace < translationY) {
                        mScrolled = true;
                        return true;
                    }
                }
                mScrolled = false;
                return false;
            }

            @Override
            public void onDownMotionEvent(MotionEvent ev) {

                mActivePointerId = ev.getPointerId(0);
                mScroller.forceFinished(true);
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mBaseTranslationY = ViewHelper.getTranslationY(touchFrameLayout);
                mVelocityTracker.addMovement(ev);
            }

            @Override
            public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
                int flexibleSpace = getScollRange();
                float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(touchFrameLayout) + diffY, -flexibleSpace, 0);
                MotionEvent e = MotionEvent.obtainNoHistory(ev);
                e.offsetLocation(0, translationY - mBaseTranslationY);
                mVelocityTracker.addMovement(e);
                updateFlexibleSpace(translationY);
            }

            @Override
            public void onUpOrCancelMotionEvent(MotionEvent ev) {
                mScrolled = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity(mActivePointerId);
                mActivePointerId = INVALID_POINTER;
                mScroller.forceFinished(true);
                int baseTranslationY = (int) ViewHelper.getTranslationY(touchFrameLayout);

                int minY = -(getScollRange());
                int maxY = 0;
                mScroller.fling(0, baseTranslationY, 0, velocityY, 0, 0, minY, maxY);
                if (bottom_content > 0){
                    int contenth = (int) (getContentHeight() * UiProxy.bottom_content_ratio);
                    int range = contenth - bottom_h;
                    if (-mScroller.getFinalY() <= (range) / 2f ){
                        startAnimation(mScroller.getStartY(), -range);
                        mScroller.forceFinished(true);
                        return;
                    }else if (-mScroller.getFinalY() > range / 2f ){
                        startAnimation(mScroller.getStartY(), 0);
                        mScroller.forceFinished(true);
                        return;
                    }
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateLayout();
                    }
                });
            }
        };

        ValueAnimator valueAnimator = null;
        private void startAnimation(float baseTranslationY, float end) {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }

            valueAnimator = ValueAnimator.ofFloat(baseTranslationY, end);
            valueAnimator.addUpdateListener(this);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.start();
        }

        private void updateFlexibleSpace(float translationY) {
            ViewHelper.setTranslationY(touchFrameLayout, translationY);
            offsetContentView(translationY);
        }

        private void offsetContentView(float translationY) {
            ViewHelper.setTranslationY(content_frame, translationY / 2f);
        }

        private void updateLayout() {
            boolean needsUpdate = false;
            float translationY = 0;
            if (mScroller.computeScrollOffset()) {
                translationY = mScroller.getCurrY();
                int flexibleSpace = getScollRange();
                if (-flexibleSpace <= translationY && translationY <= 0) {
                    needsUpdate = true;
                } else if (translationY < -flexibleSpace) {
                    translationY = -flexibleSpace;
                    needsUpdate = true;
                } else if (0 < translationY) {
                    translationY = 0;
                    needsUpdate = true;
                }
            }

            if (needsUpdate) {
                updateFlexibleSpace(translationY);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateLayout();
                    }
                });
            }
        }

        private int getScollRange() {
            int ret = 0;
            int ch = getContentHeight();
            float endtop = ch * (1 - UiProxy.bottom_content_ratio);
            float starttop = ch - bottom_h;

            ret = (int) (starttop - endtop);
            return ret;
        }


        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float y = (float) animation.getAnimatedValue();
            updateFlexibleSpace(y);
        }

        public void showBar() {
            if (isHidden()){
                startAnimation(ViewHelper.getTranslationY(touchFrameLayout), 0);
            }
        }

        public boolean isShow(){
            return ViewHelper.getTranslationY(touchFrameLayout) <= 0;
        }

        public boolean isHidden(){
            return ViewHelper.getTranslationY(touchFrameLayout) == bottom_h;
        }

        public void hiddenBar() {
            if (!isHidden()){
                startAnimation(ViewHelper.getTranslationY(touchFrameLayout), bottom_h);
            }
        }
    }


    public class WebViewHelper{
        WebView webView;
        String url;
        String lastUrl;
        UrlOpt urlOpt;
        public WebViewHelper(WebView view, UrlOpt opt){
            this.webView = view;
            // 得到设置属性的对象
            WebSettings webSettings = webView.getSettings();

            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            // 使能JavaScript
            webSettings.setJavaScriptEnabled(true);

            // 支持中文，否则页面中中文显示乱码
            webSettings.setDefaultTextEncodingName("GBK");

            webView.addJavascriptInterface(new JavaScriptInterface(), "TubbanXAndroid");

            // 限制在WebView中打开网页，而不用默认浏览器
            this.webView.setWebViewClient(new WebViewClient());
            this.urlOpt = opt;
        }

        public void load(){
            url = urlOpt.getUrl();
            webView.loadUrl(url);
            if (!TextUtils.equals(url, lastUrl)){
                urlOpt.urlChange(url, lastUrl);
                lastUrl = url;
            }
        }

        public WebView getWebView() {
            return webView;
        }

        public void setWebView(WebView webView) {
            this.webView = webView;
        }
    }

    private class JavaScriptInterface{

        @JavascriptInterface
        public void onLocationChange(boolean move, String lat, String lon){};

        @JavascriptInterface
        public void onLocationCoordinateChange(float x, float y){};

        @JavascriptInterface
        public void showBar(){
            uiProxy.showBar();
        };

        @JavascriptInterface
        public void hiddenBar(){
            uiProxy.hiddenBar();
        };
    }
    public interface UrlOpt{
        public  String getUrl();
        public void urlChange(String newUrl, String oldUrl);
    }
}
