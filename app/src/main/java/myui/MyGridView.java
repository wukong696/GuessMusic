package myui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.yue.gm.R;

import java.util.ArrayList;


import model.IWordButtonClickListener;
import model.WordButton;
import util.Util;

public class MyGridView extends GridView {
    //MyGridView是自己定位的控件，显示待选文字
    //定义待选字数
    public final static int COUNTS_WORD = 24;

    //定义一个装字的数据容器，数据源
    private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();

    //定义一个adapter
    private MyGridAdapter mAdapter;

    //定义一个context
    private Context mContext;
    //定义动画容器
    private Animation mScaleAnimation;

    //定义接口
    private IWordButtonClickListener mWordButtonListener;

    public MyGridView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        mContext = context;

        //将adapter和view关联，因为是继承的所以直接用this
        mAdapter = new MyGridAdapter();
        this.setAdapter(mAdapter);

    }

    public void updateData(ArrayList<WordButton>list){
        mArrayList = list;

        //重新设置数据源
        setAdapter(mAdapter);
    }


    //定义adapter
    class MyGridAdapter extends BaseAdapter{
        @Override
        public int getCount(){
            //查看adapter的数据集有多少项
            return mArrayList.size();
        }

        @Override
        public Object getItem(int pos) {
            //当前选择adapter的位置（对象）相关联的数据
            return mArrayList.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            //获取列表中指定位置的行ID
            return pos;
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            //获取一个view，并且将对应位置的数据显示在上面
            //1.当前选择项。2.对应的view 3.view所在的组
            final WordButton holder;

            if(convertView == null){//判断是否创建了

                convertView = Util.getView(mContext, R.layout.self_ui_gridview_item);

                holder = mArrayList.get(pos);

                mScaleAnimation = AnimationUtils.loadAnimation(mContext,R.anim.scale);//获取动画

                //设置动画延迟时间
                mScaleAnimation.setStartOffset(pos * 50);

                holder.mindex = pos;

                if(holder.mViewButton== null) {
                    holder.mViewButton = (Button) convertView.findViewById(R.id.item_btn);

                    //设置点击事件
                    holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mWordButtonListener.onWordButtonClick(holder);
                        }
                    });
                }
                convertView.setTag(holder);
            }else {
                holder = (WordButton)convertView.getTag();

            }
            holder.mViewButton.setText(holder.mWordString);//设置显示文字
            convertView.startAnimation(mScaleAnimation);//设置动画
            return convertView;


        }
    }


    //注册接口
    public void registOnWordButtonClick(IWordButtonClickListener listener){
        mWordButtonListener = listener;
    }
}
