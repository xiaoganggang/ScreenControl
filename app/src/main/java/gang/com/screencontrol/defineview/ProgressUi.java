package gang.com.screencontrol.defineview;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import gang.com.screencontrol.R;
import gang.com.screencontrol.input.App;
import gang.com.screencontrol.util.DensityUtil;


public class ProgressUi extends Dialog{
	
	/**
	 * 图片宽度
	 */
	private static final int imageWidth=40;
	
	/**
	 * 间距
	 */
	private static final int padding=15;
	
	/**
	 * 布局文件
	 */
    int layoutRes;
    
    /**
     * ImageView
     */
    private ImageView ivImage;
    
    /**
     * TextView
     */
    private TextView tvTxt;
    
    /**
     * 展示文字内容
     */
    private String showTxt;
    
    /**
     * 设置文字内容
     * @param showTxt
     */
    public void setShowTxt(String showTxt) {
		this.showTxt = showTxt;
	}
    
    /**
     * 设置提示文字ID
     * @param id
     */
    public void setShowTxt(int id){
    	this.showTxt= App.getContext().getString(id);
    }


    
    
    /**
     * 构造函数
     */
	public ProgressUi(Activity activity) {
        super(activity, R.style.loading_dialog);
        this.layoutRes=R.layout.dialog;
        this.showTxt=App.getContext().getString(R.string.loading);
        this.setCancelable(false);// 返回键Dialog不消失,false 是返回也不消失
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失,false 是点击屏幕不消失
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.setContentView(layoutRes);
		this.setCancelable(false);// 设置点击返回键Dialog不消失,false 是点击屏幕不消失
		ivImage=(ImageView)findViewById(R.id.iv_image);		
		tvTxt=(TextView)findViewById(R.id.tv_txt);
		setAnimation();
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		if(showTxt.equals("")){
			tvTxt.setVisibility(View.GONE);
		}else{
			tvTxt.setText(showTxt);
			
			Paint paint=new Paint();
			float textWidth = paint.measureText(showTxt);
			int width= DensityUtil.dip2px(App.getContext(), imageWidth+padding*2);
			if(textWidth>40){
				//如果字体宽度大于屏幕3/5则换行显示
				if(DensityUtil.dip2px(App.getContext(),textWidth)>UiManager.screenWidth*3/5){
					width=UiManager.screenWidth*3/5;
				}else{
					width=DensityUtil.dip2px(App.getContext(),textWidth+padding*2);
				}
			}
			//设置窗体宽度
			LayoutParams params=tvTxt.getLayoutParams();
			params.width=width;
		}		
	}
	
	 // 加载动画  
	Animation  an;
	@SuppressLint("NewApi")
	public void setAnimation() {
		//ivImage.clearAnimation();
		if(an==null){
			an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);  
	        an.setInterpolator(new LinearInterpolator());//不停顿  
	        an.setRepeatCount(-1);//重复次数  
	        an.setFillAfter(true);//停在最后  
	        an.setDuration(1000);  
		}
        // 使用ImageView显示动画  
        ivImage.startAnimation(an);  
	}
	
	@Override
	public void show() {   
		if(this.isShowing()){
			this.dismiss();
		}
		super.show();		
		setAnimation();
		if(showTxt.equals("")){
			tvTxt.setVisibility(View.GONE);
		}else{
			tvTxt.setText(showTxt);
			
			Paint paint=new Paint();
			float textWidth = paint.measureText(showTxt);
			int width=DensityUtil.dip2px(App.getContext(), imageWidth+padding*2);
			if(textWidth>40){
				//如果字体宽度大于屏幕3/5则换行显示
				if(DensityUtil.dip2px(App.getContext(),textWidth)>UiManager.screenWidth*3/5){
					width=UiManager.screenWidth*3/5;
				}else{
					width=DensityUtil.dip2px(App.getContext(),textWidth+padding*2);
				}
			}
			//设置窗体宽度
			LayoutParams params=tvTxt.getLayoutParams();
			params.width=width;
		}	
	}
	
	
	@Override
	public void dismiss() {
		super.dismiss();
		if(ivImage!=null){
			ivImage.clearAnimation();
		}		
	};

}
