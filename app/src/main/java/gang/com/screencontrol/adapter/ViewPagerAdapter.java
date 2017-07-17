package gang.com.screencontrol.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.bean.FragmentInfo;
import gang.com.screencontrol.fragment.Device_Fragment;
import gang.com.screencontrol.fragment.Grouping_Fragment;
import gang.com.screencontrol.fragment.Media_Fragment;
import gang.com.screencontrol.fragment.Message_Fragment;
import gang.com.screencontrol.fragment.Model_Fragment;
import gang.com.screencontrol.util.LogUtil;

import static java.lang.Class.forName;


/**
 * Created by xiaogangzai on 2017/7/10.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private List<FragmentInfo> mFragments = new ArrayList<>(4);


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    private void initFragments() {
        mFragments.add(new FragmentInfo("模式", Model_Fragment.class));
        mFragments.add(new FragmentInfo("分组", Grouping_Fragment.class));
        mFragments.add(new FragmentInfo("媒体", Media_Fragment.class));
        mFragments.add(new FragmentInfo("设备", Device_Fragment.class));
        mFragments.add(new FragmentInfo("信息", Message_Fragment.class));
    }

    @Override
    public Fragment getItem(int position) {


        try {
            return (Fragment) mFragments.get(position).getFragment().newInstance();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

    //Remove a page for the given position. The adapter is responsible for removing the view from its container
    //防止重新销毁视图的方法，来回滑动销毁fragment
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }
}