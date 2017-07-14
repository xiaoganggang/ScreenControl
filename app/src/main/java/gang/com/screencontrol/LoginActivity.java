package gang.com.screencontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.java_websocket.client.WebSocketClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gang.com.screencontrol.fragment.Login_Fragment_one;
import gang.com.screencontrol.fragment.Login_Fragment_two;
import gang.com.screencontrol.util.AppManager;
import gang.com.screencontrol.websocketo.Task;

/**
 * http://blog.csdn.net/flowingflying/article/details/12995519 Fragment动画加载
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_qiehuan)
    ImageView loginQiehuan;
    @BindView(R.id.login_out)
    ImageView loginOut;
    @BindView(R.id.login_framelayout)
    FrameLayout loginFramelayout;
    private int flag = 0;
    private Unbinder mUnbider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        AppManager.addActivity(this);
        //绑定视图一定要在填充视图后绑定
        mUnbider = ButterKnife.bind(this);
        AppManager.addActivity(this);
        replaceFragment(new Login_Fragment_one());

    }

    @OnClick({R.id.login_qiehuan, R.id.login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_qiehuan:
                if (flag == 0) {
                    replaceFragment(new Login_Fragment_two());
                    flag = 1;
                } else {
                    replaceFragment(new Login_Fragment_one());
                    flag = 0;
                }
                break;
            case R.id.login_out:
                AppManager.finishActivity(LoginActivity.this);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//Fragment中增加系统自带动画效果
        transaction.replace(R.id.login_framelayout, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        //解绑视图
        mUnbider.unbind();
        super.onDestroy();
    }
}
