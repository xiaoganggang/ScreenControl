package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.DeviceBean;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class DeviceAdapter extends BaseSimpleAdapter<DeviceBean> {
    public DeviceAdapter(Context context, List<DeviceBean> dates) {
        super(context, dates, R.layout.item_device);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, DeviceBean deviceBean) {
        viewHolder.getTextView(R.id.item_device_name).setText(deviceBean.getName());
        //getType是1手机设备，2是电脑设备
        if (deviceBean.getType() == 1) {
            viewHolder.getImageView(R.id.item_device_pc).setBackgroundResource(R.mipmap.device_phone);
        } else if (deviceBean.getType() == 2) {
            viewHolder.getImageView(R.id.item_device_pc).setBackgroundResource(R.mipmap.device_computer);
        }
    }
}
