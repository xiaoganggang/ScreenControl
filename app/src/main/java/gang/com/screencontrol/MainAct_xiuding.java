package gang.com.screencontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.bean.DeviceBean;
import gang.com.screencontrol.fragment.Device_Fragment;
import gang.com.screencontrol.fragment.Grouping_Fragment;
import gang.com.screencontrol.fragment.Media_Fragment;
import gang.com.screencontrol.fragment.Message_Fragment;
import gang.com.screencontrol.fragment.Model_Fragment;
import gang.com.screencontrol.service.MainService;
import okhttp3.WebSocket;

public class MainAct_xiuding extends AppCompatActivity implements View.OnClickListener, MainService.MessageCallBackListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_act_xiuding);
        mViewPagerjingdian = (ViewPager) findViewById(R.id.jingdianviewpager);
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
                show_model_dialog();
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
    private void show_model_dialog() {
        final Dialog dialog_model = new Dialog(MainAct_xiuding.this, R.style.dialog);
        dialog_model.setContentView(R.layout.dialog_clockl);
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
        Button dialog_model_delete = (Button) dialog_model.findViewById(R.id.dialog_clock_add);
        dialog_model_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调，调用发送websocket接口
                WebSocket webSocket = MainService.getWebSocket();
                if (null != webSocket) {
                    MainService.setCallBackListener(MainAct_xiuding.this);
                    webSocket.send("");
                }
            }
        });
        Button dialog_close = (Button) dialog_model.findViewById(R.id.dialog_clock_close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_model.cancel();
            }
        });
        dialog_model.show();
    }

    @Override
    public void onRcvMessage(String text) {

    }
}
