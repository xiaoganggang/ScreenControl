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
import gang.com.screencontrol.adapter.MessageAdapter;
import gang.com.screencontrol.bean.MessageBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Message_Fragment extends Fragment {
    public static Message_Fragment instance = null;//给代码加一个单例模式
    private RecyclerView recycler_message;
    private MessageAdapter messageadapter;
    private List<MessageBean> mdata = new ArrayList<>();

    public static Message_Fragment getInstance() {
        if (instance == null) {
            instance = new Message_Fragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_message, container, false);
        recycler_message = (RecyclerView) view.findViewById(R.id.recyle_message);
        getData();
        return view;
    }

    private void getData() {
    }

    private void showView() {
        messageadapter = new MessageAdapter(getActivity(), mdata);
        recycler_message.setAdapter(messageadapter);
        recycler_message.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recycler_message.setItemAnimator(new DefaultItemAnimator());
        recycler_message.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        messageadapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
    }
}
