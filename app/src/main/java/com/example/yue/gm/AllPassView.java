package com.example.yue.gm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import data.Const;
import util.MyApplication;
import util.Util;

public class AllPassView extends Activity{

    private ImageButton weixin;

    private ImageButton passmoney;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.all_pass_view);

        //隐藏金币按钮
        FrameLayout view = (FrameLayout)findViewById(R.id.layout_bar_coin);
        view.setVisibility(view.INVISIBLE);

        weixin = (ImageButton)findViewById(R.id.btn_restet);
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(),"微信：",Toast.LENGTH_SHORT).show();
            }
        });

        passmoney = (ImageButton)findViewById(R.id.pass_money);
        passmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllPassView.this,Money.class);
                startActivity(intent);
            }
        });




    }
}
