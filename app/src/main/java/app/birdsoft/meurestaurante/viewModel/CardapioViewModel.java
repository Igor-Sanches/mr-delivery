package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.CardapioElements;
import app.birdsoft.meurestaurante.repository.CardapioRepository;

public class CardapioViewModel extends ViewModel {
    private MutableLiveData<CardapioElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = CardapioRepository.getInstance(context);
    }

    public MutableLiveData<CardapioElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            CardapioRepository.update(mutableLiveData, context);
        }

    }

}
