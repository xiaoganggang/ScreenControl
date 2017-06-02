package gang.com.screencontrol.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xiaogangzai on 2017/5/21.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private BaseAdapter.OnItemClickListener listener;
    private BaseAdapter.OnItemLongClickListener listenerlong;
    private SparseArray<View> views;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener, BaseAdapter.OnItemLongClickListener listenerlong) {
        super(itemView);
        this.listener = listener;
        this.listenerlong = listenerlong;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.views = new SparseArray<View>();
    }

    public TextView getTextView(int id) {
        return findView(id);
    }

    public ImageView getImageView(int id) {
        return findView(id);
    }

    public Button getButton(int id) {
        return findView(id);
    }

    public View getView(int id) {
        return findView(id);
    }

    private <T extends View> T findView(int id) {
        View view = views.get(id);
        /**
         * 卧槽了，就这的一个错坑了我一天时间草草view == null不是view!=null
         */
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (listenerlong != null) {
            listenerlong.onClick(v, getLayoutPosition());
        }
        return false;
    }
}
