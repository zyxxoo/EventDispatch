package demo.zy.com.demo.view.widget.PullZoom;

import android.view.View;

import com.github.ksoichiro.android.observablescrollview.Scrollable;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/2/3.
 */
public interface IPullToZoom<T extends View> {
    public View getZoomView();
    public boolean isPullToZoomEnabled();
    public boolean isZooming();
}
