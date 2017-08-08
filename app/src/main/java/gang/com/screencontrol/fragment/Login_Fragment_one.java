package gang.com.screencontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gang.com.screencontrol.MainAct_xiuding;
import gang.com.screencontrol.MainActivity;
import gang.com.screencontrol.R;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


/**
 * Created by xiaogangzai on 2017/5/16.
 * http://www.chengxuyuans.com/Android/97798.html-----websocket简介
 * http://www.open-open.com/lib/view/open1476778263175.html----两种方法实现websocket
 */

public class Login_Fragment_one extends Fragment implements MainService.MessageCallBackListener {
    //长连接的建立
    static OkHttpClient mOkHttpClient;
    public int msgCount;
    private WebSocket mWebSocket;
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
                //点击登录按钮首先要连接websocket，开启这个服务
              /*  Intent i = new Intent(getActivity(), MainService.class);
                i.putExtra("adress", editLogin1Dizhi.getText().toString());
                i.putExtra("port", editLogin1Duankou.getText().toString());
                getActivity().startService(i);*/
                WebSocket webSocket = MainService.getWebSocket();
                if (null != webSocket) {
                    MainService.addListener(this);
                    webSocket.send("    {\n" +
                            "       \"body\" : {\n" +
                            "          \"userName\" : \"Admin\",\n" +
                            "          \"userPassword\" : \"admin\"\n" +
                            "       },\n" +
                            "       \"guid\" : \"M-0\",\n" +
                            "       \"type\" : \"QUERYUSERLOGIN\"\n" +
                            "    }");
                } else {
                    ToastUtil.show(getActivity(), "输入的端口和账号有误或检查服务器是否开启");
                }
                break;
        }
    }

    private void StartActivity(Class activityo) {
        Intent a = new Intent();
        a.setClass(getActivity(), activityo);
        startActivity(a);
    }


    //每秒发送一条消息
   /* private void startTask(final WebSocket webSocket) {
        Timer mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (webSocket == null) return;
                msgCount++;
                boolean isSuccessed = webSocket.send("    {\n" +
                        "       \"body\" : {\n" +
                        "          \"userName\" : \"Admin\",\n" +
                        "          \"userPassword\" : \"admin\"\n" +
                        "       },\n" +
                        "       \"guid\" : \"M-0\",\n" +
                        "       \"type\" : \"QUERYUSERLOGIN\"\n" +
                        "    }");
                //除了文本内容外，还可以将如图像，声音，视频等内容转为ByteString发送
                //boolean send(ByteString bytes);
            }
        };
        mTimer.schedule(timerTask, 0, 30000);
    }*/

    /**
     * https://www.showdoc.cc/2452?page_id=11985
     * 登录接口的使用
     * @param text
     */
    @Override
    public void onRcvMessage(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("哈哈哈", text);
                try {
                    JSONObject loginobject = new JSONObject(text);
                    if (loginobject.getString("type").equals("QUERYUSERLOGIN") ) {
                        /**
                         * 这里需要注意的是假如登录成功返回的数据会缺少一个字段loginType ，登录失败这个字段才会有值
                         */
                        String bodystring = loginobject.getString("body");
                        JSONObject bodyobject = new JSONObject(bodystring);
                        bodyobject.getBoolean("loginSuccess");
                        if (bodyobject.getBoolean("loginSuccess") == true)
                        {
                            StartActivity(MainAct_xiuding.class);
                        }
                        else if ( bodyobject.getInt("loginType") == 3) {
                            ToastUtil.show(getActivity(), "用户已在其他设备登录");
                        } else {
                            ToastUtil.show(getActivity(), "请输入正确的用户名和密码");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
