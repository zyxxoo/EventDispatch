package demo.zy.com.demo.test;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import demo.zy.com.demo.R;
import demo.zy.com.demo.view.MulRuleView;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2015/12/30.
 */
public class MulRultViewActivity extends BaseTitleActivity{
    MulRuleView mulRuleView;
    int[] color = {Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN, Color.GREEN, Color.DKGRAY};
    int i = 0;
    @Override
    protected void initData() {
         mulRuleView = (MulRuleView) findViewById(R.id.mulruleView);
        ImageView imageView = new ImageView(MulRultViewActivity.this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundColor(getColor());
         mulRuleView.addView(imageView);
    }

    private int getColor() {
        return color[(i++) % color.length];
    }

    @Override
    protected void initListener() {

        mulRuleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = new ImageView(MulRultViewActivity.this);
                imageView.setBackgroundColor(getColor());
                imageView.setImageResource(R.mipmap.ic_launcher);
                if (mulRuleView.getChildCount() < 6){
                    mulRuleView.addView(imageView);
                }
            }
        });
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mulrult, null);
    }
}
