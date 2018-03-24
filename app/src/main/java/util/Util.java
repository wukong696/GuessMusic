package util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Util {
//动态载入界面需要用到inflater
    public static View getView(Context context,int layoutId){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId,null);//第二个参数是layout的信息（宽高等）
        return layout;
    }

}
