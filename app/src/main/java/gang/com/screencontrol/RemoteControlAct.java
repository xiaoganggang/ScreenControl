package gang.com.screencontrol;

import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import gang.com.screencontrol.potting.BaseActivity;
import gang.com.screencontrol.service.MainService;

public class RemoteControlAct extends BaseActivity implements MainService.MessageCallBackListener {
    @BindView(R.id.close_telecontrol)
    ImageView closeTelecontrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDate();
    }

    private void getDate() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_remote_control;
    }

    @OnClick(R.id.close_telecontrol)
    public void onViewClicked() {

    }

    @Override
    public void onRcvMessage(String text) {

    }
}
