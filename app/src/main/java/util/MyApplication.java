package util;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    //用于获取全局context
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
