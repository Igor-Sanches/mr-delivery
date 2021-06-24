package app.birdsoft.meurestaurante;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import app.birdsoft.meurestaurante.adaptador.AdaptadorAnuncios;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.view.AcompanhamentoPedidoActivity;
import app.birdsoft.meurestaurante.view.AppSettingsActivity;
import app.birdsoft.meurestaurante.view.CardapioActivity;
import app.birdsoft.meurestaurante.view.CarrinhoActivity;
import app.birdsoft.meurestaurante.view.CuponsActivity;
import app.birdsoft.meurestaurante.view.HistoricosActivity;
import app.birdsoft.meurestaurante.viewModel.AnunciosViewModel;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.viewModel.PedidosViewModel;
import app.birdsoft.meurestaurante.viewModel.SettingsViewModel;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class MainActivity extends AppCompatActivity {

    private LinearLayout vazia_layout, lyt_progress, layout_conexao;
    private RecyclerView lista_layout;
    private AnunciosViewModel viewModel;
    private AdaptadorAnuncios adaptador;
    private MenuItem novo, meu, historico, cupons;
    private long time = 0;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vazia_layout = findViewById(R.id.vazia_layout);
        lyt_progress = findViewById(R.id.lyt_progress);
        lista_layout = findViewById(R.id.lista_layout);
        layout_conexao = findViewById(R.id.layout_conexao_error);
        lista_layout.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorAnuncios(new ArrayList<>(), this);
        lista_layout.setAdapter(adaptador);
        viewModel = new ViewModelProvider(this).get(AnunciosViewModel.class);
        viewModel.init(this);
        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        novo = nav_view.getMenu().findItem(R.id.novopedidosBtn);
        meu = nav_view.getMenu().findItem(R.id.meuspedidosBtn);
        cupons = nav_view.getMenu().findItem(R.id.cuponsBtn);
        historico = nav_view.getMenu().findItem(R.id.historicosBtn);
        nav_view.setOnNavigationItemSelectedListener(item -> {
            if(meu != null) meu.setEnabled(false);
            if(novo != null) novo.setEnabled(false);
            if(cupons != null) cupons.setEnabled(false);
            if(historico != null) historico.setEnabled(false);
            switch (item.getItemId()){
                case R.id.cuponsBtn: navigate(CuponsActivity.class); break;
                case R.id.historicosBtn: navigate(HistoricosActivity.class); break;
                case R.id.meuspedidosBtn: navigate(AcompanhamentoPedidoActivity.class); break;
                case R.id.novopedidosBtn: navigate(CardapioActivity.class); break;
            }
            return false;
        });
        viewModel.getMutableLiveData().observe(this, anunciosElements -> {
            adaptador.insert(anunciosElements.getAnuncios());
            lista_layout.setVisibility(anunciosElements.getListaVisibility());
            vazia_layout.setVisibility(anunciosElements.getVazioVisibility());
            lyt_progress.setVisibility(anunciosElements.getProgressVisibility());
            layout_conexao.setVisibility(anunciosElements.getLayoutConexao());
        });
        PedidosViewModel vm = new ViewModelProvider(this)
                .get(PedidosViewModel.class);
        vm.init(this);
        vm.getMutableLiveData().observe(this, pedido ->{
            meu.setVisible(pedido.isPedido());
            novo.setVisible(!pedido.isPedido());
        });
        TextView contador_carrinho = findViewById(R.id.contador_carrinho);
        CarrinhoViewModel viewModel = new ViewModelProvider(this).get(CarrinhoViewModel.class);
        viewModel.init(this);
        viewModel.getMutableLiveData().observe(this, carrinhoElements -> {
            if(carrinhoElements != null){
                if(carrinhoElements.isCarrinho()){
                    if(carrinhoElements.getCount() > 0){
                        contador_carrinho.setText(String.valueOf(carrinhoElements.getCount()));
                        contador_carrinho.setVisibility(View.VISIBLE);
                    }else{
                        contador_carrinho.setText("");
                        contador_carrinho.setVisibility(View.GONE);
                    }
                }else{
                    contador_carrinho.setText("");
                    contador_carrinho.setVisibility(View.GONE);
                }
            }else{
                contador_carrinho.setText("");
                contador_carrinho.setVisibility(View.GONE);
            }
        });

    }

    public void oncarrinho(View view) {
        if(HelperManager.isClicked()){
            startActivity(new Intent(this, CarrinhoActivity.class));
        }
    }

    @Override
    protected void onResume() {
        HelperManager.clieckTime = 0;
        if(meu != null) meu.setEnabled(true);
        if(novo != null) novo.setEnabled(true);
        if(cupons != null) cupons.setEnabled(true);
        if(historico != null) historico.setEnabled(true);
        ((TextView)findViewById(R.id.localizacao)).setText(Usuario.getEndereco(this));
        viewModel.update(this);
        super.onResume();
    }

    private void navigate(Class<?> aClass) {
        //if(HelperManager.isClicked(this)){
        startActivity(new Intent(this, aClass));
        //}
    }

    public void onSettings(View view) {
        navigate(AppSettingsActivity.class);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - time > 2000){
            MySnackbar.makeText(this, R.string.clique_para_sair).show();
            time = System.currentTimeMillis();
            return;
        }
        finishAffinity();
    }

    public void onEnderecoChanged(View view) {
        if(HelperManager.isClicked()){
            HelperManager.inputEnderecoCliente(this).observe(this, (endereco1 -> {
                if(endereco1 != null){
                    LoadingDialog dialog = new LoadingDialog(this);
                    dialog.show();
                    new ViewModelProvider(this).get(SettingsViewModel.class).trocaEndereco(endereco1, this).observe(this, (sucesso -> {
                        if(sucesso){
                            Usuario.setEndereco(endereco1.getEndereco(), this);
                            Endereco _endereco = new Endereco();
                            _endereco.setEndereco(endereco1.getEndereco());
                            _endereco.setBairro(endereco1.getBairro());
                            _endereco.setNomeRuaNumero(endereco1.getNomeRuaNumero());
                            _endereco.setNumeroCasa(endereco1.getNumeroCasa());
                            _endereco.setReferencia(endereco1.getReferencia());
                            _endereco.setCidade(endereco1.getCidade());
                            _endereco.setSn(endereco1.isSn());
                            _endereco.setTipo_lugar(endereco1.getTipo_lugar());
                            _endereco.setComplemento(endereco1.getComplemento());
                            _endereco.setBloco_n_ap(endereco1.getBloco_n_ap());
                            Settings.setEndereco(_endereco, this);
                            ((TextView)findViewById(R.id.localizacao)).setText(Usuario.getEndereco(this));
                            MyToast.makeText(this, R.string.troca_endereco, ModoColor._success).show();
                            new ViewModelProvider(this).get(SettingsViewModel.class).update(this);
                        }else{
                            MyToast.makeText(this, R.string.error_troca_endereco, ModoColor._falha).show();
                        }
                        dialog.dismiss();
                    }));
                }
                HelperManager.clieckTime = 0;
            }));

        }
    }

    public void onUpdate(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        viewModel.update(this);
    }
}