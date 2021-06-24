package app.birdsoft.meurestaurante.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.adaptador.AdaptadorHistoricos;
import app.birdsoft.meurestaurante.dialogo.DialogMessage;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.tools.Mask;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.viewModel.HistoricosViewModel;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class
HistoricosActivity extends AppCompatActivity {

    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;
    private AdaptadorHistoricos adaptador;
    private LinearLayout lyt_progress, layout_conexao_error, layout_wifi_error, listaLayout, vazio;
    private HistoricosViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicos);
        View sheetBottom = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
        adaptador = new AdaptadorHistoricos(this, new ArrayList<>());
        lyt_progress = findViewById(R.id.lyt_progress);
        listaLayout = findViewById(R.id.listaLayout);
        vazio = findViewById(R.id.vazio);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adaptador);
        viewModel = new ViewModelProvider(this).get(HistoricosViewModel.class);
        runOnUiThread(()-> viewModel.init(this));
        viewModel.getMutableLiveData().observe(this, historicoElements -> {
            adaptador.setInsert(historicoElements.getPedidos());
            listaLayout.setVisibility(historicoElements.getListaVisibility());
            lyt_progress.setVisibility(historicoElements.getProgressVisibility());
            vazio.setVisibility(historicoElements.getVazioVisibility());
            layout_conexao_error.setVisibility(historicoElements.getLayoutConexao());
            layout_wifi_error.setVisibility(historicoElements.getLayoutWifiOffline());
        });
        adaptador.setOnClickItem((view, pedido, position) -> onDialog(pedido));
    }

    private void onDialog(Pedido pedido) {
        try{
            if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.dialog_historico_item_opcao, null);
            sheetDialog = new BottomSheetDialog(this);
            sheetDialog.setCancelable(false);
            sheetDialog.setContentView(_view);
            String name;
            if(pedido.getItensPedido().size() > 1){
                name = pedido.getItensPedido().get(0).getDisplayName() + " "+getString(R.string.e_mais) + " "+ (pedido.getItensPedido().size() - 1);
            }else name = pedido.getItensPedido().get(0).getDisplayName();
            String msg="";
            msg += "Pedido: " + name + "\n" + "Cliente: " + pedido.getClienteNome() + "\nValor: " + Mask.formatarValor(pedido.getValorTotal());
            ((TextView)_view.findViewById(R.id.sobre_cliente)).setText(msg);
            _view.findViewById(R.id.adicionar_ao_carrinho).setOnClickListener((v -> {
                sheetDialog.dismiss();
                comprarNovamente(pedido);
            }));
            _view.findViewById(R.id.cancel).setOnClickListener((v->sheetDialog.dismiss()));
             _view.findViewById(R.id.deletar_do_historico).setOnClickListener((v -> {
                sheetDialog.dismiss();
                 DialogMessage message = new DialogMessage(this, getString(R.string.delete_historico_msg), true, getString(R.string.confirmar), getString(R.string.deletar_historico));
                 message.show();
                 message.setOnPossiveButtonClicked(()->{
                      LoadingDialog loadingDialog = new LoadingDialog(this);
                            loadingDialog.show();
                            viewModel.delete(pedido, this).observe(this, (success ->{
                                if(success){
                                    viewModel.update(this);
                                    MySnackbar.makeText(this, R.string.historico_deletado, ModoColor._success).show();
                                }else{
                                    MySnackbar.makeText(this, R.string.historico_deletado_erro, ModoColor._success).show();
                                }
                                loadingDialog.dismiss();
                            }));
                 });
            }));
            if(Build.VERSION.SDK_INT >= 21){
                sheetDialog.getWindow().addFlags(67108864);
            }
            ((View)_view.getParent()).setBackgroundColor(getResources().getColor(R.color.transparent));
            sheetDialog.show();

        }catch (Exception ignored){

        }
    }

    private void comprarNovamente(Pedido pedido) {
        new ViewModelProvider(this).get(CarrinhoViewModel.class)
                .insert(pedido.getItensPedido(), pedido.getUid_client(), this).observe(this, (success -> {
            if(success){
                View _view = LayoutInflater.from(this).inflate(R.layout.dialog_carrinho, null);
                androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();
                _view.findViewById(R.id.carGo).setOnClickListener(view -> {
                    dialog.dismiss();
                    startActivity(new Intent(this, CarrinhoActivity.class));
                });
                _view.findViewById(R.id.continueCar).setOnClickListener(view -> dialog.dismiss());
                if(Build.VERSION.SDK_INT >= 21){
                    dialog.getWindow().addFlags(67108864);
                }
                dialog.setView(_view);
                dialog.show();
            }else{
                MySnackbar.makeText(this, R.string.error_adicionar_carrinho).show();
            }
        }));
    }

    @Override
    protected void onResume() {
        onUpdateData();
        super.onResume();
    }

    private void onUpdateData() {
        viewModel.update(this);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onUpdateConnection(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        viewModel.update(this);
    }
}