package gang.com.screencontrol;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gang.com.screencontrol.adapter.ViewPagerAdapter;
import gang.com.screencontrol.bean.CengYuansuBean;
import gang.com.screencontrol.bean.Devicebean_child.BodyBean.InfoListBean;
import gang.com.screencontrol.bean.LoadVideoWallInfo_Bean.BodyBean.SlaveInfoBean.MonitorInfoBean;
import gang.com.screencontrol.bean.MediaBean_childdetial.BodyBean;
import gang.com.screencontrol.bean.MobelBean.BasicInfoBean;
import gang.com.screencontrol.bean.ScreenContent.BodyBean.ContentBeanX;
import gang.com.screencontrol.bean.ScreenContent.BodyBean.ContentBeanX.ContentBean;
import gang.com.screencontrol.defineview.AlertDialog;
import gang.com.screencontrol.defineview.DragScaleView;
import gang.com.screencontrol.fragment.Device_Fragment;
import gang.com.screencontrol.fragment.Device_Fragment.DeviceAddCallBackListener;
import gang.com.screencontrol.fragment.Grouping_Fragment;
import gang.com.screencontrol.fragment.Media_Fragment;
import gang.com.screencontrol.fragment.Media_Fragment.MediaAddCallBackListener;
import gang.com.screencontrol.fragment.Message_Fragment;
import gang.com.screencontrol.fragment.Model_Fragment;
import gang.com.screencontrol.fragment.Model_Fragment.ModelAddCallBackListener;
import gang.com.screencontrol.input.Global_public;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.service.MainService.MessageCallBackListener;
import gang.com.screencontrol.util.AppManager;
import gang.com.screencontrol.util.JsonUitl;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

/**
 * 本次开发主要针对一个控制器也就是一个slaveID
 * BUG请求后返回来的内容总是被那些fragment的onMessage拦截掉
 * 图片的Base64编解码  http://blog.csdn.net/lincyang/article/details/46596899
 * Base64解码问题  http://blog.sina.com.cn/s/blog_54410d940101esw5.html
 * TabLayout的使用  ：http://www.jianshu.com/p/2b2bb6be83a8
 * 处理Fragment+Viewpage一起使用时候获取Fragment对象：http://m.jb51.net/article/102213.htm
 */
