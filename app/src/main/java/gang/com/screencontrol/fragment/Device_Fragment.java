package gang.com.screencontrol.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gang.com.screencontrol.R;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Device_Fragment extends Fragment {
    public static Device_Fragment instance = null;

    public static Device_Fragment getInstance() {
        if (instance == null) {
            instance = new Device_Fragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_device, container, false);
        return view;
    }
}
