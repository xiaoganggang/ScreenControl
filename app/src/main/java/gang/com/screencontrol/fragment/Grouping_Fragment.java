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

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.GroupAdapter;
import gang.com.screencontrol.bean.GroupBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Grouping_Fragment extends Fragment {
    public static Grouping_Fragment instance = null;
    private RecyclerView recyler_group;
    private GroupAdapter groupAdapter;
    private List<GroupBean> list_group = new ArrayList<>();

    public static Grouping_Fragment getInstance() {
        if (instance == null) {
            instance = new Grouping_Fragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_group, container, false);
        recyler_group = (RecyclerView) view.findViewById(R.id.recyle_group);
        getData();
        return view;
    }

    //网络请求数据
    private void getData() {
    }

    private void show_group() {
        groupAdapter = new GroupAdapter(getActivity(), list_group);
        recyler_group.setAdapter(groupAdapter);
        recyler_group.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyler_group.setItemAnimator(new DefaultItemAnimator());
        recyler_group.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        groupAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
    }
}
