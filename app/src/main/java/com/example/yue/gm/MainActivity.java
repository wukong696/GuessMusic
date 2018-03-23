package com.example.yue.gm;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    //唱片动画定义
    private Animation mPanAnim;//定义动画
    private LinearInterpolator mPanLin;//定义动画线性速度
    //拨杆进唱片动画定义
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;
    //拨杆弹出动画定义
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //播放按钮事件定义
    private ImageButton mBtnPlaystart;

    //设置动画是否在播放判断
    private boolean mIsRuning = false;

    //定义唱片和拨杆的控件
    private ImageView mViewPan;
    private ImageView mViewPanbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化唱片动画
        mPanAnim = AnimationUtils.loadAnimation(this,R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);//设置线性速度
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            //设置动画监听器
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPanbar.startAnimation(mBarOutAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复

            }
        });



        //初始化拨杆In动画
        mBarInAnim = AnimationUtils.loadAnimation(this,R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //初始化拨杆弹出动画
        mBarOutAnim = AnimationUtils.loadAnimation(this,R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRuning = false;//播放完动画后，设置mIsRuning为false，之后点击播放按钮就可以执行动画
                mBtnPlaystart.setVisibility(View.VISIBLE);//播放完毕设置播放按钮可见
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnPlaystart = (ImageButton)findViewById(R.id.btn_play_start);
        mViewPan = (ImageView)findViewById(R.id.pan_1);
        mViewPanbar = (ImageView)findViewById(R.id.pin_1);

        mBtnPlaystart.setOnClickListener(new View.OnClickListener() {
            //设置播放按钮点击事件
            @Override
            public void onClick(View v) {
                hangdlePlayButton();
            }
        });
    }

    //播放动画总方法
    private void hangdlePlayButton(){
        if(mViewPanbar != null){
            if(!mIsRuning){
                mIsRuning = true;//播放后设置mIsRuning值,期间不能执行动画
                mViewPanbar.startAnimation(mBarInAnim);//播放动画
                mBtnPlaystart.setVisibility(View.INVISIBLE);//播放期间播放按钮隐藏
            }
        }

    }
}
