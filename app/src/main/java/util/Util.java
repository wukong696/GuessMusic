package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yue.gm.R;

import model.IAlertDialogButtonListener;

public class Util {

    private static AlertDialog mAlertDialog;

//动态载入界面需要用到inflater
    public static View getView(Context context,int layoutId){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId,null);//第二个参数是layout的信息（宽高等）
        return layout;
    }


    //界面跳转
    public static  void startActivity(Context context,Class destic){
        Intent intent = new Intent();
        intent.setClass(context,destic);
        context.startActivity(intent);

        //关闭当前的Activity
        ((Activity)context).finish();

    }

    //显示自定义对话框
    public static void showDialog(final Context context, String message, final IAlertDialogButtonListener listener){
        View dialogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_Transparent);

        dialogView = getView(context, R.layout.dialog_view);

        ImageButton btnOkView = (ImageButton)dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView = (ImageButton)dialogView.findViewById(R.id.btn_dialog_concel);

        TextView textMessageView = (TextView)dialogView.findViewById(R.id.text_dialog_message);

        textMessageView.setText(message);

        btnOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                if(mAlertDialog != null){
                    mAlertDialog.cancel();
                }
                //事件回调
                if(listener != null){
                    listener.onClick();
                }

                //播放点击音效
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_ENTER);

            }
        });

        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                if(mAlertDialog != null){
                    mAlertDialog.cancel();
                }

                //播放点击音效
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_CANCEL);


            }
        });

        //为dialog设置view
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        //显示对话框
        mAlertDialog.show();
    }

}
