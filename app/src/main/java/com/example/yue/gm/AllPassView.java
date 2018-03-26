package com.example.yue.gm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

public class AllPassView extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.all_pass_view);

        //隐藏头上就爱哦的金币按钮
        FrameLayout view = (FrameLayout)findViewById(R.id.layout_bar_coin);
        view.setVisibility(view.INVISIBLE);
    }
}
