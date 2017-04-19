package jp.ne.sakura.k001_works.rpggame;

/**
 * サウンド定数クラス
 *
 * @author K'
 */
public class SoundConstracts {

    /**
     * コンストラクタ
     */
    private SoundConstracts() {}

    // サウンド関連定数
    public final static String TAG_BGM = "bgm";
    public final static String TAG_SE = "se";
    public final static String TAG_RAW = "raw";

    /** SEの最大数 */
    public final static int SE_NUM_MAX = 6;
    /** BGMの最大数 */
    public final static int BGM_NUM_MAX = 6;

    // 再生状態定数
    /** 初期状態 */
    public final static int MP_STS_INIT = -1;
    /** 再生状態 */
    public final static int MP_STS_PLAY = 0;
    /** 一時停止状態 */
    public final static int MP_STS_PAUSE = 1;

    // SE関連の定数
    /** 勇者の攻撃 */
    public final static int SE_NUM_ATACK_YU = 0;
    /** 逃亡 */
    public final static int SE_NUM_ESCAPE = 1;
    /** 全回復 */
    public final static int SE_NUM_RECOVER = 2;
    /** 敵(スライム)の攻撃 */
    public final static int SE_NUM_ATACK_EN = 3;
    /** 敵(ボス)の攻撃 */
    public final static int SE_NUM_ATACK_BOSS = 4;
    /** レベルアップ */
    public final static int SE_NUM_LEVELUP = 5;

    // BGM関連の定数
    /** 未選択 */
    public final static int BGM_NUM_NONE = -1;
    /** MAP */
    public final static int BGM_NUM_MAP = 0;
    /** タイトル */
    public final static int BGM_NUM_TITLE = 1;
    /** ゲームオーバー */
    public final static int BGM_NUM_GAMEOVER = 2;
    /** 戦闘(スライム) */
    public final static int BGM_NUM_BATTLE_EN = 3;
    /** 戦闘(ボス) */
    public final static int BGM_NUM_BATTLE_BOSS = 4;
    /** エンディング */
    public final static int BGM_NUM_ENDING = 5;

}
