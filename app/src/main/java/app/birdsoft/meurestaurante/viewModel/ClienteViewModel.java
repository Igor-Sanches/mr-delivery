package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.ClienteElements;
import app.birdsoft.meurestaurante.repository.ClienteRepository;

public class ClienteViewModel extends ViewModel {
    private MutableLiveData<ClienteElements> mutableLiveData;

    public void init(Context context){

        mutableLiveData = ClienteRepository.getInstance(context);
    }

    public MutableLiveData<ClienteElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            ClienteRepository.update(mutableLiveData, context);
        }
    }
}
