package gang.com.screencontrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.ModelAdapter;
import gang.com.screencontrol.bean.MobelBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Model_Fragment extends Fragment {
    public static Model_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView mobel_recyle;
    private ModelAdapter modelAdapter;
    private List<MobelBean.BodyBean.BasicInfoBean> datalist = new ArrayList<>();
    //长连接的建立
    static OkHttpClient mOkHttpClient;
    private WebSocket mWebSocket;
    public static Model_Fragment getInstance() {
        if (instance == null) {
            instance = new Model_Fragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_mobel, container, false);
        mobel_recyle = (RecyclerView) view.findViewById(R.id.recyle_model);
        getData();
        return view;
    }

    private void getData() {
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
                            "       \"body\" : {\n" +
                            "          \"id\" : 1,\n" +
                            "          \"keyword\" : \"\"\n" +
                            "       },\n" +
                            "       \"guid\" : \"M-42\",\n" +
                            "       \"type\" : \"GETMEDIAFILELIST\"\n" +
                            "    }");
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.print("client onMessage");
                LogUtil.d("模式接收的数据",text);
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

    private void showView() {
        modelAdapter = new ModelAdapter(getActivity(), datalist);
        mobel_recyle.setAdapter(modelAdapter);
        mobel_recyle.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mobel_recyle.setItemAnimator(new DefaultItemAnimator());
        mobel_recyle.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        modelAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
    }

}
