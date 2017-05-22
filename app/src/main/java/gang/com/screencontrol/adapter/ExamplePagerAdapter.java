package gang.com.screencontrol.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.DeviceData;
import gang.com.screencontrol.bean.Type;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;

/**
 * Created by xiaogangzai on 2017/5/19.
 */

public class ExamplePagerAdapter extends PagerAdapter {
    private List<DeviceData> mDataList;
    private List<DeviceData> mDataList_device = new ArrayList<>();
    private List<DeviceData> mDataList_moshi = new ArrayList<>();
    private List<DeviceData> mDataList_fenzu = new ArrayList<>();
    private List<DeviceData> mDataList_meiti = new ArrayList<>();
    private List<DeviceData> mDataList_xiaoxi = new ArrayList<>();

    private List<String> mDataList_title;
    private Context context;
    //下方视图
    private RecyclerView recyleview_view;
    private Adapter_TypeView mAdapter_type;

    //改用封装Adapter
    public ExamplePagerAdapter(List<DeviceData> dataList, List<String> mDataList_title, Context context) {
        this.mDataList_title = mDataList_title;
        mDataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataList_title == null ? 0 : mDataList_title.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //根据position绑定数据
        final View view = View.inflate(context, R.layout.layout_main_view_recyle, null);
        recyleview_view = (RecyclerView) view.findViewById(R.id.recyle_type_view);
        /**
         * 封装Adapter的使用实例
         recyleview_view.setAdapter(new BaseAdapter<DeviceData, BaseViewHolder>(context, mDataList, R.layout.layout_deviceitem) {
        @Override public void bindData(BaseViewHolder viewHolder, DeviceData deviceData) {
        TextView device_plala = (TextView) viewHolder.getView(R.id.lalal);
        device_plala.setText(deviceData.getType());
        ImageView device_pc = (ImageView)viewHolder.getView(R.id.device_pc);
        device_pc.setImageResource(deviceData.getImg());
        if (mDataList_title.get(position).equals("设备")) {

        } else if (mDataList_title.get(position).equals("模式")) {

        } else if (mDataList_title.get(position).equals("分组")) {

        }
        }
        });
         recyleview_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
         container.addView(view);*/
        if (mDataList_title.get(position).equals("设备")) {
            mDataList_device.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                if (mDataList.get(i).getType().equals("设备")) {
                    mDataList_device.add(mDataList.get(i));
                }
            }
            showAdapter(mDataList_device);
        } else if (mDataList_title.get(position).equals("模式")) {
            mDataList_moshi.clear();
            for (int i = 0; i < mDataList.size(); i++) {

                if (mDataList.get(i).getType().equals("模式")) {
                    mDataList_moshi.add(mDataList.get(i));
                }
            }
            showAdapter(mDataList_moshi);
        } else if (mDataList_title.get(position).equals("分组")) {
            mDataList_fenzu.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                if (mDataList.get(i).getType().equals("分组")) {
                    mDataList_fenzu.add(mDataList.get(i));
                }
            }
            showAdapter(mDataList_fenzu);
        } else if (mDataList_title.get(position).equals("媒体")) {
            mDataList_meiti.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                if (mDataList.get(i).getType().equals("媒体")) {
                    mDataList_meiti.add(mDataList.get(i));
                }
            }
            showAdapter(mDataList_meiti);
        } else if (mDataList_title.get(position).equals("消息")) {
            mDataList_xiaoxi.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                if (mDataList.get(i).getType().equals("消息")) {
                    mDataList_xiaoxi.add(mDataList.get(i));
                }
            }
            showAdapter(mDataList_xiaoxi);
        }

        container.addView(view);
        return view;
    }

    //显示recyleView的数据
    private void showAdapter(List<DeviceData> mDataList) {
        mAdapter_type = new Adapter_TypeView(context, mDataList);
        recyleview_view.setAdapter(mAdapter_type);
        recyleview_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyleview_view.setItemAnimator(new DefaultItemAnimator());
        recyleview_view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST));
        mAdapter_type.setOnItemClickListener(new Adapter_TypeView.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                ToastUtil.show(context, "点击事件");
            }
        });

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        TextView textView = (TextView) object;
        String text = textView.getText().toString();
        int index = mDataList_title.indexOf(text);
        if (index >= 0) {
            return index;
        }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList_title.get(position);
    }


}