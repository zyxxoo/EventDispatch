package demo.zy.com.demo.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2015/12/4.
 */
public class RecycleViewCardViewActivity  extends BaseTitleActivity{

    RecyclerView recyclerView;

    @Override
    protected void initData() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected View getContentView() {

        return LayoutInflater.from(this).inflate(R.layout.activity_recyclecardview, null);
    }


    public static class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
