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
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.ModelAdapter;
import gang.com.screencontrol.bean.MediaBean_childdetial;
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

public class Model_Fragment extends BaseFragment implements MainService.MessageCallBackListener {
    public static Model_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView mobel_recyle;
    private ModelAdapter modelAdapter;
    private List<MobelBean.BasicInfoBean> datalist = new ArrayList<>();
    private Gson gson = new Gson();
    //删除对应的item模式的id值
    private int pitchid;
    //item的position值
    private int positionvalue;
    private static ModelAddCallBackListener modelAddCallBackListener;
    private WebSocket webSocket;
    //长连接的建立
    public static Model_Fragment getInstance() {
        if (instance == null) {
            instance = new Model_Fragment();
        }
        return instance;
    }

    //Fragment预加载问题
    private long exitTime = 0;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_mobel, container, false);
        mobel_recyle = (RecyclerView) view.findViewById(R.id.recyle_model);
        initWebsocket();
        isPrepared = true;
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

    /**
     * 获取模式数据
     */
    private void getModelData() {
            webSocket.send("{\n" +
                    "       \"body\" : \"\",\n" +
                    "       \"guid\" : \"M-18\",\n" +
                    "       \"type\" : \"SEARCHPROGRAMBASICINFO\"\n" +
                    "    }");
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
                if (null != modelAddCallBackListener) {
                    //在一定时间范围内双击事件的实现
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        //Toast.makeText(getActivity(), "双击选择模式", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        //将点击事件传递给回调函数
                        modelAddCallBackListener.OnAddModelView(v, datalist.get(position), datalist);
                    }


                }
            }
        });
        modelAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(View v, int position) {
                pitchid = datalist.get(position).getID();
                positionvalue = position;
                ToastUtil.show(getActivity(), "长按事件删除事件"+pitchid+"**"+position);
                show_model_dialog();
            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有模式list", text);
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
                        mHasLoadedOnce = true;
                        showView();
                    } else if (allmodelobject.getString("type").equals("DELETEPROGRAMLIST")) {
                        ToastUtil.show(getActivity(), "删除成功");
                        //滑动到第一个位置
                        //mobel_recyle.scrollToPosition(0);
                        datalist.remove(positionvalue);
                        modelAdapter.notifyItemRemoved(positionvalue);
                        modelAdapter.notifyItemRangeChanged(0, datalist.size());//此处第二个参数不减一，会素数组越界的
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
        Button dialog_model_delete = (Button) dialog_model.findViewById(R.id.dialog_mobel_delete);
        dialog_model_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getActivity(),"删除按钮"+pitchid);
                //接口回调，调用发送websocket接口
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


    /**
     * 用于注册回调事件
     */
    public static void SetModelAddListener(ModelAddCallBackListener mymediaaddListener) {
        modelAddCallBackListener = mymediaaddListener;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
       getModelData();
    }

    /**
     * 定义一个接口
     * 方法参数是传递点击item的详细数据
     *
     * @author xiaogangzai
     */
    public interface ModelAddCallBackListener {
        void OnAddModelView(View v, MobelBean.BasicInfoBean modelbean, List<MobelBean.BasicInfoBean> mobel_list);
    }

    /**
     * 在主Activity中点击刷新按钮时候调用的方法
     */
    public void refreshData()
    {
        LogUtil.e("调用刷新数据","啦啦啦模式数据");
        if (modelAdapter!=null)
        {
            modelAdapter.clearData();
            //重新发送请求获取数据
            webSocket.send("{\n" +
                    "       \"body\" : \"\",\n" +
                    "       \"guid\" : \"M-18\",\n" +
                    "       \"type\" : \"SEARCHPROGRAMBASICINFO\"\n" +
                    "    }");
        }else
        {
            LogUtil.e("调用刷新数据","请求数据失败，modelAdapter为空");
        }
    }

    @Override
    public void onDestroyView() {
        MainService.removeListener(this);
        super.onDestroyView();
    }
}
