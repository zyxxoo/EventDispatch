package demo.zy.com.demo.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/3/4.
 */
public class ListActivity extends Activity{
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter());
    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return LayoutInflater.from(ListActivity.this).inflate(R.layout.item_listview, parent, false);
        }
    }
}
