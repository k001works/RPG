package jp.ne.sakura.k001_works.rpggame;

/**
 * RPG定数クラス
 *
 * @author K'
 */
public class RpgConstracts {

    /**
     * コンストラクタ
     */
    private RpgConstracts() {}

    // シーン定数
    /** 初期状態 */
    public final static int S_INIT = -1;
    /** 開始時 */
    public final static int S_START = 0;
    /** マップ中 */
    public final static int S_MAP = 1;
    /** 敵出現時 */
    public final static int S_APPEAR = 2;
    /** コマンド入力待ち(戦闘中) */
    public final static int S_COMMAND = 3;
    /** 敵を攻撃時 */
    public final static int S_ATTACK = 4;
    /** 防御時(敵からの攻撃) */
    public final static int S_DEFENCE = 5;
    /** 逃亡時 */
    public final static int S_ESCAPE = 6;
    /** マップイベント */
    public final static int S_MAPEVENT = 7;
    /** ゲームクリア */
    public final static int S_GAMECLEAR = 8;
    /** ゲームオーバー */
    public final static int S_GAMEOVER = 9;

    // キー定数
    /** キー入力(未選択) */
    public final static int KEY_NONE = -1;
    /** キー入力(左) */
    public final static int KEY_LEFT = 0;
    /** キー入力(右) */
    public final static int KEY_RIGHT = 1;
    /** キー入力(上) */
    public final static int KEY_UP = 2;
    /** キー入力(下) */
    public final static int KEY_DOWN = 3;
    /** キー入力(1：攻撃) */
    public final static int KEY_1 = 4;
    /** キー入力(2：逃げる) */
    public final static int KEY_2 = 5;
    /** キー入力(決定キー入力済み) */
    public final static int KEY_SELECT = 6;

