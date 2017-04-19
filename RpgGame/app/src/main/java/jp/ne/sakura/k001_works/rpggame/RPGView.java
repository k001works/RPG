package jp.ne.sakura.k001_works.rpggame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static jp.ne.sakura.k001_works.rpggame.RpgConstracts.*;
import static jp.ne.sakura.k001_works.rpggame.SoundConstracts.*;

import java.util.Random;

/**
 * RPGビュー描画クラス
 *
 * @author K'
 */
public class RPGView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // システム
    /** サーフェイスハンドラ */
    private SurfaceHolder holder;
    /** グラフィック */
    private Graphics g;
    /** サウンド */
    private Sounds s;
    /** スレッド */
    private Thread thread;
    /** RPG情報 */
    private RpgData rpgData;


    // その他
    /** 乱数取得用 */
    private static Random rand = new Random();
    /** 画像情報 */
    private Bitmap[] bmp = new Bitmap[IMG_MAX_NUM];

    /**
     * コンストラクタ
     *
     * @param activity アクティビティ
     */
    public RPGView(Activity activity, Sounds sounds) {
        super(activity);

        // ビットマップの読み込み
        for (int i = 0; i < IMG_MAX_NUM; i++) {
            this.bmp[i] = readBitmap(activity, TAG_FILE_RPG + i);
        }

        // サーフェイスホルダーの生成
        this.holder = getHolder();
        this.holder.setFormat(PixelFormat.RGBA_8888);
        this.holder.addCallback(this);

        // 描画サイズの指定
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int dw = H * p.x / p.y;
        this.holder.setFixedSize(dw, H);

        // グラフィックスの生成
        this.g = new Graphics(this.holder);
        this.g.setOrigin((dw - W) / 2, 0);

        // サウンドの生成
        this.s = sounds;

        // BGM初期化
        this.s.stopBgmAll();

        // パラメータの初期値を設定
        this.rpgData = new RpgData(S_START, S_START, KEY_NONE, 1, 2, 1, 30, 0, EV_TYPE_NONE);
    }

    /**
     * サーフェイスの生成
     *
     * @param surfaceHolder サーフェイスハンドラ
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        // スレッド実行
        thread = new Thread(this);
        thread.start();
    }

    /**
     * サーフェイスの終了
     *
     * @param surfaceHolder サーフェイスハンドラ
     * @param format ピクセルフォーマット
     * @param w 幅
     * @param h 高さ
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {

    }

    /**
     * サーフェイスの終了
     *
     * @param surfaceHolder サーフェイスハンドラ
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        // スレッドを初期化
        thread = null;
    }

    /**
     * スレッド処理
     */
    @Override
    public void run() {

        // スレッドが生きている限り無限ループ
        while (thread != null) {

            // 遷移先が正の数の場合(次の遷移先が決定している場合)
            if (rpgData.getNextScene() >= S_START) {

                // 次遷移先を現在の状態に設定
                rpgData.setScene(rpgData.getNextScene());

                // スタートの場合
                if (rpgData.getScene() == S_START) {

                    // 設定値の初期化
                    procStart();
                }

                // 遷移先、キー入力を初期化
                rpgData.setNextScene(S_INIT);
                rpgData.setKey(KEY_NONE);
            }

            // シーン判定
            switch (rpgData.getScene()) {
                case S_MAP:         // マップ

                    // マップを表示
                    procMap();
                    break;

                case S_COMMAND:     // コマンド入力待ち

                    // コマンド待機
                    procCommand();
                    break;

                case S_APPEAR:      // 敵出現

                    // 敵出現
                    procAppear();
                    break;

                case S_ATTACK:      // 攻撃

                    // 勇者の攻撃
                    procAttack();
                    break;

                case S_DEFENCE:     // 防御

                    // 敵の攻撃を防御
                    procDefence();
                    break;

                case S_ESCAPE:      // 逃亡

                    // 逃亡
                    procEscape();
                    break;

                case S_GAMECLEAR:      // ゲームクリア

                    // エンディング表示
                    procGameClear();
                    break;

                case S_GAMEOVER:      // ゲームオーバー

                    // ゲームオーバー表示
                    procGameOver();
                    break;

                case S_MAPEVENT:      // マップイベント

                    // イベント表示
                    procMapEvent();
                    break;

                default:            // 上記以外(異常時)

                    // エラーログ出力
                    Log.e("run", "unknown scene: " + rpgData.getScene());
                    break;
            }

            // キー入力を初期化
            rpgData.setKey(KEY_NONE);

            //スリープ
            sleep(200);
        }
    }

    /**
     * タッチイベント
     *
     * @param event イベント情報
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // タッチ座標を保持
        int touchX = (int)((event.getX() * W) / getWidth());
        int touchY = (int)((event.getY() * H) / getHeight());

        int touchAction = event.getAction();

        // タッチイベントがタッチ押下の場合
        if (touchAction == MotionEvent.ACTION_DOWN) {

            // シーン判定
            switch (rpgData.getScene()) {
                case S_MAP:         // マップ

                    // タッチ位置が中心点を基準に縦と横の幅が大きいほうを判定(X座標が大きい場合)
                    if (Math.abs(touchX - (W / 2)) > Math.abs(touchY - (H / 2))) {

                        // タッチ位置が左側の場合
                        if ((touchX - (W / 2)) < 0) {

                            rpgData.setKey(KEY_LEFT);

                        // タッチ位置が右側の場合
                        } else {

                            rpgData.setKey(KEY_RIGHT);
                        }

                    // タッチ位置がY座標の方が大きい場合
                    } else {

                        // タッチ位置が上側の場合
                        if ((touchY - (H / 2)) < 0) {

                            rpgData.setKey(KEY_UP);

                        // タッチ位置が下側の場合
                        } else {

                            rpgData.setKey(KEY_DOWN);
                        }
                    }
                    break;

                case S_START:       // 開始
                case S_APPEAR:      // 敵出現
                case S_ATTACK:      // 攻撃
                case S_DEFENCE:     // 防御
                case S_ESCAPE:      // 逃亡
                case S_GAMECLEAR:   // クリア
                case S_GAMEOVER:    // ゲームオーバー
                case S_MAPEVENT:    // マップイベント

                    // キー入力に決定キー入力済みを設定
                    rpgData.setKey(KEY_SELECT);
                    break;

                case S_COMMAND:     // コマンド入力待ち

                    // 攻撃を選択時
                    if ((((W / 2) - 250) < touchX) && (touchX < (W / 2)) && ((H - 190) < touchY) && (touchY < H)) {

                        // キー入力に1(攻撃)を設定
                        rpgData.setKey(KEY_1);

                        // 逃げるを選択時
                    } else if (((W / 2) < touchX) && touchX < ((W / 2) + 250) && ((H - 190) < touchY) && (touchY < H)) {

                        // キー入力に2(逃げる)を設定
                        rpgData.setKey(KEY_2);
                    }
                    break;

                default:            // 上記以外

                    // 処理なし
                    break;
            }
        }
        return true;
    }

    /**
     * 初期化
     */
    private void procStart() {

        // BGM再生
        s.playBgm(BGM_NUM_TITLE);

        // タイトル画面描画
        drawTitle();

        // パラメータの初期値を設定
        rpgData.setScene(S_MAPEVENT);
        rpgData.setNextScene(S_INIT);
        rpgData.setEvType(EV_TYPE_OPENING);
        rpgData.setYuX(1);
        rpgData.setYuY(2);
        rpgData.setYuLv(1);
        rpgData.setYuHp(30);
        rpgData.setYuExp(0);
    }

    /**
     * マップ
     */
    private void procMap() {

        // BGM再生
        s.playBgm(BGM_NUM_MAP);

        // イベント判定用フラグ
        boolean flag = false;

        // 移動可否判定
        switch (rpgData.getKey()) {
            case KEY_UP:        // 上

                // 移動先が移動可能な場合
                if (MAP[rpgData.getYuY() - 1][rpgData.getYuX()] <= M_VAL_MOVE_JUDGE) {

                    // 勇者位置、フラグの更新
                    rpgData.setYuY(rpgData.getYuY() - 1);
                    flag = true;
                }
                break;

            case KEY_DOWN:      // 下

                // 移動先が移動可能な場合
                if (MAP[rpgData.getYuY() + 1][rpgData.getYuX()] <= M_VAL_MOVE_JUDGE) {

                    // 勇者位置、フラグの更新
                    rpgData.setYuY(rpgData.getYuY() + 1);
                    flag = true;
                }
                break;

            case KEY_LEFT:      // 左

                // 移動先が移動可能な場合
                if (MAP[rpgData.getYuY()][rpgData.getYuX() - 1] <= M_VAL_MOVE_JUDGE) {

                    // 勇者位置、フラグの更新
                    rpgData.setYuX(rpgData.getYuX() - 1);
                    flag = true;
                }
                break;

            case KEY_RIGHT:     // 右

                // 移動先が移動可能な場合
                if (MAP[rpgData.getYuY()][rpgData.getYuX() + 1] <= M_VAL_MOVE_JUDGE) {

                    // 勇者位置、フラグの更新
                    rpgData.setYuX(rpgData.getYuX() + 1);
                    flag = true;
                }
                break;

            case KEY_NONE:     // 未入力

                // 処理なし
                break;

            default:            // 上記以外(異常)

                // エラーログ出力
                Log.e("procMap", "unknown key: " + rpgData.getKey());
                break;
        }

        // イベント判定用フラグがtrueの場合
        if (flag) {

            // マップチップ種別によりイベント（敵の出現等）判定
            switch (MAP[rpgData.getYuY()][rpgData.getYuX()]) {
                case M_GROUND:         // 平地

                    // ランダムで敵出現
                    if (rand(10) == 0) {

                        //　ランダムで敵種別を設定
                        switch (rand(3)) {
                            case 0:

                                // 敵種別設定
                                rpgData.setEnType(EN_TYPE_0);
                                break;

                            case 1:

                                // 敵種別設定
                                rpgData.setEnType(EN_TYPE_1);
                                break;

                            case 2:

                                // 敵種別設定
                                rpgData.setEnType(EN_TYPE_2);
                                break;

                            default:

                                // 敵種別設定
                                rpgData.setEnType(EN_TYPE_0);
                                break;
                        }

                        // 敵出現
                        rpgData.setNextScene(S_APPEAR);
                    }
                    break;

                case M_HOUSE:         // 家

                    // イベント(HP全回復)
                    rpgData.setEvType(EV_TYPE_RECOVER);
                    rpgData.setNextScene(S_MAPEVENT);
                    break;

                case M_CASTLE:        // 城

                    // ボス出現
                    rpgData.setEnType(EN_TYPE_BOSS);
                    rpgData.setNextScene(S_APPEAR);
                    break;

                default:        // 上記以外(異常)

                    // エラーログ出力
                    Log.e("procMap", "unknown map kind: "
                            + MAP[rpgData.getYuY()][rpgData.getYuX()]
                            + " x: " + rpgData.getYuX()
                            + " y: " + rpgData.getYuY());
                    break;
            }
        }

        // 描画スレッドをロック
        g.lock();

        // マップ描画
        drawMap();

        // 描画スレッドをロック解除
        g.unlock();
    }

    /**
     * 敵出現
     */
    private void procAppear() {

        // ボスの場合
        if (rpgData.getEnType() == EN_TYPE_BOSS) {
            // BGM再生
            s.playBgm(BGM_NUM_BATTLE_BOSS);

        // スライムの場合
        } else {
            // BGM再生
            s.playBgm(BGM_NUM_BATTLE_EN);
        }

        // 初期化
       rpgData.setEnHp(EN_MAXHP[rpgData.getEnType()]);

        // スリープ
        sleep(300);

        // フラッシュ表示
        drawFlash();

        // メッセージ設定(戦闘画面描画)
        drawBattle(EN_NAME[rpgData.getEnType()] + TAG_MSG_APPEAR);

        // 決定キー待ち
        waitSelect();

        // 次遷移先にコマンド状態を設定
        rpgData.setNextScene(S_COMMAND);
    }

    /**
     * コマンド入力待ち
     */
    private void procCommand() {

        // メッセージ設定(戦闘画面描画)
        drawBattle(TAG_MSG_COMMAND);

        // キー入力初期化
        rpgData.setKey(KEY_NONE);

        // 次の遷移先が初期化状態(未決定)の場合
        while (rpgData.getNextScene() == S_INIT) {

            // キー入力が1(攻撃)の場合
            if (rpgData.getKey() == KEY_1) {

                // 次遷移先に攻撃を設定
                rpgData.setNextScene(S_ATTACK);

            // キー入力が2(逃げる)の場合
            } else if (rpgData.getKey() == KEY_2) {

                // 次遷移先に逃げるを設定
                rpgData.setNextScene(S_ESCAPE);
            }

            // スリープ
            sleep(100);
        }
    }

    /**
     * マップイベント(後発型イベント)
     */
    private void procMapEvent() {

        // 次遷移先格納用
        int next;

        // イベント種別がHP全回復の場合
        if (rpgData.getEvType() == EV_TYPE_RECOVER) {

            // SE再生
            s.playSe(SE_NUM_RECOVER);

            // 全回復
            rpgData.setYuHp(YU_MAXHP[rpgData.getYuLv()]);

            // 描画スレッドをロック
            g.lock();

            // マップ描画
            drawMap();

            // メッセージ描画
            drawMessage(YU_NAME + TAG_MSG_EVT_HP_RECOVER);

            // 描画スレッドをロック解除
            g.unlock();

            // 決定キー入力待ち
            waitSelect();

            // 次遷移先にMAPを設定
            next = S_MAP;

        // イベント種別がオープニングの場合
        } else if (rpgData.getEvType() == EV_TYPE_OPENING) {

            // BGM再生
            s.playBgm(BGM_NUM_MAP);

            // 描画スレッドをロック
            g.lock();

            // マップ描画
            drawMap();

            // 描画スレッドをロック解除
            g.unlock();

            // 決定キー入力待ち
            waitSelect();

            // 描画スレッドをロック
            g.lock();

            // マップ描画
            drawMap();

            // メッセージ描画
            drawMessage("天の声「お城を目指すのじゃ！」");

            // 描画スレッドをロック解除
            g.unlock();

            // 決定キー入力待ち
            waitSelect();

            // 次遷移先にMAPを設定
            next = S_MAP;

        // イベント種別なしの場合
        } else {

            // BGM再生
            s.playBgm(BGM_NUM_MAP);

            // 描画スレッドをロック
            g.lock();

            // マップ描画
            drawMap();

            // 描画スレッドをロック解除
            g.unlock();

            // 次遷移先にMAPを設定
            next = S_MAP;
        }

        // イベント種別を初期化
        rpgData.setEvType(EV_TYPE_NONE);

        // 次遷移先にマップを設定
        rpgData.setNextScene(next);
    }

    /**
     * 防御
     */
    private void procDefence() {

        // 戦闘メッセージ
        String message = EN_NAME[rpgData.getEnType()] + TAG_MSG_ATTACK;

        // メッセージ設定(戦闘画面描画)
        drawBattle(message);

        // 決定キー入力待ち
        waitSelect();

        // ボスの場合
        if (rpgData.getEnType() == EN_TYPE_BOSS) {

            // SE再生
            s.playSe(SE_NUM_ATACK_BOSS);

        // スライムの場合
        } else {

            // SE再生
            s.playSe(SE_NUM_ATACK_EN);
        }

        //フラッシュ
        drawFlash(message);

        // 勇者が受けるダメージを算出
        int damage = EN_ATTACK[rpgData.getEnType()] - YU_DEFENCE[rpgData.getYuLv()] + rand(10);

        // ダメージが最低値以下の場合
        if (damage <= BT_DAMAGE_MIN) {

            // ダメージを最低値に置き換え
            damage = BT_DAMAGE_MIN;

        // ダメージが上限以上の場合
        } else if (damage >= BT_DAMAGE_MAX) {

            // ダメージを上限値に置き換え
            damage = BT_DAMAGE_MAX;
        }

        // メッセージ設定(戦闘画面描画)
        drawBattle(damage + TAG_MSG_DEFENCE);

        // 決定キー入力待ち
        waitSelect();

        // 勇者のHP更新
        rpgData.setYuHp(rpgData.getYuHp() - damage);

        // HPの負数対策
        if (rpgData.getYuHp() <= 0) {
            rpgData.setYuHp(0);
        }

        // 次遷移先にコマンド入力待ちを設定
        rpgData.setNextScene(S_COMMAND);

        // 勇者HPが0の場合(敗北)
        if (rpgData.getYuHp()  <= 0) {

            // メッセージ設定(戦闘画面描画)
            drawBattle(TAG_MSG_HP0);

            // 決定キー入力待ち
            waitSelect();

            // 次遷移先にゲームオーバーを設定
            rpgData.setNextScene(S_GAMEOVER);
        }
    }

    /**
     * 攻撃
     */
    private void procAttack() {

        String message = YU_NAME + TAG_MSG_ATTACK;

        // メッセージ設定(戦闘画面描画)
        drawBattle(message);

        // 決定キー入力待ち
        waitSelect();

        // SE再生
        s.playSe(SE_NUM_ATACK_YU);

        // フラッシュ
        for (int i = 0; i < 10; i++) {

            drawBattle(message, i % 2 == 0);

            // スリープ
            sleep(100);
        }

        // 勇者の攻撃ダメージの算出
        int damage = YU_ATTACK[rpgData.getYuLv()] - EN_DEFENCE[rpgData.getEnType()] + rand(10);

        // ダメージが最低値以下の場合
        if (damage <= BT_DAMAGE_MIN) {

            // ダメージを最低値に置き換え
            damage = BT_DAMAGE_MIN;

        // ダメージが最大値以上の場合
        } else if (damage >= BT_DAMAGE_MAX) {

            // ダメージを最大値に置き換え
            damage = BT_DAMAGE_MAX;
        }

        // メッセージ設定(戦闘画面描画)
        drawBattle(damage + TAG_MSG_ATTACK_DMAGE);

        // 決定キー入力待ち
        waitSelect();

        //体力の計算
        rpgData.setEnHp(rpgData.getEnHp() - damage);

        // 負数の場合、体力を0に置き換え
        if (rpgData.getEnHp() <= 0) {
            rpgData.setEnHp(0);
        }


        // 次遷移先に防御を設定
        rpgData.setNextScene(S_DEFENCE);

        // 敵を倒した場合(敵HPが0)
        if (rpgData.getEnHp() <= 0) {

            //メッセージ
            drawBattle(EN_NAME[rpgData.getEnType()] + TAG_MSG_DEAD_EN);
            waitSelect();

            //経験値計算
            rpgData.setYuExp(rpgData.getYuExp() + EN_EXP[rpgData.getEnType()]);

            // レベルアップの場合
            if ((rpgData.getYuLv() < 3) && (YU_EXP[rpgData.getYuLv() + 1] <= rpgData.getYuExp())) {

                // SE再生
                s.playSe(SE_NUM_LEVELUP);

                // 勇者のレベルを更新
                rpgData.setYuLv(rpgData.getYuLv() + 1);

                // メッセージ設定(戦闘画面描画)
                drawBattle(YU_NAME + TAG_MSG_LEVELUP_UPPER + rpgData.getYuLv() + TAG_MSG_LEVELUP_LOWER);

                // 決定キー入力待ち
                waitSelect();
            }

            // 倒した敵種別がボスの場合(エンディング）
            if (rpgData.getEnType() == EN_TYPE_BOSS) {

                // BGM再生
                s.playBgm(BGM_NUM_ENDING);

                // エンディング画面描画
                drawEnding();

                // 決定キー入力待ち
                waitSelect();

                // 次遷移先にゲームクリアを設定
                rpgData.setNextScene(S_GAMECLEAR);
                return;
            }

            // 次遷移先にマップを設定
            rpgData.setNextScene(S_MAP);
        }
    }

    /**
     * 逃走
     */
    private void procEscape() {

        // SE再生
        s.playSe(SE_NUM_ESCAPE);

        //メッセージ設定(逃走)
        drawBattle(YU_NAME + TAG_MSG_ESCAPE);

        // 決定キー入力待ち
        waitSelect();

        // 次遷移先にマップを設定
        rpgData.setNextScene(S_MAP);

        // 敵がボスの場合
        if (rpgData.getEnType() == EN_TYPE_BOSS) {

            //メッセージ設定(逃走不可)
            drawBattle(TAG_MSG_CANNOT_ESCAPE_UPPER + EN_NAME[rpgData.getEnType()] + TAG_MSG_CANNOT_ESCAPE_LOWER);

            // 決定キー入力待ち
            waitSelect();

            // 次遷移先にコマンド入力待ち状態を設定
            rpgData.setNextScene(S_COMMAND);

        // 敵がボス以外の場合
        } else {

            // 逃亡失敗が発生した場合(指定の確率で発生)
            if (rand(PER_MAX) < PER_ESCAPE_FAIL) {

                // メッセージ設定(逃亡失敗)
                drawBattle(TAG_MSG_ESCAPE_FAIL);

                // 決定キー入力待ち
                waitSelect();

                // 次遷移先に防御状態を設定
                rpgData.setNextScene(S_DEFENCE);
            }
        }
    }

    /**
     * ゲームクリア
     */
    private void procGameClear() {

        // 描画スレッドをロック
        g.lock();

        // 背景描画
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect(0, 0, W, H);

        // クリアメッセージ描画
        g.setColor(Color.rgb(255, 255, 255));
        g.setTextSize(CHAR_SIZE_H2);
        String message = TAG_MSG_FIN;
        int x = (W / 2) - (g.measureText(message) / 2);
        int y = (H / 2) - (((int)g.getFontMetrics().bottom - (int)g.getFontMetrics().top) / 2);
        g.drawText(message, x, y);

        // 描画スレッドをロック解除
        g.unlock();

        // 決定キー入力待ち
        waitSelect();

        // 次遷移先にマップを設定
        rpgData.setNextScene(S_START);
    }

    /**
     * ゲームオーバー
     */
    private void procGameOver() {

        // BGM再生
        s.playBgm(BGM_NUM_GAMEOVER);

        // 描画スレッドをロック
        g.lock();

        // 背景描画
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect(0, 0, W, H);

        // メッセージ設定(ゲームオーバー)
        g.setColor(Color.rgb(255, 255, 255));
        g.setTextSize(CHAR_SIZE_H2);
        String message = TAG_MSG_GAMEOVER;
        int x = (W / 2) - (g.measureText(message) / 2);
        int y = (H / 2) - (((int)g.getFontMetrics().bottom - (int)g.getFontMetrics().top) / 2);
        g.drawText(message, x, y);

        // 描画スレッドをロック解除
        g.unlock();

        // 決定キー入力待ち
        waitSelect();

        // 次遷移先に開始を設定
        rpgData.setNextScene(S_START);
    }

    /**
     * エンディング画面描画
     */
    private void drawEnding() {

        // 描画スレッドをロック
        g.lock();

        // 背景描画
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect(0, 0, W, H);

        // メッセージ設定(エンディング)
        g.setColor(Color.rgb(255, 255, 255));
        g.setTextSize(CHAR_SIZE_MESSAGE);
        String endMessage = TAG_MSG_ENDING;
        int x = (W / 2) - (g.measureText(endMessage) / 2);
        int y = (H / 2) - (((int)g.getFontMetrics().bottom - (int)g.getFontMetrics().top) / 2);
        g.drawText(endMessage, x, y);

        // 描画スレッドをロック解除
        g.unlock();
    }

    /**
     * タイトル画面描画
     */
    private void drawTitle() {

        // 描画スレッドをロック
        g.lock();

        // 背景画像描画
        g.drawBitmapResize(bmp[IMG_NUM_TITLE], W, H);

        // 補助テキスト描画
        g.setColor(Color.rgb(255, 255, 255));
        g.setTextSize(CHAR_SIZE_H2);
        String message = TAG_MSG_TITLE_SUB;
        int x = (W / 2) - (g.measureText(message) / 2);
        int y = (H) - ((int)g.getFontMetrics().bottom * 15);
        g.drawText(message, x, y);

        // 文字色、文字サイズを設定
        g.setColor(Color.rgb(255, 255, 255));
        g.setTextSize(CHAR_SIZE_STATUS);

        // タイトル描画
        String title = TAG_MSG_TITLE;
        g.setTextSize(CHAR_SIZE_TITLE);
        int tX = (W / 2) - (g.measureText(title) / 2);
        int tY = (H / 2);
        g.drawText(title, tX, tY);

        // 描画スレッドをロック解除
        g.unlock();

        // 決定キー入力待ち
        waitSelect();
    }
    /**
     * マップ描画
     * ※呼び元で排他制御しているため処理内ではロック不要
     */
    private void drawMap() {

        // MAP画像の配置(11×7マスで構成)
        for (int j = -3; j <= 3; j++) {
            for (int i = -5;i <= 5; i++) {

                // MAP画像種別を初期化
                int idx = 3;

                // 配置するMAP画像種別の判定
                if ((0 <= rpgData.getYuX() + i) && ((rpgData.getYuX() + i) < MAP[0].length) && (0 <= (rpgData.getYuY() + j)) && ((rpgData.getYuY() + j) < MAP.length)) {
                    idx = MAP[rpgData.getYuY() + j][rpgData.getYuX() + i];
                }

                // MAP画像を配置
                g.drawBitmap(bmp[idx], (W / 2) - (IMG_W / 2) + (IMG_W * i), (H / 2) - (IMG_H / 2) + (IMG_H * j));
            }
        }

        // 勇者画像の配置
        g.drawBitmap(bmp[IMG_NUM_YU], (W / 2) - (IMG_W / 2), (H / 2) - (IMG_H / 2));
    }

    /**
     * フラッシュ描画(メッセージなし)
     */
    private void drawFlash() {

        // フラッシュ表示ループ
        for (int i = 0; i < 6; i++) {

            // 描画スレッドをロック
            g.lock();

            // フラッシュ色判定(黒白を交互に反転）
            if ((i % 2) == 0) {
                g.setColor(Color.rgb(0, 0, 0));
            } else {
                g.setColor(Color.rgb(255, 255, 255));
            }

            // フラッシュ背景色を描画
            g.fillRect(0, 0, W, H);

            // 描画スレッドをロック解除
            g.unlock();

            // スリープ
            sleep(100);
        }
    }

    /**
     * フラッシュ描画(メッセージ表示あり)
     *
     * @param message 表示メッセージ
     */
    private void drawFlash(String message) {

        // フラッシュ表示ループ
        for (int i = 0; i < 10; i++) {

            // フラッシュ色判定(交互に反転）
            if (i % 2 == 0) {

                // ロック
                g.lock();

                // フラッシュ背景色を描画
                g.setColor(Color.rgb(255, 255, 255));
                g.fillRect(0, 0, W, H);

                // ロック解除
                g.unlock();

            } else {

                // メッセージ設定(戦闘画面描画)
                drawBattle(message);
            }

            // スリープ
            sleep(100);
        }
    }

    /**
     *  戦闘画面描画IF
     */
    private void drawBattle(String message) {

        // 戦闘画面の描画(敵死亡：敵非表示 敵生存：敵表示)
        drawBattle(message, rpgData.getEnHp() > 0);
    }

    /**
     * 戦闘画面描画(メイン処理)
     *
     * @param message 表示メッセージ
     * @param visible 敵の表示/非表示
     */
    private void drawBattle(String message, boolean visible) {

        // ロック
        g.lock();

        // 背景描画
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect(0, 0, W, H);

        // 勇者ステータス描画
        drawStatus();

        // 敵表示の場合
        if (visible) {

            // 敵画像の描画
            g.drawBitmap(bmp[IMG_NUM_EN_AID + rpgData.getEnType()], (W - bmp[IMG_NUM_EN_AID + rpgData.getEnType()].getWidth()) / 2, H - 100 - bmp[IMG_NUM_EN_AID + rpgData.getEnType()].getHeight());
        }

        // メッセージ描画
        drawMessage(message);

        // ロック解除
        g.unlock();

    }

    /**
     * メッセージの描画<br />
     * ※呼び元で排他制御しているため処理内ではロック不要
     *
     * @param message 表示メッセージ
     */
    private void drawMessage(String message) {

        // 文字色格納用
        int color;

        // 勇者HPに合わせて色判定(死亡：赤　生存：白)
        if (rpgData.getYuHp() > 0) {
            color = Color.rgb(255, 255, 255);
        }else {
            color = Color.rgb(255, 0, 0);
        }

        // 枠線用の色を設定
        g.setColor(color);
        g.fillRect((W - 504) / 2, H - 122, 504, 104);

        // 背景色
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect((W - 500) / 2, H - 120, 500, 100);

        // 文字色、文字サイズの設定
        g.setColor(color);
        g.setTextSize(CHAR_SIZE_MESSAGE);

        // メッセージを描画
        g.drawText(message, (W - 500) / 2 + 50, 370 - (int)g.getFontMetrics().top);
    }

    /**
     * 勇者ステータスの描画<br />
     * ※呼び元で排他制御しているため処理内ではロック不要
     */
    private void drawStatus() {

        // 文字色格納用
        int color;

        // 勇者HPに合わせて色判定(死亡：赤　生存：白)
        if (rpgData.getYuHp() > 0) {
            color = Color.rgb(255, 255, 255);
        } else {
            color = Color.rgb(255, 0, 0);
        }

        // 枠線用の色を設定
        g.setColor(color);
        g.fillRect((W - 504) / 2, 8, 504, 54);

        // 背景色を設定
        g.setColor(Color.rgb(0, 0, 0));
        g.fillRect((W - 500) / 2, 10, 500, 50);

        // 文字色、文字サイズを設定
        g.setColor(color);
        g.setTextSize(CHAR_SIZE_STATUS);

        // 勇者ステータスを描画
        g.drawText(YU_NAME + TAG_STS_LV + rpgData.getYuLv() + TAG_STS_HP + rpgData.getYuHp() + TAG_STS_HP_SIGN + YU_MAXHP[rpgData.getYuLv()], (W - 500) / 2 + (CHAR_SIZE_STATUS * 4), 15 - (int)g.getFontMetrics().top);
    }

    /**
     * 決定キー待ち
     */
    private void waitSelect() {

        // キー入力状態を初期化
        rpgData.setKey(KEY_NONE);

        // キー入力が選択状態になるまでループ
        while (rpgData.getKey() != KEY_SELECT) {
            sleep(100);
        }
    }

    /**
     * スリープ
     *
     * @param time スリープ時間(単位：ms)
     */
    private void sleep(int time) {

        try {
            // スリープ
            Thread.sleep(time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 乱数の取得
     *
     * @param num 乱数の限界値
     */
    private static int rand(int num) {

        // 0～(指定値 - 1)までの乱数を返却
        return (rand.nextInt(num));
    }

    /**
     * ビットマップの読み込み
     *
     * @param context コンテキスト
     * @param name ファイル名
     */
    private static Bitmap readBitmap(Context context, String name) {

        // リソースIDの取得
        int resID = context.getResources().getIdentifier(name,TAG_DIR_DRAWABLE, context.getPackageName());

        // 画像読み込み
        return BitmapFactory.decodeResource(context.getResources(), resID);
    }
}
