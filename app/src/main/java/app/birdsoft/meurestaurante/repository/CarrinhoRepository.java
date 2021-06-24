package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FirebaseUtils;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Carrinho;
import app.birdsoft.meurestaurante.model.CarrinhoElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class CarrinhoRepository {
    private static MutableLiveData<CarrinhoElements> elementos;
    public static MutableLiveData<CarrinhoElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<CarrinhoElements> GetElements(Context context) {
        MutableLiveData<CarrinhoElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<CarrinhoElements> data, Context context) {
        CarrinhoElements carrinhoElements = new CarrinhoElements();
        if(Conexao.isConnected(context)){
            FirebaseUtils.getDatabaseRefOnline()
                    .child(Chave.CARRINHO)
                    .child(Settings.getUID(context))
                    .child(Usuario.getUid(context))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if(snapshot.exists()){
                                    List<Carrinho> carrinhos = new ArrayList<>();
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        Carrinho carrinho = snapshot1.getValue(Carrinho.class);
                                        carrinhos.add(carrinho);
                                    }
                                    if(carrinhos.size() > 0){
                                        carrinhoElements.setCarrinho(true);
                                        carrinhoElements.setCount(carrinhos.size());
                                        carrinhoElements.setCarrinhos(carrinhos);
                                        carrinhoElements.setListaVisibility(View.VISIBLE);
                                        carrinhoElements.setProgressVisibility(View.GONE);
                                        carrinhoElements.setLayoutConexao(View.GONE);
                                        carrinhoElements.setVazioVisibility(View.GONE);
                                    }else{
                                        carrinhoElements.setCarrinho(false);
                                        carrinhoElements.setCount(0);
                                        carrinhoElements.setCarrinhos(new ArrayList<>());
                                        carrinhoElements.setListaVisibility(View.GONE);
                                        carrinhoElements.setProgressVisibility(View.GONE);
                                        carrinhoElements.setLayoutConexao(View.GONE);
                                        carrinhoElements.setVazioVisibility(View.VISIBLE);
                                    }
                                    carrinhoElements.setLayoutWifiOffline(View.GONE);
                                }else{
                                    carrinhoElements.setCarrinho(false);
                                    carrinhoElements.setCount(0);
                                    carrinhoElements.setCarrinhos(new ArrayList<>());
                                    carrinhoElements.setLayoutConexao(View.GONE);
                                    carrinhoElements.setListaVisibility(View.GONE);
                                    carrinhoElements.setLayoutWifiOffline(View.GONE);
                                    carrinhoElements.setProgressVisibility(View.GONE);
                                    carrinhoElements.setVazioVisibility(View.VISIBLE);
                                }
                                data.setValue(carrinhoElements);
                            }catch (Exception x){
                                carrinhoElements.setCarrinho(false);
                                carrinhoElements.setCount(0);
                                carrinhoElements.setCarrinhos(new ArrayList<>());
                                carrinhoElements.setListaVisibility(View.GONE);
                                carrinhoElements.setProgressVisibility(View.GONE);
                                carrinhoElements.setLayoutConexao(View.GONE);
                                carrinhoElements.setLayoutWifiOffline(View.GONE);
                                carrinhoElements.setVazioVisibility(View.VISIBLE);
                                data.setValue(carrinhoElements);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            carrinhoElements.setCarrinho(false);
                            carrinhoElements.setCount(0);
                            carrinhoElements.setCarrinhos(new ArrayList<>());
                            carrinhoElements.setListaVisibility(View.GONE);
                            carrinhoElements.setProgressVisibility(View.GONE);
                            carrinhoElements.setLayoutConexao(View.VISIBLE);
                            carrinhoElements.setLayoutWifiOffline(View.GONE);
                            carrinhoElements.setVazioVisibility(View.GONE);
                            data.setValue(carrinhoElements);
                        }
                    });
        }else{
            carrinhoElements.setCarrinho(false);
            carrinhoElements.setCount(0);
            carrinhoElements.setCarrinhos(new ArrayList<>());
            carrinhoElements.setListaVisibility(View.GONE);
            carrinhoElements.setProgressVisibility(View.GONE);
            carrinhoElements.setLayoutConexao(View.GONE);
            carrinhoElements.setLayoutWifiOffline(View.VISIBLE);
            carrinhoElements.setVazioVisibility(View.GONE);
            data.setValue(carrinhoElements);
        }


    }

    public static void update(MutableLiveData<CarrinhoElements> data, Context context) {
        getFirebaseData(data, context);
    }

    public static void delete(Carrinho carrinho, MutableLiveData<Integer> data, Context context) {
        FirebaseUtils
                .getDatabaseRefOnline()
                .child(Chave.CARRINHO)
                .child(Settings.getUID(context))
                .child(carrinho.getUid_client())
                .child(carrinho.getUid())
                .removeValue().addOnSuccessListener(aVoid -> data.setValue(2)).addOnFailureListener((command -> data.setValue(1)));
        }

    public static void insert(Carrinho carrinho, MutableLiveData<Boolean> data, Context context) {
        FirebaseUtils
                .getDatabaseRefOnline()
                .child(Chave.CARRINHO)
                .child(Settings.getUID(context))
                .child(carrinho.getUid_client())
                .child(carrinho.getUid())
                .setValue(carrinho).addOnCompleteListener((command -> data.setValue(command.isSuccessful())));
    }

    public static void insert(List<Carrinho> carrinhos, String uid_client, MutableLiveData<Boolean> data, Context context) {
        for(Carrinho carrinho : carrinhos){
            carrinho.setUid(UUID.randomUUID().toString());
            carrinho.setUid_client(uid_client);
            FirebaseUtils
                    .getDatabaseRefOnline()
                    .child(Chave.CARRINHO)
                    .child(Settings.getUID(context))
                    .child(carrinho.getUid_client())
                    .child(carrinho.getUid())
                    .setValue(carrinho);
        }
        data.setValue(true);
     }
}
