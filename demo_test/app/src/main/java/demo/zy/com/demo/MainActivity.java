package demo.zy.com.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import demo.zy.com.demo.test.MulRultViewActivity;
import demo.zy.com.demo.test.RecycleViewCardViewActivity;
import demo.zy.com.demo.test.SpecialCharactersActivity;

public class MainActivity extends AppCompatActivity {

    ListView list;
    UIActivity uiActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listview);
        uiActivity = new UIActivity();
        list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, uiActivity.getData()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              uiActivity.openUI(MainActivity.this, position);
            }
        });
    }





    public static class UIActivity{
        public List<Class> uis;
        public List<String> names;
        public UIActivity(){
            uis = new LinkedList<>();
            uis.add(RecycleViewCardViewActivity.class);
            uis.add(SpecialCharactersActivity.class);
            uis.add(MulRultViewActivity.class);
            names = new LinkedList<>();
        }

        public void openUI(Context context, int position){

            Intent intent = new Intent(context, uis.get(position));
            context.startActivity(intent);
        }

        public List<String> getData(){
            names.clear();
            for (Class c: uis){
                names.add(c.getSimpleName());
            }

            return names;
        }
    }

}
