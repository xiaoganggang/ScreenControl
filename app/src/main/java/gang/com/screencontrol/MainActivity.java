package gang.com.screencontrol;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaopo.flying.sticker.StickerView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import gang.com.screencontrol.adapter.ExamplePagerAdapter;
import gang.com.screencontrol.bean.DeviceData;

public class MainActivity extends AppCompatActivity {


    private Unbinder unbinder;
    private static final String[] CHANNELS = new String[]{"媒体", "设备", "模式", "分组", "消息"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private List<DeviceData> DeviceDatalist = new ArrayList<>();
    private ExamplePagerAdapter mExamplePagerAdapter;
    private ViewPager mViewPager;
    private static final String TAG = "MainActivity";
    //拖拽图像相关
    private StickerView stickerView;
    //存储贴纸列表
    private ArrayList<View> mStickers;
    private LinearLayout contain_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        getDeviceDatalist();
        mExamplePagerAdapter = new ExamplePagerAdapter(DeviceDatalist, mDataList, MainActivity.this);
        mViewPager.setAdapter(mExamplePagerAdapter);
        initMagicIndicator1();
    }




    //获取下方的各种数据信息
    private void getDeviceDatalist() {
        DeviceData deviceData = new DeviceData(R.drawable.startupicon, "设备");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "设备");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "设备");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "模式");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "分组");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "分组");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "消息");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
        deviceData = new DeviceData(R.drawable.startupicon, "媒体");
        DeviceDatalist.add(deviceData);
    }

    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setTextSize(24);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                        badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);


                // set badge position
                if (index == 0) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_LEFT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                } else if (index == 1) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                } else if (index == 2) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CENTER_X, -UIUtil.dip2px(context, 3)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_BOTTOM, UIUtil.dip2px(context, 2)));
                }

                // don't cancel badge when tab selected
                badgePagerTitleView.setAutoCancelBadge(false);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#FFFFFF"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
