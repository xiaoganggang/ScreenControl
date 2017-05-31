package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.bean.MobelBean;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class ModelAdapter extends BaseSimpleAdapter<MobelBean.BodyBean.BasicInfoBean> {

    public ModelAdapter(Context context, List<MobelBean.BodyBean.BasicInfoBean> dates) {
        super(context, dates, R.layout.item_model);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MobelBean.BodyBean.BasicInfoBean basicInfoBean) {
        viewHolder.getTextView(R.id.model_name).setText(basicInfoBean.getName());
        viewHolder.getImageView(R.id.model_pc).setBackgroundResource(R.mipmap.model_moren);
    }
}
