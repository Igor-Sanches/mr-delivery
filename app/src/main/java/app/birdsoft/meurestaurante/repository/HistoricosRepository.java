package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.HistoricoElements;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class HistoricosRepository {
    private static MutableLiveData<HistoricoElements> elementos;
    public static MutableLiveData<HistoricoElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<HistoricoElements> GetElements(Context context) {
        MutableLiveData<HistoricoElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<HistoricoElements> data, Context context) {
        HistoricoElements historicoElements = new HistoricoElements();

        if(Conexao.isConnected(context)){
            FireStoreUtils.getDatabase()
                    .collection(Chave.HISTORICO)
                    .document(Settings.getUID(context))
                    .collection(Chave.HISTORICO)
                    .whereEqualTo("uid_client", Usuario.getUid(context))
                    .get().addOnCompleteListener((result -> {
                if(result.isSuccessful()){
                    if(result.getResult() != null){
                        List<Pedido> pedidos = new ArrayList<>();
                        for (DocumentSnapshot snapshot : result.getResult()){
                            Pedido pedido = snapshot.toObject(Pedido.class);
                            pedidos.add(pedido);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            pedidos.sort(Comparator.comparingLong(Pedido::getDataPedido).reversed());
                        }
                        if(pedidos.size() > 0){
                            historicoElements.setProgressVisibility(View.GONE);
                            historicoElements.setVazioVisibility(View.GONE);
                            historicoElements.setLayoutConexao(View.GONE);
                            historicoElements.setListaVisibility(View.VISIBLE);
                            historicoElements.setLayoutWifiOffline(View.GONE);
                        }else{
                            historicoElements.setListaVisibility(View.GONE);
                            historicoElements.setProgressVisibility(View.GONE);
                            historicoElements.setLayoutWifiOffline(View.GONE);
                            historicoElements.setLayoutConexao(View.GONE);
                            historicoElements.setVazioVisibility(View.VISIBLE);
                        }
                        historicoElements.setPedidos(pedidos);
                    }else{
                        historicoElements.setListaVisibility(View.GONE);
                        historicoElements.setProgressVisibility(View.GONE);
                        historicoElements.setLayoutConexao(View.GONE);
                        historicoElements.setLayoutWifiOffline(View.GONE);
                        historicoElements.setVazioVisibility(View.VISIBLE);
                    }
                }else{
                    historicoElements.setListaVisibility(View.GONE);
                    historicoElements.setProgressVisibility(View.GONE);
                    historicoElements.setLayoutConexao(View.VISIBLE);
                    historicoElements.setVazioVisibility(View.GONE);
                    historicoElements.setLayoutWifiOffline(View.GONE);
                }
                data.setValue(historicoElements);

            }));
        }else{
            historicoElements.setListaVisibility(View.GONE);
            historicoElements.setProgressVisibility(View.GONE);
            historicoElements.setLayoutConexao(View.GONE);
            historicoElements.setVazioVisibility(View.GONE);
            historicoElements.setLayoutWifiOffline(View.VISIBLE);
            data.setValue(historicoElements);
        }
    }

    public static void update(MutableLiveData<HistoricoElements> data, Context context) {
        getFirebaseData(data, context);
    }

    public static void delete(Pedido pedido, MutableLiveData<Boolean> data, Context context) {
        FireStoreUtils
                .getDatabase()
                .collection(Chave.HISTORICO)
                .document(Settings.getUID(context))
                .collection(Chave.HISTORICO)
                .document(pedido.getUid())
                .delete().addOnCompleteListener((command -> data.setValue(command.isSuccessful())));
    }
}
