package com.totoro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.totoro.view.PullZoomListView;

import java.util.ArrayList;
import java.util.List;

public class Pull2ListViewActivity extends AppCompatActivity {

    PullZoomListView pullZoomListView;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull2_list_view);
        pullZoomListView = (PullZoomListView) findViewById(R.id.pullzoomlistview);
        listView = (ListView) findViewById(R.id.listview);

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));
    }


    List<String> getData(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 1000; i++){
            data.add(String.format("item_%d", i));
        }

        return data;
    }
}
