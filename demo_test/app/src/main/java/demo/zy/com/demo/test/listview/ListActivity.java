package demo.zy.com.demo.test.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/3/6.
 */
public class ListActivity extends Activity{

    ListView listView;
    TypeMulityAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter = new TypeMulityAdapter());
        Helper.listActivity = this;
    }

    public void change() {
        for (int i = 0; i < 2; i++) {
            int temp = mAdapter.list.get(i).type;
            temp = Math.abs(1 - temp);
            mAdapter.list.get(i).type = temp;
        }
        mAdapter.notifyDataSetChanged();
        listView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 2; i++){
                    String tag = (String) listView.getChildAt(i).getTag(R.id.tag_type);
                    Toast.makeText(ListActivity.this, tag, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class TypeMulityAdapter extends BaseAdapter{
        List<Data> list = new LinkedList<>();
        public TypeMulityAdapter(){
//            Random random = new Random();

            for (int i = 0; i < 4; i++){

                Data data = new Data();
                data.s = i + "";
                data.type = i % 2;
                list.add(data);
            }
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ret = null;
            switch (getItemViewType(position)){
                case 0:
                    ret = getView0(position, convertView, parent);
                    break;
                case 1:
                    ret = getView1(position, convertView, parent);
                    break;
            }
            return ret;
        }

        private View getView0(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder2 holder2 = null;
            if (view == null){
                view = LayoutInflater.from(ListActivity.this).inflate(R.layout.item_type2_t, parent, false);
                holder2 = new ViewHolder2();
                holder2.tv = (TextView) view.findViewById(R.id.tv);
                view.setTag(holder2);
            }else{
                holder2 = (ViewHolder2) view.getTag();
            }

            Data data = (Data) getItem(position);
            holder2.tv.setText(data.s);
            view.setTag(R.id.tag_type, "text");
            return view;
        }

        private View getView1(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null){
                view = LayoutInflater.from(ListActivity.this).inflate(R.layout.item_type1_t_i, parent, false);
                holder = new ViewHolder();
                holder.tv = (TextView) view.findViewById(R.id.tv);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            Data data = (Data) getItem(position);
            holder.tv.setText(data.s);
            view.setTag(R.id.tag_type, "text_image");
            return view;
        }

        public class ViewHolder{
            TextView tv;
            ImageView iv;
        }
        public class ViewHolder2{
            TextView tv;
        }

        @Override
        public int getItemViewType(int position) {
            int ret = 0;
            Data data = (Data) getItem(position);
            ret = data.type;
            return ret;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
        public class Data{
            public String s;
            public int type = 0;
        }
    }

    public void click(View v){
        startActivity(new Intent(this, OtherActivity.class));
//        change();
    }

    @Override
    protected void onDestroy() {
        Helper.listActivity = null;
        super.onDestroy();
    }

    public static class Helper{
        public static ListActivity listActivity;
    }
}
