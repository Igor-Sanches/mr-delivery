package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FirebaseUtils;
import app.birdsoft.meurestaurante.model.Anuncio;
import app.birdsoft.meurestaurante.model.AnunciosElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class AnunciosRepository {
    private static MutableLiveData<AnunciosElements> elementos;
    public static MutableLiveData<AnunciosElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<AnunciosElements> GetElements(Context context) {
        MutableLiveData<AnunciosElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<AnunciosElements> data, Context context) {
        AnunciosElements anunciosElements = new AnunciosElements();
        if (Conexao.isConnected(context)) {
            FirebaseUtils
                    .getDatabaseRef()
                    .child(Chave.ANUNCIOS)
                    .child(Settings.getUID(context))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.exists()) {
                                    GenericTypeIndicator<List<Anuncio>> list = new GenericTypeIndicator<List<Anuncio>>() {
                                    };
                                    List<Anuncio> anuncios = snapshot.getValue(list);
                                    if (anuncios != null) {
                                        if (anuncios.size() > 0) {
                                            anunciosElements.setProgressVisibility(View.GONE);
                                            anunciosElements.setVazioVisibility(View.GONE);
                                            anunciosElements.setLayoutConexao(View.GONE);
                                            anunciosElements.setListaVisibility(View.VISIBLE);
                                        } else {
                                            anunciosElements.setLayoutConexao(View.GONE);
                                            anunciosElements.setListaVisibility(View.GONE);
                                            anunciosElements.setProgressVisibility(View.GONE);
                                            anunciosElements.setVazioVisibility(View.VISIBLE);
                                        }
                                        anunciosElements.setAnuncios(anuncios);
                                    } else {
                                        anunciosElements.setListaVisibility(View.GONE);
                                        anunciosElements.setProgressVisibility(View.GONE);
                                        anunciosElements.setLayoutConexao(View.GONE);
                                        anunciosElements.setVazioVisibility(View.VISIBLE);
                                        anunciosElements.setAnuncios(new ArrayList<>());
                                    }
                                } else {
                                    anunciosElements.setListaVisibility(View.GONE);
                                    anunciosElements.setProgressVisibility(View.GONE);
                                    anunciosElements.setLayoutConexao(View.GONE);
                                    anunciosElements.setVazioVisibility(View.VISIBLE);
                                    anunciosElements.setAnuncios(new ArrayList<>());
                                }
                                data.setValue(anunciosElements);

                            } catch (Exception x) {
                                anunciosElements.setListaVisibility(View.GONE);
                                anunciosElements.setProgressVisibility(View.GONE);
                                anunciosElements.setVazioVisibility(View.GONE);
                                anunciosElements.setLayoutConexao(View.VISIBLE);
                                anunciosElements.setAnuncios(new ArrayList<>());
                                data.setValue(anunciosElements);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            anunciosElements.setListaVisibility(View.GONE);
                            anunciosElements.setProgressVisibility(View.GONE);
                            anunciosElements.setVazioVisibility(View.GONE);
                            anunciosElements.setLayoutConexao(View.VISIBLE);
                            anunciosElements.setAnuncios(new ArrayList<>());
                            data.setValue(anunciosElements);
                        }
                    });
        } else {
            anunciosElements.setListaVisibility(View.GONE);
            anunciosElements.setProgressVisibility(View.GONE);
            anunciosElements.setLayoutConexao(View.VISIBLE);
            anunciosElements.setVazioVisibility(View.GONE);
            anunciosElements.setAnuncios(new ArrayList<>());
            data.setValue(anunciosElements);
        }


    }

    public static void update(MutableLiveData<AnunciosElements> data, Context context) {
        getFirebaseData(data, context);
    }
}
