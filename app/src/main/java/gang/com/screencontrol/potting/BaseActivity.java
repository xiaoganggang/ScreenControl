package gang.com.screencontrol.potting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import gang.com.screencontrol.R;


/**
 * Created by xiaogangzai on 2017/5/31.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private RelativeLayout contentlayout;

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //设置主题
        setTheme(R.style.AppTheme);
        setContentView(R.layout.ac_base_layout);
        //这里会用到ioc机制
        contentlayout = (RelativeLayout) findViewById(R.id.contentlayout);
        View view = getLayoutInflater().inflate(getLayoutId(), contentlayout, false);//IOC机制，控制反转，在父类中的调用子类的实现
        contentlayout.addView(view);
        //activity在跳转的时候的一个方法，进入动画和提出动画
        overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    public abstract int getLayoutId();

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }
}
