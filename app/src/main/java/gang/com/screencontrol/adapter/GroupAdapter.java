package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.GroupBean;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class GroupAdapter extends BaseSimpleAdapter<GroupBean> {
    public GroupAdapter(Context context, List<GroupBean> dates) {
        super(context, dates, R.layout.item_group);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, GroupBean groupBean) {
        viewHolder.getTextView(R.id.item_group_name).setText(groupBean.getFolderName());
        viewHolder.getImageView(R.id.item_group_pc).setBackgroundResource(R.mipmap.group);
    }
}
