package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.FirebaseUtils;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.model.PedidosElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.tools.Status;

public class PedidosRepository {
    private static MutableLiveData<PedidosElements> elementos;
    public static MutableLiveData<PedidosElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<PedidosElements> GetElements(Context context) {
        MutableLiveData<PedidosElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<PedidosElements> data, Context context) {
        PedidosElements pedidosElements = new PedidosElements();
        if(Conexao.isConnected(context)){
            FirebaseUtils
                    .getDatabaseRefOnline()
                    .child(Chave.PEDIDOS)
                    .child(Settings.getUID(context))
                    .child(Usuario.getUid(context))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try{
                                if(snapshot.exists()){
                                    Pedido pedido = snapshot.getValue(Pedido.class);
                                    if(pedido != null){
                                        pedidosElements.setStatusPedido(Status.valueOf(pedido.getStatusPedido()));
                                        pedidosElements.setCancelado(pedido.isCancelado());
                                        pedidosElements.setAtrasado(pedido.isAtrasado());
                                        pedidosElements.setEndereco(pedido.getEndereco());
                                        pedidosElements.setPedido(true);
                                        pedidosElements.setPedido(pedido);
                                        pedidosElements.setPrevisaoDeEntrega(HelperManager.getPrazo(pedido.getPrazo(), pedido.getDataPedido()));
                                        pedidosElements.setBtnAlteracaoPedido(pedido.isAlteracao() && !pedido.getStatusPedido().equals(Status.pedidoCancelado) ? View.VISIBLE : View.GONE);
                                        pedidosElements.setBtnRelatarProblemas(pedido.isCancelado() || pedido.getStatusPedido().equals(Status.pedidoEntregue.toString()) || pedido.getStatusPedido().equals(Status.pedidoCancelado.toString()) ? View.GONE : View.VISIBLE);
                                        if(pedido.isCancelado() || pedido.getStatusPedido().equals(Status.pedidoCancelado)){
                                            pedidosElements.setProgressPreparando(R.drawable.loading_cancel);
                                            pedidosElements.setProgressTransporte(R.drawable.loading_cancel);
                                            pedidosElements.setProgressFinal(R.drawable.loading_cancel);
                                            pedidosElements.setImageLogo(R.drawable.cancelado);
                                            pedidosElements.setStatus(context.getString(R.string.pedido_cancelado_success));
                                            pedidosElements.setMensagemCancelamento(pedido.getMsgCancelamento());
                                            pedidosElements.setBtnComfirmarCancelamento(pedido.getStatusPedido().equals(Status.pedidoCancelado.toString()) ? View.VISIBLE : View.GONE);
                                            pedidosElements.setBtnFinalizarPedido(pedido.getStatusPedido().equals(Status.pedidoEntregue.toString()) ? View.VISIBLE : View.GONE);
                                        }else{
                                            pedidosElements.setImageLogo(getLoaderImage(pedido.getStatusPedido()));
                                            switch (Status.valueOf(pedido.getStatusPedido())) {
                                                case novoPedido:
                                                    pedidosElements.setProgressPreparando(R.drawable.loading);
                                                    pedidosElements.setProgressTransporte(R.drawable.loading_neulto);
                                                    pedidosElements.setProgressFinal(R.drawable.loading_neulto);
                                                    pedidosElements.setStatus(context.getString(R.string.novo_pedido_b));
                                                    break;
                                                case preparandoPedido:
                                                    pedidosElements.setProgressPreparando(R.drawable.loading_finish);
                                                    pedidosElements.setProgressTransporte(R.drawable.loading);
                                                    pedidosElements.setProgressFinal(R.drawable.loading_neulto);
                                                    pedidosElements.setStatus(context.getString(R.string.pedido_preparando));
                                                    break;
                                                case pedidoEmTransido:
                                                    pedidosElements.setProgressPreparando(R.drawable.loading_finish);
                                                    pedidosElements.setProgressTransporte(R.drawable.loading_finish);
                                                    pedidosElements.setProgressFinal(R.drawable.loading);
                                                    pedidosElements.setStatus(context.getString(R.string.pedido_em_trasito));
                                                    break;
                                                case pedidoEntregue:
                                                    pedidosElements.setProgressPreparando(R.drawable.loading_finish);
                                                    pedidosElements.setProgressTransporte(R.drawable.loading_finish);
                                                    pedidosElements.setProgressFinal(R.drawable.loading_finish);
                                                    pedidosElements.setStatus(context.getString(R.string.pedido_entregue_success));
                                                    break;
                                                case pedidoCancelado:
                                                    pedidosElements.setProgressPreparando(R.drawable.loading_cancel);
                                                    pedidosElements.setProgressTransporte(R.drawable.loading_cancel);
                                                    pedidosElements.setProgressFinal(R.drawable.loading_cancel);
                                                    pedidosElements.setStatus(context.getString(R.string.cancelamento_pedido));
                                                    break;
                                            }
                                        }
                                        pedidosElements.setBtnRecebiPedido(pedido.getStatusPedido().equals(Status.pedidoEmTransido.toString()) ? View.VISIBLE : View.GONE);
                                        pedidosElements.setBtnVerMsgCancelamentoPedido(pedido.isCancelado() ? View.VISIBLE : View.GONE);
                                        pedidosElements.setBtnFinalizarPedido(pedido.getStatusPedido().equals(Status.pedidoEntregue.toString()) ? View.VISIBLE : View.GONE);
                                        pedidosElements.setRelogioView(pedido.isCancelado() || pedido.getStatusPedido().equals(Status.pedidoEntregue.toString()) || pedido.getStatusPedido().equals(Status.pedidoCancelado.toString()) ? View.GONE : View.VISIBLE);
                                        pedidosElements.setLayoutPrincipal(View.VISIBLE);
                                        pedidosElements.setLytProgress(View.GONE);
                                        pedidosElements.setLayoutWifiOffline(View.GONE);
                                        pedidosElements.setLayoutConexao(View.GONE);
                                        pedidosElements.setErrorDados(View.GONE);
                                    }
                                }else {
                                    pedidosElements.setPedido(false);
                                    pedidosElements.setPedido(null);
                                    pedidosElements.setLayoutPrincipal(View.GONE);
                                    pedidosElements.setLytProgress(View.GONE);
                                    pedidosElements.setLayoutConexao(View.GONE);
                                    pedidosElements.setLayoutWifiOffline(View.GONE);
                                    pedidosElements.setErrorDados(View.VISIBLE);
                                }

                                data.setValue(pedidosElements);
                            }catch (Exception x){
                                pedidosElements.setPedido(false);
                                pedidosElements.setPedido(null);
                                pedidosElements.setLayoutPrincipal(View.GONE);
                                pedidosElements.setLytProgress(View.GONE);
                                pedidosElements.setLayoutConexao(View.VISIBLE);
                                pedidosElements.setLayoutWifiOffline(View.GONE);
                                pedidosElements.setErrorDados(View.GONE);
                                data.setValue(pedidosElements);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            pedidosElements.setPedido(false);
                            pedidosElements.setPedido(null);
                            pedidosElements.setLayoutPrincipal(View.GONE);
                            pedidosElements.setLayoutConexao(View.VISIBLE);
                            pedidosElements.setLytProgress(View.GONE);
                            pedidosElements.setLayoutWifiOffline(View.GONE);
                            pedidosElements.setErrorDados(View.GONE);
                            data.setValue(pedidosElements);
                        }
                    });
        }else{
            pedidosElements.setPedido(false);
            pedidosElements.setPedido(null);
            pedidosElements.setLayoutConexao(View.GONE);
            pedidosElements.setLayoutPrincipal(View.GONE);
            pedidosElements.setLytProgress(View.GONE);
            pedidosElements.setLayoutWifiOffline(View.VISIBLE);
            pedidosElements.setErrorDados(View.GONE);
            data.setValue(pedidosElements);
        }


    }


    public static void update(MutableLiveData<PedidosElements> data, Context context) {
        getFirebaseData(data, context);
    }

    private static int getLoaderImage(String statusPedido) {
        int res = R.drawable.pedido_entregue_gif;
        switch (Status.valueOf(statusPedido)) {
            case preparandoPedido:
            case novoPedido:
                res = R.drawable.cozinhando_gif;
                break;
            case pedidoEmTransido:
                res = R.drawable.moto_entrega_gif;
                break;
            case pedidoCancelado:
                res = R.drawable.cancel;
                break;
        }
        return res;
    }

    public static void putPedido(Pedido pedido, MutableLiveData<Boolean> data, Context context) {
        FirebaseUtils
                .getDatabaseRefOnline()
                .child(Chave.PEDIDOS)
                .child(Settings.getUID(context))
                .child(pedido.getUid_client())
                .setValue(pedido)
                .addOnCompleteListener((result->{
                    if(result.isSuccessful()){
                        if(pedido.getCupom() !=null){
                            Map<String, Object> map = new HashMap<>();
                            map.put(pedido.getUid_client(), com.google.firebase.firestore.FieldValue.increment(-1));
                            FireStoreUtils
                                    .getDatabase()
                                    .collection(Chave.CUPOM)
                                    .document(Settings.getUID(context))
                                    .collection(Chave.CUPOM)
                                    .document(pedido.getCupom().getCodigo())
                                    .update(map);
                        }
                    }
                    data.setValue(result.isSuccessful());
                }));

    }

    public static void relatarAtraso(Pedido pedido, MutableLiveData<Boolean> data, Context context) {
        pedido.setAtrasado(true);
        FirebaseUtils.getDatabaseRefOnline()
                .child(Chave.PEDIDOS)
                .child(Settings.getUID(context))
                .child(pedido.getUid_client())
                .setValue(pedido)
                .addOnCompleteListener((result->data.setValue(result.isSuccessful())));
    }

    public static void cancelamento(Pedido pedido, String mensagem, MutableLiveData<Boolean> data, Context context){
        pedido.setMsgCancelamento(mensagem);
        pedido.setCancelado(true);
        FirebaseUtils.getDatabaseRefOnline()
                .child(Chave.PEDIDOS)
                .child(Settings.getUID(context))
                .child(pedido.getUid_client())
                .setValue(pedido)
                .addOnCompleteListener((result->data.setValue(result.isSuccessful())));
    }

    public static void finalizarPedido(Pedido pedido, MutableLiveData<Boolean> data, Context context){
        pedido.setCupom(null);
        pedido.setDataRecebimento(DateTime.getTime());
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.HISTORICO)
                .document(Settings.getUID(context))
                .collection(Chave.HISTORICO)
                .document(pedido.getUid())
                .set(pedido)
                .addOnCompleteListener((result->{
                    if(result.isSuccessful()){
                        FirebaseUtils.getDatabaseRef()
                                .child(Chave.PEDIDOS)
                                .child(Settings.getUID(context))
                                .child(pedido.getUid_client()).removeValue();
                    }
                    data.setValue(result.isSuccessful());
                }));
    }

    public static void recebi(Pedido pedido, MutableLiveData<Boolean> data, Context context){
        pedido.setStatusPedido(Status.pedidoEntregue.toString());
        pedido.setDataRecebimento(DateTime.getTime());
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.HISTORICO)
                .document(Settings.getUID(context))
                .collection(Chave.HISTORICO)
                .document(pedido.getUid())
                .set(pedido)
                .addOnCompleteListener((result->{
                    if(result.isSuccessful()){
                        FirebaseUtils
                                .getDatabaseRef()
                                .child(Chave.PEDIDOS)
                                .child(Settings.getUID(context))
                                .child(pedido.getUid_client()).removeValue();
                    }
                    data.setValue(result.isSuccessful());
                }));
    }

    public static void isPedido(Context context, MutableLiveData<Boolean> data) {
        FirebaseUtils
                .getDatabaseRefOnline()
                .child(Chave.PEDIDOS)
                .child(Settings.getUID(context))
                .child(Usuario.getUid(context))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data.setValue(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        data.setValue(true);
                    }
                });
    }
}
