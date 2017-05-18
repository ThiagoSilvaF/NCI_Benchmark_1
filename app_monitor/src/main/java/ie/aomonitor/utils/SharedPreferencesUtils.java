package ie.aomonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static ie.aomonitor.Constants.*;

/**
 * Created by Administrativo on 18/05/2017.
 */

public class SharedPreferencesUtils {
    private Context context;

    public SharedPreferencesUtils(Context context){
        this.context = context;
    }

    public void setPreference(String key, String value){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public boolean getPreferenceBoolean(String key){
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(key, false);
    }

    public String getPreferenceString(String key){
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(key, null);
    }

}
