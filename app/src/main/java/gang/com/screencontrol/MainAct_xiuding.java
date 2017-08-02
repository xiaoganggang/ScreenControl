package gang.com.screencontrol;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gang.com.screencontrol.adapter.ViewPagerAdapter;
import gang.com.screencontrol.bean.LoadVideoWallInfo_Bean;
import gang.com.screencontrol.bean.MediaBean_childdetial;
import gang.com.screencontrol.bean.MobelBean;
import gang.com.screencontrol.bean.ScreenContent;
import gang.com.screencontrol.defineview.DragScaleView;
import gang.com.screencontrol.fragment.Media_Fragment;
import gang.com.screencontrol.fragment.Model_Fragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.AppManager;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

/**
 * BUG请求后返回来的内容总是被那些fragment的onMessage拦截掉
 * 图片的Base64编解码  http://blog.csdn.net/lincyang/article/details/46596899
 * Base64解码问题  http://blog.sina.com.cn/s/blog_54410d940101esw5.html
 */
public class MainAct_xiuding extends AppCompatActivity implements View.OnClickListener, MainService.MessageCallBackListener, Media_Fragment.MediaAddCallBackListener, Model_Fragment.ModelAddCallBackListener {
    @BindView(R.id.lastmodel)
    ImageView lastmodel;
    @BindView(R.id.nextmodel)
    ImageView nextmodel;
    @BindView(R.id.close_software)
    ImageView closeSoftware;
    @BindView(R.id.startlock)
    ImageView startlock;
    //用来放置所有的层的布局
    @BindView(R.id.contain_view_xiuding)
    RelativeLayout containViewXiuding;


    /**
     * 选项卡标题
     */
    private ImageView pause_player;
    private ViewPager mViewPagerjingdian;
    private LinearLayout linearLayout_caidan;
    private ImageView clock, weather, refresh;
    private WebSocket webSocket;
    private Gson gson = new Gson();
    private static final String TAG = "MainAct_xiuding";
    //窗口锁定状态，0是锁住状态，1是开锁状态
    private int LOCKSTATE = 0;

