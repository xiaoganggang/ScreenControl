package gang.com.screencontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gang.com.screencontrol.potting.BaseActivity;

public class RemoteControlAct extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_remote_control;
    }
}
