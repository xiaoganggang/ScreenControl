package gang.com.screencontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21.
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    //首先通过构造方法把数据传进来

    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context context;
    protected int mLayoutResId;

    public BaseAdapter(Context context, List<T> dates, int mLayoutResId) {
        this.mDatas = dates;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mLayoutResId = mLayoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(mLayoutResId,null,false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //数据绑定用抽象方法来实现
        T t = getItem(position);
        bindData(holder, t);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public abstract void bindData(BaseViewHolder viewHolder, T t);

}
