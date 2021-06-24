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
import app.birdsoft.meurestaurante.model.Cardapio;
import app.birdsoft.meurestaurante.model.CardapioElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class CardapioRepository {
    private static MutableLiveData<CardapioElements> elementos;
    public static MutableLiveData<CardapioElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<CardapioElements> GetElements(Context context) {
        MutableLiveData<CardapioElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<CardapioElements> data, Context context) {
        CardapioElements cardapioElements = new CardapioElements();
        if(Conexao.isConnected(context)){
            FireStoreUtils.getDatabaseOnline()
                    .collection(Chave.CARDAPIO)
                    .document(Settings.getUID(context))
                    .collection(Chave.CARDAPIO)
                    .whereEqualTo("disponivel", true)
                    .get().addOnCompleteListener((result -> {
                if(result.isSuccessful()){
                    if(result.getResult() != null){
                        List<Cardapio> cardapios = new ArrayList<>();
                        for (DocumentSnapshot snapshot : result.getResult()){
                            Cardapio cardapio = snapshot.toObject(Cardapio.class);
                            assert cardapio != null;
                            cardapios.add(cardapio);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            cardapios.sort(Comparator.comparingInt(Cardapio::getPosition));
                        }
                        if(cardapios.size() > 0){
                            cardapioElements.setProgressVisibility(View.GONE);
                            cardapioElements.setVazioVisibility(View.GONE);
                            cardapioElements.setLayoutConexao(View.GONE);
                            cardapioElements.setListaVisibility(View.VISIBLE);
                        }else{
                            cardapioElements.setListaVisibility(View.GONE);
                            cardapioElements.setLayoutConexao(View.GONE);
                            cardapioElements.setProgressVisibility(View.GONE);
                            cardapioElements.setVazioVisibility(View.VISIBLE);
                        }
                        cardapioElements.setLayoutWifiOffline(View.GONE);
                        cardapioElements.setCardapios(cardapios);
                    }else{
                        cardapioElements.setListaVisibility(View.GONE);
                        cardapioElements.setProgressVisibility(View.GONE);
                        cardapioElements.setLayoutConexao(View.GONE);
                        cardapioElements.setVazioVisibility(View.VISIBLE);
                        cardapioElements.setLayoutWifiOffline(View.GONE);
                        cardapioElements.setCardapios(null);
                    }
                }else{
                    cardapioElements.setListaVisibility(View.GONE);
                    cardapioElements.setProgressVisibility(View.GONE);
                    cardapioElements.setLayoutConexao(View.VISIBLE);
                    cardapioElements.setVazioVisibility(View.GONE);
                    cardapioElements.setLayoutWifiOffline(View.GONE);
                    cardapioElements.setCardapios(null);
                }

                data.setValue(cardapioElements);
            }));
        }else{
            cardapioElements.setListaVisibility(View.GONE);
            cardapioElements.setProgressVisibility(View.GONE);
            cardapioElements.setLayoutConexao(View.GONE);
            cardapioElements.setVazioVisibility(View.GONE);
            cardapioElements.setLayoutWifiOffline(View.VISIBLE);
            cardapioElements.setCardapios(null);
            data.setValue(cardapioElements);
        }

    }

    public static void update(MutableLiveData<CardapioElements> data, Context context) {
        getFirebaseData(data, context);
    }

}
