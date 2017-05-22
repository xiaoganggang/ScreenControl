package gang.com.screencontrol.websocketo;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;


import java.net.URI;
import java.net.URISyntaxException;

import gang.com.screencontrol.util.LogUtil;

/**
 * Created by xiaogangzai on 2017/5/22.
 * http://www.cnblogs.com/Conker/p/6508420.html
 */

public class Task implements Runnable {
    private String mUlWan = "";
    private WebSocketClient mClient;

    @Override
    public void run() {
        try {
            if (mUlWan.equals("")) {
                //ToastUtil.show("没有获取到连接地址");

                return;
            }
            mClient = new WebSocketClient(new URI(mUlWan), new Draft_17(), null, 3000) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

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
            mClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
