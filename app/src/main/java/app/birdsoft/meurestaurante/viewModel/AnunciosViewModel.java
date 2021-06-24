package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.AnunciosElements;
import app.birdsoft.meurestaurante.repository.AnunciosRepository;

public class AnunciosViewModel extends ViewModel {
    private MutableLiveData<AnunciosElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = AnunciosRepository.getInstance(context);
    }

    public MutableLiveData<AnunciosElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            AnunciosRepository.update(mutableLiveData, context);
        }

    }
}
