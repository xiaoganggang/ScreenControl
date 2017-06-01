package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.MediaBean;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class MediaAdapter extends BaseSimpleAdapter<MediaBean> {

    public MediaAdapter(Context context, List<MediaBean> dates) {
        super(context, dates, R.layout.item_media);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MediaBean mediaBean) {

    }
}
