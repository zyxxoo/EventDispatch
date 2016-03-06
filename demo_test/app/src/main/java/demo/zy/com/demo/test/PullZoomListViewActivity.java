package demo.zy.com.demo.test;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import demo.zy.com.demo.R;
import demo.zy.com.demo.view.widget.PullZoom.PullToZoomListView;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/2/4.
 */
public class PullZoomListViewActivity extends BaseTitleActivity{
    PullToZoomListView pullToZoomListView;
    ListView listView;
    List<String> total;
    @Override
    protected void initData() {
        pullToZoomListView = (PullToZoomListView) findViewById(R.id.pullzoomlistview);
        listView = pullToZoomListView.getRootView();
        total = getTotal();
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, total));

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_pullzoomlistview, null);
    }
}
