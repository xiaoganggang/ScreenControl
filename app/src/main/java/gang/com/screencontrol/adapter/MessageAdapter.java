package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.MessageBean;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class MessageAdapter extends BaseSimpleAdapter<MessageBean> {

    public MessageAdapter(Context context, List<MessageBean> dates) {
        super(context, dates, R.layout.item_message);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MessageBean messageBean) {

    }
}
