package gang.com.screencontrol.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.DeviceAdapter;
import gang.com.screencontrol.bean.Devicebean_child;
import gang.com.screencontrol.bean.Devicebean_childfolder;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.input.Global_public;
import gang.com.screencontrol.potting.BaseFragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Device_Fragment extends BaseFragment implements MainService.MessageCallBackListener {
    public static Device_Fragment instance = null;
    private RecyclerView recycler_device;
    private DeviceAdapter deviceadapter;
    private List<Devicebean_childfolder.BodyBean.DeviceFolderInfoBean> devicebean_childfolders=new ArrayList<>();
    private List<Devicebean_child.BodyBean.InfoListBean> devicebean_childall=new ArrayList<>();
    private Gson gson = new Gson();
    private WebSocket webSocket;
    private static DeviceAddCallBackListener mydeviceaddListener1;
    public static Device_Fragment getInstance() {
        if (instance == null) {
            instance = new Device_Fragment();
        }
        return instance;
    }
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_device, container, false);
        initWebsocket();
        recycler_device = (RecyclerView) view.findViewById(R.id.recyle_device);
        isPrepared=true;
        lazyLoad();
        return view;
    }
    /**
     * 初始化websocket
     */
    private void initWebsocket() {
        //接口回调，调用发送websocket接口
        webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.addListener(this);
        }
    }
    private void getData() {
            webSocket.send("    {\n" +
                    "       \"body\" : {\n" +
                    "          \"typeid\" : [ 0 ]\n" +
                    "       },\n" +
                    "       \"guid\" : \"M-117\",\n" +
                    "       \"type\" : \"GETDEVICETYPEFOLDERLIST\"\n" +
                    "    }");
    }

    private void showView() {
        deviceadapter = new DeviceAdapter(getActivity(), devicebean_childall);
        recycler_device.setAdapter(deviceadapter);
        recycler_device.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recycler_device.setItemAnimator(new DefaultItemAnimator());
        recycler_device.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        deviceadapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (null != mydeviceaddListener1) {
                    //添加设备回调这里，需要判断那个设备是不是主机，如果是主机则不能添加
                    if (Global_public.slaveip_list.contains(devicebean_childall.get(position).getAddressIp()))
                    {
                        DialogUIUtils.showAlert(getActivity(), "鼎泓提示", "操作主机设备不可添加到显示墙！", null, null, null, null, true, true, true, new DialogUIListener() {
                            @Override
                            public void onPositive() {

                            }

                            @Override
                            public void onNegative() {

                            }
                        }).show();
                    }
                    else
                    {
                        //将点击事件传递给回调函数
                        mydeviceaddListener1.OnAddDeviceView(devicebean_childall.get(position));
                    }
                }
            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有设备list",text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("GETDEVICETYPEFOLDERLIST")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        devicebean_childfolders = gson.fromJson(basicInfoboj.getString("deviceFolderInfo"), new TypeToken<List<Devicebean_childfolder.BodyBean.DeviceFolderInfoBean>>() {
                        }.getType());
                        basicInfoboj.getString("deviceFolderInfo");
                        for (int i = 0; i < devicebean_childfolders.size(); i++) {
                            if (devicebean_childfolders.get(i).getFolderID()!=8)
                            {
                                webSocket.send("    {\n" +
                                        "       \"body\" : {\n" +
                                        "          \"id\" : "+devicebean_childfolders.get(i).getFolderID()+",\n" +
                                        "          \"keyword\" : \"\"\n" +
                                        "       },\n" +
                                        "       \"guid\" : \"M-134\",\n" +
                                        "       \"type\" : \"GETDEVICELIST\"\n" +
                                        "    }");
                            }
                        }

                    }
                    else if (allmodelobject.getString("type").equals("GETDEVICELIST"))
                    {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        List<Devicebean_child.BodyBean.InfoListBean>   devicebean_child = gson.fromJson(basicInfoboj.getString("infoList"), new TypeToken<List<Devicebean_child.BodyBean.InfoListBean>>() {
                        }.getType());
                        for(int i=0;i<devicebean_child.size();i++)
                        {
                            devicebean_childall.add(devicebean_child.get(i));
                        }
                        mHasLoadedOnce=true;
                        showView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
/**
 * 点击接口的回调
 */
public interface DeviceAddCallBackListener {
    void OnAddDeviceView(Devicebean_child.BodyBean.InfoListBean deviceBean_info);
}
    /**
     * 用于注册回调事件
     */
    public static void SetDeviceAddListener(DeviceAddCallBackListener mydeviceaddListener) {
        mydeviceaddListener1 = mydeviceaddListener;
    }

    /**
     * 取消预加载方法的实现
     */
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getData();
    }

    @Override
    public void onDestroyView() {
        MainService.removeListener(this);
        super.onDestroyView();
    }


}
