package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.HistoricoElements;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.repository.HistoricosRepository;

public class HistoricosViewModel extends ViewModel {
    private MutableLiveData<HistoricoElements> mutableLiveData;

    public void init(Context contextx){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = HistoricosRepository.getInstance(contextx);
    }

    public MutableLiveData<HistoricoElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            HistoricosRepository.update(mutableLiveData, context);
        }

    }

    public LiveData<Boolean> delete(Pedido pedido, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        HistoricosRepository.delete(pedido, data, context);
        return data;
    }
}
