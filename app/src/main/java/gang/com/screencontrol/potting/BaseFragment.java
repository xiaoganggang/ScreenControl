package gang.com.screencontrol.potting;

import android.content.Intent;
import android.support.v4.app.Fragment;


/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class BaseFragment extends Fragment {
    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        //是否fragemnt添加动画
        //getActivity().overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent, requestCode);
    }
}
