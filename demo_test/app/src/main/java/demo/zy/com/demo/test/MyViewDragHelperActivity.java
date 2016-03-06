package demo.zy.com.demo.test;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.LinkedList;
import java.util.List;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/1/27.
 */
public class MyViewDragHelperActivity extends BaseTitleActivity{
    ListView listView;
    List<String>total;
    @Override
    protected void initData() {
     listView = (ListView) findViewById(R.id.listView);
        total = new LinkedList<>();
        for (int i = 0; i < 200; i ++){
            total.add("test"+i);
        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, total));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mydrawhelper, null);
    }
}
