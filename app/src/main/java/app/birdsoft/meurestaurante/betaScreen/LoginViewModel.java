package app.birdsoft.meurestaurante.betaScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public LiveData<Integer> isContaExist(String codigo){
        MutableLiveData<Integer> data = new MutableLiveData<>();
        LoginRepository.isContaExist(data, codigo);
        return data;
    }

}
