package gang.com.screencontrol.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.DeviceData;
import gang.com.screencontrol.bean.Type;

/**
 * Created by xiaoganggang on 2017/5/17.
 */

public class Adapter_TypeView extends RecyclerView.Adapter<Adapter_TypeView.viewholder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<DeviceData> mDates;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int postion);
    }

    private Adapter_TypeView.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    /**
     * 刷新前清空数据的方法
     */
    public void clearDate() {
        mDates.clear();
    }

    //刷新需要用到的方法
    public void addDate(List<DeviceData> dates) {
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    //最后给外面的调用者，定义一个设置Listener的方法（）
    public void setOnItemClickListener(Adapter_TypeView.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //一个构造函数
    public Adapter_TypeView(Context context, List<DeviceData> mDates) {
        this.context = context;
        this.mDates = mDates;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_deviceitem, parent, false);
        Adapter_TypeView.viewholder vh = new Adapter_TypeView.viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(viewholder holder, final int position) {
        DeviceData deviceData = mDates.get(position);
        holder.miaoshu.setText(deviceData.getType());
        holder.imageView.setImageResource(deviceData.getImg());
        //将创建的View注册点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView miaoshu;

        public viewholder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.device_pc);
            miaoshu = (TextView) itemView.findViewById(R.id.miaoshu);
        }
    }
}
