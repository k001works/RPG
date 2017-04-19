package jp.ne.sakura.k001_works.rpggame;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import static jp.ne.sakura.k001_works.rpggame.SoundConstracts.*;

/**
 * サウンド関連クラス
 *
 * @author K'
 */
public class Sounds {

    /** メディアプレイヤーの再生状態 */
    private int mpPlayStatus = MP_STS_INIT;
    /** 選択BGM */
    private int mpBgm = BGM_NUM_NONE;
    /** メディアプレイヤー */
    private MediaPlayer[] mediaPlayer = new MediaPlayer[BGM_NUM_MAX];
    /** サウンドプール */
    private SoundPool soundPool;
    /** SEのID情報 */
    private int[] soundId = new int[SE_NUM_MAX];

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public Sounds(Context context) {

        // バージョンがロリポップより前の場合
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            // サウンドプールの生成
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        // バージョンがロリポップ以降の場合
        } else {

            // サウンドプールの生成
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
        }

        try {

            // メディアプレイヤーの生成ループ
            for (int i = 0; i < BGM_NUM_MAX; i++) {

                // リソースIDの取得
                int resBgmID = context.getResources().getIdentifier(TAG_BGM + i, TAG_RAW, context.getPackageName());

                // メディアプレイヤーの生成
                mediaPlayer[i] = MediaPlayer.create(context, resBgmID);
                mediaPlayer[i].setLooping(true);
                mediaPlayer[i].seekTo(0);
            }

            // 初期化
            setMpPlayStatus(MP_STS_INIT);

        } catch (Exception e) {

            Log.e("Sounds", "MediaPlayer create failure.");
        }


