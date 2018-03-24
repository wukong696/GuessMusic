package model;

import android.widget.Button;


//定义每一个下文字按钮
public class WordButton {
    public int mindex; //定义按钮索引
    public boolean mIsVisiable; // 定义按钮可见性
    public String mWordString; // 定位按钮显示文字

    public Button mViewButton;//按钮显示

    public WordButton(){
        //初始化相关设置
        mIsVisiable = true;
        mWordString = "";
    }
}
