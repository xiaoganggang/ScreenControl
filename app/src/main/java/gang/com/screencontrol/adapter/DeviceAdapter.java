package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.DeviceBean;
import gang.com.screencontrol.bean.Devicebean_child;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class DeviceAdapter extends BaseSimpleAdapter<Devicebean_child.BodyBean.InfoListBean> {
    public DeviceAdapter(Context context, List<Devicebean_child.BodyBean.InfoListBean> dates) {
        super(context, dates, R.layout.item_device);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Devicebean_child.BodyBean.InfoListBean deviceBean) {
        viewHolder.getTextView(R.id.item_device_name).setText(deviceBean.getDeviceName());
        //getType是1手机设备，2是电脑设备
        if (deviceBean.getDeviceType() == 1) {
            viewHolder.getImageView(R.id.item_device_pc).setBackgroundResource(R.mipmap.device_phone);
        } else if (deviceBean.getDeviceType() == 2) {
            viewHolder.getImageView(R.id.item_device_pc).setBackgroundResource(R.mipmap.device_computer);
        }
    }
}
