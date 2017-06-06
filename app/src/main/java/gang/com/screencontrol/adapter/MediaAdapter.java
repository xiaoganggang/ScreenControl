package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.MediaBean_childdetial;
import gang.com.screencontrol.util.LogUtil;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class MediaAdapter extends BaseSimpleAdapter<MediaBean_childdetial> {
    private String InServerPath;
    private String InServerPath_true;
    public MediaAdapter(Context context, List<MediaBean_childdetial> dates) {
        super(context, dates, R.layout.item_media);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MediaBean_childdetial mediaBean) {
        if (mediaBean.getFolderId() == 11) {
            viewHolder.getImageView(R.id.media_pc).setImageResource(R.mipmap.video);
        } else if (mediaBean.getFolderId() == 12) {
            //12代表的是图片
            InServerPath = mediaBean.getInServerPath();
            InServerPath_true="wss://192.168.10.168:7681"+InServerPath.replace("\\\\", "/");
            LogUtil.d("粑粑接口",InServerPath_true);
            viewHolder.getImageView(R.id.media_pc).setImageResource(R.mipmap.image);
        } else if (mediaBean.getFolderId() == 13) {
            viewHolder.getImageView(R.id.media_pc).setImageResource(R.mipmap.office);
        } else if ((mediaBean.getFolderId() == 14)) {
            viewHolder.getImageView(R.id.media_pc).setImageResource(R.mipmap.internet);
        } else if (mediaBean.getFolderId() == 15) {
            viewHolder.getImageView(R.id.media_pc).setImageResource(R.mipmap.photokuang);
        }

        viewHolder.getTextView(R.id.media_name).setText(mediaBean.getFileName());
    }
}
