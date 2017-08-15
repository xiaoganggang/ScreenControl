package gang.com.screencontrol.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.MediaBean_childdetial;
import gang.com.screencontrol.util.JsonUitl;
import gang.com.screencontrol.util.LogUtil;

/**
 * Created by xiaogangzai on 2017/6/1.
 * http://blog.csdn.net/jhear/article/details/52997822
 */

public class MediaAdapter extends BaseSimpleAdapter<MediaBean_childdetial.BodyBean> {
    private String InServerPath;
    private String InServerPath_true;

    public MediaAdapter(Context context, List<MediaBean_childdetial.BodyBean> dates) {
        super(context, dates, R.layout.item_media);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MediaBean_childdetial.BodyBean mediaBean) {
        if (mediaBean.getFolderId() == 11) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.video);
        } else if (mediaBean.getFolderId() == 12) {
            //12代表的是图片
            mediaBean.getThumbnailPix();
           LogUtil.d("卧槽RGB图片", JsonUitl.toJson(mediaBean.getThumbnailPix())+"");
           /* InServerPath = mediaBean.getInServerPath();
            InServerPath_true = "wss://192.168.10.168:7681" + InServerPath.replace("\\", "/");
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageURI(InServerPath_true);*/
            try {
                JSONObject pixObject=new JSONObject(JsonUitl.toJson(mediaBean.getThumbnailPix()));
                pixObject.getString("pixValue");
                LogUtil.d("卧槽RGB图片int数组", pixObject.getString("pixValue")+"高度"+mediaBean.getHeight()+"宽度"+mediaBean.getWidth());
                byte b[] = pixObject.getString("pixValue").getBytes() ;
                LogUtil.d("卧槽RGB图片byte数组", pixObject.getString("pixValue")+"");
               getOriginalBitmap(b,12,12);
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
                simpleDraweeView.setImageBitmap( getOriginalBitmap(b,50,40));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mediaBean.getFolderId()  == 13) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.office);
        } else if ((mediaBean.getFolderId()  == 14)) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.internet);
        } else if (mediaBean.getFolderId()  == 15) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.media_pc);
            simpleDraweeView.setImageResource(R.mipmap.photokuang);
        }
        viewHolder.getTextView(R.id.media_name).setText(mediaBean.getFileName());
    }


    /**
     * RGB 565图片颜色数组封装成图片
     * @param data          颜色数组
     * @param height        高度
     * @param width         宽度
     * @return
     */
    public static Bitmap getOriginalBitmap(byte[] data, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        //java.lang.RuntimeException: Buffer not large enough for pixels缓冲区不够大的错误
        bitmap.copyPixelsFromBuffer(buffer);
        buffer.position(0);
        return bitmap;
    }
}
