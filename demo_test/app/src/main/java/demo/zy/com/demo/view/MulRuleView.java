package demo.zy.com.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * viewgroup 学习之旅
 * 公司:Tubban
 * 作者:章炎
 * 时间:2015/12/30.
 */
public class MulRuleView extends ViewGroup{

    List<Data> dataList = new LinkedList<>();
    Config config;

    public MulRuleView(Context context) {
        this(context, null);
    }

    public MulRuleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MulRuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @TargetApi(21)
    public MulRuleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        config = new Config(context);
    }


    public void setData(List<Data> datas){
        removeAllViews();
        this.dataList.addAll(datas);
        for (Data data : datas){
            addView(data.view);
        }
        requestLayout();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = getChildCount();

        int height = 0;
        int width = MeasureSpec.getSize(resolveSize(config.screenWidth, widthMeasureSpec));
        int cw = 0;
        switch (size){
            case 1:
                cw = (int) (width * config.ratio_one);
                height = cw;
                  break;
            case 2:
                cw = (width - config.Hpadding) / 2;
                height = cw;
                  break;
            case 3:
                cw = (width - 2 * config.Hpadding) / 3;
                height = cw;
                  break;
            case 4:

                cw = (width - config.Hpadding) / 2;
                height = 2 * cw + config.Vpadding;
                  break;
            case 5:
            case 6:

                cw = (width - 2 * config.Hpadding) / 3;
                height = 2 * cw + config.Vpadding;
                  break;
        }

//        measureChildren(MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY));
        View child = null;
        for (int i = 0; i < size; i++){
            child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY));
        }

        height = MeasureSpec.getSize(resolveSize(height, heightMeasureSpec));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int size = getChildCount();
        int line = getLine(size);
        int row = getRow(size); //列

        int tl = 0;//行
        int tr = 0;//列


        int cw = 0;
        int ch = 0;
        int left = 0;
        int top = 0;
        for (int i = 1; i <= size; i++){
            tr = i % row == 0 ? row : i % row;//列
            tl = (i - tr) / row + 1;//行

            View child = getChildAt(i - 1);
            cw = child.getMeasuredWidth();
            ch = child.getMeasuredHeight();

            top = (tl - 1) * (ch + config.Hpadding);
            left = (tr - 1) * (cw + config.Vpadding);
            child.layout(left, top, left + cw, top + ch);
        }

    }

    private int getRow(int size) {
        int ret = 0;
        switch (size){
            case 1:
                ret = 1;
                break;
            case 2:
            case 4:
                ret = 2;
                break;
            case 3:
            case 5:
            case 6:
                ret = 3;
                break;
        }
        return ret;
    }

    private int getLine(int size) {
        int ret = 0;
        switch (size){
            case 1:
            case 2:
            case 3:
                ret = 1;
                break;
            case 4:
            case 5:
            case 6:
                ret = 2;
                break;
        }
        return ret;
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
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    public class Data{
        ImageView view;
    }

    public class Config{
        public int screenWidth = 0;
        public float ratio_one = 2f / 3f;
        public int Hpadding = 20;
        public int Vpadding = 20;

        public Config(Context context){
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
    }
}