    //判读显示窗口当前是否有模式存在,默认是0没有模式，1是有模式
    private int modeljudge_state = 0;
    private TextView model_add_window;
    private ImageView start_remote_control;
    //暂停点击状态pausestate
    private int pausestate = 0;//暂停是0
    private TabLayout tabLayout;
    private PagerAdapter mAdapter;
    private Drawable drawable;
    //用来放显示的图片的list，避免新的返回覆盖
    private List<String> list_returnpc = new ArrayList<>();
    //用来放层信息的list
    List<ScreenContent.BodyBean.ContentBeanX> ContentXlist = new ArrayList<>();
    //全局变量，屏幕ID
    private int slaveID;
    //用来存放所有的图层
    private List<DragScaleView> dragScaleViewList = new ArrayList<>();
    //用来存放所有的图层对应的guid也就是后面的layerid
    private List<String> guidlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_act_xiuding);
        ButterKnife.bind(this);
        AppManager.addActivity(this);
        mViewPagerjingdian = (ViewPager) findViewById(R.id.jingdianviewpager);
        initWebsocket();
        //主动向服务器发送获取大屏内容请求
        SendNotify();
        //加载显示墙信息指令发送
        loadWallInfo();
        initView();
        initViewpage();
    }

    /**
     * 主动向服务器发送获取大屏内容请求 :NotifyScreenContent
     * https://www.showdoc.cc/2452?page_id=12292
     */
    private void SendNotify() {
        webSocket.send(
                "    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-10\",\n" +
                        "       \"type\" : \"GETSCREENCONTENT\"\n" +
                        "    }"
        );
    }

    /**
     * 加载显示墙信息，看有几块显示墙
     */
    private void loadWallInfo() {
        webSocket.send("    {\n" +
                "       \"body\" : \"\",\n" +
                "       \"guid\" : \"M-44\",\n" +
                "       \"type\" : \"LOADVIDEOWALLINFO\"\n" +
                "    }");
    }


    private void initViewpage() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerjingdian.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPagerjingdian);
        //3个page，所以第一个page启动的时候，右侧的page会加载，中间第二个page选中的时候，不会做任何事情，因为左右两侧现在各有一个，第三个page选中的时候，销毁第一个page的视图，
        //因为现在page3左侧有2个page了，必须销毁远的那个
        //意思就是说设置当前page左右两侧应该被保持的page数量，超过这个限制，page会被销毁重建（只是销毁视图），onDestroy-onCreateView,但不会执行onDestroy
        //尽量维持这个值小，特别是有复杂布局的时候，因为如果这个值很大，就会占用很多内存，如果只有3-4page的话，可以全部保持active，可以保持page切换的顺滑
        mViewPagerjingdian.setOffscreenPageLimit(4);
        //预加载问题如何取消
    }

    /**
     * 接口回调，获取websocket
     */
    private void initWebsocket() {
        //接口回调，调用发送websocket接口
        webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.addListener(MainAct_xiuding.this);
        } else {
            ToastUtil.show(MainAct_xiuding.this, "websocket连接错误");
        }
    }


    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        clock = (ImageView) findViewById(R.id.clock);
        clock.setOnClickListener(this);
        weather = (ImageView) findViewById(R.id.weather);
        weather.setOnClickListener(this);
        model_add_window = (TextView) findViewById(R.id.model_add_window);
        start_remote_control = (ImageView) findViewById(R.id.start_remote_control);
        start_remote_control.setOnClickListener(this);
        pause_player = (ImageView) findViewById(R.id.pause_player);
        pause_player.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock:
                show_clock_dialog();
                break;
            case R.id.weather:
                show_weather_dialog();
                break;
            case R.id.start_remote_control:
                Intent a = new Intent(this, RemoteControlAct.class);
                startActivity(a);
                break;
            //暂停显示墙播放和开始显示墙播放
            case R.id.pause_player:
                if (pausestate == 0) {
                    pause_player();
                    ToastUtil.show(MainAct_xiuding.this, "暂停窗体播放");
                    pausestate = 1;
                } else {
                    continue_player();
                    ToastUtil.show(MainAct_xiuding.this, "继续窗体播放");
                    pausestate = 0;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 暂停显示窗
     */
    private void pause_player() {
        webSocket.send("    {\n" +
                "       \"body\" : {\n" +
                "          \"pause\" : true\n" +
                "       },\n" +
                "       \"guid\" : \"M-103\",\n" +
                "       \"type\" : \"PAUSEPLAYER\"\n" +
                "    }");
    }

    /**
     * 继续显示窗
     */
    private void continue_player() {
        webSocket.send("    {\n" +
                "       \"body\" : {\n" +
                "          \"pause\" : false\n" +
                "       },\n" +
                "       \"guid\" : \"M-103\",\n" +
                "       \"type\" : \"PAUSEPLAYER\"\n" +
                "    }");
    }


    /**
     * 开启窗口
     */
    private void start_window() {
        webSocket.send("    {\n" +
                "       \"body\" : {\n" +
                "          \"programID\" : 1605,\n" +
                "          \"sceneID\" : 256,\n" +
                "          \"type\" : \"AddProgram\"\n" +
                "       },\n" +
                "       \"guid\" : \"M-90\",\n" +
                "       \"type\" : \"OPERATEFAVORITEPROGRAM\"\n" +
                "    }");
    }

    //显示dialog，是否添加闹钟
    private void show_clock_dialog() {
        final Dialog dialog_clock = new Dialog(MainAct_xiuding.this, R.style.dialog);
        dialog_clock.setContentView(R.layout.dialog_clockl);
        Button dialog_model_add = (Button) dialog_clock.findViewById(R.id.dialog_clock_add);
        dialog_model_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webSocket.send("    {\n" +
                        "       \"body\" : {\n" +
                        "          \"clock\" : {\n" +
                        "             \"countdowndatatime\" : \"00:10:00\",\n" +
                        "             \"iscountdownclock\" : false,\n" +
                        "             \"language\" : true,\n" +
                        "             \"showAnalogClock\" : false,\n" +
                        "             \"showDate\" : true,\n" +
                        "             \"showDigitalClock\" : true,\n" +
                        "             \"showIn24Hours\" : false,\n" +
                        "             \"showbackground\" : false\n" +
                        "          },\n" +
                        "          \"height\" : 400,\n" +
                        "          \"slaveID\" : 100,\n" +
                        "          \"slave_scale_height\" : 0.370370,\n" +
                        "          \"slave_scale_width\" : 0.1250,\n" +
                        "          \"slave_scale_x\" : 0.0,\n" +
                        "          \"slave_scale_y\" : 0.0,\n" +
                        "          \"type\" : 0,\n" +
                        "          \"widgetPositionXML\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" ?>\\n<Root>\\n    <X>0</X>\\n    <Y>0</Y>\\n    <Width>400</Width>\\n    <Height>400</Height>\\n    <Slave ID=\\\"100\\\">\\n        <X>0</X>\\n        <Y>0</Y>\\n        <Width>0.125</Width>\\n        <Height>0.37037</Height>\\n    </Slave>\\n</Root>\\n\",\n" +
                        "          \"width\" : 400,\n" +
                        "          \"x\" : 0,\n" +
                        "          \"y\" : 0,\n" +
                        "          \"zorder\" : 1501\n" +
                        "       },\n" +
                        "       \"guid\" : \"M-117\",\n" +
                        "       \"type\" : \"ADDWIDGET\"\n" +
                        "    }");
            }
        });
        Button dialog_close = (Button) dialog_clock.findViewById(R.id.dialog_clock_close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_clock.cancel();
            }
        });
        dialog_clock.show();
    }

    //显示dialog,是否添加天气
    private void show_weather_dialog() {
        final Dialog dialog_weather = new Dialog(MainAct_xiuding.this, R.style.dialog);
        dialog_weather.setContentView(R.layout.dialog_weather);
        Button dialog_model_add = (Button) dialog_weather.findViewById(R.id.dialog_weather_add);
        dialog_model_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webSocket.send("    {\n" +
                        "       \"body\" : {\n" +
                        "          \"height\" : 400,\n" +
                        "          \"slaveID\" : 100,\n" +
                        "          \"slave_scale_height\" : 0.370370,\n" +
                        "          \"slave_scale_width\" : 0.1250,\n" +
                        "          \"slave_scale_x\" : 0.0,\n" +
                        "          \"slave_scale_y\" : 0.0,\n" +
                        "          \"type\" : 1,\n" +
                        "          \"weather\" : {\n" +
                        "             \"city\" : \"Shanghai\",\n" +
                        "             \"highTemperature\" : \"20\",\n" +
                        "             \"language\" : \"zh-CN\",\n" +
                        "             \"lowTemperature\" : \"10\",\n" +
                        "             \"proxyIP\" : \"\",\n" +
                        "             \"proxyPort\" : \"\",\n" +
                        "             \"temperFormat\" : \"C\",\n" +
                        "             \"useProxy\" : false,\n" +
                        "             \"useTrans\" : false,\n" +
                        "             \"weatherImage\" : \"32d.png\",\n" +
                        "             \"weatherName\" : \"Weather\",\n" +
                        "             \"weatherTitle\" : \"tornado\",\n" +
                        "             \"woeid\" : \"2151849\"\n" +
                        "          },\n" +
                        "          \"widgetPositionXML\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" ?>\\n<Root>\\n    <X>0</X>\\n    <Y>0</Y>\\n    <Width>400</Width>\\n    <Height>400</Height>\\n    <Slave ID=\\\"100\\\">\\n        <X>0</X>\\n        <Y>0</Y>\\n        <Width>0.125</Width>\\n        <Height>0.37037</Height>\\n    </Slave>\\n</Root>\\n\",\n" +
                        "          \"width\" : 400,\n" +
                        "          \"x\" : 0,\n" +
                        "          \"y\" : 0,\n" +
                        "          \"zorder\" : 1501\n" +
                        "       },\n" +
                        "       \"guid\" : \"M-31\",\n" +
                        "       \"type\" : \"ADDWIDGET\"\n" +
                        "    }");
            }
        });
        Button dialog_close = (Button) dialog_weather.findViewById(R.id.dialog_weather_close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_weather.cancel();
            }
        });
        dialog_weather.show();
    }

    @Override
    public void onRcvMessage(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    /**
                     *显示屏内的层
                     */
                    if (allmodelobject.getString("type").equals("MAXWALLSCREENCONTENT")) {
                        //每次接到这个层的请求时候，都要清空之前的ContentXlist，同时要清空之前的布局中所有的View
                        if (ContentXlist.size() > 0 && dragScaleViewList.size() > 0) {
                            ContentXlist.clear();
                            dragScaleViewList.clear();
                            guidlist.clear();
                            containViewXiuding.removeAllViews();
                        }

                        String bodystring = allmodelobject.getString("body");
                        JSONObject contentoboj = new JSONObject(bodystring);
                        JSONArray slaveInfoJsonArray = contentoboj.getJSONArray("Content");
                        //获取对应的slaveID,对应的控制器
                        slaveID = contentoboj.getInt("slaveID");
                        //将Array数据转换成List
                        ContentXlist = gson.fromJson(slaveInfoJsonArray.toString(), new TypeToken<List<ScreenContent.BodyBean.ContentBeanX>>() {
                        }.getType());
                        for (int i = 0; i < ContentXlist.size(); i++) {
                            DragScaleView mDragView = new DragScaleView(MainAct_xiuding.this);  //创建imageview
                            mDragView.isFocused();
                            //mDragView1.setBackgroundResource(R.mipmap.ic_launcher);
                            //mDragView.setClickable(true);//相当于android:clickable="true"
                            int w = ContentXlist.get(i).getRight() - ContentXlist.get(i).getLeft();
                            int h = ContentXlist.get(i).getBottom() - ContentXlist.get(i).getTop();
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
                            mDragView.setLayoutParams(lp);  //image的布局方式
                            mDragView.setTag(ContentXlist.get(i).getGuid());
                            //设置图片的位置
                            lp.setMargins(ContentXlist.get(i).getLeft(), ContentXlist.get(i).getTop(), 0, 0);
                            containViewXiuding.addView(mDragView);
                            //向两个list中添加数据
                            dragScaleViewList.add(mDragView);
                            guidlist.add(ContentXlist.get(i).getGuid());
                            //rebuild(mDragView1,123);
                            //View就是你自定义的控件，h跟w就是根据屏幕分辨率来设置view的宽高。不要说屏幕分辨率怎么获取！
                        }
                    }

                    /**SS
                     * 如果监听到了显示窗回显的字段
                     * 接口地址如下http://www.showdoc.cc/2452?page_id=13041     MAXWALLSNAPSHOT
                     */
                    else if (allmodelobject.getString("type").equals("MAXWALLSNAPSHOT")) {
                        if (ContentXlist.size() == 0||dragScaleViewList.size()==0) {
                            //说明目前没有返回的层信息
                            ToastUtil.show(MainAct_xiuding.this, "显示器中的层信息为空");
                        } else {
                            String bodystring_snapshot = allmodelobject.getString("body");
                            JSONObject snapshotoboj = new JSONObject(bodystring_snapshot);
                            LogUtil.e("回显层中内容", snapshotoboj.getInt("slaveID") + "");
                            //snapshot获取的slaveID
                            snapshotoboj.getInt("slaveID");
                            //循环遍历
                            for (int i = 0; i < ContentXlist.size(); i++) {
                                if (snapshotoboj.getInt("slaveID") == slaveID && snapshotoboj.getString("layerID").equals(ContentXlist.get(i).getGuid())) {
                                    String imageDataStr1 = snapshotoboj.getString("imageDataStr");
                                    //替换掉\r\n
                                    imageDataStr1 = imageDataStr1.replaceAll("\r\n", "");
                                    if (!TextUtils.isEmpty(imageDataStr1) && list_returnpc.size() != 0) {
                                        list_returnpc.removeAll(list_returnpc);
                                    }
                                    list_returnpc.add(imageDataStr1);
                                    LogUtil.d("图像字  符内容", list_returnpc.get(0));
                                    String imageDataStr = list_returnpc.get(0);
                                    if (!TextUtils.isEmpty(imageDataStr)) {
                                        Log.d(TAG, "getUidFromBase64 enUID = " + imageDataStr);
                                        String result = "";
                                        if (!TextUtils.isEmpty(imageDataStr)) {
                                            result = new String(Base64.decode(imageDataStr.getBytes(), Base64.DEFAULT));
                                            LogUtil.d("图像字  解码后符内容", result);
                                            byte[] decode = result.getBytes();

                                           // byte[] decode = Base64.decode(result, Base64.DEFAULT);
                                            LogUtil.d("卧槽总长度",decode.length+"");
                                            YuvImage yuvimage = new YuvImage(decode, ImageFormat.NV21, Integer.valueOf(snapshotoboj.getString("width").toString()), Integer.valueOf(snapshotoboj.getString("height").toString()), null);//20、20分别是图的宽度与高度
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            yuvimage.compressToJpeg(new Rect(0, 0, 20, 20), 60, baos);//80--JPG图片的质量[0-100],100最高
                                            byte[] jdata = baos.toByteArray();
                                            Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
                                            bmp = Bitmap.createScaledBitmap(bmp, Integer.valueOf(snapshotoboj.getString("width")), Integer.valueOf(snapshotoboj.getString("height")), false);
                                            drawable = new BitmapDrawable(bmp);
                                            dragScaleViewList.get(i).setBackground(drawable);
                                        }
                                    }
                                }
                            }
                        }

                    } else if (allmodelobject.getString("type").equals("ADDWIDGET") && allmodelobject.getString("guid").equals("M-117")) {

                        ToastUtil.show(MainAct_xiuding.this, "时钟添加成功");

                    } else if (allmodelobject.getString("type").equals("ADDWIDGET") && allmodelobject.getString("guid").equals("M-31")) {
                        ToastUtil.show(MainAct_xiuding.this, "天气添加成功");
                    }
                    //增加模式到播放列表
                    else if (allmodelobject.getString("type").equals("OPERATEFAVORITEPROGRAM")) {

                    }
                    //加载显示墙配置信息  LoadVideoWallInfo，也就是显示几个屏幕几个显示器
                    else if (allmodelobject.getString("type").equals("LOADVIDEOWALLINFO")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        JSONArray slaveInfoJsonArray = basicInfoboj.getJSONArray("slaveInfo");
                        JSONObject slaveInfoJsonObject = slaveInfoJsonArray.getJSONObject(0);
                        JSONArray monitorInfoJsonArray = slaveInfoJsonObject.getJSONArray("monitorInfo");
                        //将Array数据转换成List
                        List<LoadVideoWallInfo_Bean.BodyBean.SlaveInfoBean.MonitorInfoBean> MonitorInfolist = gson.fromJson(monitorInfoJsonArray.toString(), new TypeToken<List<LoadVideoWallInfo_Bean.BodyBean.SlaveInfoBean.MonitorInfoBean>>() {
                        }.getType());
                        if (MonitorInfolist.size() == 0) {
                            //墙配置信息为0，显示墙显示内容为空
                        } else if (MonitorInfolist.size() == 1) {
                            //一个墙配置，也就是显示一个显示器
                            ToastUtil.show(MainAct_xiuding.this, "一个显示器");
                           /* ViewGroup group = (ViewGroup) findViewById(R.id.sticker_view); //获取原来的布局容器
                            ImageView imageView = new ImageView(MainAct_xiuding.this);  //创建imageview
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MonitorInfolist.get(0).getWidth(), MonitorInfolist.get(0).getHeight());
                            imageView.setLayoutParams(lp);  //image的布局方式
                            imageView.setImageResource(R.mipmap.xianshiqiang);  //设置imageview呈现的图片
                            //设置图片的位置
                            lp.setMargins(MonitorInfolist.get(0).getLeft(), MonitorInfolist.get(0).getTop(), 0, 0);
                            group.addView(imageView);  //添加到布局容器中，显示图片。*/
                        } else if (MonitorInfolist.size() > 1) {
                            //一个墙配置，也就是显示一个显示器
                           /* ViewGroup group = (ViewGroup) findViewById(R.id.sticker_view); //获取原来的布局容器
                            ImageView imageView = new ImageView(MainAct_xiuding.this);  //创建imageview
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            imageView.setLayoutParams(lp);  //image的布局方式
                            imageView.setImageResource(R.mipmap.xianshiqiang);  //设置imageview呈现的图片
                            //设置图片的位置
                            lp.setMargins(400, 20, 30, 40);
                            group.addView(imageView);  //添加到布局容器中，显示图片。*/
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Media_Fragment.SetMediaAddListener(this);
        Model_Fragment.SetModelAddListener(this);
    }

    /**
     * 媒体添加接口回调的方法重写
     *
     * @param mediaBean_childdetial
     */
    @Override
    public void OnAddMediaView(MediaBean_childdetial.BodyBean mediaBean_childdetial) {
        //同时在这里执行view添加到StickView的操作
        if (mediaBean_childdetial.getFolderId() == 11) {
            Media_Addceng();
        } else if (mediaBean_childdetial.getFolderId() == 12) {
            Media_Addceng();
        }
    }

    /**
     * 媒体模块向屏幕中添加层
     */
     private void Media_Addceng()
     {
         DragScaleView mDragView = new DragScaleView(MainAct_xiuding.this);  //创建imageview
         mDragView.isFocused();
         mDragView.setBackgroundResource(R.mipmap.video);
         mDragView.setClickable(true);//相当于android:clickable="true"
         RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         mDragView.setLayoutParams(lp);  //image的布局方式
         //设置图片的位置
         lp.setMargins(0, 0, 30, 40);
         containViewXiuding.addView(mDragView);
         //向两个list中添加数据
         dragScaleViewList.add(mDragView);
     }
    /**
     * 模式添加的接口回调
     *
     * @param modelbean
     * @param mobel_list
     */
    @Override
    public void OnAddModelView(View v, final MobelBean.BasicInfoBean modelbean, List<MobelBean.BasicInfoBean> mobel_list) {
        //显示窗中没有模式,且是开锁状态
        if (LOCKSTATE == 1) {
            if (modeljudge_state == 0) {
                start_window();
                //调用加载模式的接口LoadProgram，接口地址：https://www.showdoc.cc/2452?page_id=11152
                showNormalDialog(modelbean.getID(), modelbean.getName());

            } else {
                Snackbar.make(v, "显示窗已有模式确认要替换吗?", Snackbar.LENGTH_LONG)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //替换时点击的模式内容的id和模式内容的名称
                                modelbean.getName();
                                modelbean.getID();
                                //替换之前的模式
                                model_add_window.setText("模 式:" + modelbean.getName());
                                webSocket.send("{\n" +
                                        "   \"body\" : {\n" +
                                        "      \"id\" :" + modelbean.getID() + "   },\n" +
                                        "   \"guid\" : \"M-79\",\n" +
                                        "   \"type\" : \"PLAYPROGRAM\"\n" +
                                        "}");
                                modeljudge_state = 1;
                            }
                        }).show();
            }
        } else {
            Snackbar.make(v, "请先解锁", Snackbar.LENGTH_LONG)
                    .show();
        }

    }
    private void showNormalDialog(final int modelId, final String modelname) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainAct_xiuding.this);
        normalDialog.setIcon(R.mipmap.applog);
        normalDialog.setTitle("播放模式");
        normalDialog.setMessage("是否播放" + modelname + "?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dragScaleViewList.clear();
                        guidlist.clear();
                        containViewXiuding.removeAllViews();
                        model_add_window.setText("模 式:" + modelname);
                        webSocket.send("{\n" +
                                "   \"body\" : {\n" +
                                "      \"id\" :" + modelId + "   },\n" +
                                "   \"guid\" : \"M-79\",\n" +
                                "   \"type\" : \"PLAYPROGRAM\"\n" +
                                "}");
                        modeljudge_state = 1;
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }


    @OnClick({R.id.lastmodel, R.id.nextmodel, R.id.close_software, R.id.startlock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lastmodel:
                //播放上一个模式的时候清空之前的View和数据
                dragScaleViewList.clear();
                guidlist.clear();
                containViewXiuding.removeAllViews();
                ToastUtil.show(MainAct_xiuding.this, "播放上一个模式");
                webSocket.send("    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-172\",\n" +
                        "       \"type\" : \"PREVIOUSPROGRAM\"\n" +
                        "    }");
                break;
            case R.id.nextmodel:
                //播放下一个模式的时候清空之前的View
                dragScaleViewList.clear();
                guidlist.clear();
                containViewXiuding.removeAllViews();
                ToastUtil.show(MainAct_xiuding.this, "播放下一个模式");
                webSocket.send("    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-171\",\n" +
                        "       \"type\" : \"NEXTPROGRAM\"\n" +
                        "    }");
                break;
            //增加模式到播放列表
            case R.id.startlock:
                ToastUtil.show(MainAct_xiuding.this, "点击了");
                if (LOCKSTATE == 0) {
                    LOCKSTATE = 1;
                    //点击开锁按钮之后才可以调控层
                    startlock_SCREENCONTENT();
                } else {
                    LOCKSTATE = 0;
                    //锁住层的内容
                    closelock_SCREENCONTENT();
                }
                break;

            case R.id.close_software:
                //关闭软件，关闭所有Activity
                AppManager.finishAllActivity();
                break;

        }
    }

    /**
     * 对屏幕总的层可以进行控制-------开锁
     */
    private void startlock_SCREENCONTENT() {
        //首先判断ContentXlist不为0就是里面有层
        if (ContentXlist.size() > 0) {
            for (int i = 0; i < ContentXlist.size(); i++) {
                dragScaleViewList.get(i).setClickable(true);
                dragScaleViewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(MainAct_xiuding.this, "位置变化" + "顶部" + v.getTop() + "左边" + v.getLeft());
                    }
                });
            }
        } else {
            ToastUtil.show(MainAct_xiuding.this, "屏幕中没有可以控制的层空空空");
        }
    }

    /**
     * 对屏幕总的层可以进行控制-------关锁
     */
    private void closelock_SCREENCONTENT() {
        //首先判断ContentXlist不为0就是里面有层
        if (ContentXlist.size() > 0) {
            for (int i = 0; i < ContentXlist.size(); i++) {
                dragScaleViewList.get(i).setClickable(false);
                dragScaleViewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(MainAct_xiuding.this, "清先开锁");
                    }
                });
            }
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.finishActivity(MainAct_xiuding.this);
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        MainService.removeListener(this);
        super.onDestroy();
    }


}
