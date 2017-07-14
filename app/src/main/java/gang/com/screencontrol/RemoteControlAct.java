package gang.com.screencontrol;

import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import gang.com.screencontrol.potting.BaseActivity;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

public class RemoteControlAct extends BaseActivity implements MainService.MessageCallBackListener {
    @BindView(R.id.close_telecontrol)
    ImageView closeTelecontrol;
    private WebSocket webSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebsocket();
        //开始远程控制的接口调用StartRemoteControl
        StartRemoteControl();
    }
    /**
     *     开启远程控制的方法
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
                    JSONObject allmodelobject = new JSONObject(text);
                 /*   if (allmodelobject.getString("type").equals("ADDWIDGET") && allmodelobject.getString("guid").equals("M-117")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        ToastUtil.show(MainAct_xiuding.this, "时钟添加成功");
                    } else if (allmodelobject.getString("type").equals("ADDWIDGET") && allmodelobject.getString("guid").equals("M-31")) {
                        ToastUtil.show(MainAct_xiuding.this, "天气添加成功");
                    }//增加模式到播放列表
                    else if (allmodelobject.getString("type").equals("OPERATEFAVORITEPROGRAM")) {
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
