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

import gang.com.screencontrol.MainAct_xiuding;
import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.MediaAdapter;
import gang.com.screencontrol.bean.MediaBean_child;
import gang.com.screencontrol.bean.MediaBean_childdetial;
import gang.com.screencontrol.bean.Mediabean_childfolder;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.potting.BaseFragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Media_Fragment extends BaseFragment implements MainService.MessageCallBackListener {
    public static Media_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView recyle_media;
    private MediaAdapter mediaadapter;
    private List<Mediabean_childfolder.BodyBean.FolderInfoBean> mediabean_childfolders = new ArrayList<>();
    private List<MediaBean_childdetial> mdata_child_detial = new ArrayList<>();
    private List<MediaBean_child.BodyBean.InfolistBean> mdata_child = new ArrayList<>();
    private Gson gson = new Gson();
    private static  MediaAddCallBackListener mymediaaddListener1;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private WebSocket webSocket;
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
        //请求所有文件中的子文件
        isPrepared=true;
        lazyLoad();

        return view;
    }

    /**
     * 公共请求的方法
     */
    private void getData() {
        //接口回调，调用发送websocket接口
         webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.setCallBackListener(this);
            webSocket.send("    {\n" +
                    "       \"body\" : {\n" +
                    "          \"parentID\" : 0\n" +
                    "       },\n" +
                    "       \"guid\" : \"M-36\",\n" +
                    "       \"type\" : \"GETCHILDMEDIAFOLDERLIST\"\n" +
                    "    }");
        }
    }

    private void showRecyleview() {
        mediaadapter = new MediaAdapter(getActivity(), mdata_child_detial);
        recyle_media.setAdapter(mediaadapter);
        recyle_media.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyle_media.setItemAnimator(new DefaultItemAnimator());
        recyle_media.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        mediaadapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ToastUtil.show(getActivity(),"媒体item点击事件");
                if (null != mymediaaddListener1) {
                    //将点击事件传递给回调函数
                    mymediaaddListener1.OnAddMediaView(mdata_child_detial.get(position));
                }
            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有媒体list", text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    //一次遍历
                    if (allmodelobject.getString("type").equals("GETCHILDMEDIAFOLDERLIST")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        mediabean_childfolders = gson.fromJson(basicInfoboj.getString("folderInfo"), new TypeToken<List<Mediabean_childfolder.BodyBean.FolderInfoBean>>() {
                        }.getType());

                        for (int i = 0; i < mediabean_childfolders.size(); i++) {
                            mediabean_childfolders.get(i).getFolderID();
                            webSocket.send("{\n" +
                                    "   \"body\" : {\n" +
                                    "      \"id\" :"
                                    + mediabean_childfolders.get(i).getFolderID() + ",\n" +
                                    "      \"keyword\" : \"\"\n" +
                                    "   },\n" +
                                    "   \"guid\" : \"M-42\",\n" +
                                    "   \"type\" : \"GETMEDIAFILELIST\"\n" +
                                    "}");
                            LogUtil.d("7777777", mediabean_childfolders.get(i).getFolderID() + "");
                        }
                        //二次遍历
                    } else if (allmodelobject.getString("type").equals("GETMEDIAFILELIST") || allmodelobject.getString("guid").equals("M-42")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        mdata_child = gson.fromJson(basicInfoboj.getString("infolist"), new TypeToken<List<MediaBean_child.BodyBean.InfolistBean>>() {
                        }.getType());
                        for (int i = 0; i < mdata_child.size(); i++) {
                            LogUtil.d("获取的所有媒体list子文件夹List信息", mdata_child.get(i).getFileId() + "11111￥￥￥￥￥");
                            mdata_child.get(i).getFileId();
                            webSocket.send("{\n" +
                                    "   \"body\" : {\n" +
                                    "      \"idlist\" : ["
                                    + mdata_child.get(i).getFileId() + "]\n" +
                                    "   },\n" +
                                    "   \"guid\" : \"M-48\",\n" +
                                    "   \"type\" : \"GETMEDIAFILEINFO\"\n" +
                                    "}");
                            LogUtil.d("获取的所有媒体list子文件夹List信息", mdata_child.get(i).getFileId() + "呵呵呵222");
                        }
                    } else if (allmodelobject.getString("type").equals("GETMEDIAFILEINFO")) {
                        LogUtil.d("获取的所有媒体list子文件夹的单个详细信息", text + "呵呵呵");
                        String bodystring = allmodelobject.getString("body");
                        MediaBean_childdetial msg = gson.fromJson(bodystring, MediaBean_childdetial.class);
                        mdata_child_detial.add(msg);
                        mHasLoadedOnce = true;
                        showRecyleview();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 用于注册回调事件
     */
    public static void SetMediaAddListener(MediaAddCallBackListener mymediaaddListener) {
        mymediaaddListener1 = mymediaaddListener;
    }

    /**
     * 可见的情况下加载数据，取消了之前的预加载
     */
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getData();
    }

    /**
     * 定义一个接口
     * 方法参数是传递点击item的详细数据
     *
     * @author fox
     */
    public interface MediaAddCallBackListener {
        void OnAddMediaView(MediaBean_childdetial mediaBean_childdetial);
    }
}
