package gang.com.screencontrol;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gang.com.screencontrol.adapter.ViewPagerAdapter;
import gang.com.screencontrol.bean.LoadVideoWallInfo_Bean;
import gang.com.screencontrol.bean.MediaBean_childdetial;
import gang.com.screencontrol.bean.MobelBean;
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
 */
public class MainAct_xiuding extends AppCompatActivity implements View.OnClickListener, MainService.MessageCallBackListener, Media_Fragment.MediaAddCallBackListener, Model_Fragment.ModelAddCallBackListener {
    @BindView(R.id.lastmodel)
    ImageView lastmodel;
    @BindView(R.id.nextmodel)
    ImageView nextmodel;
    @BindView(R.id.close_software)
    ImageView closeSoftware;
    @BindView(R.id.huixianceshi)
    ImageView huixianceshi;


    /**
     * 选项卡标题
     */
    private ImageView pause_player;
    private ViewPager mViewPagerjingdian;
    private LinearLayout linearLayout_caidan;
    private ImageView clock, weather, refresh;
    private WebSocket webSocket;
    private Gson gson = new Gson();
    private TextView jiekouhuidiao;
    private StickerView stickerView;
    private static final String TAG = "MainAct_xiuding";
    private ImageView startlock;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_act_xiuding);
        ButterKnife.bind(this);
        AppManager.addActivity(this);
        jiekouhuidiao = (TextView) findViewById(R.id.jiekouhuidiao);
        mViewPagerjingdian = (ViewPager) findViewById(R.id.jingdianviewpager);
        stickerView = (StickerView) findViewById(R.id.sticker_view);
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        stickerView.measure(w, h);
        int height = stickerView.getMeasuredHeight();
        int width = stickerView.getMeasuredWidth();
        System.out.println("measure width=" + width + " height=" + height);
        initWebsocket();
        //加载显示墙信息指令发送
        loadWallInfo();
        initView();
        initViewpage();
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
        startlock = (ImageView) findViewById(R.id.startlock);
        startlock.setOnClickListener(this);
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
            //增加模式到播放列表
            case R.id.startlock:
                ToastUtil.show(MainAct_xiuding.this, "点击了");
                if (LOCKSTATE == 0) {
                    LOCKSTATE = 1;
                } else {
                    LOCKSTATE = 0;
                }
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
                     * 如果监听到了显示窗回显的字段
                     * 接口地址如下http://www.showdoc.cc/2452?page_id=13041     MAXWALLSNAPSHOT
                     */
                    if (allmodelobject.getString("type").equals("MAXWALLSNAPSHOT")) {
                       ToastUtil.show(MainAct_xiuding.this,"有回显啊");
                        //huixianceshi.setImageResource(R.mipmap.xianshiqiang);
                        LogUtil.d("!!!!!!!!!!!!!!MAXWALLSNAPSHOT", text);
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        //获取base64编码的数据
                        basicInfoboj.getString("imageDataStr");
                        LogUtil.d("图像字符内容", basicInfoboj.getString("imageDataStr").toString());
                        //将图片字符串数据通过base64的decode解码
                        byte[] decode = Base64.decode(basicInfoboj.getString("imageDataStr").toString(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        //save to image on sdcard
                        saveBitmap(bitmap);
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
                        LogUtil.d("!!!!!!!!!!!!!!LOADVIDEOWALLINFOLOADVIDEOWALLINFO", text);
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        //basicInfoboj.getString("slaveInfo");
                        JSONArray slaveInfoJsonArray = basicInfoboj.getJSONArray("slaveInfo");
                        JSONObject slaveInfoJsonObject = slaveInfoJsonArray.getJSONObject(0);
                        JSONArray monitorInfoJsonArray = slaveInfoJsonObject.getJSONArray("monitorInfo");
                        LogUtil.d("!!!!!!!!!!!!!!xiaopingmu", monitorInfoJsonArray.toString());
                        //将Array数据转换成List
                        List<LoadVideoWallInfo_Bean.BodyBean.SlaveInfoBean.MonitorInfoBean> MonitorInfolist = gson.fromJson(basicInfoboj.getString("basicInfo"), new TypeToken<List<LoadVideoWallInfo_Bean.BodyBean.SlaveInfoBean.MonitorInfoBean>>() {
                        }.getType());
                        if (MonitorInfolist.size() == 0) {
                            //墙配置信息为0，显示墙显示内容为空

                        } else if (MonitorInfolist.size() == 1) {
                            //一个墙配置，也就是显示一个显示器
                            ViewGroup group = (ViewGroup) findViewById(R.id.sticker_view); //获取原来的布局容器
                            ImageView imageView = new ImageView(MainAct_xiuding.this);  //创建imageview
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //image的布局方式
                            imageView.setImageResource(R.mipmap.xianshiqiang);  //设置imageview呈现的图片
                            group.addView(imageView);  //添加到布局容器中，显示图片。
                        } else if (MonitorInfolist.size() > 1) {
                            //一个墙配置，也就是显示一个显示器
                            ViewGroup group = (ViewGroup) findViewById(R.id.sticker_view); //获取原来的布局容器
                            ImageView imageView = new ImageView(MainAct_xiuding.this);  //创建imageview
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //image的布局方式
                            imageView.setImageResource(R.mipmap.xianshiqiang);  //设置imageview呈现的图片
                            group.addView(imageView);  //添加到布局容器中，显示图片。
                        }
                       /* for (int i = 0; i <= MonitorInfolist.size(); i++) {
                            MonitorInfolist.get(i).getWidth();
                        }*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 将解码的图片保存到SD卡
     *
     * @param bitmap
     */
    private void saveBitmap(Bitmap bitmap) {
        huixianceshi.setImageBitmap(bitmap);
        /*try {
            String path = Environment.getExternalStorageDirectory().getPath()
                    +"/decodeImage.jpg";
            Log.d("linc","path is "+path);
            OutputStream stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.close();
            Log.e("linc","jpg okay!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("linc","failed: "+e.getMessage());
        }*/
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
    public void OnAddMediaView(MediaBean_childdetial mediaBean_childdetial) {
        jiekouhuidiao.setText(mediaBean_childdetial.getFileName());
        //同时在这里执行view添加到StickView的操作
        if (mediaBean_childdetial.getFolderId() == 11) {
            Drawable drawable =
                    ContextCompat.getDrawable(this, R.mipmap.video);
            final DrawableSticker sticker = new DrawableSticker(drawable);
            stickerView.addSticker(sticker);
        } else if (mediaBean_childdetial.getFolderId() == 12) {
            Drawable drawable =
                    ContextCompat.getDrawable(this, R.mipmap.image);
            final DrawableSticker sticker = new DrawableSticker(drawable);
            stickerView.addSticker(sticker);
        }

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


    @OnClick({R.id.lastmodel, R.id.nextmodel, R.id.close_software})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lastmodel:
                ToastUtil.show(MainAct_xiuding.this, "播放上一个模式");
                webSocket.send("    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-172\",\n" +
                        "       \"type\" : \"PREVIOUSPROGRAM\"\n" +
                        "    }");
                break;
            case R.id.nextmodel:
                ToastUtil.show(MainAct_xiuding.this, "播放下一个模式");
                webSocket.send("    {\n" +
                        "       \"body\" : \"\",\n" +
                        "       \"guid\" : \"M-171\",\n" +
                        "       \"type\" : \"NEXTPROGRAM\"\n" +
                        "    }");
                break;
            case R.id.close_software:
                //关闭软件，关闭所有Activity
                AppManager.finishAllActivity();
                break;
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
