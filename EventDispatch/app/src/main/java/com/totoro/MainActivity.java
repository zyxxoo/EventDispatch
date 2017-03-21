package com.totoro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

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


        B b = new B();
        A a = b;
        Log.d("---->b instanceof c", (a instanceof C) + "");
    }





    public static class UIActivity{
        public List<Class> uis;
        public List<String> names;
        public UIActivity(){
            uis = new LinkedList<>();
            uis.add(Pull2ScrollActivity.class);
            uis.add(Pull2ListViewActivity.class);
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

    public class A{

    }

    public class B extends A{}

    public class C extends A{};
}
