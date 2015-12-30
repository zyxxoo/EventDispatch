package demo.zy.com.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by wangchao on 2015/7/16.
 */
public class MyListView extends ListView {
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    private void setListViewHeightBasedOnChildren() {
//        // 获取ListView对应的Adapter
//        ListAdapter listAdapter = this.getAdapter();
//
//        if (listAdapter == null) {
//            return;
//        }
//        int totalHeight = 0;
//        int len = listAdapter.getCount();
//        for (int i = 0; i < len; i++) { // listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, this);
//            listItem.measure(0, 0); // 计算子项View 的宽高
//            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
//        }
//        ViewGroup.LayoutParams params = this.getLayoutParams();
//        params.height = totalHeight
//                + (this.getDividerHeight() * (listAdapter.getCount() - 1));
//        // listView.getDividerHeight()获取子项间分隔符占用的高度
//        // params.height最后得到整个ListView完整显示需要的高度
//        this.setLayoutParams(params);
//    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

//        setListViewHeightBasedOnChildren();
    }

}
