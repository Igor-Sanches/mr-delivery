package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.model.Estabelecimento;
import app.birdsoft.meurestaurante.model.EstabelecimentoElements;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;

public class EstabelecimentoRepository {
    public static MutableLiveData<EstabelecimentoElements> getInstance(Context context) {
        return GetElements(context);
    }

    private synchronized static MutableLiveData<EstabelecimentoElements> GetElements(Context context) {
        MutableLiveData<EstabelecimentoElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<EstabelecimentoElements> data, Context context) {
        EstabelecimentoElements estabelecimentoElements = new EstabelecimentoElements();
        if(Conexao.isConnected(context)){
            FireStoreUtils
                    .getDatabaseOnline()
                    .collection(Chave.ESTABELECIMENTO)
                    .document(Settings.getUID(context))
                    .collection(Chave.ESTABELECIMENTO)
                    .document(Chave.DADOS)
                    .get().addOnCompleteListener((documento ->{
                if(documento.isSuccessful()){
                    if (documento.getResult() != null) {
                        Estabelecimento estabelecimento = documento.getResult().toObject(Estabelecimento.class);
                        if(estabelecimento != null){
                            estabelecimentoElements.setAberto(estabelecimento.isAberto());
                            estabelecimentoElements.setEndereco(estabelecimento.getEndereco() == null ? "" : estabelecimento.getEndereco());
                            estabelecimentoElements.setGeoPoint(estabelecimento.getLocal());
                            estabelecimentoElements.setHorarios(estabelecimento.getHorarios() == null ? HelperManager.drive(null) : estabelecimento.getHorarios());
                            estabelecimentoElements.setLigacao(estabelecimento.getTelefone() == null ? "" : estabelecimento.getTelefone());
                            estabelecimentoElements.setWhatsapp(estabelecimento.getWhatsapp() == null ? "" : estabelecimento.getWhatsapp());
                            estabelecimentoElements.setKm(estabelecimento.getKm() == null ? 0 : estabelecimento.getKm());
                            estabelecimentoElements.setPrazoMinutoFixo(estabelecimento.getPrazoMinutoFixo() == null ? 0 : estabelecimento.getPrazoMinutoFixo());
                            estabelecimentoElements.setTaxa(estabelecimento.getTaxa() == null ? -1 : estabelecimento.getTaxa());
                            estabelecimentoElements.setValorPerKm(estabelecimento.getValorPerKm() == null ? 0 : estabelecimento.getValorPerKm());
                            estabelecimentoElements.setValorFixo(estabelecimento.getValorFixo() == null ? 0 : estabelecimento.getValorFixo());
                            estabelecimentoElements.setPrazo(estabelecimento.getPrazo() == null ? 0 : estabelecimento.getPrazo());
                            estabelecimentoElements.setLayoutConexao(View.GONE);
                            estabelecimentoElements.setLayoutWifiOffline(View.GONE);
                            data.setValue(estabelecimentoElements);
                        }else{
                            erro(data, estabelecimentoElements);
                        }
                    }else{
                        erro(data, estabelecimentoElements);
                    }
                }else{
                    estabelecimentoElements.setAberto(false);
                    estabelecimentoElements.setEndereco(null);
                    estabelecimentoElements.setGeoPoint(null);
                    estabelecimentoElements.setHorarios(null);
                    estabelecimentoElements.setLigacao(null);
                    estabelecimentoElements.setLayoutConexao(View.VISIBLE);
                    estabelecimentoElements.setPrazoMinutoFixo(0);
                    estabelecimentoElements.setPrazo(0);
                    estabelecimentoElements.setWhatsapp(null);
                    estabelecimentoElements.setLayoutWifiOffline(View.GONE);
                    estabelecimentoElements.setLayoutFechado(View.GONE);
                    data.setValue(estabelecimentoElements);
                }

            }));
        }else{
            estabelecimentoElements.setAberto(false);
            estabelecimentoElements.setEndereco(null);
            estabelecimentoElements.setGeoPoint(null);
            estabelecimentoElements.setHorarios(null);
            estabelecimentoElements.setPrazoMinutoFixo(0);
            estabelecimentoElements.setPrazo(0);
            estabelecimentoElements.setLigacao(null);
            estabelecimentoElements.setWhatsapp(null);
            estabelecimentoElements.setLayoutWifiOffline(View.VISIBLE);
            estabelecimentoElements.setLayoutFechado(View.GONE);
            estabelecimentoElements.setLayoutConexao(View.GONE);
            data.setValue(estabelecimentoElements);
        }


    }

    private static void erro(MutableLiveData<EstabelecimentoElements> data, EstabelecimentoElements estabelecimentoElements) {
        estabelecimentoElements.setAberto(false);
        estabelecimentoElements.setEndereco(null);
        estabelecimentoElements.setGeoPoint(null);
        estabelecimentoElements.setHorarios(null);
        estabelecimentoElements.setLigacao(null);
        estabelecimentoElements.setLayoutConexao(View.GONE);
        estabelecimentoElements.setPrazoMinutoFixo(0);
        estabelecimentoElements.setPrazo(0);
        estabelecimentoElements.setWhatsapp(null);
        estabelecimentoElements.setLayoutWifiOffline(View.GONE);
        estabelecimentoElements.setLayoutFechado(View.GONE);
        data.setValue(estabelecimentoElements);
    }

    public static void update(MutableLiveData<EstabelecimentoElements> data, Context context) {
        getFirebaseData(data, context);
    }
}
