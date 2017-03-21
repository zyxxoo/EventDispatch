package com.pullzoom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.pullzoom.view.PullZoomScrollView;


public class Pull2ScrollActivity extends AppCompatActivity {

    PullZoomScrollView pullZoomScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull2_scroll);
        pullZoomScrollView = (PullZoomScrollView) findViewById(R.id.scrollview);
    }
}
