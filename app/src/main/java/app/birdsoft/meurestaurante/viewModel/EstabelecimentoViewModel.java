package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.EstabelecimentoElements;
import app.birdsoft.meurestaurante.repository.EstabelecimentoRepository;

public class EstabelecimentoViewModel extends ViewModel {
    private MutableLiveData<EstabelecimentoElements> mutableLiveData;

    public void init(Context context){
        mutableLiveData = EstabelecimentoRepository.getInstance(context);
    }

    public MutableLiveData<EstabelecimentoElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            EstabelecimentoRepository.update(mutableLiveData, context);
        }

    }
}
