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
import gang.com.screencontrol.adapter.ModelAdapter;
import gang.com.screencontrol.bean.MobelBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Model_Fragment extends Fragment implements MainService.MessageCallBackListener {
    public static Model_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView mobel_recyle;
    private ModelAdapter modelAdapter;
    private List<MobelBean.BasicInfoBean> datalist = new ArrayList<>();
    private Gson gson = new Gson();

    //长连接的建立
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
        //接口回调，调用发送websocket接口
        WebSocket webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.setCallBackListener(this);
            webSocket.send("{\n" +
                    "       \"body\" : \"\",\n" +
                    "       \"guid\" : \"M-18\",\n" +
                    "       \"type\" : \"SEARCHPROGRAMBASICINFO\"\n" +
                    "    }");
        }
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

    @Override
    public void onRcvMessage(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("SEARCHPROGRAMBASICINFO")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        basicInfoboj.getString("basicInfo");
                        LogUtil.d("获取的所有模式list", basicInfoboj.getString("basicInfo"));
                        List<MobelBean.BasicInfoBean> ps = gson.fromJson(basicInfoboj.getString("basicInfo"), new TypeToken<List<MobelBean.BasicInfoBean>>() {
                        }.getType());
                        datalist = ps;
                        showView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
