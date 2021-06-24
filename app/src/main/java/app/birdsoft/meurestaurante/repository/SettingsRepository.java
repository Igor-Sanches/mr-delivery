package app.birdsoft.meurestaurante.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cliente;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.model.SettingsElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class SettingsRepository {
    private static MutableLiveData<SettingsElements> elementos;
    public static MutableLiveData<SettingsElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<SettingsElements> GetElements(Context context) {
        MutableLiveData<SettingsElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<SettingsElements> data, Context context) {
        SettingsElements settingsElements = new SettingsElements();
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.Usuario)
                .document(Usuario.getUid(context))
                .get()
                .addOnSuccessListener((success->{
                   try{
                       if(success != null){
                           Cliente cliente = success.toObject(Cliente.class);
                           if(cliente != null){
                               Usuario.setEndereco(cliente.getEndereco(), context);
                               Settings.setEndereco(cliente, context);
                               settingsElements.setCliente(cliente);
                               settingsElements.setSuccess(true);
                           }else {
                               settingsElements.setSuccess(false);
                               settingsElements.setCliente(null);
                           }
                       }else {
                           settingsElements.setSuccess(false);
                           settingsElements.setCliente(null);
                       }
                       data.setValue(settingsElements);
                   }catch (Exception x){
                       settingsElements.setSuccess(false);
                       settingsElements.setCliente(null);
                       data.setValue(settingsElements);
                   }
                })).addOnFailureListener((falha->{
            settingsElements.setSuccess(false);
            settingsElements.setCliente(null);
            data.setValue(settingsElements);
        }));
    }

    public static void update(MutableLiveData<SettingsElements> data, Context context) {
        getFirebaseData(data, context);
    }

    public static void trocaNome(String nome, Context context, MutableLiveData<Boolean> data) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
        Conexao.getFirebaseAuth().getCurrentUser().updateProfile(profileChangeRequest)
                .addOnCompleteListener((command -> {
                    if(command.isSuccessful()){
                        FireStoreUtils
                                .getDatabaseOnline()
                                .collection(Chave.Usuario)
                                .document(Usuario.getUid(context))
                                .update("nome", nome)
                                .addOnCompleteListener((command1 ->
                                        data.setValue(command1.isSuccessful())));

                    }else{
                        data.setValue(false);
                    }
                }));

    }

    public static void trocaEmail(String email, Context context, MutableLiveData<Boolean> data) {
        Conexao.getFirebaseAuth().getCurrentUser().updateEmail(email)
                .addOnCompleteListener((command -> {
                    if(command.isSuccessful()){
                        FireStoreUtils
                                .getDatabaseOnline()
                                .collection(Chave.Usuario)
                                .document(Usuario.getUid(context))
                                .update("email", email)
                                .addOnCompleteListener((command1 -> data.setValue(command1.isSuccessful())));

                    }else{
                        data.setValue(false);
                    }
                }));

    }

    public static void trocaTelefone(String telefone, Context context, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.Usuario)
                .document(Usuario.getUid(context))
                .update("telefone", telefone)
                .addOnCompleteListener((command1 -> data.setValue(command1.isSuccessful())));
    }

    public static void trocaEndereco(String endereco, Context context, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.Usuario)
                .document(Usuario.getUid(context))
                .update("endereco", endereco)
                .addOnCompleteListener((command1 -> data.setValue(command1.isSuccessful())));
    }

    public static void trocaEndereco(Endereco endereco, Context context, MutableLiveData<Boolean> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("nomeRuaNumero", endereco.getNomeRuaNumero());
        map.put("bairro", endereco.getBairro());
        map.put("cidade", endereco.getCidade());
        map.put("referencia", endereco.getReferencia());
        map.put("numeroCasa", endereco.getNumeroCasa());
        map.put("endereco", endereco.getEndereco());
        map.put("tipo_lugar", endereco.getTipo_lugar());
        map.put("complemento", endereco.getComplemento());
        map.put("bloco_n_ap", endereco.getBloco_n_ap());
        map.put("sn", endereco.isSn());
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.Usuario)
                .document(Usuario.getUid(context))
                .update(map)
                .addOnCompleteListener((command1 -> data.setValue(command1.isSuccessful())));
    }
}
