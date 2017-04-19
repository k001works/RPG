package jp.ne.sakura.k001_works.rpggame;

/**
 * RPGデータクラス
 *
 * @author K'
 */
public class RpgData {

    /** 次の遷移状態 */
    private int nextScene;
    /** 現在の状態(シーン) */
    private int scene;
    /** 入力キー */
    private int key;

    // 勇者パラメータ
    /** X座標 */
    private int yuX;
    /** Y座標 */
    private int yuY;
    /** レベル */
    private int yuLv;
    /** 体力 */
    private int yuHp;
    /** 経験値 */
    private int yuExp;

    // 敵パラメータ
    /** 種類 */
    private int enType;
    /** 体力 */
    private int enHp;

    // イベント
    /** 種類 */
    private int evType;

    /**
     * コンストラクタ
     *
     * @param scene 現在の状態
     * @param nextScene 次の遷移状態
     * @param yuX 勇者座標(X軸)
     * @param yuY 勇者座標(Y軸)
     * @param yuLv 勇者(レベル)
     * @param yuHp 勇者(体力)
     * @param yuExp 勇者(経験値)
     */
    public RpgData(int scene, int nextScene, int key, int yuX, int yuY, int yuLv, int yuHp, int yuExp, int evType) {

        // パラメータの初期化
        this.scene = scene;
        this.nextScene = nextScene;
        this.key = key;
        this.yuX = yuX;
        this.yuY = yuY;
        this.yuLv = yuLv;
        this.yuHp = yuHp;
        this.yuExp = yuExp;
        this.evType = evType;
    }

    /**
     * 次の遷移状態の取得
     *
     * @return 次の遷移状態
     */
    public int getNextScene() {
        return this.nextScene;
    }

    /**
     * 現在の状態の取得
     *
     * @return 現在の状態
     */
    public int getScene() {
        return this.scene;
    }

    /**
     * 入力キーの取得
     *
     * @return 入力キー
     */
    public int getKey() {
        return this.key;
    }

    /**
     * 勇者座標(X軸)の取得
     *
     * @return 勇者座標(X軸)
     */
    public int getYuX() {
        return this.yuX;
    }

    /**
     * 勇者座標(Y軸)の取得
     *
     * @return 勇者座標(Y軸)
     */
    public int getYuY() {
        return this.yuY;
    }

    /**
     * 勇者(レベル)の取得
     *
     * @return 勇者(レベル)
     */
    public int getYuLv() {
        return this.yuLv;
    }

    /**
     * 勇者(体力)の取得
     *
     * @return 勇者(体力)
     */
    public int getYuHp() {
        return this.yuHp;
    }

    /**
     * 勇者(経験値)の取得
     *
     * @return 勇者(経験値)
     */
    public int getYuExp() {
        return this.yuExp;
    }

    /**
     * 敵(種別)の取得
     *
     * @return 敵(種別)
     */
    public int getEnType() {
        return this.enType;
    }

    /**
     * 敵(体力)の取得
     *
     * @return 敵(体力)
     */
    public int getEnHp() {
        return this.enHp;
    }

    /**
     * イベント種別の取得
     *
     * @return イベント種別
     */
    public int getEvType() {
        return this.evType;
    }

    /**
     * 次の遷移状態の設定
     *
     * @param nextScene 次の遷移状態
     */
    public void setNextScene(int nextScene) {
        this.nextScene = nextScene;
    }

    /**
     * 現在の状態の設定
     *
     * @param scene 現在の状態
     */
    public void setScene(int scene) {
        this.scene = scene;
    }

    /**
     * 入力キーの取得
     *
     * @param key 入力キー
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * 勇者座標(X軸)の設定
     *
     * @param yuX 勇者座標(X軸)
     */
    public void setYuX(int yuX) {
        this.yuX = yuX;
    }

    /**
     * 勇者座標(Y軸)の設定
     *
     * @param yuY 勇者座標(Y軸)
     */
    public void setYuY(int yuY) {
        this.yuY = yuY;
    }

    /**
     * 勇者(レベル)の設定
     *
     * @param yuLv 勇者(レベル)
     */
    public void setYuLv(int yuLv) {
        this.yuLv = yuLv;
    }

    /**
     * 勇者(体力)の設定
     *
     * @param yuHp 勇者(体力)
     */
    public void setYuHp(int yuHp) {
        this.yuHp = yuHp;
    }

    /**
     * 勇者(経験値)の設定
     *
     * @param yuExp 勇者(経験値)
     */
    public void setYuExp(int yuExp) {
        this.yuExp = yuExp;
    }

    /**
     * 敵(種別)の設定
     *
     * @param enType 敵(種別)
     */
    public void setEnType(int enType) {
        this.enType = enType;
    }

    /**
     * 敵(体力)の設定
     *
     * @param enHp 敵(体力)
     */
    public void setEnHp(int enHp) {
        this.enHp = enHp;
    }

    /**
     * イベント種別の設定
     *
     * @param evType イベント種別
     */
    public void setEvType(int evType) {
        this.evType = evType;
    }
}
