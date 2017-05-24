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



import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import gang.com.screencontrol.websocketo.Task;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;


/**
 * Created by xiaogangzai on 2017/5/16.
 * http://www.chengxuyuans.com/Android/97798.html-----websocket简介
 * http://www.open-open.com/lib/view/open1476778263175.html----两种方法实现websocket
 */

public class Login_Fragment_one extends Fragment {
    //长连接的建立
    static OkHttpClient mOkHttpClient;

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
        Task task = new Task(getActivity());
        Thread td1 = new Thread(task, "td1");
        //多个Thread也可以同时使用一个Runbale，
        td1.start();
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

              /*  ExampleClient c = null;
                try {
                    c = new ExampleClient(new URI("wss://192.168.10.168:7681/test"), new Draft_17());
                    c.connectBlocking();
                    c.send("handshake");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                /*try {
                    client = new WebSocketClient(URI.create(""), new Draft_17(), null, 10) {
                        //已连接服务器
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            System.out.println("client onOpen");

                        }

                        //获取服务器信息
                        @Override
                        public void onMessage(String message) {
                            System.out.println("client onMessage:" + message);
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            System.out.println("client onClose:" + code + " " + reason + " " + remote);
                        }

                        @Override
                        public void onError(Exception ex) {
                            System.out.println("client onError:" + ex);
                        }
                    };
                    LogUtil.d("啦啦啦", "客户端建立连接connect()");
                    client.connect();
                }  catch (Exception e) {
                    e.printStackTrace();
                }*/

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
        WebSocketCall webSocketCall = WebSocketCall.create(mOkHttpClient, request);
        webSocketCall.enqueue(new WebSocketListener() {
            private final ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
            private WebSocket webSocket;

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("WebSocketCall", "onOpen");
                this.webSocket = webSocket;
            }

            /**
             * 连接失败
             * @param e
             * @param response Present when the failure is a direct result of the response (e.g., failed
             * upgrade, non-101 response code, etc.). {@code null} otherwise.
             */
            @Override
            public void onFailure(IOException e, Response response) {
                Log.d("WebSocketCall", "onFailure"+e.toString());
            }

            /**
             * 接收到消息
             * @param message
             * @throws IOException
             */
            @Override
            public void onMessage(ResponseBody message) throws IOException {
                final RequestBody response;
                Log.d("WebSocketCall", "onMessage:" + message.source().readByteString().utf8());
                String msg = message.source().readByteString().utf8();
                message.source().close();
//                sendExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000*60);
//                            webSocket.sendMessage(response);//发送消息
//                        } catch (IOException e) {
//                            e.printStackTrace(System.out);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }

            @Override
            public void onPong(Buffer payload) {
                Log.d("WebSocketCall", "onPong:");
            }


            /**
             * 关闭
             * @param code The <a href="http://tools.ietf.org/html/rfc6455#section-7.4.1">RFC-compliant</a>
             * status code.
             * @param reason Reason for close or an empty string.
             */
            @Override
            public void onClose(int code, String reason) {
                sendExecutor.shutdown();
            }
        });
    }
}
