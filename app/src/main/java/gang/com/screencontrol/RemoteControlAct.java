package gang.com.screencontrol;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import gang.com.screencontrol.potting.BaseActivity;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.AppManager;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

public class RemoteControlAct extends BaseActivity implements MainService.MessageCallBackListener {
    @BindView(R.id.close_telecontrol)
    ImageView closeTelecontrol;
    private WebSocket webSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.addActivity(this);
        initWebsocket();
        //开始远程控制的接口调用StartRemoteControl
        StartRemoteControl();
    }
    /**
     *     开启远程控制的方法,对应接口  https://www.showdoc.cc/2452?page_id=11281
     */
    private void StartRemoteControl() {
        webSocket.send("    {\n" +
                "       \"body\" : \"\",\n" +
                "       \"guid\" : \"M-182\",\n" +
                "       \"type\" : \"STARTREMOTECONTROL\"\n" +
                "    }");
    }

    /**
     *      接口回调，获取websocket
     */
    private void initWebsocket() {
        //接口回调，调用发送websocket接口
        webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.setCallBackListener(RemoteControlAct.this);
        } else {
            ToastUtil.show(RemoteControlAct.this, "websocket连接错误");
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_remote_control;
    }

    @OnClick(R.id.close_telecontrol)
    public void onViewClicked() {

    }

    @Override
    public void onRcvMessage(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject Remotecontrolobject = new JSONObject(text);
                    LogUtil.d("77777",text);
                    //远程控制开启
                   if (Remotecontrolobject.getString("type").equals("STARTREMOTECONTROL")) {
                       ToastUtil.show(RemoteControlAct.this, "远程控制开启成功");
                       //手势控制传入xy
                       //远程开启之后就像鼠标移动
                       webSocket.send("    {\n" +
                               "       \"body\" : {\n" +
                               "          \"MK_MouseEvent\" : 0,\n" +
                               "          \"x\" : 191,\n" +
                               "          \"y\" : 205\n" +
                               "       },\n" +
                               "       \"guid\" : \"M-210\",\n" +
                               "       \"type\" : \"MOUSEMOVEREQUEST\"\n" +
                               "    }");

                   }
                   //远程控制关闭
                   else if (Remotecontrolobject.getString("type").equals("STOPREMOTECONTROL"))
                    {
                        ToastUtil.show(RemoteControlAct.this, "远程控制关闭成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 监听返回键，关闭远程控制
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            //发送关闭远程控制
            webSocket.send("    {\n" +
                    "       \"body\" : \"\",\n" +
                    "       \"guid\" : \"M-211\",\n" +
                    "       \"type\" : \"STOPREMOTECONTROL\"\n" +
                    "    }");
            AppManager.finishActivity(RemoteControlAct.this);
        }

        return false;

    }
}
