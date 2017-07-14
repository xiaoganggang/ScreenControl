package gang.com.screencontrol.adapter;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;

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
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.video);
        } else if (mediaBean.getFolderId() == 12) {
            //12代表的是图片
            InServerPath = mediaBean.getInServerPath();
            InServerPath_true = "wss://192.168.10.219:7681" + InServerPath.replace("\\", "/");
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageURI(InServerPath_true);

        } else if (mediaBean.getFolderId() == 13) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.office);
        } else if ((mediaBean.getFolderId() == 14)) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.internet);
        } else if (mediaBean.getFolderId() == 15) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.photokuang);
        }
        viewHolder.getTextView(R.id.media_name).setText(mediaBean.getFileName());
    }
}
