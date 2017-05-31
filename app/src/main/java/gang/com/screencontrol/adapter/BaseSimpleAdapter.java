package gang.com.screencontrol.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/5/22.
 */

public abstract class BaseSimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {
    public BaseSimpleAdapter(Context context, List<T> dates, int mLayoutResId) {
        // super();和this();都是构造函数的第一行
        //this指的是本类的，super指的是父类的。反正就是this是本类的，super是父类的
        super(context, dates, mLayoutResId);
    }



    /**
     * 抽象方法可以实现可以不实现，不实现需要把它定义为抽象类，油它的一个子类来实现
     */
    /*@Override
    public void bindData(BaseViewHolder viewHolder, T t) {

    }*/
}
