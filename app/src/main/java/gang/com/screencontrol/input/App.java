package gang.com.screencontrol.input;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import gang.com.screencontrol.service.MainService;

/**
 * Created by Administrator on 2017/5/24.
 */

public class App extends Application {
    private static App instances;
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        //流氓方法，强制打开蓝牙
        applicationContext = getApplicationContext();
        //渐进式图片加载的配置
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .build();
        Fresco.initialize(this);
        Intent i = new Intent(this, MainService.class);
        this.startService(i);

    }

    //获取上下文全局变量
    public static Context getContext() {
        return applicationContext;
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    public static App getInstances() {
        return instances;
    }
}

