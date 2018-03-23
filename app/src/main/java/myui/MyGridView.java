package myui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.yue.gm.R;

import java.util.ArrayList;


import model.WordButton;
import util.Util;

public class MyGridView extends GridView {

    //定义字数
    public final static int COUNTS_WORD = 24;

    //定义一个装字的数据容器
    private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();
    //定义一个adapter
    private MyGridAdapter mAdapter;

    //定义一个context
    private Context mContext;

    public MyGridView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        mContext = context;

        //将adapter和view关联
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
        public View getView(int pos, View v, ViewGroup p){
            //获取一个view，并且将对应位置的数据显示在上面
            //1.当前选择项。2.对应的view 3.view所在的组
            WordButton holder;

            if(v == null){//判断是否创建了
                v = Util.getView(mContext, R.layout.self_ui_gridview_item);

                holder = mArrayList.get(pos);
                holder.mindex = pos;
                holder.mViewButton = (Button)v.findViewById(R.id.item_btn);

                v.setTag(holder);
            }else {
                holder = (WordButton)v.getTag();

            }
            holder.mViewButton.setText(holder.mWordString);//设置显示文字
            return v;


        }
    }



}
