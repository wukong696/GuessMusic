package util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

//音乐播放类
public class MyPlayer {
    //音效类型
    public final static int INDEX_STONE_ENTER = 0;
    public final static int INDEX_STONE_CANCEL = 1;
    public final static int INDEX_STONG_COIN =2;


    //音效文件名
    private final static String[] SONG_NAMES =
            {"enter.mp3","cancel.mp3","coin.mp3"};

    //定义音效数组
    private static MediaPlayer[] mToneMediaPlayer = new MediaPlayer[SONG_NAMES.length];
    //歌曲播放
    private static MediaPlayer mMusicMediaPlayer;


    //播放音效
    public static void playTone(Context context,int index){
        //加载声音
        AssetManager assetManager = context.getAssets();

        if(mToneMediaPlayer[index] ==  null ){
            mToneMediaPlayer[index] = new MediaPlayer();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd(SONG_NAMES[index]);
                mToneMediaPlayer[index].setDataSource(fileDescriptor.getFileDescriptor()
                        ,fileDescriptor.getStartOffset(),fileDescriptor.getLength());

                mToneMediaPlayer[index].prepare();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mToneMediaPlayer[index].start();

    }


    //播放歌曲
    public static void playSong(Context context,String fileName){
        if(mMusicMediaPlayer == null){
            mMusicMediaPlayer = new MediaPlayer();
        }
        //强制重置状态
        mMusicMediaPlayer.reset();
        //加载声音文件
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            //设置数据源
            mMusicMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor()
                    ,fileDescriptor.getStartOffset()
                    ,fileDescriptor.getLength());

            mMusicMediaPlayer.prepare();
            //声音播放
        }catch (IOException e){
            e.printStackTrace();
        }
        mMusicMediaPlayer.start();
    }


    //停止歌曲播放
    public static void stopTheSong(Context context){
        if(mMusicMediaPlayer != null){
            mMusicMediaPlayer.stop();

        }
    }

}
