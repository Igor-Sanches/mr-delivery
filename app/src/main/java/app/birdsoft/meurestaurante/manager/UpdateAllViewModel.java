package app.birdsoft.meurestaurante.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import app.birdsoft.meurestaurante.viewModel.AnunciosViewModel;
import app.birdsoft.meurestaurante.viewModel.CardapioViewModel;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.viewModel.ClienteViewModel;
import app.birdsoft.meurestaurante.viewModel.CuponsViewModel;
import app.birdsoft.meurestaurante.viewModel.EstabelecimentoViewModel;
import app.birdsoft.meurestaurante.viewModel.HistoricosViewModel;
import app.birdsoft.meurestaurante.viewModel.PedidosViewModel;
import app.birdsoft.meurestaurante.viewModel.SettingsViewModel;

public class UpdateAllViewModel extends AsyncTask<ViewModelStoreOwner, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    public UpdateAllViewModel(Context context){
        this.context = context;
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(ViewModelStoreOwner... owner) {
        new ViewModelProvider(owner[0]).get(AnunciosViewModel.class).init(context);          //Atualizar os anúncios
        new ViewModelProvider(owner[0]).get(CarrinhoViewModel.class).init(context);   //Atualizar o carrinho
        new ViewModelProvider(owner[0]).get(CardapioViewModel.class).init(context);          //Atualizar o cardapio
        new ViewModelProvider(owner[0]).get(ClienteViewModel.class).init(context);    //Atualizar o cliente
        new ViewModelProvider(owner[0]).get(CuponsViewModel.class).init(context);     //Atualizar os cupons
        new ViewModelProvider(owner[0]).get(EstabelecimentoViewModel.class).init(context);   //Atualizar o estabelecimento
        new ViewModelProvider(owner[0]).get(HistoricosViewModel.class).init(context); //Atualizar os históricos
        new ViewModelProvider(owner[0]).get(PedidosViewModel.class).init(context);    //Atualizar os pedidos
        new ViewModelProvider(owner[0]).get(SettingsViewModel.class).init(context);   //Atualizar as configurações
        return null;
    }
}
