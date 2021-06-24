package app.birdsoft.meurestaurante.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class Usuario {
    private static final String PREFERENCE_FILE_NAME = "Birdsoft_db";
    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences getmSharedPreferencesEditor(Context context){
        if(mSharedPreferences ==null){
            mSharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        }
        return mSharedPreferences;
    }

    public static String getUid(Context context){
        return getmSharedPreferencesEditor(context).getString("Uid", "");
    }

    public static void setUid(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("Uid", value);
        editor.apply();
    }

    public static String getEndereco(Context context){
        return getmSharedPreferencesEditor(context).getString("Endereco", null);
    }

    public static void setEndereco(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("Endereco", value);
        editor.apply();
    }

}
