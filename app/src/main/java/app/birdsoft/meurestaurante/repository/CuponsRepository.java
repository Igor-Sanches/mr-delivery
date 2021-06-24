package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cupom;
import app.birdsoft.meurestaurante.model.CuponsElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class CuponsRepository {
    public static final int CUPOM_NAO_ENCONTRADO = 0, CUPOM_JA_USADO_POR_VC = 6, CUPOM_NAO_DISPONIVEL = 1, CUPOM_NAO_ATIVO = 2, CUPOM_EXPIRADO= 3, CUPOM_NAO_DISPONIVEL_PARA_VOCE = 4, SUCESSO = 5;
    private static MutableLiveData<CuponsElements> elementos;
    public static MutableLiveData<CuponsElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<CuponsElements> GetElements(Context context) {
        MutableLiveData<CuponsElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<CuponsElements> data, Context context) {
        CuponsElements cuponsElements = new CuponsElements();
        if(Conexao.isConnected(context)){
            FireStoreUtils.getDatabaseOnline()
                    .collection(Chave.CUPOM)
                    .document(Settings.getUID(context))
                    .collection(Chave.CUPOM)
                    .whereEqualTo("ativo", true)
                    .get()
                    .addOnCompleteListener((result -> {
                        if(result.isSuccessful()){
                            if(result.getResult() != null){
                                List<Cupom> cupoms = new ArrayList<>();
                                for(DocumentSnapshot snapshot : result.getResult()){
                                    Map<String, Object> map = snapshot.getData();
                                    Cupom cupom = snapshot.toObject(Cupom.class);
                                    assert cupom != null;
                                    if(cupom.isAtivo()){
                                        if(!cupom.isExpirado()){
                                            assert map != null;
                                            if(map.get(Usuario.getUid(context)) != null){
                                                if((Long)map.get(Usuario.getUid(context)) > 0) {
                                                    if(cupom.isVencimento()){
                                                        if(HelperManager.isVencido(cupom.getDataValidade())){
                                                            cupoms.add(cupom);
                                                        }
                                                    }else cupoms.add(cupom);
                                                }
                                            }
                                        }
                                    }
                                }

                                if(cupoms.size() > 0){
                                    cuponsElements.setProgressVisibility(View.GONE);
                                    cuponsElements.setLayoutConexao(View.GONE);
                                    cuponsElements.setVazioVisibility(View.GONE);
                                    cuponsElements.setListaVisibility(View.VISIBLE);
                                }else{
                                    cuponsElements.setListaVisibility(View.GONE);
                                    cuponsElements.setProgressVisibility(View.GONE);
                                    cuponsElements.setVazioVisibility(View.VISIBLE);
                                    cuponsElements.setLayoutConexao(View.GONE);
                                }
                                cuponsElements.setLayoutWifiOffline(View.GONE);
                                cuponsElements.setCupoms(cupoms);
                            }else{
                                cuponsElements.setListaVisibility(View.GONE);
                                cuponsElements.setProgressVisibility(View.GONE);
                                cuponsElements.setLayoutConexao(View.GONE);
                                cuponsElements.setVazioVisibility(View.VISIBLE);
                                cuponsElements.setLayoutWifiOffline(View.GONE);
                                cuponsElements.setCupoms(null);
                            }
                        }else{
                            cuponsElements.setListaVisibility(View.GONE);
                            cuponsElements.setProgressVisibility(View.GONE);
                            cuponsElements.setLayoutConexao(View.VISIBLE);
                            cuponsElements.setVazioVisibility(View.GONE);
                            cuponsElements.setLayoutWifiOffline(View.GONE);
                            cuponsElements.setCupoms(null);
                        }

                        data.setValue(cuponsElements);
                    }));
        }else{
            cuponsElements.setListaVisibility(View.GONE);
            cuponsElements.setProgressVisibility(View.GONE);
            cuponsElements.setLayoutConexao(View.GONE);
            cuponsElements.setVazioVisibility(View.GONE);
            cuponsElements.setLayoutWifiOffline(View.VISIBLE);
            cuponsElements.setCupoms(null);
            data.setValue(cuponsElements);
        }
    }

    public static void update(MutableLiveData<CuponsElements> data, Context context) {
        getFirebaseData(data, context);
    }

    public static void onFind(String cupom, MutableLiveData<Integer> data, Context context) {
        FireStoreUtils.getDatabaseOnline()
                .collection(Chave.CUPOM)
                .document(Settings.getUID(context))
                .collection(Chave.CUPOM)
                .document(cupom)
                .get().addOnCompleteListener((result -> {
                    if(result.isSuccessful()){
                        Cupom _cupom = result.getResult().toObject(Cupom.class);
                        Map<String, Object> map = result.getResult().getData();
                        if(_cupom != null){
                            if(_cupom.isAtivo()){
                                if(!_cupom.isExpirado()){
                                    if(_cupom.isVencimento()){
                                        if(HelperManager.isVencido(_cupom.getDataValidade())){
                                            data.setValue(CUPOM_EXPIRADO);
                                        }else{
                                            inserir(_cupom, map, data, context);
                                        }
                                    }else{
                                        inserir(_cupom, map, data, context);
                                    }
                                }else data.setValue(CUPOM_EXPIRADO);
                            }else data.setValue(CUPOM_NAO_ATIVO);
                        }else data.setValue(CUPOM_NAO_ENCONTRADO);
                    }else{
                        data.setValue(CUPOM_NAO_ENCONTRADO);
                    }
        }));

    }

    private static void inserir(Cupom cupom, Map<String, Object> _map, MutableLiveData<Integer> data, Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put(Usuario.getUid(context), 1);
        assert _map != null;
        if(cupom.isAlluser()){
            if(_map.get(Usuario.getUid(context)) == null || (Long)_map.get(Usuario.getUid(context)) == -1){
                FireStoreUtils
                        .getDatabase()
                        .collection(Chave.CUPOM)
                        .document(Settings.getUID(context))
                        .collection(Chave.CUPOM)
                        .document(cupom.getCodigo())
                        .update(map).addOnCompleteListener((result -> data.setValue(SUCESSO)));
            }else data.setValue(CUPOM_JA_USADO_POR_VC);
        }else{
            if(_map.get(Usuario.getUid(context)) != null){
                if((Long)_map.get(Usuario.getUid(context)) == -1) {
                    FireStoreUtils
                            .getDatabase()
                            .collection(Chave.CUPOM)
                            .document(Settings.getUID(context))
                            .collection(Chave.CUPOM)
                            .document(cupom.getCodigo())
                            .update(map).addOnCompleteListener((result -> data.setValue(SUCESSO)));
                }else data.setValue(CUPOM_NAO_DISPONIVEL_PARA_VOCE);
            }else data.setValue(CUPOM_NAO_DISPONIVEL_PARA_VOCE);
        }
    }

    public static void onRemove(Cupom cupom, MutableLiveData<Boolean> data, Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put(Usuario.getUid(context), -1);
        FireStoreUtils
                .getDatabase()
                .collection(Chave.CUPOM)
                .document(Settings.getUID(context))
                .collection(Chave.CUPOM)
                .document(cupom.getCodigo())
                .update(map).addOnCompleteListener((result -> data.setValue(result.isSuccessful())));
    }
}
