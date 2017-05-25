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
import gang.com.screencontrol.MainActivity;
import gang.com.screencontrol.R;
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

public class Login_Fragment_one extends Fragment {
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
                initWebSocket();
                StartActivity(MainActivity.class);
                break;
        }
    }

    private void StartActivity(Class activityo) {
        Intent a = new Intent();
        a.setClass(getActivity(), activityo);
        startActivity(a);
    }


    private void initWebSocket() {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                .build();


        Request request = new Request.Builder().url("wss://192.168.10.168:7681").build();

        //建立连接
        mOkHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                mWebSocket = webSocket;
                System.out.println("client onOpen");
                System.out.println("client request header:" + response.request().headers());
                System.out.println("client response header:" + response.headers());
                System.out.println("client response:" + response);
                //开启消息定时发送
                if (mWebSocket == null) {
                    System.out.println("message:啦dasd啦kong啦阿拉");
                } else {
                    mWebSocket.send("{\n" +
                            "     \"body\" : \"\",\n" +
                            "     \"guid\" : \"\",\n" +
                            "     \"type\" : \"MASTERHEARTBEAT\"\n" +
                            "  }");
                }

            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("client onMessage");
                System.out.println("message:啦dasd啦啦阿拉" + "111" + text + "1111");
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("client onClosing");
                System.out.println("code:" + code + " reason:" + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("client onClosed");
                System.out.println("code:" + code + " reason:" + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                //出现异常会进入此回调
                System.out.println("client onFailure");
                System.out.println("throwable:" + t);
                System.out.println("response:" + response);
            }
        });
    }

    //每秒发送一条消息
    private void startTask(final WebSocket webSocket) {
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
    }

}
