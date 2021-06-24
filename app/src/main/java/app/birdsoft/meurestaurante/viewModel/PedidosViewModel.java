package app.birdsoft.meurestaurante.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.model.PedidosElements;
import app.birdsoft.meurestaurante.repository.PedidosRepository;

public class PedidosViewModel extends ViewModel {
    private MutableLiveData<PedidosElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = PedidosRepository.getInstance(context);
    }

    public MutableLiveData<PedidosElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            PedidosRepository.update(mutableLiveData, context);
        }

    }

    public LiveData<Boolean> cancelamento(Pedido pedido, String mensagem, Context context){
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        PedidosRepository.cancelamento(pedido, mensagem, data, context);
        return data;
    }

    public LiveData<Boolean> relatarAtraso(Pedido pedido, Context context){
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        PedidosRepository.relatarAtraso(pedido, data, context);
        return data;
    }

    public LiveData<Boolean> putPedido(Pedido pedido, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        PedidosRepository.putPedido(pedido, data, context);
        return data;
    }

    public LiveData<Boolean> confirmarpedido(Pedido pedido, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        PedidosRepository.finalizarPedido(pedido, data, context);
        return data;
    }

    public LiveData<Boolean> recebi(Pedido pedido, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        PedidosRepository.recebi(pedido, data, context);
        return data;
    }

    public LiveData<Boolean> isPedido(Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        PedidosRepository.isPedido(context, data);
        return data;
    }
}
