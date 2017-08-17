package gang.com.screencontrol.defineview;

import java.io.IOException;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import gang.com.screencontrol.input.App;

/**
 * 获取UI的基本信息
 * @author Administrator
 *
 */
public class UiManager {
	
	 /**
     * UI设计基准宽度（竖屏）
     */
    private static final int UI_BASE_W = 720;

    /**
     * UI设计基准高度（竖屏）
     */
    private static final int UI_BASE_H = 1280;
    
    /**
     * 设置圆角矩形的圆角半径为正圆
     */
    public static final int ROUND_CORNERSIZE_ROUND = -1;
  
    /**
     * 实际屏幕宽度
     */
    public static int screenWidth;
    /**
     * 实际屏幕高度
     */
    public static int screenHeight;
    /**
     * 实际屏幕的像素密度
     */
    public static int screenDpi;
        
    /**
     * UI设计映射到实际屏幕时横向拉伸比例
     */
    private static float uiScaleX;
    
    /**
     * UI设计映射到实际屏幕时纵向拉伸比例
     */
    private static float uiScaleY;
    
    /**
     * UI设计映射到实际屏幕时整体拉伸比例，uiScale=Math.min(uiScaleX,uiScaleY)
     */
    public static float uiScale;


    

    /**
     * 初始化UiManager
     */
    public static void init() {
        Context context = App.getContext();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        // XXX 假定屏幕为竖屏
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        screenDpi = metrics.densityDpi;
        
        uiScale = uiScaleX = (float) screenWidth / UI_BASE_W;
        uiScaleY = (float) screenHeight / UI_BASE_H;
        if (uiScaleY < uiScale)
            uiScale = uiScaleY;
    }
    
    /**
     * 根据全局缩放因子返回dimen值
     * 
     * @param dimen
     * @return
     */
    public static int scaledDimen(int dimen) {
        return Math.round(uiScale * dimen);
    }
    /**
     * 关闭输入法键盘
     * 
     * @param activity
     */
    public static void hideImm(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(
                        currentFocus.getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
