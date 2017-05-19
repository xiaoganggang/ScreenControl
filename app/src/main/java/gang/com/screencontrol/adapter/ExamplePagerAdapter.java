package gang.com.screencontrol.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gang.com.screencontrol.R;

/**
 * Created by xiaogangzai on 2017/5/19.
 */

public class ExamplePagerAdapter extends PagerAdapter {
    private List<String> mDataList;
    private Context context;
    //下方视图
    private RecyclerView recyleview_view;
    private Adapter_TypeView mAdapter_type;
    public ExamplePagerAdapter(List<String> dataList, Context context) {
        mDataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       //根据position绑定数据
        View view = View.inflate(context, R.layout.layout_main_view_recyle, null);
        recyleview_view=(RecyclerView)view.findViewById(R.id.recyle_type_view);
        mAdapter_type = new Adapter_TypeView(context, mDataList);
        recyleview_view.setAdapter(mAdapter_type);
        recyleview_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        TextView textView = (TextView) object;
        String text = textView.getText().toString();
        int index = mDataList.indexOf(text);
        if (index >= 0) {
            return index;
        }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }



}