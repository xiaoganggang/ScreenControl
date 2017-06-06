package gang.com.screencontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.bean.MediaBean_childdetial;
import gang.com.screencontrol.fragment.Device_Fragment;
import gang.com.screencontrol.fragment.Grouping_Fragment;
import gang.com.screencontrol.fragment.Media_Fragment;
import gang.com.screencontrol.fragment.Message_Fragment;
import gang.com.screencontrol.fragment.Model_Fragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import gang.com.screencontrol.util.ToastUtil;
import okhttp3.WebSocket;

public class MainAct_xiuding extends AppCompatActivity implements View.OnClickListener, MainService.MessageCallBackListener, Media_Fragment.MediaAddCallBackListener {
    /**
     * 代表选项卡下的下划线的imageview
     */
    private ImageView cursor = null;
    /**
     * 选项卡下划线长度
     */
    private static int lineWidth = 0;

    /**
     * 偏移量
     * （手机屏幕宽度/3-选项卡长度）/2
     */
    private static int offset = 0;

    /**
     * 选项卡总数
     */
    private static final int TAB_COUNT = 5;
    /**
     * 当前显示的选项卡位置
     */
    private int current_index = 0;

    /**
     * 选项卡标题
     */
    private TextView model, group, media, device, message;

    private ViewPager mViewPagerjingdian;
    private List<Fragment> mFragmentsjidngdian;
    private FragmentPagerAdapter mAdapterJingdian;
    private LinearLayout linearLayout_caidan;
    private ImageView clock, weather, refresh;
    private Gson gson = new Gson();
    private TextView jiekouhuidiao;
    private Media_Fragment.MediaAddCallBackListener mediaAddCallBackListener;
    private StickerView stickerView;
    private static final  String TAG="MainAct_xiuding";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_act_xiuding);
        jiekouhuidiao = (TextView) findViewById(R.id.jiekouhuidiao);
        mViewPagerjingdian = (ViewPager) findViewById(R.id.jingdianviewpager);
        stickerView = (StickerView) findViewById(R.id.sticker_view);
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        initImageView();
        initView();
        final TextView[] titles = {model, group, media, device, message};
        mFragmentsjidngdian = new ArrayList<Fragment>();
        //这就是单例模式的好处，不用多次声明对象
        mFragmentsjidngdian.add(Model_Fragment.getInstance());
        mFragmentsjidngdian.add(Grouping_Fragment.getInstance());
        mFragmentsjidngdian.add(Media_Fragment.getInstance());
        mFragmentsjidngdian.add(Device_Fragment.getInstance());
        mFragmentsjidngdian.add(Message_Fragment.getInstance());

        mAdapterJingdian = new FragmentPagerAdapter(getSupportFragmentManager()) {

            public int getCount() {
                return mFragmentsjidngdian.size();
            }

            public Fragment getItem(int position) {
                return mFragmentsjidngdian.get(position);
            }
        };
        mViewPagerjingdian.setAdapter(mAdapterJingdian);
        mViewPagerjingdian.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int one = offset * 5 + lineWidth;// 页卡1 -> 页卡2 偏移量

            @Override
            public void onPageSelected(int index)//设置标题的颜色以及下划线的移动效果
            {
                Animation animation = new TranslateAnimation(one * current_index, one * index, 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                cursor.startAnimation(animation);
                titles[current_index].setTextColor(Color.parseColor("#2c2c2c"));
                titles[index].setTextColor(Color.parseColor("#0195ff"));
                current_index = index;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int index) {
            }
        });
        mViewPagerjingdian.setCurrentItem(0);
    }

    private void initView() {
        model = (TextView) findViewById(R.id.model);
        model.setOnClickListener(this);
        group = (TextView) findViewById(R.id.group);
        group.setOnClickListener(this);
        media = (TextView) findViewById(R.id.media);
        media.setOnClickListener(this);
        device = (TextView) findViewById(R.id.device);
        device.setOnClickListener(this);
        message = (TextView) findViewById(R.id.message);
        message.setOnClickListener(this);
        clock = (ImageView) findViewById(R.id.clock);
        clock.setOnClickListener(this);
        weather = (ImageView) findViewById(R.id.weather);
        weather.setOnClickListener(this);
    }

    private void initImageView() {
        cursor = (ImageView) findViewById(R.id.liaotianline);
        linearLayout_caidan = (LinearLayout) findViewById(R.id.linearLayout_caidan);
        //获取图片宽度
        lineWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.line).getWidth();
        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取屏幕宽度
         int screenWidth = dm.widthPixels;*/
        linearLayout_caidan.measure(0, 0);
        //获取组件的宽度
        int screenWidth = linearLayout_caidan.getMeasuredWidth();
        //获取组件的高度
        int height = linearLayout_caidan.getMeasuredHeight();

        Matrix matrix = new Matrix();
        offset = (int) ((screenWidth / (float) TAB_COUNT - 100) / 5);
        matrix.postTranslate(offset, 0);
        //设置初始位置
        cursor.setImageMatrix(matrix);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.model:
                setSelect(0);
                break;
            case R.id.group:
                setSelect(1);
                break;
            case R.id.media:
                setSelect(2);
                break;
            case R.id.device:
                setSelect(3);
                break;
            case R.id.message:
                setSelect(4);
                break;
            case R.id.clock:
                show_clock_dialog();
                break;
            case R.id.weather:
                show_weather_dialog();
                break;
            default:
                break;
        }
    }

    private void setSelect(int i) {
        setTab(i);
        mViewPagerjingdian.setCurrentItem(i);
    }

    @SuppressLint("NewApi")
    private void setTab(int i) {
        resetImgs();
        // 设置图片为亮色
        // 切换内容区域
        switch (i) {
            case 0:
                model.setAlpha(0.5f);
                break;
            case 1:
                group.setAlpha(0.5f);
                break;
            case 2:
                media.setAlpha(0.5f);
                break;
            case 3:
                device.setAlpha(0.5f);
                break;
            case 4:
                message.setAlpha(0.5f);
                break;
        }
    }

    /**
     * 切换图片至暗色
     */
    @SuppressLint("NewApi")
    private void resetImgs() {
        model.setAlpha(1f);
        group.setAlpha(1f);
        media.setAlpha(1f);
        device.setAlpha(1f);
        message.setAlpha(1f);
    }

    //显示dialog，是否添加闹钟
    private void show_clock_dialog() {
        final Dialog dialog_clock = new Dialog(MainAct_xiuding.this, R.style.dialog);
        dialog_clock.setContentView(R.layout.dialog_clockl);
        Button dialog_model_add = (Button) dialog_clock.findViewById(R.id.dialog_clock_add);
        dialog_model_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调，调用发送websocket接口
                WebSocket webSocket = MainService.getWebSocket();
                if (null != webSocket) {
                    MainService.setCallBackListener(MainAct_xiuding.this);
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
                //接口回调，调用发送websocket接口
                WebSocket webSocket = MainService.getWebSocket();
                if (null != webSocket) {
                    MainService.setCallBackListener(MainAct_xiuding.this);
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
        LogUtil.d("hehe", text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("ADDWIDGET") && allmodelobject.getString("guid").equals("M-117")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        ToastUtil.show(MainAct_xiuding.this, "时钟添加成功");
                    } else if (allmodelobject.getString("type").equals("ADDWIDGET") && allmodelobject.getString("guid").equals("M-31")) {
                        ToastUtil.show(MainAct_xiuding.this, "天气添加成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 媒体添加接口回调的方法重写
     * @param mediaBean_childdetial
     */
    @Override
    public void OnAddMediaView(MediaBean_childdetial mediaBean_childdetial) {
        jiekouhuidiao.setText(mediaBean_childdetial.getFileName());
        //同时在这里执行view添加到StickView的操作
        if (mediaBean_childdetial.getFolderId()==11)
        {
            Drawable drawable =
                    ContextCompat.getDrawable(this, R.mipmap.video);
            final DrawableSticker sticker = new DrawableSticker(drawable);
            stickerView.addSticker(sticker);
        }else if (mediaBean_childdetial.getFolderId()==12)
        {
            Drawable drawable =
                    ContextCompat.getDrawable(this, R.mipmap.image);
            final DrawableSticker sticker = new DrawableSticker(drawable);
            stickerView.addSticker(sticker);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Media_Fragment.SetMediaAddListener(this);
    }
}
