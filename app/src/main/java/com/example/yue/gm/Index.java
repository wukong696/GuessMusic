package com.example.yue.gm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import data.Const;
import util.MyApplication;
import util.MyPlayer;
import util.Util;

//目录
public class Index extends Activity {

    private ImageButton indexPlay;
    private ImageView something;
    private ImageView money;
    private ImageView quit;

    //金币View
    private TextView mViewCurrentCoins;

    //当前关的索引,因为数组的索引问题，所以初始化为-1
    private int mCurrentStageIndex = -1;
    //当前金币的数量
    private int mCurrentCoins = Const.TOTAL_COINS;
    //过关界面显示的当前关卡索引
    private TextView mCurrentStagePassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_layout);

        //读取游戏数据
        int[] datas = Util.loadData(this);
        mCurrentStageIndex = datas[Const.INDEX_LOAD_DATA_STAGE];
        mCurrentCoins = datas[Const.INDEX_LOAD_DATA_COINS];

        //右上角金币
        mViewCurrentCoins = (TextView)findViewById(R.id.txt_bar_coins);
        mViewCurrentCoins.setText(mCurrentCoins + "");

        //当前关的索引
        mCurrentStagePassView = (TextView)findViewById(R.id.index_passstage);

        if(mCurrentStagePassView != null){
            mCurrentStagePassView.setText((mCurrentStageIndex + 1) + "");
        }



        //播放按钮点击事件
        indexPlay = (ImageButton) findViewById(R.id.indexPlay);
        indexPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Index.this,MainActivity.class);
                startActivity(intent);

            }
        });

        //相关信息按钮
        something = (ImageView)findViewById(R.id.btn_something);
        something.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //播放点击音效
                MyPlayer.playTone(MyApplication.getContext(),MyPlayer.INDEX_STONE_ENTER);

                Intent intent = new Intent(Index.this,Something.class);
                startActivity(intent);
            }
        });
        //打赏按钮
        money = (ImageView)findViewById(R.id.btn_money);
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放点击音效
                MyPlayer.playTone(MyApplication.getContext(),MyPlayer.INDEX_STONE_ENTER);
                Intent intent = new Intent(Index.this,Money.class);
                startActivity(intent);
            }
        });

        quit = (ImageButton)findViewById(R.id.btn_bar_back);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}
