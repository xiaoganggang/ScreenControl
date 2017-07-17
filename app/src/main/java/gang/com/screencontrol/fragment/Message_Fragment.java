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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.MessageAdapter;
import gang.com.screencontrol.bean.MessageBean;
import gang.com.screencontrol.bean.MobelBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.potting.BaseFragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Message_Fragment extends BaseFragment implements MainService.MessageCallBackListener {
    public static Message_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView recycler_message;
    private MessageAdapter messageadapter;
    private List<MessageBean> mdata = new ArrayList<>();
    private Gson gson = new Gson();

    public static Message_Fragment getInstance() {
        if (instance == null) {
            instance = new Message_Fragment();
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
    private WebSocket webSocket;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_message, container, false);
        recycler_message = (RecyclerView) view.findViewById(R.id.recyle_message);
        isPrepared=true;
        lazyLoad();
        return view;
    }

    private void getData() {
        //接口回调，调用发送websocket接口
        WebSocket webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.addListener(this);
            webSocket.send("    {\n" +
                    "       \"body\" : {\n" +
                    "          \"keyWords\" : \""+"1"+"\"\n" +
                    "       },\n" +
                    "       \"guid\" : \"M-91\",\n" +
                    "       \"type\" : \"QUERYMESSAGE\"\n" +
                    "    }");
        }
    }

    private void showView() {
        messageadapter = new MessageAdapter(getActivity(), mdata);
        recycler_message.setAdapter(messageadapter);
        recycler_message.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recycler_message.setItemAnimator(new DefaultItemAnimator());
        recycler_message.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        messageadapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有消息list", text);
       getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("QUERYMESSAGE")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        basicInfoboj.getString("messageItemInfo");
                        LogUtil.d("获取的所有消息list", basicInfoboj.getString("messageItemInfo"));
                       List<MessageBean> ps = gson.fromJson(basicInfoboj.getString("messageItemInfo"), new TypeToken<List<MobelBean.BasicInfoBean>>() {
                        }.getType());
                        mdata = ps;
                        showView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

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
