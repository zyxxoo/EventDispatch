package demo.zy.com.demo.test;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import demo.zy.com.demo.R;

/**
 * 公司:Tubban
 * 作者:章炎
 * 时间:2016/1/15.
 */
public class HandlerTestActivity extends BaseTitleActivity implements View.OnClickListener {
    Button send;
    Button cancel;
    @Override
    protected void initData() {

        send = (Button) findViewById(R.id.send_btn);
        cancel = (Button) findViewById(R.id.cancel);
    }

    @Override
    protected void initListener() {

        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_handlertest, null);
    }

    Handler handler = new Handler();
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.send_btn:

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HandlerTestActivity.this, "执行啊哈哈哈", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
                break;
            case R.id.cancel:

                break;
        }
    }
}
