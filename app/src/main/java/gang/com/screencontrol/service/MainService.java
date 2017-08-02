package gang.com.screencontrol.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import gang.com.screencontrol.MainActivity;
import gang.com.screencontrol.R;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 该服务是开启与服务端websocket的长连接
 * 开启前台服务还是不能避免进程杀死
 * http://blog.csdn.net/double2hao/article/details/49742265
 * https://juejin.im/entry/5889719c128fe10068530882  深入浅出 OkHttp Websocket-- 使用篇
 */
public class MainService extends Service {
    private IntentFilter mIF;
    private BroadcastReceiver mBR;
    //长连接的建立
    static OkHttpClient mOkHttpClient;
    public static WebSocket mWebSocket;
    static List<MessageCallBackListener>  mListenerList = new ArrayList<>();

    //private static MessageCallBackListener messageLister;
    private static String adress;
    private static String port;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 第一次开启服务会调用
     * 创建一个前台服务
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Intent a = new Intent(MainService.this, MainService.class);
                startService(a);
            }
        };
        mIF = new IntentFilter();
        mIF.addAction("listener");
        registerReceiver(mBR, mIF);
    }

    /**
     * 每次开启都会调用执行需要的事件
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启用前台服务，主要是startForeground()
        intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("服务器连接中")
                //.setContentText("this is a text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.startupicon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.starticon184))
                .setContentIntent(pi)
                .build();
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        startForeground(1, notification);

       /* adress = intent.getStringExtra("adress");
        port = intent.getStringExtra("port");*/

        initWebSocket(adress, port);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("listener");
        sendBroadcast(intent);
        unregisterReceiver(mBR);
    }

    public static void initWebSocket(String adress, String port) {
        //屏蔽安全证书的代码
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
        //网络请求
        mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        //Request request = new Request.Builder().url("wss://" + adress + ":" + port).build();
        Request request = new Request.Builder().url("wss://192.168.10.57:7681").build();
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
                startTask(mWebSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("client onMessage");
                System.out.println("message:" + text);
                callBack(text);
                /*if (null != messageLister) {
                    messageLister.onRcvMessage(text);
                }*/
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

    private static void callBack(String text) {
        Iterator<MessageCallBackListener> iterator = mListenerList.iterator();
        while(iterator.hasNext()){
            MessageCallBackListener listener = iterator.next();
            if (null != listener) {
                listener.onRcvMessage(text);
            }
        }
    }

    //每秒发送一条消息
    private static void startTask(final WebSocket mWebSocket) {
        Timer mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mWebSocket == null) return;
                boolean isSuccessed = mWebSocket.send("{\n" +
                        "     \"body\" : \"\",\n" +
                        "     \"guid\" : \"\",\n" +
                        "     \"type\" : \"MASTERHEARTBEAT\"\n" +
                        "  }");
                //除了文本内容外，还可以将如图像，声音，视频等内容转为ByteString发送
                //boolean send(ByteString bytes);
            }
        };
        mTimer.schedule(timerTask, 0, 10);
    }

    public static WebSocket getWebSocket() {
        if (mWebSocket == null) {
            initWebSocket(adress, port);
        }
        return mWebSocket;
    }

   /* public static void setCallBackListener(MessageCallBackListener listener) {
        messageLister = listener;
    }*/

    public static void addListener(MessageCallBackListener listener){
        mListenerList.add(listener);
    }

    public static void removeListener(MessageCallBackListener listener){
        if(mListenerList.contains(listener)){
            mListenerList.remove(listener);
        }
    }

    public interface MessageCallBackListener {

        void onRcvMessage(String text);
    }
}