public class MainAct_xiuding extends AppCompatActivity implements MessageCallBackListener, MediaAddCallBackListener, ModelAddCallBackListener, DeviceAddCallBackListener {
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
    @BindView(R.id.clock)
    ImageView clock;
    @BindView(R.id.weather)
    ImageView weather;
    @BindView(R.id.jingdianviewpager)
    ViewPager mViewPagerjingdian;
    @BindView(R.id.pause_player)
    ImageView pausePlayer;
    @BindView(R.id.start_remote_control)
    ImageView startRemoteControl;
    @BindView(R.id.model_add_window)
    TextView modelAddWindow;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.refresh_all_bottom)
    ImageView refreshAllBottom;
    @BindView(R.id.lock_yorn)
    LinearLayout lockYorn;
    @BindView(R.id.voice_controller)
    ImageView voiceController;
    @BindView(R.id.deleteceng)
    ImageView deleteceng;
    @BindView(R.id.save_ceng)
    ImageView saveCeng;
    @BindView(R.id.quanping_ceng)
    ImageView quanpingCeng;
    @BindView(R.id.caifen_controller)
    ImageView caifenController;
    @BindView(R.id.light_controller)
    ImageView lightController;
    @BindView(R.id.top_ceng)
    ImageView topCeng;
    @BindView(R.id.bottom_ceng)
    ImageView bottomCeng;
    /**
     * 选项卡标题
     */
    private WebSocket webSocket;
    private Gson gson = new Gson();
    private static final String TAG = "MainAct_xiuding";
    //窗口锁定状态，0是锁住状态图片也是锁图片，1是开锁状态
    private int LOCKSTATE = 0;
    //判读显示窗口当前是否有模式存在,默认是0没有模式，1是有模式
    private int modeljudge_state = 0;
    //暂停点击状态pausestate
    private int pausestate = 0;//暂停是0
    private PagerAdapter mAdapter;
    private Drawable drawable;
    //用来放显示的图片的list，避免新的返回覆盖
    private List<String> list_returnpc = new ArrayList<>();
    //用来放层信息的list
    List<ContentBeanX> ContentXlist = new ArrayList<>();
    //for循环 Contentlist时候的变量初始化
    int i_contentxlist = 0;
    List<ContentBean> yuansulist;
    //全局变量，屏幕ID
    private int slaveID=0;
    //用来存放所有的图层
    private List<DragScaleView> dragScaleViewList = new ArrayList<>();
    //用来存放所有的图层对应的guid也就是后面的layerid
    private List<String> guidlist = new ArrayList<>();
    //判断声音按钮点击状态
    private int state_voice = 0;
    /**
     * 下面两个全局变量都是在MAXWALLSCREENCONTENT这个接口解析时候执行点击事件对这两个赋值，给这两个全局变量操作
     */
    //用来表示点击的那个层特定的数据
    private ContentBeanX quan_One_contentbean = null;
    //用来表示点击的那个层里面具有的特定的元素list，已经被转成String的json
    private String quanju_yuansu_list_json;
    //参数传递时候，与1920和1080形成比例控制pieceXml
    private int wallheight = 0;
    private int wallwidth = 0;
    //Fragment滑动项的标志
    private int Fragment_Position = 0;
    //Fragment的声明
    private Model_Fragment model_fragment = null;
    private Grouping_Fragment grouping_fragment = null;
    private Media_Fragment media_fragment;
    private Device_Fragment device_fragment;
    private Message_Fragment message_fragment;
    //用来存layerid的list，比较新加入的层id与之前是否相同，相同就是替换
    private List<String> layerid_list = new ArrayList<>();
    //每次添加层。模式。或者场景的时候都需要获取一个不唯一的id
    private int unlayerid = 0;
    private static final int NOTIFICATION_FLAG = 1;
    //用于存放每个层的zoder，用于置顶排序用
    private List<Integer> layer_zoderlist = new ArrayList<>();
    //存放点击的媒体的信息,用于添加层
    private BodyBean mediabodybean_add;
    //存放点击的设备信息，用于添加层
    private InfoListBean devicebodybean_add;
    //添加层的时候的加载框
    private Dialog addceng_dialog = null;
    //开锁开启显示墙实时控制时的加载框
    private Dialog startExhi_dialog=null;
    //关锁关闭显示墙实时控制的加载框
    private Dialog stopExhi_dialog=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消顶部标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_act_xiuding);
        ButterKnife.bind(this);
        AppManager.addActivity(this);
        initWebsocket();
        getWall_content();
        //主动向服务器发送获取大屏内容请求
        SendNotify();
        //加载显示墙信息指令发送
        loadWallInfo();
        initViewpage();
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
        //给Viewpage加监听事件

        mViewPagerjingdian.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment_Position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取app界面中VIew显示墙的高度和宽度---id:contain_view_xiuding
     * 获取屏幕宽高要在执行完OnCreate，才可以onMeasure。。。这个可以获取应该是有回调方法
     * 需要注意的是屏幕的实际宽高比大屏幕的宽高按照比例小十分之一
     */
    private void getWall_content() {
        ViewTreeObserver vto2 = containViewXiuding.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                containViewXiuding.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                wallheight = containViewXiuding.getHeight();
                wallwidth = containViewXiuding.getWidth();
                ToastUtil.show(MainAct_xiuding.this, "屏幕高度" + containViewXiuding.getHeight() + "屏幕宽度" + containViewXiuding.getWidth());
            }
        });
    }

    /**
     * 接口回调，获取websocket-------1
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

    /**
     * 主动向服务器发送获取大屏内容请求 :NotifyScreenContent-------2
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
     * 加载显示墙信息，看有几块显示墙--------3
     */
    private void loadWallInfo() {
        webSocket.send("    {\n" +
                "       \"body\" : \"\",\n" +
                "       \"guid\" : \"M-44\",\n" +
                "       \"type\" : \"LOADVIDEOWALLINFO\"\n" +
                "    }");
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

    /**
     * 显示dialog，是否添加闹钟
     */

    private void show_clock_dialog() {
        final Dialog dialog_clock = new Dialog(MainAct_xiuding.this, R.style.dialog);
        dialog_clock.setContentView(R.layout.dialog_clockl);
        Button dialog_model_add = (Button) dialog_clock.findViewById(R.id.dialog_clock_add);
        dialog_model_add.setOnClickListener(new OnClickListener() {
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
                        "          \"slaveID\" : "+slaveID+",\n" +
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
        dialog_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_clock.cancel();
            }
        });
        dialog_clock.show();
    }

    /**
     * 显示dialog,是否添加天气
     */

    private void show_weather_dialog() {
        final Dialog dialog_weather = new Dialog(MainAct_xiuding.this, R.style.dialog);
        dialog_weather.setContentView(R.layout.dialog_weather);
        Button dialog_model_add = (Button) dialog_weather.findViewById(R.id.dialog_weather_add);
        dialog_model_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                webSocket.send("    {\n" +
                        "       \"body\" : {\n" +
                        "          \"height\" : 400,\n" +
                        "          \"slaveID\" : "+slaveID+",\n" +
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
        dialog_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_weather.cancel();
            }
        });
        dialog_weather.show();
    }


    /**
     * dialog是否播放模式
     *
     * @param modelId
     * @param modelname
     */
    private void showNormalDialog(final int modelId, final String modelname) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final Builder normalDialog =
                new Builder(MainAct_xiuding.this);
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
                        //播放新模式的时候也会重新变回锁定状态
                        startlock.setImageResource(R.mipmap.lock_close);
                        LOCKSTATE = 0;
                        Anim_contro_ceng(1);
                        StopExhibition();
                        ToastUtil.show(MainAct_xiuding.this, "播放上一个模式");
                        modelAddWindow.setText("模 式:" + modelname);
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

    /**
     * 对屏幕总的层可以进行控制-------开锁
     */
    private void startlock_SCREENCONTENT() {
        //首先判断ContentXlist不为0就是里面有层
        if (ContentXlist.size() > 0) {
            Iterator<DragScaleView> iterator = dragScaleViewList.iterator();
            while (iterator.hasNext()) {
                iterator.next().mClick = true;
            }
            /*for ( i_contentxlist = 0; i_contentxlist < ContentXlist.size(); i_contentxlist++) {
                dragScaleViewList.get(i_contentxlist).setClickable(true);
                //获取layerid
                ContentXlist.get(i_contentxlist).getGuid();
                //点击view的时候可以直接把元素获取出来
                //元素list，可以有多个元素。一个ContentXlist中可以有多个元素
                 yuansulist = ContentXlist.get(i_contentxlist).getContent();
                dragScaleViewList.get(i_contentxlist).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d("FUCK",ContentXlist.get(i_contentxlist-1).getGuid());
                        ToastUtil.show(MainAct_xiuding.this, "位置变化" + "顶部" + v.getTop() + "左边" + v.getLeft()+ContentXlist.get(i_contentxlist-1).getGuid());
                    }
                });
            }*/
        } else {
            ToastUtil.show(MainAct_xiuding.this, "屏幕中没有可以控制的层空空空");
        }
    }

    /**
     * 发送层变化移动请求
     */
    private void move_layer() {
    }

    /**
     * 对屏幕总的层可以进行控制-------关锁
     */
    private void closelock_SCREENCONTENT() {
        //首先判断ContentXlist不为0就是里面有层
        if (ContentXlist.size() > 0) {
            Iterator<DragScaleView> iterator = dragScaleViewList.iterator();
            while (iterator.hasNext()) {
                iterator.next().mClick = false;
                /*for (int i = 0; i < ContentXlist.size(); i++) {
                    dragScaleViewList.get(i).setClickable(false);
                    dragScaleViewList.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.show(MainAct_xiuding.this, "清先开锁");
                        }
                    });
                }*/
            }
        }
    }

    /**
     * 发送开始实时控制显示墙指令
     */
    private void StartExhibition() {
        webSocket.send("{\n" +
                "       \"body\" : \"\",\n" +
                "       \"guid\" : \"M-212\",\n" +
                "       \"type\" : \"STARTEXHIBITION\"\n" +
                "    }");
    }

    /**
     * 发送关闭实时控制显示墙指令
     */
    private void StopExhibition() {
        webSocket.send("    {\n" +
                "       \"body\" : \"\",\n" +
                "       \"guid\" : \"M-181\",\n" +
                "       \"type\" : \"STOPEXHIBITION\"\n" +
                "    }");
    }

    /**
     * 动画显示控制层的几个按钮
     */
    private void Anim_contro_ceng(int state_click) {
        if (state_click == 0) {
            lockYorn.setVisibility(View.VISIBLE);
            //通过加载XML动画设置文件来创建一个Animation对象；
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.lin_item_anim);
            //得到一个LayoutAnimationController对象；
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            //设置控件显示的顺序；
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            //设置控件显示间隔时间；
            controller.setDelay(0.3f);
            //为ListView设置LayoutAnimationController属性；
            lockYorn.setLayoutAnimation(controller);
        } else {
            lockYorn.setVisibility(View.GONE);
        }
    }

    /**
     * 声音控制的方法--------------MuteAllLayer
     */
    private void MuteAllLayer(Boolean voice) {

        webSocket.send(" {\n" +
                "   \"body\" : {\n" +
                "      \"mute\" :" + voice + "   },\n" +
                "   \"guid\" : \"M-44\",\n" +
                "   \"type\" : \"MUTEALLLAYER\"\n" +
                "}");
    }

    /**
     * 媒体添加接口回调的方法重写
     *
     * @param mediaBean_childdetial
     */
    @Override
    public void OnAddMediaView(BodyBean mediaBean_childdetial) {
        //添加媒体层的时候也需要先判断
        mediabodybean_add = mediaBean_childdetial;
        Addceng_AskID("开锁后才可以添加媒体层","媒体层添加中");
    }
    /**
     * 设备添加接口的回调重写
     *
     * @param deviceBean_info
     */
    @Override
    public void OnAddDeviceView(InfoListBean deviceBean_info) {
        devicebodybean_add=deviceBean_info;
        Addceng_AskID("开锁后才可以添加设备层。","设备层添加中");
    }
    /**
     * 媒体模块向屏幕中添加层，需要判断一下当前内容中是否有这个层
     */
    private void Addceng_AskID(String a,String b) {
        //只有在开锁情况下才可以添加媒体层
        if (LOCKSTATE == 0) {
            DialogUIUtils.showAlert(MainAct_xiuding.this, "鼎泓提示", a, null, null, null, null, true, true, true, new DialogUIListener() {
                @Override
                public void onPositive() {

                }

                @Override
                public void onNegative() {

                }
            }).show();
        } else {
            addceng_dialog = DialogUIUtils.showLoading(MainAct_xiuding.this, b, false, false, false, false).show();
            //发送获取一个唯一的id
            webSocket.send("    {\n" +
                    "       \"body\" : \"\",\n" +
                    "       \"guid\" : \"M-80\",\n" +
                    "       \"type\" : \"GETUNIQUEID\"\n" +
                    "    }");
        }


    }

    /**
     * 模式添加的接口回调
     *
     * @param modelbean
     * @param mobel_list
     */
    @Override
    public void OnAddModelView(View v, final BasicInfoBean modelbean, List<BasicInfoBean> mobel_list) {

        if (modeljudge_state == 0) {
            start_window();
            //调用加载模式的接口LoadProgram，接口地址：https://www.showdoc.cc/2452?page_id=11152
            showNormalDialog(modelbean.getID(), modelbean.getName());

        } else {
            Snackbar.make(v, "显示窗已有模式确认要替换吗?", Snackbar.LENGTH_LONG)
                    .setAction("确认", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //替换时点击的模式内容的id和模式内容的名称
                            modelbean.getName();
                            modelbean.getID();
                            //替换之前的模式
                            modelAddWindow.setText("模 式:" + modelbean.getName());
                            webSocket.send("{\n" +
                                    "   \"body\" : {\n" +
                                    "      \"id\" :" + modelbean.getID() + "   },\n" +
                                    "   \"guid\" : \"M-79\",\n" +
                                    "   \"type\" : \"PLAYPROGRAM\"\n" +
                                    "}");
                            modeljudge_state = 1;
                            //替换模式也要把之前的数据等内容还有list清空重新回到锁住状态
                            dragScaleViewList.clear();
                            guidlist.clear();
                            containViewXiuding.removeAllViews();
                            //播放新模式的时候也会重新变回锁定状态
                            startlock.setImageResource(R.mipmap.lock_close);
                            LOCKSTATE = 0;
                            Anim_contro_ceng(1);
                            StopExhibition();
                        }
                    }).show();
        }
    }



    /**
     * 消息接收的接口回调方法
     *
     * @param text
     */
    @Override
    public void onRcvMessage(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    /**
                     * 第一步
                     * 首先应该是先加载显示墙，看看slaveInfo里面包含几个，就有几个slaveid，几路显示
                     * 然后遍历加到list里面创建一个list<String></String>
                     */
                    /**
                     * 第二步
                     *显示屏内的层,首先应该遍历第一步中的list，slaveid看有几个显示器，
                     * 遍历之后让那些层显示在固定的显示器
                     * 在点击事件中，对于固定的层在传递的时候，把所在显示器对应的slaveid也要传递过去 setListener(mDragView，slaveid)
                     */
                    if (allmodelobject.getString("type").equals("MAXWALLSCREENCONTENT")) {
                        //每次接到这个层的请求时候，都要清空之前的ContentXlist，同时要清空之前的布局中所有的View
                        if (ContentXlist.size() > 0 && dragScaleViewList.size() > 0) {
                            ContentXlist.clear();
                            dragScaleViewList.clear();
                            guidlist.clear();
                            containViewXiuding.removeAllViews();
                            //获取到新的变化以后把layer_zoderlist清空
                            layer_zoderlist.clear();
                        }
                        String bodystring = allmodelobject.getString("body");
                        JSONObject contentoboj = new JSONObject(bodystring);
                        JSONArray slaveInfoJsonArray = contentoboj.getJSONArray("Content");
                        //将Array数据转换成List
                        ContentXlist = gson.fromJson(slaveInfoJsonArray.toString(), new TypeToken<List<ContentBeanX>>() {
                        }.getType());
                        for (int i = 0; i < ContentXlist.size(); i++) {
                            DragScaleView mDragView = new DragScaleView(MainAct_xiuding.this);  //创建imageview
                            mDragView.isFocused();
                            //mDragView1.setBackgroundResource(R.mipmap.ic_launcher);
                            //mDragView.setClickable(false);//相当于android:clickable="true"
                            int w = ContentXlist.get(i).getRight() - ContentXlist.get(i).getLeft();
                            float w_float = w;
                            float hehewallwidth = wallwidth;
                            float finally_w = w_float * (hehewallwidth / 1920);
                            int h = ContentXlist.get(i).getBottom() - ContentXlist.get(i).getTop();
                            float h_float = h;
                            float hahawallheight = wallheight;
                            float finally_h = h_float * (hahawallheight / 1080);
                            LayoutParams lp = new LayoutParams((int) finally_w, (int) finally_h);
                            mDragView.setLayoutParams(lp);  //image的布局方式
                            //mDragView.setTag(ContentXlist.get(i).getGuid());
                            mDragView.setTag(ContentXlist.get(i));
                            //设置图片的位置
                            float m_left = ContentXlist.get(i).getLeft();
                            float finally_m_left = m_left * (hehewallwidth / 1920);
                            float m_top = ContentXlist.get(i).getTop();
                            float finally_m_top = m_top * (hahawallheight / 1080);
                            lp.setMargins((int) finally_m_left, (int) finally_m_top, 0, 0);
                            layer_zoderlist.add(ContentXlist.get(i).getZorder());
                            Global_public.Maxzoder = get_Max_zoder();
                            layerid_list.add(ContentXlist.get(i).getGuid());
                            containViewXiuding.addView(mDragView);
                            //向两个list中添加数据
                            setListener(mDragView, slaveID);
                            dragScaleViewList.add(mDragView);
                            guidlist.add(ContentXlist.get(i).getGuid());
                            //rebuild(mDragView1,123);
                            //View就是你自定义的控件，h跟w就是根据屏幕分辨率来设置view的宽高。不要说屏幕分辨率怎么获取！
                            //像媒体层，设备层等添加成功以后都收到服务器的这个回调，然后结束dialog
                            if (addceng_dialog != null) {
                                DialogUIUtils.dismiss(addceng_dialog);
                            }
                        }
                    }
                    /**
                     * 第三步向层中添加图片
                     * 如果监听到了显示窗回显的字段
                     * 接口地址如下http://www.showdoc.cc/2452?page_id=13041     MAXWALLSNAPSHOT
                     */
                    else if (allmodelobject.getString("type").equals("MAXWALLSNAPSHOT")) {
                        if (ContentXlist.size() == 0 || dragScaleViewList.size() == 0) {
                            //说明目前没有返回的层信息
                            ToastUtil.show(MainAct_xiuding.this, "显示器中的层信息为空");
                        } else {
                            String bodystring_snapshot = allmodelobject.getString("body");
                            JSONObject snapshotoboj = new JSONObject(bodystring_snapshot);
                            // LogUtil.e("回显层中内容", snapshotoboj.getInt("slaveID") + "");
                            //snapshot获取的slaveID
                            snapshotoboj.getInt("slaveID");
                            //循环遍历
                            for (int i = 0; i < ContentXlist.size(); i++) {
                                if (snapshotoboj.getInt("slaveID") == slaveID && snapshotoboj.getString("layerID").equals(ContentXlist.get(i).getGuid())) {
                                    String imageDataStr1 = snapshotoboj.getString("imageDataStr");
                                    LogUtil.e("哎这个图片", imageDataStr1);
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
                                            LogUtil.d("卧槽总长度", decode.length + "");
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
                        //收到这个请求之后先把公共类里的存slaveip的list清空
                        if (Global_public.slaveip_list != null) {
                            Global_public.slaveip_list.clear();
                        }
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        JSONArray slaveInfoJsonArray = basicInfoboj.getJSONArray("slaveInfo");
                        //开发针对一个控制器所以这么全局赋值
                        slaveID=slaveInfoJsonArray.getJSONObject(0).getInt("id");
                        for (int i = 0; i < slaveInfoJsonArray.length(); i++) {
                            JSONObject slaveInfoJsonObject = slaveInfoJsonArray.getJSONObject(i);
                            String slaveip = slaveInfoJsonObject.getString("ip");
                            Global_public.slaveip_list.add(slaveip);
                        }
                        JSONObject slaveInfoJsonObject = slaveInfoJsonArray.getJSONObject(0);
                        JSONArray monitorInfoJsonArray = slaveInfoJsonObject.getJSONArray("monitorInfo");
                        //将Array数据转换成List
                        List<MonitorInfoBean> MonitorInfolist = gson.fromJson(monitorInfoJsonArray.toString(), new TypeToken<List<MonitorInfoBean>>() {
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
                    //发送获取一个唯一的layerid的请求
                    else if (allmodelobject.getString("type").equals("GETUNIQUEID")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject GETUNIQUEIDobject = new JSONObject(bodystring);
                        unlayerid = GETUNIQUEIDobject.getInt("id");
                        LogUtil.e("添加层内容不唯一ID", unlayerid + "");
                       /* ========================================================在刚一发送请求的时候就弹出dialog
                        然后接收到那个添加以后dialog取消，其中注意重新连接的情况，断开连接的时候也要取消dialog,
                        还得监听断开连接时候dialog取消，
                        最后呢根据Fragment的对于类型判断是发添加设备请求还是添加媒体请求*/
                        /**
                         * 防止空指针判断取到那个id没有，
                         * 接下来根据Fragment判断滑动到媒体就发送添加媒体的请求
                         * 滑动到设备添加设备的请求,
                         * 2是媒体Fragment
                         * 3是设备Fragment
                         */
                        if (Fragment_Position == 2) {
                            if (unlayerid != 0) {
                                if (layerid_list.contains(mediabodybean_add.getFileId())) {
                                    DialogUIUtils.showToastCenter("确定替换层吗");
                                } else {
                                    DragScaleView mDragView = new DragScaleView(MainAct_xiuding.this);  //创建imageview
                                    mDragView.isFocused();
                                    //mDragView.setBackgroundResource(R.mipmap.video);
                                    mDragView.setClickable(true);//相当于android:clickable="true"
                                    LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                    mDragView.setLayoutParams(lp);  //image的布局方式
                                    //设置图片的位置
                                    lp.setMargins(10, 10, 10, 10);
                                    containViewXiuding.addView(mDragView);
                                    //向两个list中添加数据
                                    dragScaleViewList.add(mDragView);
                                    int addzoder = Global_public.Maxzoder + 1;
                                    LogUtil.e("添加层内容", unlayerid + mediabodybean_add.getDescription() + "=====" + mediabodybean_add.getFileId() + "=====" + mediabodybean_add.getFileName() + "层Zoder" + addzoder);
                                    webSocket.send("    {\n" +
                                            "       \"body\" : {\n" +
                                            "          \"alpha\" : 1.0,\n" +
                                            "          \"highlight\" : false,\n" +
                                            "          \"layerID\" : " + unlayerid + ",\n" +
                                            "          \"layerItem\" : [\n" +
                                            "             {\n" +
                                            "                \"ID\" : " + unlayerid + ",\n" +
                                            "                \"description\" :" + mediabodybean_add.getDescription() + " \"\",\n" +
                                            "                \"majorID\" : -1,\n" +
                                            "                \"minorID\" : " + mediabodybean_add.getFileId() + ",\n" +
                                            "                \"name\" : \"" + mediabodybean_add.getFileName() + "\",\n" +
                                            "                \"playOrder\" : 0,\n" +
                                            "                \"playTime\" : 30,\n" +
                                            "                \"refreshTime\" : 1719895702,\n" +
                                            "                \"type\" : " + mediabodybean_add.getType() + ",\n" +
                                            "                \"validSource\" : 1\n" +
                                            "             }\n" +
                                            "          ],\n" +
                                            "          \"pieceXml\" : \"\n" +
                                            "              <Layer>\\n    \n" +
                                            "                <Piece slaveid=\\\"" + slaveID + "\\\" slaveleft=\\\"0\\\" slavetop=\\\"0\\\" slavewidth=\\\"1\\\" slaveheight=\\\"1\\\" layerleft=\\\"0\\\" layertop=\\\"0\\\" layerwidth=\\\"1\\\" layerheight=\\\"1\\\" />\\n\n" +
                                            "            </Layer>\\n\",\n" +
                                            "          \"type\" : \"Add\",\n" +
                                            "          \"zOrder\" : " + addzoder + "\n" +
                                            "       },\n" +
                                            "       \"guid\" : \"M-45\",\n" +
                                            "       \"type\" : \"LAYERACTION\"\n" +
                                            "    }");
                                }
                            }
                        }
                    }

                    //判断控制器是否断开连接
                    else if (allmodelobject.getString("type").equals("SLAVECONNECTED")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject SLAVECONNECTED = new JSONObject(bodystring);
                        if (SLAVECONNECTED.getInt("slaveStatus") == 0) {
                            Notifi_Controller();
                        }
                    }
                    //开锁，开始实时控制显示墙
                    else if (allmodelobject.getString("type").equals("STARTEXHIBITION"))
                    {
                    //关闭加载
                        startExhi_dialog.dismiss();
                    }
                    //关锁，关闭实时控制显示墙
                    else if(allmodelobject.getString("type").equals("STOPEXHIBITION"))
                    {
                     //关闭加载
                        stopExhi_dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置监听
     *
     * @param dragView
     */
    private void setListener(DragScaleView dragView, final int slaveID) {
        if (LOCKSTATE == 1) {
            // 开锁状态 可以点击
            dragView.mClick = true;
            dragView.setClickable(true);
        }
        dragView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof DragScaleView) {
                    if (!((DragScaleView) v).mClick) {
                        // 禁止点击
                        return;
                    }
                    ContentBeanX contentBeanX = (ContentBeanX) v.getTag();
                    //讲bean转成json，取到其中的元素数组
                    // ScreenContent.BodyBean.ContentBeanX.ContentBean contentBean=new ScreenContent.BodyBean.ContentBeanX.ContentBean();
                    try {
                        JSONObject bigcontent = new JSONObject(JsonUitl.toJson(contentBeanX));
                        String content_yuansu = bigcontent.getString("Content");
                        List<ContentBean> contentBean = gson.fromJson(content_yuansu, new TypeToken<List<ContentBean>>() {
                        }.getType());
                        List<CengYuansuBean> cengYuansuBeenlist = new ArrayList<CengYuansuBean>();
                        LogUtil.e("fuck转过来的json", content_yuansu);
                        for (int i = 0; i < contentBean.size(); i++) {
                            CengYuansuBean cengYuansuBean = new CengYuansuBean();
                            cengYuansuBean.setID(contentBean.get(i).getLayerItemID());
                            cengYuansuBean.setName(contentBean.get(i).getName());
                            cengYuansuBean.setType(contentBean.get(i).getLayerItemType());
                            cengYuansuBean.setMajorID(contentBean.get(i).getMajorID());
                            cengYuansuBean.setMinorID(contentBean.get(i).getMinorID());
                            cengYuansuBean.setPlayOrder(contentBean.get(i).getPlayOrder());
                            cengYuansuBean.setPlayTime(contentBean.get(i).getPlayTime());
                            //cengYuansuBean.setValidSource();
                            cengYuansuBean.setRefreshTime(contentBean.get(i).getRefreshTime());
                            //cengYuansuBean.setProtoType();
                            cengYuansuBean.setDescription(contentBean.get(i).getDescription());
                            //cengYuansuBean.setUrlList();
                            cengYuansuBean.setCascadeServerId(contentBean.get(i).getCascadeServerId());
                            cengYuansuBean.setCascadeServerSourceType(contentBean.get(i).getCascadeServerSourceType());
                            //cengYuansuBean.setReserved();
                            cengYuansuBean.setItemThumbnialPath(contentBean.get(i).getItemThumbnailPath());
                            //cengYuansuBean.setSourceTotalTime();
                            cengYuansuBeenlist.add(cengYuansuBean);
                        }
                        //将cengYuansuBeenlist转成json数组发送请求·
                        String yuansu_list_json = JsonUitl.objectToString(cengYuansuBeenlist);
                        //把下面两个赋值给全局变量
                        quanju_yuansu_list_json = yuansu_list_json;
                        quan_One_contentbean = contentBeanX;
                        ToastUtil.show(MainAct_xiuding.this, "位置变化" + "顶部" + v.getTop() + "左边" + v.getLeft() + contentBeanX.getGuid());
                        float left_float = v.getLeft();
                        float top_float = v.getTop();
                        //完美需要注意的是这里都不需要乘以以比例*1920/wallwidth，在绘制时候控制好比例就可以了
                        float slaveleft = (left_float / wallwidth);
                        float slavetop = (top_float / wallheight);
                        float width_float = v.getRight() - v.getLeft();
                        float height_float = v.getBottom() - v.getTop();
                        float slavewidth = (width_float / wallwidth);
                        float slaveheight = (height_float / wallheight);
                        LogUtil.e("变换的比例", "左端的比例" + slaveleft + "顶端的比例" + slavetop);
                        String requesturl = "{\"body\": {\n" + "\"alpha\": 1.0,\n" +
                                "\"highlight\": false,\n" +
                                "\"layerID\": " + contentBeanX.getGuid() + ",\n" +
                                "\"layerItem\":" + yuansu_list_json + ",\"pieceXml\": \"<Layer>\\n   <Piece slaveid=\\\"" + slaveID + "\\\" slaveleft=\\\"" + slaveleft + "\\\" slavetop=\\\"" + slavetop + "\\\" slavewidth=\\\"" + slavewidth + "\\\" slaveheight=\\\"" + slaveheight + "\\\" layerleft=\\\"0\\\" layertop=\\\"0\\\" layerwidth=\\\"1\\\" layerheight=\\\"1\\\" />\\n</Layer>\\n\",\n" +
                                "\"type\": \"Move\",\n" +
                                "\"zOrder\": " + contentBeanX.getZorder() + "\n" +
                                "},\n" +
                                "\"guid\": \"M-45\",\n" +
                                "\"type\": \"LAYERACTION\"\n" +
                                "}";
                        LogUtil.e("FUCK", "requesturl" + requesturl);
                        webSocket.send(requesturl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @OnClick({R.id.deleteceng, R.id.voice_controller, R.id.lastmodel, R.id.nextmodel, R.id.close_software, R.id.startlock, R.id.clock, R.id.weather, R.id.pause_player, R.id.start_remote_control, R.id.save_ceng, R.id.quanping_ceng, R.id.caifen_controller, R.id.light_controller, R.id.top_ceng, R.id.bottom_ceng, R.id.refresh_all_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lastmodel:
                //播放上一个模式的时候清空之前的View和数据
                dragScaleViewList.clear();
                guidlist.clear();
                containViewXiuding.removeAllViews();
                startlock.setImageResource(R.mipmap.lock_close);
                LOCKSTATE = 0;
                Anim_contro_ceng(1);
                StopExhibition();
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
                //播放下一个和上一个模式的时候也是锁住屏幕
                startlock.setImageResource(R.mipmap.lock_close);
                LOCKSTATE = 0;
                Anim_contro_ceng(1);
                StopExhibition();
                ToastUtil.show(MainAct_xiuding.this, "播放下一个模式");
                webSocket.send("    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-171\",\n" +
                        "       \"type\" : \"NEXTPROGRAM\"\n" +
                        "    }");
                break;
            //增加模式到播放列表
            case R.id.startlock:
                if (LOCKSTATE == 0) {
                    LOCKSTATE = 1;
                    //发送开始实时控制显示墙指令
                    startExhi_dialog = DialogUIUtils.showLoading(MainAct_xiuding.this, "开启实时控制", false, true, false, false).show();
                    StartExhibition();
                    //点击开锁按钮之后才可以调控层
                    startlock_SCREENCONTENT();
                    startlock.setImageResource(R.mipmap.lock_open);
                    Anim_contro_ceng(0);
                } else {
                    stopExhi_dialog = DialogUIUtils.showLoading(MainAct_xiuding.this, "关闭实时控制", false, true, false, false).show();
                    //锁住层的内容
                    closelock_SCREENCONTENT();
                    startlock.setImageResource(R.mipmap.lock_close);
                    StopExhibition();
                    LOCKSTATE = 0;
                    Anim_contro_ceng(1);
                }
                break;
            case R.id.close_software:
                //关闭软件，关闭所有Activity
                new AlertDialog(MainAct_xiuding.this).builder().setTitle("退出当前程序")
                        .setMsg("确定退出鼎泓导播吗？")
                        .setPositiveButton("确认退出", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent stopIntent = new Intent(MainAct_xiuding.this, MainService.class);
                                stopService(stopIntent);
                                AppManager.finishAllActivity();
                                //彻底结束程序进程
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                break;
            case R.id.clock:
                show_clock_dialog();
                break;
            case R.id.weather:
                show_weather_dialog();
                break;
            //暂停显示墙播放和开始显示墙播放
            case R.id.pause_player:
                if (pausestate == 0) {
                    pause_player();
                    pausePlayer.setImageResource(R.mipmap.zanting);
                    pausestate = 1;
                } else {
                    continue_player();
                    pausePlayer.setImageResource(R.mipmap.bofang);
                    pausestate = 0;
                }
                break;
            case R.id.start_remote_control:
                Intent a = new Intent(this, RemoteControlAct.class);
                startActivity(a);
                break;
            //声音控制
            case R.id.voice_controller:
                if (state_voice == 0) {
                    voiceController.setImageResource(R.mipmap.voice_close);
                    state_voice = 1;
                    MuteAllLayer(true);

                } else {
                    voiceController.setImageResource(R.mipmap.voice_open);
                    state_voice = 0;
                    MuteAllLayer(false);
                }
                break;
            case R.id.deleteceng:
                if (quan_One_contentbean == null) {
                    showClearScreenDialog();
                } else {
                    showDeletecheckDialog();
                }
                break;
            case R.id.save_ceng:
                showSaveDialog();
                break;
            case R.id.quanping_ceng:
                if (quan_One_contentbean == null) {
                    //表示没有选中层
                    DialogUIUtils.showToastCenterLong("清先点击一个层再进行全屏显示");
                } else {
                    showQuanpingcheckDialog();
                }
                /**
                 * 分屏，四分屏，点击之后现在屏幕上，每次先确定几个数据，超过四个就不能进行分屏
                 * 因为是拆分，所有宽高都是一样的，都是屏幕的一半
                 */
                //之前这里缺一个break尴尬
                break;
            case R.id.caifen_controller:
                showCaifenDialog();
                break;
            case R.id.light_controller:
                //调高亮度该层
                ToastUtil.show(MainAct_xiuding.this, "调高亮度该层");
                String request_light_ceng = "{\"body\": {\n" + "\"alpha\": 0.1,\n" +
                        "\"highlight\": true,\n" +
                        "\"layerID\": " + quan_One_contentbean.getGuid() + ",\n" +
                        "\"layerItem\":" + quanju_yuansu_list_json + ",\"pieceXml\": \"<Layer>\\n   <Piece slaveid=\\\"" + slaveID + "\\\" slaveleft=\\\"0.6\\\" slavetop=\\\"0\\\" slavewidth=\\\"0.4\\\" slaveheight=\\\"0.948148\\\" layerleft=\\\"0\\\" layertop=\\\"0\\\" layerwidth=\\\"1\\\" layerheight=\\\"1\\\" />\\n</Layer>\\n\",\n" +
                        "\"type\": \"Highlight\",\n" +
                        "\"zOrder\": " + quan_One_contentbean.getZorder() + "\n" +
                        "},\n" +
                        "\"guid\": \"M-45\",\n" +
                        "\"type\": \"LAYERACTION\"\n" +
                        "}";
                webSocket.send(request_light_ceng);
                break;
            case R.id.top_ceng:
                ToastUtil.show(MainAct_xiuding.this, "调高该层位于顶部");
                int topzoder = Global_public.Maxzoder + 1;
                String request_top_ceng = "{\"body\": {\n" + "\"alpha\": 1.0,\n" +
                        "\"highlight\": false,\n" +
                        "\"layerID\": " + quan_One_contentbean.getGuid() + ",\n" +
                        "\"type\": \"ZOrder\",\n" +
                        "\"zOrder\": " + topzoder + "\n" +
                        "},\n" +
                        "\"guid\": \"M-45\",\n" +
                        "\"type\": \"LAYERACTION\"\n" +
                        "}";
                webSocket.send(request_top_ceng);
                break;
            case R.id.bottom_ceng:
                ToastUtil.show(MainAct_xiuding.this, "调高该层位于底部");
                String request_bottom_ceng = "\n" +
                        "       \"body\" : {\n" +
                        "          \"layerAction\" : [\n" +
                        "             {\n" +
                        "                \"layerID\" : " + quan_One_contentbean.getGuid() + ",\n" +
                        "                \"type\" : \"ZOrder\",\n" +
                        "                \"zOrder\" : 0\n" +
                        "             }\n" +
                        "          ],\n" +
                        "          \"type\" : \"ZOrder\"\n" +
                        "       },\n" +
                        "       \"guid\" : \"M-54\",\n" +
                        "       \"type\" : \"LAYERLISTACTION\"\n" +
                        "    }";
                LogUtil.d("层置底", request_bottom_ceng);
                webSocket.send(request_bottom_ceng);
                break;
            case R.id.refresh_all_bottom:
                ToastUtil.show(MainAct_xiuding.this, "刷新当前数据");
                refData();
                break;
        }
    }

    /**
     * 置顶用到的方法，获取到最大的zoder
     */
    private int get_Max_zoder() {
        int max_zoder = layer_zoderlist.get(0);
        for (int i = 0; i < layer_zoderlist.size(); i++) {
            if (layer_zoderlist.get(i) > max_zoder) {
                max_zoder = layer_zoderlist.get(i);
            }
        }
        LogUtil.e("最大的Zoder数", max_zoder + "");
        return max_zoder;
    }

    /**
     * 四分屏：针对手机屏幕中的内容发送View的变换
     *
     * @param view_top
     * @param view_left
     */
    private void Phone_Caifen_View(int view_top, int view_left) {
        DragScaleView mDragView = new DragScaleView(MainAct_xiuding.this);  //创建imageview
        mDragView.isFocused();
        //mDragView1.setBackgroundResource(R.mipmap.ic_launcher);
        mDragView.setClickable(false);//相当于android:clickable="true"
        //设置图片大小
        LayoutParams lp = new LayoutParams(wallwidth / 2, wallheight / 2);
        mDragView.setLayoutParams(lp);  //image的布局方式
        //mDragView.setTag(ContentXlist.get(i).getGuid());
        //mDragView.setTag(ContentXlist.get(0));
        //设置图片的位置
        lp.setMargins(view_left, view_top, 0, 0);
        containViewXiuding.addView(mDragView);
        //向两个list中添加数据
        //setListener(mDragView, slaveID);
        dragScaleViewList.add(mDragView);
        //guidlist.add(ContentXlist.get(0).getGuid());
    }

    /**
     * 四分屏：针对四分屏进行发送移动的层请求的方法,操作显示墙里面的内容
     *
     * @param contentBeanXo
     * @param wall_slave_left
     * @param wall_slave_top
     */
    private void Wall_Caifen_Move(ContentBeanX contentBeanXo, float wall_slave_left, float wall_slave_top) {
        //讲bean转成json，取到其中的元素数组
        // ScreenContent.BodyBean.ContentBeanX.ContentBean contentBean=new ScreenContent.BodyBean.ContentBeanX.ContentBean();
        try {
            JSONObject bigcontent = new JSONObject(JsonUitl.toJson(contentBeanXo));
            String content_yuansu = bigcontent.getString("Content");
            List<ContentBean> contentBean = gson.fromJson(content_yuansu, new TypeToken<List<ContentBean>>() {
            }.getType());
            List<CengYuansuBean> cengYuansuBeenlist = new ArrayList<CengYuansuBean>();
            LogUtil.e("fuck转过来的json", content_yuansu);
            for (int i = 0; i < contentBean.size(); i++) {
                CengYuansuBean cengYuansuBean = new CengYuansuBean();
                cengYuansuBean.setID(contentBean.get(i).getLayerItemID());
                cengYuansuBean.setName(contentBean.get(i).getName());
                cengYuansuBean.setType(contentBean.get(i).getLayerItemType());
                cengYuansuBean.setMajorID(contentBean.get(i).getMajorID());
                cengYuansuBean.setMinorID(contentBean.get(i).getMinorID());
                cengYuansuBean.setPlayOrder(contentBean.get(i).getPlayOrder());
                cengYuansuBean.setPlayTime(contentBean.get(i).getPlayTime());
                //cengYuansuBean.setValidSource();
                cengYuansuBean.setRefreshTime(contentBean.get(i).getRefreshTime());
                //cengYuansuBean.setProtoType();
                cengYuansuBean.setDescription(contentBean.get(i).getDescription());
                //cengYuansuBean.setUrlList();
                cengYuansuBean.setCascadeServerId(contentBean.get(i).getCascadeServerId());
                cengYuansuBean.setCascadeServerSourceType(contentBean.get(i).getCascadeServerSourceType());
                //cengYuansuBean.setReserved();
                cengYuansuBean.setItemThumbnialPath(contentBean.get(i).getItemThumbnailPath());
                //cengYuansuBean.setSourceTotalTime();
                cengYuansuBeenlist.add(cengYuansuBean);
            }
            //将cengYuansuBeenlist转成json数组发送请求·
            String yuansu_list_json = JsonUitl.objectToString(cengYuansuBeenlist);
            //把下面两个赋值给全局变量
            //完美需要注意的是这里都不需要乘以以比例*1920/wallwidth，在绘制时候控制好比例就可以了
            String requesturl = "{\"body\": {\n" + "\"alpha\": 1.0,\n" +
                    "\"highlight\": false,\n" +
                    "\"layerID\": " + contentBeanXo.getGuid() + ",\n" +
                    "\"layerItem\":" + yuansu_list_json + ",\"pieceXml\": \"<Layer>\\n   <Piece slaveid=\\\"" + slaveID + "\\\" slaveleft=\\\"" + wall_slave_left + "\\\" slavetop=\\\"" + wall_slave_top + "\\\" slavewidth=\\\"" + 0.5 + "\\\" slaveheight=\\\"" + 0.5 + "\\\" layerleft=\\\"0\\\" layertop=\\\"0\\\" layerwidth=\\\"1\\\" layerheight=\\\"1\\\" />\\n</Layer>\\n\",\n" +
                    "\"type\": \"Move\",\n" +
                    "\"zOrder\": " + contentBeanXo.getZorder() + "\n" +
                    "},\n" +
                    "\"guid\": \"M-45\",\n" +
                    "\"type\": \"LAYERACTION\"\n" +
                    "}";
            webSocket.send(requesturl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用Fragment的数据刷新
     */
    private void refData() {
        if (Fragment_Position == 0) {
            //这种事根据如果之前设置了tag进行寻找
          /*Fragment fragment = getSupportFragmentManager().findFragmentByTag("模式");
          Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.model_fragment);
          if (fragment != null) {
              Model_Fragment cartFragment = (Model_Fragment) fragment;
              cartFragment.refreshData();
          }*/
            /**
             *Viewpager + FragmentPagerAdapter 情况下 获取 当前显示的Fragment,只能找已经加载过的Fragment
             *注意这种智能针对FragmentPagerAdapter，FragmentStatePagerAdapter不好使
             */
            if (model_fragment == null) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPagerjingdian.getId() + ":" + Fragment_Position);
                if (fragment != null) {
                    model_fragment = (Model_Fragment) fragment;
                    model_fragment.refreshData();
                }
            } else {
                model_fragment.refreshData();
            }
        } else if (Fragment_Position == 1) {
            if (grouping_fragment == null) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPagerjingdian.getId() + ":" + Fragment_Position);
                if (fragment != null) {
                    grouping_fragment = (Grouping_Fragment) fragment;
                    grouping_fragment.refreshData();
                }
            } else {
                grouping_fragment.refreshData();
            }
        } else if (Fragment_Position == 2) {
            if (media_fragment == null) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPagerjingdian.getId() + ":" + Fragment_Position);
                if (fragment != null) {
                    media_fragment = (Media_Fragment) fragment;
                    media_fragment.refreshData();
                }
            } else {
                grouping_fragment.refreshData();
            }
        }

    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog(MainAct_xiuding.this).builder().setTitle("退出当前程序")
                    .setMsg("确定退出鼎泓导播吗？")
                    .setPositiveButton("确认退出", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent stopIntent = new Intent(MainAct_xiuding.this, MainService.class);
                            stopService(stopIntent);
                            AppManager.finishAllActivity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("取消", new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();

        }
        return false;
    }

    /**
     * dialog清空所有屏幕内容的
     */
    private void showClearScreenDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_deleteall);
        Button deleteallceng_quxiao = (Button) dialog.findViewById(R.id.deleteallceng_quxiao);
        deleteallceng_quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button deleteallceng_ok = (Button) dialog.findViewById(R.id.deleteallceng_ok);
        deleteallceng_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除所有的层接口----https://www.showdoc.cc/2452?page_id=11259
                String request_deleteall_ceng = "    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-213\",\n" +
                        "       \"type\" : \"CLEARSCREENCONTENT\"\n" +
                        "    }";
                webSocket.send(request_deleteall_ceng);
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.x = 0;
        lp.y = 0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width1 = dm.widthPixels;
        int height1 = dm.heightPixels;
        lp.width = (int) (0.55 * width1);
        lp.height = (int) (0.25 * height1);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * dialog全屏选中的层
     */
    private void showQuanpingcheckDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_fixwall);
        Window dialogWindow = dialog.getWindow();
        Button quanpingcheeckceng_quxiao = (Button) dialog.findViewById(R.id.quanpingceng_quxiao);
        quanpingcheeckceng_quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button quanpingcheck_ok = (Button) dialog.findViewById(R.id.quanpingceng_ok);
        quanpingcheck_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //全屏该层
                String request_quanping_ceng = "{\"body\": {\n" + "\"alpha\": 1.0,\n" +
                        "\"highlight\": false,\n" +
                        "\"layerID\": " + quan_One_contentbean.getGuid() + ",\n" +
                        "\"layerItem\":" + quanju_yuansu_list_json + ",\"pieceXml\": \"<Layer>\\n   <Piece slaveid=\\\"" + slaveID + "\\\" slaveleft=\\\"" + 0 + "\\\" slavetop=\\\"" + 0 + "\\\" slavewidth=\\\"" + 1 + "\\\" slaveheight=\\\"" + 1 + "\\\" layerleft=\\\"0\\\" layertop=\\\"0\\\" layerwidth=\\\"1\\\" layerheight=\\\"1\\\" />\\n</Layer>\\n\",\n" +
                        "\"type\": \"Move\",\n" +
                        "\"zOrder\": " + quan_One_contentbean.getZorder() + "\n" +
                        "},\n" +
                        "\"guid\": \"M-45\",\n" +
                        "\"type\": \"LAYERACTION\"\n" +
                        "}";
                webSocket.send(request_quanping_ceng);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.x = 0;
        lp.y = 0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width1 = dm.widthPixels;
        int height1 = dm.heightPixels;
        lp.width = (int) (0.55 * width1);
        lp.height = (int) (0.25 * height1);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * dialog保存模式的弹出方法
     */
    private void showSaveDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_saveprogram);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.x = 0;
        lp.y = 0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width1 = dm.widthPixels;
        int height1 = dm.heightPixels;
        lp.width = (int) (0.65 * width1);
        lp.height = (int) (0.3 * height1);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * dialog删除选中的层
     */
    private void showDeletecheckDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_delete);
        Window dialogWindow = dialog.getWindow();
        Button deletecheeckceng_quxiao = (Button) dialog.findViewById(R.id.deletecheckceng_quxiao);
        deletecheeckceng_quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button deletecheck_ok = (Button) dialog.findViewById(R.id.deletecheckceng_ok);
        deletecheck_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除该层
                String request_delete_ceng = "{\"body\": {\n" + "\"alpha\": 1.0,\n" +
                        "\"highlight\": false,\n" +
                        "\"layerID\": " + quan_One_contentbean.getGuid() + ",\n" +
                        "\"layerItem\":" + quanju_yuansu_list_json + ",\"pieceXml\": \"<Layer>\\n   <Piece slaveid=\\\"207\\\" slaveleft=\\\"0.6\\\" slavetop=\\\"0\\\" slavewidth=\\\"0.4\\\" slaveheight=\\\"0.948148\\\" layerleft=\\\"0\\\" layertop=\\\"0\\\" layerwidth=\\\"1\\\" layerheight=\\\"1\\\" />\\n</Layer>\\n\",\n" +
                        "\"type\": \"Delete\",\n" +
                        "\"zOrder\": " + quan_One_contentbean.getZorder() + "\n" +
                        "},\n" +
                        "\"guid\": \"M-45\",\n" +
                        "\"type\": \"LAYERACTION\"\n" +
                        "}";
                webSocket.send(request_delete_ceng);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.x = 0;
        lp.y = 0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width1 = dm.widthPixels;
        int height1 = dm.heightPixels;
        lp.width = (int) (0.55 * width1);
        lp.height = (int) (0.25 * height1);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * dialog分屏四分层
     */
    private void showCaifenDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_caifen);
        Window dialogWindow = dialog.getWindow();
        Button caifen_quxiao = (Button) dialog.findViewById(R.id.chaifen_quxiao);
        caifen_quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button chaifen_ok = (Button) dialog.findViewById(R.id.chaifen_ok);
        chaifen_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //四分屏幕
                if (ContentXlist.size() > 4) {
                    DialogUIUtils.showToastCenterLong("由于层数过多不可四分屏");
                } else if (ContentXlist.size() == 1) {
                    //一个元素
                    //手机屏幕View添加
                    Phone_Caifen_View(0, 0);
                    //拆分屏幕View的变换
                    Wall_Caifen_Move(ContentXlist.get(0), 0, 0);
                } else if (ContentXlist.size() == 2) {
                    //两个元素
                    Phone_Caifen_View(0, 0);
                    Phone_Caifen_View(0, wallwidth / 2);
                    Wall_Caifen_Move(ContentXlist.get(0), 0, 0);
                    Wall_Caifen_Move(ContentXlist.get(1), 0.5f, 0);
                } else if (ContentXlist.size() == 3) {
                    //三个元素
                    Phone_Caifen_View(0, 0);
                    Phone_Caifen_View(0, wallwidth / 2);
                    Phone_Caifen_View(wallheight / 2, 0);
                    Wall_Caifen_Move(ContentXlist.get(0), 0, 0);
                    Wall_Caifen_Move(ContentXlist.get(1), 0.5f, 0);
                    Wall_Caifen_Move(ContentXlist.get(2), 0, 0.5f);
                } else if (ContentXlist.size() == 4) {
                    //四个元素
                    Phone_Caifen_View(0, 0);
                    Phone_Caifen_View(0, wallwidth / 2);
                    Phone_Caifen_View(wallheight / 2, 0);
                    Phone_Caifen_View(wallheight / 2, wallwidth / 2);
                    Wall_Caifen_Move(ContentXlist.get(0), 0, 0);
                    Wall_Caifen_Move(ContentXlist.get(1), 0.5f, 0);
                    Wall_Caifen_Move(ContentXlist.get(2), 0, 0.5f);
                    Wall_Caifen_Move(ContentXlist.get(3), 0.5f, 0.5f);
                }
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.x = 0;
        lp.y = 0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width1 = dm.widthPixels;
        int height1 = dm.heightPixels;
        lp.width = (int) (0.55 * width1);
        lp.height = (int) (0.25 * height1);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 控制器断开的Notification
     */
    private void Notifi_Controller() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        Notification notify3 = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.starticon184)
                .setTicker("东方鼎泓:" + "您有新短消息，请注意查收！")
                .setContentTitle("鼎泓导播")
                .setContentText("Controller断开，请在电脑端查看")
                .setContentIntent(pendingIntent3).setNumber(1).build(); // 需要注意build()是在API
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        manager.notify(NOTIFICATION_FLAG, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }

    /**
     * 初始化监听事件的注册
     */
    @Override
    protected void onResume() {
        super.onResume();
        Media_Fragment.SetMediaAddListener(this);
        Model_Fragment.SetModelAddListener(this);
        Device_Fragment.SetDeviceAddListener(this);
    }

    @Override
    protected void onDestroy() {
        MainService.removeListener(this);
        super.onDestroy();
    }


}

