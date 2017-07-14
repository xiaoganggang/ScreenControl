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
import gang.com.screencontrol.adapter.GroupAdapter;
import gang.com.screencontrol.bean.GroupBean;
import gang.com.screencontrol.bean.MobelBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.potting.BaseFragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Grouping_Fragment extends BaseFragment implements MainService.MessageCallBackListener {
    public static Grouping_Fragment instance = null;
    private RecyclerView recyler_group;
    private GroupAdapter groupAdapter;
    private List<GroupBean> list_group = new ArrayList<>();
    private Gson gson = new Gson();
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    /**
     * 创建Grouping_Fragment单例模式
     *
     * @return
     */
    public static Grouping_Fragment getInstance() {
        if (instance == null) {
            instance = new Grouping_Fragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_group, container, false);
        recyler_group = (RecyclerView) view.findViewById(R.id.recyle_group);
        isPrepared = true;
        lazyLoad();

        return view;
    }

    //网络请求数据
    private void getData() {
        //接口回调，调用发送websocket接口
        WebSocket webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.setCallBackListener(this);
            webSocket.send("{\n" +
                    "      \"body\" : \"\",\n" +
                    "       \"guid\" : \"M-17\",\n" +
                    "       \"type\" : \"GETGROUPFOLDERLIST\"\n" +
                    "    }");
        }
    }

    private void show_group() {
        groupAdapter = new GroupAdapter(getActivity(), list_group);
        recyler_group.setAdapter(groupAdapter);
        recyler_group.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyler_group.setItemAnimator(new DefaultItemAnimator());
        recyler_group.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        groupAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(View v, int position) {
                ToastUtil.show(getActivity(), "长按事件");
            }
        });
        groupAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ToastUtil.show(getActivity(), "点击事件");
            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有分组list", text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("GETGROUPFOLDERLIST")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        basicInfoboj.getString("folderInfo");
                        LogUtil.d("获取的所有分组list", basicInfoboj.getString("folderInfo"));
                        List<GroupBean> ps = gson.fromJson(basicInfoboj.getString("folderInfo"), new TypeToken<List<GroupBean>>() {
                        }.getType());
                        list_group = ps;
                        mHasLoadedOnce = true;
                        show_group();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 预加载处理
     */
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getData();
    }
}
