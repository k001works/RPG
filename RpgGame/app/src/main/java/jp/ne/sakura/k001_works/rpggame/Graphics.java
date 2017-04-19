package jp.ne.sakura.k001_works.rpggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * グラフィック関連クラス
 *
 * @author K'
 */
public class Graphics {

    /** サーフェイスハンドラ */
    private SurfaceHolder holder;
    /** ペイント */
    private Paint paint;
    /** キャンバス */
    private Canvas canvas;
    /** X座標 */
    private int originX;
    /** Y座標 */
    private int originY;

    /**
     * コンストラクタ
     *
     * @param holder サーフェイスハンドラ
     */
    public Graphics(SurfaceHolder holder) {

        // サーフェイスハンドラを保持
        this.holder = holder;

        // アンチエイリアスを設定
        this.paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * ロック
     */
    public void lock() {

        // 描画スレッドの排他制御をロック
        this.canvas = holder.lockCanvas();

        // 排他制御のロック失敗
        if (canvas == null) {
            return;
        }

        // キャンバスの原点座標の変更
        canvas.translate(originX, originY);
    }

    /**
     * アンロック
     */
    public void unlock() {

        // 排他制御がロック中でない場合
        if (canvas == null) {
            return;
        }

        // 描画スレッドの排他制御をロック解除
        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * 原点の設定
     *
     * @param x X座標
     * @param y Y座標
     */
    public void setOrigin(int x, int y) {
        this.originX = x;
        this.originY = y;
    }

    /**
     * カラー設定
     *
     * @param color 指定色
     */
    public  void setColor(int color) {
        paint.setColor(color);
    }

    /**
     * フォントサイズ設定
     *
     * @param fontSize フォントサイズ
     */
    public void setTextSize(int fontSize) {
        paint.setTextSize(fontSize);
    }

    /**
     * フォントメトリック取得
     *
     * @return フォントメトリック
     */
    public Paint.FontMetrics getFontMetrics() {
        return paint.getFontMetrics();
    }

    /**
     * 文字幅の取得
     *
     * @param string
     * @return
     */
    public int measureText(String string) {
        return (int)paint.measureText(string);
    }

    /**
     * 矩形描画
     *
     * @param x X座標
     * @param y Y座標
     * @param w 幅
     * @param h 高さ
     */
    public void fillRect(int x, int y, int w, int h) {

        // キャンバスがnullの場合終了
        if (canvas == null) {
            return;
        }

        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(x, y, x + w, y + h), paint);
    }

    /**
     * 画像描画
     *
     * @param bitmap 画像
     * @param x 描画位置(X座標)
     * @param y 描画位置(Y座標)
     */
    public void drawBitmap(Bitmap bitmap, int x, int y) {

        // キャンバスがnullの場合終了
        if (canvas == null) {
            return;
        }

        // 画像位置の設定
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Rect src = new Rect(0, 0, w, h);                // 元画像の範囲
        Rect dst = new Rect(x, y, (x + w), (y + h));    // 配置時の描画範囲

        // 画像描画
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    /**
     * 画像描画(リサイズ版)
     *
     * @param bitmap 画像
     * @param dispW 描画幅
     * @param dispH 描画高さ
     */
    public void drawBitmapResize(Bitmap bitmap, int dispW, int dispH) {

        // キャンバスがnullの場合終了
        if (canvas == null) {
            return;
        }

        // 画像位置の設定
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Rect src = new Rect(0, 0, w, h);                // 元画像の範囲
        Rect dst = new Rect(0, 0, dispW, dispH);    // 配置時の描画範囲

        // 画像描画
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    /**
     * テキスト描画
     *
     * @param string 描画テキスト
     * @param x 描画座標(X軸)
     * @param y 描画座標(Y軸)
     */
    public void drawText(String string, int x, int y) {

        // キャンバスがnullの場合終了
        if (canvas == null) {
            return;
        }

        // テキストの描画
        canvas.drawText(string, x, y, paint);
    }
}