        // サウンドを読み込みループ
        for (int i = 0; i < SE_NUM_MAX; i++) {

            // リソースIDの取得
            int resSeID = context.getResources().getIdentifier(TAG_SE + i, TAG_RAW, context.getPackageName());

            // サウンド読み込み
            soundId[i] = soundPool.load(context, resSeID, 1);
        }
    }

    /**
     * SEの再生
     *
     * @param seId 再生するSEのID
     */
    public void playSe(int seId) {

        // サウンド再生
        soundPool.play(soundId[seId], 1f, 1f, 0, 0, 1f);
    }

    /**
     * BGMの停止
     */
    public void stopBgm(int bgmId) {

        if (mediaPlayer[bgmId] == null) {
            return;
        }

        try {

            // メディアプレイヤーが再生中の場合
            if (mediaPlayer[bgmId].isPlaying()) {

                // メディアプレイヤーの停止
                mediaPlayer[bgmId].stop();
                mediaPlayer[bgmId].prepare();
                mediaPlayer[bgmId].seekTo(0);

                // 選択BGMと停止BGMが同じ場合
                if (bgmId == mpBgm) {

                    // メディアプレイヤーの再生状態、選択BGM種別を初期化
                    setMpPlayStatus(MP_STS_INIT);
                }
            }

        } catch (Exception e) {

            Log.e("stopBgm", "stop BGM failure.");
        }
    }

    /**
     * BGMの停止
     */
    public void stopBgm() {

        // メディアプレイヤーが初期化状態の場合
        if (getMpPlayStatus() == MP_STS_INIT) {
            return;
        }

        try {

            // メディアプレイヤーが再生中の場合
            if (mediaPlayer[getMpBgm()].isPlaying()) {

                // メディアプレイヤーの停止
                mediaPlayer[getMpBgm()].stop();
                mediaPlayer[getMpBgm()].prepare();
                mediaPlayer[getMpBgm()].seekTo(0);

                // メディアプレイヤーの再生状態、BGM種別を初期化
                setMpPlayStatus(MP_STS_INIT);
            }

        } catch (Exception e) {

            Log.e("stopBgm", "stop BGM failure.");
        }
    }

    /**
     * BGMの一時停止
     */
    public void pauseBgm() {

        // メディアプレイヤーが初期化状態の場合
        if (getMpPlayStatus() == MP_STS_INIT) {
            return;
        }

        try {

            // メディアプレイヤーの一時停止
            mediaPlayer[getMpBgm()].pause();

            // 再生状態を一時停止状態に更新
            setMpPlayStatus(MP_STS_PAUSE);

        } catch (Exception e) {

            Log.e("pauseBgm", "pause BGM failure.");
        }
    }

    /**
     * 全BGMの停止
     */
    public void stopBgmAll() {

        // 全BGMの停止ループ
        for (int i = 0; i < BGM_NUM_MAX; i++) {

            if (mediaPlayer[i] == null) {
                continue;
            }

            // メディアプレイヤーの停止
            stopBgm(i);
        }
    }

    /**
     * 一時停止中BGMの再開
     */
    public void replayBgm() {

        // 一時停止中でない、または選択BGMが未選択の場合終了
        if ((getMpPlayStatus() !=  MP_STS_PAUSE) || (getMpBgm() == BGM_NUM_NONE)) {
            return;
        }

        // BGM再生(優先度：高)
        playBgm(mpBgm, true);
    }

    /**
     * BGMの再生IF
     *
     * @param bgmId 再生するBGMのID
     */
    public void playBgm(int bgmId) {

        // BGM再生(優先度：低)
        playBgm(bgmId, false);
    }

    /**
     * BGMの再生
     *
     * @param bgmId 再生するBGMのID
     * @param priority 優先度
     */
    public void playBgm(int bgmId, boolean priority) {

        // すでに指定BGMが再生されている場合
        if ((this.mpBgm == bgmId) && (mediaPlayer[this.mpBgm].isPlaying())) {
            return;
        }

        try {

            // 別BGMが再生中の場合
            if ((getMpPlayStatus() > MP_STS_INIT) && (getMpBgm() != bgmId)) {

                // メディアプレイヤー停止
                stopBgm(this.mpBgm);
            }

            // BGM再開、または新規再生の場合
            if (((priority == true) && (getMpPlayStatus() == MP_STS_PAUSE)) || (getMpPlayStatus() != MP_STS_PAUSE)) {

                // メディアプレイヤーの再生
                mediaPlayer[bgmId].start();

                // メディアプレイヤーの再生状態、選択BGMを更新
                setMpPlayStatus(MP_STS_PLAY, bgmId);
            }

        } catch (Exception e) {

            Log.e("playBgm", "play BGM failure.");
        }
    }

    /**
     * BGMのメモリ解放
     */
    public void releaseBgm() {

        try {

            // 全BGMの解放
            for (int i = 0; i < BGM_NUM_MAX; i++) {

                // メディアプレイヤー再生中の場合
                if (mediaPlayer[i].isPlaying()) {

                    // メディアプレイヤーの停止
                    mediaPlayer[i].stop();
                }

                // メディアプレイヤーのメモリ解放
                mediaPlayer[i].release();
                mediaPlayer[i] = null;
            }

            // メディアプレイヤーの再生状態、選択BGMを初期化
            setMpPlayStatus(MP_STS_INIT);

        } catch (Exception e) {

            Log.e("stopBgm", "release BGM failure.");
        }
    }

    /**
     * BGM状態変更
     */
    private void setMpPlayStatus(int mpPlayStatus) {

        // 選択BGM情報を付加してBGM状態変更
        setMpPlayStatus(mpPlayStatus, BGM_NUM_NONE);
    }

    /**
     * BGM状態変更
     */
    private void setMpPlayStatus(int mpPlayStatus, int mpBgm) {

        // 変更状態が現在と同じ、または再生/一時停止時に選択BGMが未選択の場合
        if ((getMpPlayStatus() == mpPlayStatus) ||
                ((mpPlayStatus == MP_STS_PLAY) && (mpBgm == BGM_NUM_NONE)) ||
                ((mpPlayStatus == MP_STS_PAUSE) && (getMpBgm() == BGM_NUM_NONE))) {

            // 終了
            return;
        }

        // 変更状態判定
        switch (mpPlayStatus){
            case MP_STS_INIT:       // 初期化

                // BGMの再生状態更新
                this.mpPlayStatus = mpPlayStatus;

                // 選択BGMを初期化
                setMpBgm(BGM_NUM_NONE);
                break;

            case MP_STS_PLAY:       // 再生中

                // BGMの再生状態更新
                this.mpPlayStatus = mpPlayStatus;

                // 指定BGMを更新
                setMpBgm(mpBgm);
                break;

            case MP_STS_PAUSE:       // 一時停止

                // BGMの再生状態更新
                this.mpPlayStatus = mpPlayStatus;
                break;

            default:                 // 上記以外(異常)

                // エラーログ出力
                Log.e("setMpPlayStatus", "unknown MediaPlayer status: " + mpPlayStatus);
                break;
        }
    }

    /**
     * BGM状態取得
     */
    public int getMpPlayStatus() {
        return this.mpPlayStatus;
    }

    /**
     * 選択BGM取得
     */
    public int getMpBgm() {
        return this.mpBgm;
    }

    /**
     * 選択BGM設定
     */
    public void setMpBgm(int mpBgm) {
        this.mpBgm = mpBgm;
    }
}
