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
import gang.com.screencontrol.adapter.MediaAdapter;
import gang.com.screencontrol.bean.MediaBean;
import gang.com.screencontrol.bean.MobelBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Media_Fragment extends Fragment implements MainService.MessageCallBackListener {
    public static Media_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView recyle_media;
    private MediaAdapter mediaadapter;
    private List<MediaBean> mdata = new ArrayList<>();
    private Gson gson = new Gson();

    public static Media_Fragment getInstance() {
        if (instance == null) {
            instance = new Media_Fragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_media, container, false);
        recyle_media = (RecyclerView) view.findViewById(R.id.recyle_media);
        getData();
        return view;
    }

    private void getData() {
        //接口回调，调用发送websocket接口
        WebSocket webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.setCallBackListener(this);
            webSocket.send("    {\n" +
                    "       \"body\" : {\n" +
                    "          \"parentID\" : 3\n" +
                    "       },\n" +
                    "       \"guid\" : \"M-36\",\n" +
                    "       \"type\" : \"GETCHILDMEDIAFOLDERLIST\"\n" +
                    "    }");
        }
    }

    private void showRecyleview() {
        mediaadapter = new MediaAdapter(getActivity(), mdata);
        recyle_media.setAdapter(mediaadapter);
        recyle_media.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyle_media.setItemAnimator(new DefaultItemAnimator());
        recyle_media.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        mediaadapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有媒体list", text);
       /* getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("获取的所有媒体list", text);
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("GETCHILDMEDIAFOLDERLIST")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        LogUtil.d("获取的所有媒体list", basicInfoboj.getString("folderInfo"));
                        *//*List<MobelBean.BasicInfoBean> ps = gson.fromJson(basicInfoboj.getString("basicInfo"), new TypeToken<List<MobelBean.BasicInfoBean>>() {
                        }.getType());
                        datalist = ps;
                        showView();*//*
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
*/
    }
}
