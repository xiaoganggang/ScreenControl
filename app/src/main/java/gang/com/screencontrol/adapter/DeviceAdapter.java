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

    }
}
