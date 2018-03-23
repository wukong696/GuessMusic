package com.example.yue.gm;

import android.app.Activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

public class MainActivity extends Activity {

    //唱片动画定义
    private Animation mPanAnim;//定义动画
    private LinearInterpolator mPanLin;//定义动画线性速度
    //拨杆收回动画定义
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;
    //拨杆弹出动画定义
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化唱片动画
        mPanAnim = AnimationUtils.loadAnimation(this,R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);

        mBarOutAnim = AnimationUtils.loadAnimation(this,R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);

        mBarInAnim = AnimationUtils.loadAnimation(this,R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mBarInLin);
    }
}
