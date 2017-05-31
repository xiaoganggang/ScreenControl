package gang.com.screencontrol.okhttp;

import android.content.Context;


import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiaogangzai on 2017/5/26.
 */

public abstract class SpotsCallBack<T> extends BaseCallBack<T> {
    /**
     * 继承时，如果父类是抽象类，子类是抽象类，必须实现父类的所有抽象方法，如果子类是抽象类，可以不完全实现父类的抽象方法
     */
    SpotsDialog dialog;

    public SpotsCallBack(Context context) {
        dialog = new SpotsDialog(context);
    }

    public void showDialog() {
        dialog.show();
    }

    public void setMessage(String message) {
        dialog.setMessage(message);
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(IOException e) {

        dismissDialog();
    }


    @Override
    public void onResponse(Response response){
        dismissDialog();


    }


}