    // マップチップ関連定数
    /** 平地(移動可) */
    public final static int M_GROUND = 0;
    /** 家(移動可) */
    public final static int M_HOUSE = 1;
    /** 城(移動可) */
    public final static int M_CASTLE = 2;
    /** 木(移動不可) */
    public final static int M_TREE = 3;
    /** 移動可否判定用値 */
    public final static int M_VAL_MOVE_JUDGE = 2;
    /** マップチップ配置 */
    public final static int[][] MAP = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 1, 0, 0, 0, 0, 3, 2, 0, 3},
            {3, 0, 0, 0, 0, 0, 3, 3, 0, 3},
            {3, 3, 3, 3, 3, 0, 3, 3, 0, 3},
            {3, 0, 0, 3, 0, 0, 3, 3, 0, 3},
            {3, 3, 0, 3, 0, 3, 3, 3, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    };

    // メッセージ関連定数
    /** 敵出現メッセージ  */
    public final static String TAG_MSG_APPEAR = "があらわれた";
    /** 攻撃メッセージ  */
    public final static String TAG_MSG_ATTACK = "の攻撃！";
    /** 攻撃ダメージメッセージ  */
    public final static String TAG_MSG_ATTACK_DMAGE = "ダメージ与えた！";
    /** 敵死亡メッセージ  */
    public final static String TAG_MSG_DEAD_EN = "を倒した";
    /** レベルアップメッセージ(上部)  */
    public final static String TAG_MSG_LEVELUP_UPPER = "はレベル" ;
    /** レベルアップメッセージ(下部) */
    public final static String TAG_MSG_LEVELUP_LOWER = "にあがった！";
    /** 防御メッセージ  */
    public final static String TAG_MSG_DEFENCE = "ダメージ受けた！";
    /** エンディングメッセージ  */
    public final static String TAG_MSG_ENDING = "こうして再び平和が訪れたのでした！";
    /** クリアメッセージ  */
    public final static String TAG_MSG_FIN = "Fin.";
    /** HP0メッセージ  */
    public final static String TAG_MSG_HP0 = "勇者は力尽きた。";
    /** ゲームオーバーメッセージ  */
    public final static String TAG_MSG_GAMEOVER = "GAME OVER";
    /** コマンド入力待ちメッセージ  */
    public final static String TAG_MSG_COMMAND = "　　1.攻撃　　2.逃げる";
    /** マップイベント(体力全回復)  */
    public final static String TAG_MSG_EVT_HP_RECOVER = "の体力が回復した！";
    /** 逃亡(不可)メッセージ(上部)  */
    public final static String TAG_MSG_CANNOT_ESCAPE_UPPER = "しかし ";
    /** 逃亡(不可)メッセージ(下部)  */
    public final static String TAG_MSG_CANNOT_ESCAPE_LOWER = "からは逃げられない！";
    /** 逃亡(失敗)メッセージ  */
    public final static String TAG_MSG_ESCAPE_FAIL = "しかし 回り込まれてしまった！";
    /** 逃亡メッセージ  */
    public final static String TAG_MSG_ESCAPE = "は逃げ出した";
    /** タイトル補助メッセージ  */
    public final static String TAG_MSG_TITLE_SUB = "Touch to Start.";
    /** タイトルメッセージ  */
    public final static String TAG_MSG_TITLE = "RPG";
    /** 勇者ステータス(LV)  */
    public final static String TAG_STS_LV = "　Lv";
    /** 勇者ステータス(HP)  */
    public final static String TAG_STS_HP = "　HP ";
    /** 勇者ステータス(HP記号)  */
    public final static String TAG_STS_HP_SIGN = "/";

    // 画面サイズ定数
    /** 画面幅 */
    public final static int W = 860;
    /** 画面高さ */
    public final static int H = 480;

    // 画像関連定数
    /** 画像サイズ幅 */
    public final static int IMG_W = 80;
    /** 画像サイズ高さ */
    public final static int IMG_H = 80;
    /** 画像数 */
    public final static int IMG_MAX_NUM = 10;
    /** 敵画像番号算出用の補助値 */
    public final static int IMG_NUM_EN_AID = 6;
    /** 勇者画像番号 */
    public final static int IMG_NUM_YU = 5;
    /** タイトル画像番号 */
    public final static int IMG_NUM_TITLE = 4;


    // 画像/ディレクトリ名関連定数
    /** ファイル名(画像用) */
    public final static String TAG_FILE_RPG = "rpg";
    /** ディレクトリ名(画像用) */
    public final static String TAG_DIR_DRAWABLE =  "drawable";

    // 勇者関連定数
    /** 勇者(名前) */
    public final static String YU_NAME = "勇者";
    /** 勇者(最大体力) */
    public final static int[] YU_MAXHP = {0, 30, 50, 70};
    /** 勇者(攻撃量) */
    public final static int[] YU_ATTACK = {0, 5, 10, 30};
    /** 勇者(守備力) */
    public final static int[] YU_DEFENCE = {0, 0, 5, 10};
    /** 勇者(Lv必要経験値) */
    public final static int[] YU_EXP = {0, 0, 3, 6};

    // 敵関連定数
    /** 敵(種別(スライム)) */
    public final static int EN_TYPE_0 = 0;
    /** 敵(種別(スライム)) */
    public final static int EN_TYPE_1 = 1;
    /** 敵(種別(スライム)) */
    public final static int EN_TYPE_2 = 2;
    /** 敵(種別(ボス)) */
    public final static int EN_TYPE_BOSS = 3;
    /** 敵(名前(スライム)) */
    public final static String EN_NAME_0 = "スライム";
    /** 敵(名前(スライム)) */
    public final static String EN_NAME_1 = "レッドスライム";
    /** 敵(名前(スライム)) */
    public final static String EN_NAME_2 = "ブラックスライム";
    /** 敵(名前(ボス)) */
    public final static String EN_NAME_3 = "ボス";
    /** 敵(名前) */
    public final static String[] EN_NAME = {EN_NAME_0, EN_NAME_1, EN_NAME_2, EN_NAME_3};
    /** 敵(最大体力) */
    public final static int[] EN_MAXHP = {10, 10, 10, 50};
    /** 敵(攻撃量) */
    public final static int[] EN_ATTACK = {10, 10, 10, 26};
    /** 敵(守備力) */
    public final static int[] EN_DEFENCE = {0, 0, 0, 16};
    /** 敵(経験値) */
    public final static int[] EN_EXP = {1, 1, 2, 99};

    // 戦闘関連定数
    /** ダメージ最低値 */
    public final static int BT_DAMAGE_MIN = 1;
    /** ダメージ上限値 */
    public final static int BT_DAMAGE_MAX = 99;

    // 確率定数
    /** 確率の分母(100) */
    public final static int PER_MAX = 100;
    /** 逃亡失敗確率 */
    public final static int PER_ESCAPE_FAIL = 10;

    // イベント定数
    /** イベント種別(未発生) */
    public final static int EV_TYPE_NONE = -1;
    /** イベント種別(HP回復) */
    public final static int EV_TYPE_RECOVER = 0;
    /** イベント種別(はじまり) */
    public final static int EV_TYPE_OPENING = 1;

    // 文字サイズ
    /** タイトルの文字サイズ */
    public final static int CHAR_SIZE_TITLE = 160;
    /** メッセージの文字サイズ */
    public final static int CHAR_SIZE_MESSAGE = 28;
    /** ステータスの文字サイズ */
    public final static int CHAR_SIZE_STATUS = 28;
    /** 見出し2の文字サイズ */
    public final static int CHAR_SIZE_H2 = 32;
}
