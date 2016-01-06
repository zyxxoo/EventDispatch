package demo.zy.com.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2015/12/31.
 */
public class TriLeftOneRightView extends ViewGroup{
    Config config;
    public TriLeftOneRightView(Context context) {
        this(context, null);
    }

    public TriLeftOneRightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriLeftOneRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public TriLeftOneRightView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
         config = new Config(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(resolveSize(config.screenWidth, widthMeasureSpec));
        int height = (int) (width * config.ratio - config.vspace / 2f);
        int size = getChildCount();
        if (config.maxCount <= 0 || size == 0) return;

        View child = null;
        int cw = 0;
        int ch = 0;
        for (int i = 1; i <= size && i <= config.maxCount; i++){

            child = getChildAt(i - 1);

            cw = config.getWith(width, i);
            ch = config.getHeight(width, i);

            int widthspec = MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(ch, MeasureSpec.EXACTLY);
            measureChild(child, widthspec, heightspec);
        }

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size1 = MeasureSpec.getSize(resolveSize(height, heightMeasureSpec));
        setMeasuredDimension(width, size1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int size = getChildCount();
        View child = null;
        Point point = null;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        for (int i = 1; i <= size && i <= config.maxCount; i++){
            child = getChildAt(i - 1);
            point = config.getPoint(width, height, i);
            if (point != null){
                child.layout(point.x, point.y, point.x + child.getMeasuredWidth(), point.y + child.getMeasuredHeight());
            }
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    public class Config{
        public  int screenWidth;
        public  float ratio = 2f/3f; //左右比例
        public  int hspace = 20; //水平线的高度
        public  int vspace = 20; //垂直线的宽度
        public final int maxCount = 3;

        public Config(Context context){
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }

        public int getWith(int width, int i) {
            int ret = 0;
            switch (i){
                case 1:
                    ret = (int) (width * ratio - vspace / 2f);
                    break;
                case 2:
                case 3:
                    ret = (int) (width * (1 - ratio) - vspace / 2f);
                    break;
            }

            return ret;
        }

        public int getHeight(int width, int i) {
            int ret = 0;
            switch (i){
                case 1:

                    ret = (int) (width * ratio - vspace / 2f);
                    break;
                case 2:
                case 3:
                    ret = (int) ((width * ratio - vspace / 2f) / 2f - hspace / 2f);
                    break;
            }
            return ret;
        }

        public Point getPoint(int width, int height, int i) {
            Point point = null;

            switch (i){
                case 1:

                    point = new Point(0, 0);
                    break;
                case 2:

                    point = new Point(getWith(width, 1) + vspace, 0);
                    break;
                case 3:

                    point = new Point(getWith(width, 1) + vspace, getHeight(width, 2) + hspace);
                    break;
            }

            return point;
        }
    }
}
