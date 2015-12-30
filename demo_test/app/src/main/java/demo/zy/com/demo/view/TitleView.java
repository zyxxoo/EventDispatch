package demo.zy.com.demo.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import demo.zy.com.demo.R;


/**
 * Created by tubban on 2015/11/9.
 */
public class TitleView extends FrameLayout implements View.OnClickListener {
    public static String STYLE_DEFAULT = "default";
    private String style = STYLE_DEFAULT;
    ImageView back;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.title_default, this, true);
        back = (ImageView) findViewById(R.id.title_back);
        back.setOnClickListener(this);
    }

    public Activity getActivity(){

        return null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.title_back:
                if (titleBackListener != null){
                    titleBackListener.onBack(v);
                }else {

                    defaultHandler();
                }
                break;
        }
    }

    private void defaultHandler() {

        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public TitleBackListener getTitleBackListener() {
        return titleBackListener;
    }

    public void setTitleBackListener(TitleBackListener titleBackListener) {
        this.titleBackListener = titleBackListener;
    }

    TitleBackListener titleBackListener;
    public interface TitleBackListener{

        public void onBack(View view);
    }
}
