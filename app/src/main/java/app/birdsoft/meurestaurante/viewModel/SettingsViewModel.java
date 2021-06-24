package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.model.SettingsElements;
import app.birdsoft.meurestaurante.repository.SettingsRepository;

public class SettingsViewModel extends ViewModel {
    private MutableLiveData<SettingsElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = SettingsRepository.getInstance(context);
    }

    public MutableLiveData<SettingsElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            SettingsRepository.update(mutableLiveData, context);
        } else init(context);
    }

    public LiveData<Boolean> trocaNome(String nome, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        SettingsRepository.trocaNome(nome, context, data);
        return data;
    }

    public LiveData<Boolean> trocaEmail(String email, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        SettingsRepository.trocaEmail(email, context, data);
        return data;
    }

    public LiveData<Boolean> trocaTelefone(String telefone, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        SettingsRepository.trocaTelefone(telefone, context, data);
        return data;
    }

    public LiveData<Boolean> trocaEndereco(Endereco endereco, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        SettingsRepository.trocaEndereco(endereco, context, data);
        return data;
    }
}
