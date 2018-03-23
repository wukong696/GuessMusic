package model;

import android.widget.Button;

public class WordButton {
    public int mindex; //定义按钮索引
    public boolean mIsVisiable; // 定义按钮可见性
    public String mWordString; // 定位按钮显示文字

    public Button mViewButton;

    public WordButton(){
        //初始化相关设置
        mIsVisiable = true;
        mWordString = "";
    }
}
