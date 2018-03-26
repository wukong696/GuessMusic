package com.example.yue.gm;

import android.app.Activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import data.Const;
import model.IAlertDialogButtonListener;
import model.IWordButtonClickListener;
import model.Song;
import model.WordButton;
import myui.MyGridView;
import util.MyLog;
import util.MyPlayer;
import util.Util;

public class MainActivity extends Activity  implements IWordButtonClickListener{

    public final static String TAG = "MainActivity";
    //答案状态，正确，错误，不完整
    private final static int STATUS_ANSWER_RIGHT =1;
    private final static int STATUS_ANSWER_WEONG =2;
    private final static int STATUS_ANSWER_LACK =3;
    //闪烁次数
    private final static int SPASH_TIMES = 6;
    //提示框用到的id
    public final static int ID_DIALOG_DELETE_WORD = 1;
    public final static int ID_DIALOG_TIP_ANSWER = 2;
    public final static int ID_DIALOG_LACK_COINS = 3;

    //当前金币的数量
    private int mCurrentCoins = Const.TOTAL_COINS;

    //金币View
    private TextView mViewCurrentCoins;

    //过关界面显示的当前关卡索引
    private TextView mCurrentStagePassView;

    //当前关卡索引
    private TextView mCurrentStageView;

    //当前歌曲名称
    private TextView mCUrrentSongNamePassView;

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

    //文字框容器
    private ArrayList<WordButton> mAllWords;

    //已选框文字容器
    private ArrayList<WordButton> mBtnSelectWords;

    //待选文本框
    private MyGridView mMygridView;

    //已选文本框
    private LinearLayout mViewWordsCantainer;

    //当前歌曲的对象
    private Song mCurrentSong;

    //当前关的索引,因为数组的索引问题，所以初始化为-1
    private int mCurrentStageIndex = -1;

    //过关界面
    private View mPassView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnPlaystart = (ImageButton)findViewById(R.id.btn_play_start);
        mViewPan = (ImageView)findViewById(R.id.pan_1);
        mViewPanbar = (ImageView)findViewById(R.id.pin_1);
        mMygridView = (MyGridView)findViewById(R.id.gridview);//待选文本框
        mViewWordsCantainer = (LinearLayout)findViewById(R.id.word_select_container);//已选文本框
        //右上角金币
        mViewCurrentCoins = (TextView)findViewById(R.id.txt_bar_coins);
        mViewCurrentCoins.setText(mCurrentCoins + "");
        //注册监听
        mMygridView.registOnWordButtonClick(this);


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



        mBtnPlaystart.setOnClickListener(new View.OnClickListener() {
            //设置播放按钮点击事件
            @Override
            public void onClick(View v) {
                hangdlePlayButton();
            }
        });

        //获取更新文本框数据
        initCurrentStageData();

