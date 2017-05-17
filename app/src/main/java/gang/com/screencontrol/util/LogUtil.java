package gang.com.screencontrol.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/21.
 * 日志管理工具类
 */

public class LogUtil {
    private static boolean isOpen = true;

    //注意日志开发完成之后一定要关闭，因为打印到log中的内容都会暴露出来，抓包也能被抓出去，如果有服务器地址就危险了，对服务器多次发送请求会受到攻击
    //请求连接地址测试
    //广告那种最致命，所以最后在使用日志打印之后，一定要将isOpen设置为false，不然很危险
    //info
    public static void i(String tag, String msg) {
        if (isOpen) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isOpen) {
            Log.d(tag, msg);
        }
    }
}
