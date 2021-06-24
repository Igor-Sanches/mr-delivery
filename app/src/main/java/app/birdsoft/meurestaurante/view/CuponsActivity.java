package app.birdsoft.meurestaurante.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.adaptador.AdaptadorCupons;
import app.birdsoft.meurestaurante.dialogo.DialogMessage;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.repository.CuponsRepository;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.viewModel.CuponsViewModel;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class CuponsActivity extends AppCompatActivity {

    private AdaptadorCupons adaptador;
    private LinearLayout lyt_progress, layout_conexao_error, listaLayout, layout_wifi_error, vazio;
    private CuponsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupons);
        viewModel = new ViewModelProvider(this).get(CuponsViewModel.class);
        runOnUiThread(()-> viewModel.init(this));
        adaptador = new AdaptadorCupons(CuponsActivity.this, new ArrayList<>());
        lyt_progress = findViewById(R.id.lyt_progress);
        listaLayout = findViewById(R.id.listaLayout);
        vazio = findViewById(R.id.vazio);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adaptador);
        viewModel.getMutableLiveData().observe(this, cuponsElements -> {
            adaptador.setInsert(cuponsElements.getCupoms());
            listaLayout.setVisibility(cuponsElements.getListaVisibility());
            lyt_progress.setVisibility(cuponsElements.getProgressVisibility());
            vazio.setVisibility(cuponsElements.getVazioVisibility());
            layout_conexao_error.setVisibility(cuponsElements.getLayoutConexao());
            layout_wifi_error.setVisibility(cuponsElements.getLayoutWifiOffline());
            findViewById(R.id.layout_adicionar_cupom).setVisibility(cuponsElements.getLayoutWifiOffline() != View.VISIBLE && cuponsElements.getProgressVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
        });
        adaptador.setOnRemoveCupom(((cupom, position) -> {
            DialogMessage message = new DialogMessage(this, getString(R.string.remover_cupom_msg),  true, getString(R.string.remover), getString(R.string.deletar_cupom));
            message.show();
            message.setOnPossiveButtonClicked(() -> {
                LoadingDialog dialog = new LoadingDialog(this);
                dialog.show();
                viewModel.onRemove(cupom, this).observe(this, (sucesso->{
                    if(sucesso){
                        MySnackbar.makeText(this, R.string.cupom_removido, ModoColor._success).show();
                        onUpdateData();
                    }else{
                        MySnackbar.makeText(this, R.string.cupom_removido_falha, ModoColor._falha).show();
                    }
                    dialog.dismiss();
                }));
            });

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

    public void onAddCupom(View view) {
        String cupom = Objects.requireNonNull(((TextInputLayout) findViewById(R.id.inputCupom)).getEditText()).getText().toString().trim().toUpperCase();
        if(!cupom.equals("")){
            if(cupom.length() == 6){
                LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                loadingDialog.setMessage(getString(R.string.buscando_cupom));
                viewModel.onFind(cupom, this).observe(this, (result -> {
                    if(result == CuponsRepository.SUCESSO){
                        onUpdateData();
                        Objects.requireNonNull(((TextInputLayout) findViewById(R.id.inputCupom)).getEditText()).setText("");
                        MySnackbar.makeText(this, R.string.cupom_adicionado_com_sucesso, ModoColor._success).show();
                    }else{
                        String error= "";
                        switch (result){
                            case CuponsRepository.CUPOM_EXPIRADO:
                                error = getString(R.string.error_cupom_expirado);
                                break;
                            case CuponsRepository.CUPOM_NAO_ATIVO:
                                error = getString(R.string.error_cupom_nao_ativo);
                                break;
                            case CuponsRepository.CUPOM_NAO_DISPONIVEL:
                                error = getString(R.string.error_cupom_nao_disponivel);
                                break;
                            case CuponsRepository.CUPOM_NAO_DISPONIVEL_PARA_VOCE:
                                error = getString(R.string.error_cupom_nao_disponivel_vc);
                                break;
                            case CuponsRepository.CUPOM_NAO_ENCONTRADO:
                                error = getString(R.string.error_cupom_nao_encontrado);
                                break;
                            case CuponsRepository.CUPOM_JA_USADO_POR_VC:
                                error = getString(R.string.error_cupom_ja_usado_por_vc);
                                break;
                        }
                        MySnackbar.makeText(this, error, ModoColor._falha).show();
                    }
                    loadingDialog.dismiss();
                }));
            }else MySnackbar.makeText(this, R.string.erro_cupom_lenght, ModoColor._falha).show();
        }else MySnackbar.makeText(this, R.string.erro_cupom_limpo, ModoColor._falha).show();

    }

    public void onUpdateConnection(View view) {
        viewModel.update(this);
        MyToast.makeText(this, R.string.atualizando).show();
    }
}