package id.co.pegadaian.diarium.util;

/**
 * Created by Gilang on 12-Apr-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class DataSession {

    private static final String PREFER_NAME = "DataPalapaOne";
    public static  String ACTIVITY_KEY;
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    public DataSession(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public DataSession(Context context, String key) {
        this._context = context;
        ACTIVITY_KEY = key;
        pref = _context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void destroySession() {
        editor.clear();
        editor.commit();
    }

    public void setData(String key, String value) {
        editor.putString(ACTIVITY_KEY + key, value);
        editor.commit();
    }

    public String getData(String key) {
        return pref.getString(ACTIVITY_KEY + key, "");
    }
}
