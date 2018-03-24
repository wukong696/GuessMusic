package model;

public class Song {
    //歌曲名称
    private String mSongName;

    //歌曲文件名
    private String mSongFileName;

    //歌曲名字长度
    private int mNameLength;

    //将革命转换为一个一个单字
    public char[] getNameCharacters(){
        return  mSongName.toCharArray();
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String SongName) {
        this.mSongName = SongName;
        this.mNameLength = SongName.length();//获取歌名长度
    }

    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String SongFileName) {
        this.mSongFileName = SongFileName;
    }

    public int getNameLength() {
        return mNameLength;
    }
}
