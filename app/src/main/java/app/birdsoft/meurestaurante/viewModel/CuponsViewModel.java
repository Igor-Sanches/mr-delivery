package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.Cupom;
import app.birdsoft.meurestaurante.model.CuponsElements;
import app.birdsoft.meurestaurante.repository.CuponsRepository;

public class CuponsViewModel extends ViewModel {
    private MutableLiveData<CuponsElements> mutableLiveData;

    public void init(Context context){

        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = CuponsRepository.getInstance(context);
    }

    public MutableLiveData<CuponsElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){

            CuponsRepository.update(mutableLiveData, context);
        }

    }

    public LiveData<Integer> onFind(String cupom, Context context) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        CuponsRepository.onFind(cupom, data, context);
        return data;
    }

    public LiveData<Boolean> onRemove(Cupom cupom, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        CuponsRepository.onRemove(cupom, data, context);
        return data;
    }
}
