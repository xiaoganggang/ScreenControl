package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.MediaBean_childdetial;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class MediaAdapter extends BaseSimpleAdapter<MediaBean_childdetial> {

    public MediaAdapter(Context context, List<MediaBean_childdetial> dates) {
        super(context, dates, R.layout.item_media);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MediaBean_childdetial mediaBean) {
        viewHolder.getImageView(R.id.media_pc).setImageResource(R.mipmap.internet);
        viewHolder.getTextView(R.id.media_name).setText(mediaBean.getDescription());
    }
}
