package app.birdsoft.meurestaurante.settings;

import android.content.Context;
import android.content.SharedPreferences;

import app.birdsoft.meurestaurante.model.Cliente;
import app.birdsoft.meurestaurante.model.Endereco;
public class Settings {
    private static final String NOTIFICATION = "NOTIFICATION", FORMATO_DATA = "FORMATO_DATA";
    private static final String PREFERENCE_FILE_NAME = "BirdSoft_db";
    private static SharedPreferences mSharedPreferences;

    public static void setEndereco(Endereco endereco, Context context){
        if(endereco != null) {
            put("bairro", endereco.getBairro(), context);
            put("cidade", endereco.getCidade(), context);
            put("referencia", endereco.getReferencia(), context);
            put("numero_casa", endereco.getNumeroCasa(), context);
            put("nome_rua_numero", endereco.getNomeRuaNumero(), context);
            put("endereco", endereco.getEndereco(), context);
            put("bloco_n_ap", endereco.getBloco_n_ap(), context);
            put("s_n", endereco.isSn(), context);
            put("complemento", endereco.getComplemento(), context);
            put("tipo_lugar", endereco.getTipo_lugar(), context);
        }
    }

    public static Endereco getEndereco(Context context){
        Endereco endereco = new Endereco();
        endereco.setBairro(get("bairro", "", context));
        endereco.setCidade(get("cidade", "", context));
        endereco.setReferencia(get("referencia", "", context));
        endereco.setNumeroCasa(get("numero_casa", "", context));
        endereco.setNomeRuaNumero(get("nome_rua_numero", "", context));
        endereco.setEndereco(get("endereco", "", context));
        endereco.setBloco_n_ap(get("bloco_n_ap", null, context));
        endereco.setComplemento(get("complemento", null, context));
        endereco.setSn(get("s_n", false, context));
        endereco.setTipo_lugar(get("tipo_lugar", null, context));
        if(endereco.getNomeRuaNumero().equals(""))
            endereco = null;

        return endereco;
    }

    private static SharedPreferences getmSharedPreferencesEditor(Context context){
        if(mSharedPreferences ==null){
            mSharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        }
        return mSharedPreferences;
    }

    public static boolean isNotification(Context context){
        return get(NOTIFICATION, true, context);
    }

    public static void setFormateDate(String value, Context context){
        put(FORMATO_DATA, value, context);
    }


    public static void put(String key, String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void put(String key, boolean value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String get(String key, String _default, Context context) {
        return getmSharedPreferencesEditor(context).getString(key, _default);
    }

    public static boolean get(String key, boolean _default, Context context) {
        return getmSharedPreferencesEditor(context).getBoolean(key, _default);
    }

    public static void setEndereco(Cliente cliente, Context context) {
        Endereco endereco = new Endereco();
        endereco.setCidade(cliente.getCidade());
        endereco.setReferencia(cliente.getReferencia());
        endereco.setNumeroCasa(cliente.getNumeroCasa());
        endereco.setNomeRuaNumero(cliente.getNomeRuaNumero());
        endereco.setBairro(cliente.getBairro());
        endereco.setEndereco(cliente.getEndereco());
        endereco.setBloco_n_ap(cliente.getBloco_n_ap());
        endereco.setComplemento(cliente.getComplemento());
        endereco.setTipo_lugar(cliente.getTipo_lugar());
        endereco.setSn(cliente.isSn());
        setEndereco(endereco, context);
    }

    private static void remove(Context context){
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.remove("uid");
        editor.apply();
    }

    public static String getUID(Context context) {
        return get("uid", "null", context);
    }

    public static void setUID(String uid, Context context) {
        put("uid", uid, context);
    }

    public static void delete(Context activity) {
       remove("uid", activity);
        remove("bairro", activity);
        remove("referencia", activity);
        remove("numero_casa", activity);
        remove("nome_rua_numero", activity);
        remove("endereco", activity);
        remove("bloco_n_ap", activity);
        remove("complemento", activity);
        remove("s_n", activity);
        remove("tipo_lugar", activity);
        Usuario.remove("uid", activity);
    }
}

