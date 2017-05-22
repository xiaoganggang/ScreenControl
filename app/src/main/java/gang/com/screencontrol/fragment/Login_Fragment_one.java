package gang.com.screencontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gang.com.screencontrol.MainActivity;
import gang.com.screencontrol.R;
import gang.com.screencontrol.util.LogUtil;

/**
 * Created by xiaogangzai on 2017/5/16.
 * http://www.chengxuyuans.com/Android/97798.html-----websocket简介
 */

public class Login_Fragment_one extends Fragment {


    @BindView(R.id.edit_login1_dizhi)
    EditText editLogin1Dizhi;
    @BindView(R.id.edit_login1_duankou)
    EditText editLogin1Duankou;
    @BindView(R.id.edit_login1_username)
    EditText editLogin1Username;
    @BindView(R.id.edit_login1_pass)
    EditText editLogin1Pass;
    @BindView(R.id.image_login1_remeber)
    ImageView imageLogin1Remeber;
    @BindView(R.id.image_login1_zidonglogin)
    ImageView imageLogin1Zidonglogin;
    @BindView(R.id.button_login1)
    Button buttonLogin1;
    Unbinder unbinder;
    private WebSocketClient client;
    private String mUlWan = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_login_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();

    }

    @OnClick({R.id.image_login1_remeber, R.id.image_login1_zidonglogin, R.id.button_login1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_login1_remeber:
                break;
            case R.id.image_login1_zidonglogin:
                break;
            case R.id.button_login1:
                try {
                    client = new WebSocketClient(new URI(mUlWan), new Draft_17(), null, 3000) {
                        //已连接服务器
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {

                        }

                        //获取服务器信息
                        @Override
                        public void onMessage(String message) {

                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {

                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    };
                    LogUtil.d("啦啦啦", "客户端建立连接connect()");
                    client.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                StartActivity(MainActivity.class);
                break;
        }
    }

    private void StartActivity(Class activityo) {
        Intent a = new Intent();
        a.setClass(getActivity(), activityo);
        startActivity(a);
    }

}
