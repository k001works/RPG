package jp.ne.sakura.k001_works.rpggame;

import android.content.SharedPreferences;

/**
 * RPGビュー描画クラス
 *
 * @author K'
 */
public class SaveData {

    /** プリファレンスオブジェクト */
    public static SharedPreferences preferences;

    /**
     * コンストラクタ
     *
     * @param preferences プリファレンスオブジェクト
     */
    public SaveData(SharedPreferences preferences) {

        // プリファレンスを保持
        this.preferences = preferences;
    }

    /**
     * データ保存(数値情報)
     */
    public void savePreferencesKeyInt(String key, int val) {

        // データの保存
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    /**
     * データ保存(文字情報)
     */
    public void savePreferencesKeyInt(String key, String val) {

        // データの保存
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    /**
     * データ保存(真偽情報)
     */
    public void savePreferencesKeyBoolean(String key, boolean val) {

        // データの保存
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    /**
     * データ削除
     */
    public void deletePreferencesKey(String key) {

        // データの削除(個別にキー指定)
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * データ削除
     */
    public void deletePreferencesKeyAll() {

        // データの削除(全削除)
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * データ取得(数値情報)
     */
    public int getPreferencesKeyInt(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * データ取得(文字情報)
     */
    public String getPreferencesKeyString(String key) {
        return preferences.getString(key, "");
    }

    /**
     * データ取得(真偽情報)
     */
    public boolean getPreferencesKeyBoolean(String key) {
        return preferences.getBoolean(key, false);
    }
}
