package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yue.gm.R;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import data.Const;
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


    //游戏数据保存
    public static void saveData(Context context,int stageIndex,int coins){
        FileOutputStream fis = null;
        try {
            fis = context.openFileOutput(Const.FILE_NAME_SAVE_DATA,Context.MODE_PRIVATE);//文件名和覆盖方式,节点流

            DataOutputStream dos = new DataOutputStream(fis);//处理流
            dos.writeInt(stageIndex);
            dos.writeInt(coins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            //关闭流
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //游戏数据读取操作
    public static int[] loadData(Context context){
        FileInputStream fis = null;
        int[] datas = {-1,Const.TOTAL_COINS};//首次进入游戏

        try {
           fis = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dis = new DataInputStream(fis);

            datas[Const.INDEX_LOAD_DATA_STAGE] = dis.readInt();
            datas[Const.INDEX_LOAD_DATA_COINS] = dis.readInt();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }return datas;
    }



}
