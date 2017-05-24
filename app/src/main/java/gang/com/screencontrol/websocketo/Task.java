package gang.com.screencontrol.websocketo;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;


import java.net.URI;
import java.net.URISyntaxException;

import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;

/**
 * Created by xiaogangzai on 2017/5/22.
 * http://www.cnblogs.com/Conker/p/6508420.html
 * http://blog.csdn.net/mad1989/article/details/38234759  Runnable的使用
 */

public class Task implements Runnable {
    private String mUlWan = "wss://192.168.10.168:7681/test";
    private WebSocketClient mClient;
    private Context context;

    public Task(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            if (mUlWan.equals("")) {
                //ToastUtil.show("没有获取到连接地址");

                return;
            }
            mClient = new WebSocketClient(new URI(mUlWan), new Draft_17(), null, 30 * 1000) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
//Failed writing handshake bytes (-1 of 14): Broken pipe 失败的写入握手字节
                    send("handshakedata");
                }

                @Override
                public void onMessage(String message) {
                    Log.i("啦啦啦onMessage","onTextMessage payload = "+message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    LogUtil.d("啦啦啦关闭", reason + remote);
                }

                @Override
                public void onError(Exception ex) {
                    Log.i("啦啦啦onError","onTextMessage payload = "+ex+"");
                }
            };
            LogUtil.d("啦啦啦", "客户端建立连接connect()");
            mClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送用户名给服务器
     */
    private void sendUsername() {
        String user = "啦啦啦";
        if (user != null && user.length() != 0)
            mClient.send(user);
    }

}
