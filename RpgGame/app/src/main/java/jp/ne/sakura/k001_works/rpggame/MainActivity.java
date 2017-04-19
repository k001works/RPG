package jp.ne.sakura.k001_works.rpggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * RPGメインクラス
 *
 * @author K'
 */
public class MainActivity extends Activity {

    /** RPGビュー */
    private RPGView rpg;
    /** サウンド */
    private Sounds s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // フルスクリーン設定
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // サウンドをインスタンス化
        this.s = new Sounds(this);

        // RPGビューの生成
        this.rpg = new RPGView(this, s);
        setContentView(this.rpg);
    }

    @Override
    protected void onPause() {

        // BGMを一時停止
        s.pauseBgm();
        super.onPause();
    }

    @Override
    protected void onStop() {

        // BGMを一時停止
        s.pauseBgm();

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        // BGMの解放
        s.releaseBgm();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        // フォーカス状態がtrueの場合
        if (hasFocus) {

            // BGMの再開
            s.replayBgm();
        }

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 戻るボタンの場合
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 確認ダイアログ表示
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("確認");
            alertDialogBuilder.setMessage("アプリケーションを終了しますか？");
            alertDialogBuilder.setPositiveButton("はい",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 終了
                            finish();
                        }
                    });
            alertDialogBuilder.setNegativeButton("いいえ",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return false;

        // 戻るボタン以外
        } else {

            // そのまま処理
            return super.onKeyDown(keyCode, event);
        }
    }
}
