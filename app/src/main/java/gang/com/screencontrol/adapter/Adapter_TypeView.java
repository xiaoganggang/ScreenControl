package gang.com.screencontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.Type;

/**
 * Created by xiaoganggang on 2017/5/17.
 */

public class Adapter_TypeView extends RecyclerView.Adapter<Adapter_TypeView.viewholder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<String> mDates;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int postion);
    }

    private Adapter_TypeView.OnRecyclerViewItemClickListener mOnItemClickListener = null;


    //最后给外面的调用者，定义一个设置Listener的方法（）
    public void setOnItemClickListener(Adapter_TypeView.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //一个构造函数
    public Adapter_TypeView(Context context, List<String> mDates) {
        this.context = context;
        this.mDates = mDates;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_main_type, parent, false);
        Adapter_TypeView.viewholder vh = new Adapter_TypeView.viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(viewholder holder, final int position) {
        //holder.stamp_type_name.setText(mDates.get(position).getValue());
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
        public viewholder(View itemView) {
            super(itemView);
        }
    }
}
