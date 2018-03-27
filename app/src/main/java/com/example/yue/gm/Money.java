package com.example.yue.gm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class Money extends Activity {
    private ImageButton back;
    private FrameLayout coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_layout);

        back = (ImageButton)findViewById(R.id.btn_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        coin = (FrameLayout)findViewById(R.id.layout_bar_coin);
        coin.setVisibility(View.GONE);

    }
}
