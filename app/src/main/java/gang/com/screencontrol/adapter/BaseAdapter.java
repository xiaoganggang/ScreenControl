package gang.com.screencontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/5/21.
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    //首先通过构造方法把数据传进来

    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context context;
    protected int mLayoutResId;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public BaseAdapter(Context context, List<T> dates, int mLayoutResId) {
        this.mDatas = dates;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mLayoutResId = mLayoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false);
        BaseViewHolder vh = new BaseViewHolder(view,listener);
        // View view = mInflater.inflate(mLayoutResId, parent, false);
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //数据绑定用抽象方法来实现
        T t = getItem(position);
        bindData((H)holder, t);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public abstract void bindData(H viewHolder, T t);

    //一些公用方法
    public List<T> getDates() {
        return mDatas;
    }

    public void clearData() {
        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {
        if (datas != null & datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }
    }
}