        //删除道具
        handleDeleteWord();
        //提示道具
        handleTipAnswer();




    }

    @Override
    public void onWordButtonClick(WordButton wordButton){//点击待选框文字事件
        //Toast.makeText(this,wordButton.mindex + "",Toast.LENGTH_SHORT).show();
        setSelectWord(wordButton);

        //获得答案状态
        int checkResult = checkTheAnswer();
        //检查答案
        if(checkResult == STATUS_ANSWER_RIGHT){
            //过关并获得奖励
            handlePassEvent();
        }else if(checkResult == STATUS_ANSWER_WEONG){
            //闪烁文字并提示用户
            sparkTheWrods();
        }else if(checkResult == STATUS_ANSWER_LACK){
            //设置文字颜色为白色

        }
    }

    private void setSelectWord(WordButton wordButton){
        //设置答案
        for(int i = 0; i < mBtnSelectWords.size();i++){//遍历已选文本框
            if(mBtnSelectWords.get(i).mWordString.length() == 0){
                //判断已选文本框是否为空
                //设置答案文本框内容和可见性
                mBtnSelectWords.get(i).mViewButton.setText(wordButton.mWordString );
                mBtnSelectWords.get(i).mIsVisiable = true;
                mBtnSelectWords.get(i).mWordString = wordButton.mWordString;
                //记录索引
                mBtnSelectWords.get(i).mindex = wordButton.mindex;

                //Log.....
                MyLog.d(TAG,mBtnSelectWords.get(i).mindex +"");

                //选择之后，待选框不可见
                setButtonVisiable(wordButton,View.INVISIBLE);

                break;
            }
        }
    }
    //清除答案
    private void clearTheAnswer(WordButton wordButton){
        wordButton.mViewButton.setText("");
        wordButton.mWordString = "";
        wordButton.mIsVisiable = false;//已选框设置不可见

        //设置待选框可见性
        setButtonVisiable(mAllWords.get(wordButton.mindex),View.VISIBLE);
    }

    //设置待选文字框可见性
    private void setButtonVisiable(WordButton button,int visibility){
        button.mViewButton.setVisibility(visibility);
        button.mIsVisiable = (visibility == View.VISIBLE) ? true : false;

        //Log....
        MyLog.d(TAG,button.mIsVisiable +"");

    }

    @Override
    protected void onPause() {
        //停止动画
        mViewPan.clearAnimation();
        //暂停音乐
        MyPlayer.stopTheSong(MainActivity.this);

        super.onPause();
    }

    //播放动画总方法
    private void hangdlePlayButton(){
        if(mViewPanbar != null){
            if(!mIsRuning){
                mIsRuning = true;//播放后设置mIsRuning值,期间不能执行动画
                //开始拨杆动画
                mViewPanbar.startAnimation(mBarInAnim);//播放动画
                mBtnPlaystart.setVisibility(View.INVISIBLE);//播放期间播放按钮隐藏
                //播放音乐
                MyPlayer.playSong(MainActivity.this,mCurrentSong.getSongFileName());
            }
        }

    }

    //根据当前关卡index获取歌曲文件名和歌曲名并返回
    private Song loadStageSongInfo(int stageIndex){
        Song song = new Song();
        String[] stage = Const.SONG_INFO[stageIndex];
        song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
        song.setSongName(stage[Const.INDEX_SONG_NAME]);

        return  song;
    }


    //加载当前关卡的数据
    private void initCurrentStageData(){
        //获取一个Song实例，初始化当前关的歌曲信息
        mCurrentSong = loadStageSongInfo(++mCurrentStageIndex);

        //初始化已选文字
        mBtnSelectWords = initWordSelect();
        //清空原来的答案
        mViewWordsCantainer.removeAllViews();

        //创建LinearLayout控件用来装传入的Wordbutton控件，并且设置宽高
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(140,140);
        //增加答案框
        for (int i = 0;i <mBtnSelectWords.size();i++){
            mViewWordsCantainer.addView(mBtnSelectWords.get(i).mViewButton,params);//创建Button控件
        }

        //界面上部显示关卡当前索引
        mCurrentStageView = (TextView)findViewById(R.id.text_current_stage);
        if (mCurrentStageView != null){
            mCurrentStageView.setText((mCurrentStageIndex + 1) +"");
        }





        //从initAllword获取待选文字数据
        mAllWords = initAllword();
        //更新数据-MyGridView
        mMygridView.updateData(mAllWords);

        //播放音乐
        hangdlePlayButton();
    }


    //初始化待选文字框
    private ArrayList<WordButton> initAllword(){
        //生成所有单个字到容器并返回数据
        ArrayList<WordButton> data = new ArrayList<WordButton>();

        //获取所有待选文字
        String[] words = generateWoeds();

        for (int i = 0; i < MyGridView.COUNTS_WORD ; i++){
            WordButton button = new WordButton();
            button.mWordString = words[i];
            data.add(button);
        }
            return data;
    }

    //初始化已选文字框
    private ArrayList<WordButton> initWordSelect(){
        ArrayList<WordButton> data = new ArrayList<WordButton>();
        //获取控件，生成WordButton控件，并返回数据
        for (int i = 0; i < mCurrentSong.getNameLength();i ++){
            View view = Util.getView(MainActivity.this,R.layout.self_ui_gridview_item);

            final WordButton holder = new WordButton();

            holder.mViewButton = (Button)view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable = false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击已选框里面的文字清除文字设置不可见，并且设置其他文字颜色为白色
                    for(int i = 0;i <mBtnSelectWords.size();i++){
                        mBtnSelectWords.get(i).mViewButton.setTextColor(Color.WHITE);
                    }

                    clearTheAnswer(holder);

                }
            });


            data.add(holder);
        }
        return data;
    }

    //生成所有待选文字包括歌曲名
    private String[] generateWoeds(){

        Random random = new Random();//为了打乱顺序随机数使用
        String[] words = new String[MyGridView.COUNTS_WORD];

        //存入歌名
        for(int i  = 0; i<mCurrentSong.getNameLength(); i++){
            words[i] = mCurrentSong.getNameCharacters()[i] + "" ;//将歌曲名字字符串转为相应字符，将char类型转换为String
        }

        //除了歌名获取剩下的文字
        for (int i = mCurrentSong.getNameLength(); i<MyGridView.COUNTS_WORD; i++){
            words[i] = getRandomChar() + "";
        }

        //打乱文字在数组中的顺序,首先从所有元素中随机选取一个与第一个元素交换
        //然后在第二个之后选择一个元素与第二个交换，直到最后一个元素
        //这个算法保证每个元素在每个位置的概率都是1/n
        for(int i = MyGridView.COUNTS_WORD - 1; i >= 0 ;i--){
            int index = random.nextInt(i +1);//为了数是0-24

            String buf = words[index];
            words[index] = words [i];
            words[i] = buf;
        }



        return  words;

    }


    //生成随机汉字
    private char getRandomChar(){
        String str = "";
        int hightPos;
        int lowPos;
        //随机数
        Random random = new Random();
        //生成高低位
        hightPos = (176 +Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));
        //创建容器装高低位组成汉子
        byte[] b =new byte[2];
        //强制整形变量
        b[0] = (Integer.valueOf(hightPos).byteValue());
        b[1] = (Integer.valueOf(lowPos).byteValue());

        try {
            //生成汉字
            str = new String(b,"GBK");

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        //传char值
        return str.charAt(0);
    }


    //判断答案
    private int checkTheAnswer(){
        //先检查长度
        for(int i = 0;i <mBtnSelectWords.size();i++){
            //如果有空的，说明答案还不完整
            if (mBtnSelectWords.get(i).mWordString.length() == 0){
                return STATUS_ANSWER_LACK;
            }
        }

        //答案完整，继续检查正确性
        StringBuffer sb = new StringBuffer();//定义String变量,用于比较
        for(int i = 0;i<mBtnSelectWords.size();i++){
            sb.append(mBtnSelectWords.get(i).mWordString);
        }
        return (sb.toString().equals(mCurrentSong.getSongName()))?
                STATUS_ANSWER_RIGHT : STATUS_ANSWER_WEONG;//判断和答案是否相同

    }

    //闪烁文字
    private void  sparkTheWrods(){
        //定时器相关
         TimerTask task = new TimerTask() {

            boolean mChange = false;
            int mSpardTiems = 0;//闪烁次数
            @Override
            public void run() {
                runOnUiThread(new Runnable() {//修改ui要回到用主线程
                    @Override
                    public void run() {
                        //显示闪烁次数
                        if( ++mSpardTiems > SPASH_TIMES){

                            return;//停止闪烁，初始化
                        }
                        //执行闪烁逻辑：交替显示红色和白色文字
                        for(int i = 0;i <mBtnSelectWords.size();i++){
                            mBtnSelectWords.get(i).mViewButton.setTextColor(mChange?Color.RED : Color.WHITE);
                        }
                        mChange = !mChange;
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,1,150);
        //三个参数，1.具体任务2.什么时候开始3.持续的事件是多少
    }


    //处理过关界面和事件
    private void handlePassEvent(){
        //显示过关界面
        mPassView = (LinearLayout)findViewById(R.id.pass_view);
        mPassView.setVisibility(View.VISIBLE);


        //停止未完成的动画
        mViewPan.clearAnimation();
        //停止正在播放的音乐
        MyPlayer.stopTheSong(MainActivity.this);

        //播放金币音效
        MyPlayer.playTone(MainActivity.this,MyPlayer.INDEX_STONG_COIN);



        //当前关的索引
        mCurrentStagePassView = (TextView)findViewById(R.id.text_current_stage_pass);

        if(mCurrentStagePassView != null){
            mCurrentStagePassView.setText((mCurrentStageIndex + 1) + "");
        }

        //显示歌曲的名称
        mCUrrentSongNamePassView = (TextView)findViewById(R.id.text_current_song_pass);

        if(mCUrrentSongNamePassView != null){
            mCUrrentSongNamePassView.setText(mCurrentSong.getSongName());
        }

        //下一关按键处理
        ImageButton btnPass = (ImageButton)findViewById(R.id.btn_next);

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judegAppPassed()){
                    //进入通关界面
                    Util.startActivity(MainActivity.this,AllPassView.class);

                }else {
                    //进入下一关
                    mPassView.setVisibility(View.GONE);//隐藏通关界面

                    //加载关卡数据
                    initCurrentStageData();
                }

            }
        });

    }

    //判断是否通关
    private boolean judegAppPassed(){
        //当前关卡索引是否等于总关卡数
        return (mCurrentStageIndex  == Const.SONG_INFO.length - 1);

    }



   //自动选择一个答案
    private void tipAnswer(){


        boolean tipWord = false;
        for(int i = 0;i < mBtnSelectWords.size();i++){
            if(mBtnSelectWords.get(i).mWordString.length() == 0){
                //根据当前的答案框条件选择对应的文字并且填入
                onWordButtonClick(findIsAnswerWord(i));

                tipWord = true;

                //减少金币数量
                if(!handleCoins(-getTipCoins())){
                    //金币数量不够的时候显示对话框，提示金币数量不够
                    showConfirmDialog(ID_DIALOG_LACK_COINS);
                    return;
                }
                break;

            }
        }
        //没有找到可以填充的答案的控件
        if(!tipWord){
            //闪烁文字提示用户
            sparkTheWrods();

        }
    }


    //删除文字，应用于道具
    private void deleteOneWord(){

        //减少金币
        if (!handleCoins(-getDeleteWordCoins())){
            //金币不够，显示提示对话框
            showConfirmDialog(ID_DIALOG_LACK_COINS);
              return;
        }

        //将索引对应的按钮设置不可见
        setButtonVisiable(findNotAnswerWord(),View.INVISIBLE);
    }

    //找一个不是答案的文字，并且当前是可见的的控件
    private WordButton findNotAnswerWord(){
        Random random = new Random();
        WordButton buf = null;

        while (true){
            int index = random.nextInt(MyGridView.COUNTS_WORD);//获取随机数

            buf = mAllWords.get(index);//根据随机生成的索引把button对象取出来

            if (buf.mIsVisiable && !isTheAnswerWord(buf)){
                return buf;
            }
        }


    }

    //找到一个是答案的文字，index是当前需要填入答案的索引
    private WordButton findIsAnswerWord(int index){
        WordButton buf = null;

        for(int i = 0;i < MyGridView.COUNTS_WORD;i++){
            buf  = mAllWords.get(i);

            if (buf.mWordString.equals("" + mCurrentSong.getNameCharacters()[index])){
                return buf;
            }
        }

        return null;


    }

    //判断某个文字是否为答案
    private boolean isTheAnswerWord(WordButton word){
        boolean result = false;

        for (int i = 0;i < mCurrentSong.getNameLength(); i++){
            if(word.mWordString.equals("" + mCurrentSong.getNameCharacters()[i])){
                result = true;
                break;
            }
        }
        return result;

    }

    //增加或者减少指定数量金币，
    private boolean handleCoins(int data){
        //判断当前总金币数是否可以被减少
        if(mCurrentCoins + data >= 0){
            mCurrentCoins += data;

            mViewCurrentCoins.setText(mCurrentCoins + "");
            return true;
        }else {
            //金币不够
            return false;
        }

    }

    //获取删除待选字的金币数量
    private int getDeleteWordCoins(){
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }

    private int getTipCoins(){
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }

    //删除工具处理删除待选文字事件
    private void handleDeleteWord(){
        ImageButton button = (ImageButton)findViewById(R.id.btn_delete_word);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmDialog(ID_DIALOG_DELETE_WORD);
            }
        });
    }

    //处理提示答案事件
    private void handleTipAnswer(){

        ImageButton button = (ImageButton)findViewById(R.id.btn_tip_answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(ID_DIALOG_TIP_ANSWER);

            }
        });

    }

    //自定义AlertDIalog事件响应
    //删除错误答案道具对话框
    private IAlertDialogButtonListener mBtnOkDeleteWordListener =
            new IAlertDialogButtonListener() {
                @Override
                public void onClick() {
                    //执行
                    deleteOneWord();

                }
            };
    //答案提示道具对话框
    private IAlertDialogButtonListener mBtnOkTipAnswerListener =
            new IAlertDialogButtonListener() {
                @Override
                public void onClick() {
                    //执行
                    tipAnswer();

                }
            };
    //金币不足
    private IAlertDialogButtonListener mBtnOkLackCoinsListener =
            new IAlertDialogButtonListener() {
                @Override
                public void onClick() {
                    //执行


                }
            };

    //显示对话框方法
    private void showConfirmDialog(int id){
        switch (id){
            case ID_DIALOG_DELETE_WORD:
                Util.showDialog(MainActivity.this,"确认花掉" +getDeleteWordCoins() + "个金币去掉一个错误答案？",mBtnOkDeleteWordListener);
                break;
            case ID_DIALOG_TIP_ANSWER:
                Util.showDialog(MainActivity.this,"确认花掉" +getTipCoins() + "个金币提示一个正确答案？",mBtnOkTipAnswerListener);

                break;
            case ID_DIALOG_LACK_COINS:
                Util.showDialog(MainActivity.this,"金币不足",mBtnOkLackCoinsListener);

                break;
        }

    }


}
