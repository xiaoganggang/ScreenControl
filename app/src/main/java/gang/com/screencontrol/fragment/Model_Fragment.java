package gang.com.screencontrol.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
import gang.com.screencontrol.util.ToastUtil;
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
    //删除对应的item模式的id值
    private int pitchid;
    //item的position值
    private int positionvalue;
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
        modelAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(View v, int position) {
                pitchid = datalist.get(position).getID();
                positionvalue=position;
                ToastUtil.show(getActivity(), "长按事件");
                show_model_dialog();
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
                    } else if (allmodelobject.getString("type").equals("DELETEPROGRAMLIST")) {
                        ToastUtil.show(getActivity(), "删除成功");
                        //滑动到第一个位置
                        //mobel_recyle.scrollToPosition(0);
                        datalist.remove(positionvalue);
                        modelAdapter.notifyItemRemoved(positionvalue);
                        modelAdapter.notifyItemRangeChanged(0,datalist.size()-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void show_model_dialog() {
        final Dialog dialog_model = new Dialog(getActivity(), R.style.dialog);
        dialog_model.setContentView(R.layout.dialog_model);
       /* mDialoginit.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        mDialoginit.setCancelable(false);
        // 显示dialog的时候按返回键也不能
        mDialoginit.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return false;// 默认返回 false
                }
            }
        });*/
        Button dialog_model_delete = (Button) dialog_model.findViewById(R.id.dialog_mobel_delete);
        dialog_model_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调，调用发送websocket接口
                WebSocket webSocket = MainService.getWebSocket();
                if (null != webSocket) {
                    MainService.setCallBackListener(getInstance());
                    webSocket.send("{\n" +
                            "   \"body\" : {\n" +
                            "      \"idList\" : [\n" +
                            "         {\n" +
                            "            \"id\" :" + pitchid + "         }\n" +
                            "      ]\n" +
                            "   },\n" +
                            "   \"guid\" : \"M-134\",\n" +
                            "   \"type\" : \"DELETEPROGRAMLIST\"\n" +
                            "}");
                }
            }
        });
        Button dialog_close = (Button) dialog_model.findViewById(R.id.dialog_model_close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_model.cancel();
            }
        });
        dialog_model.show();
    }
}
