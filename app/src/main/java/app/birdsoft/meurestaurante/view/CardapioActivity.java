package app.birdsoft.meurestaurante.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.viewModel.SettingsViewModel;
import app.birdsoft.meurestaurante.widget.MyToast;

public class CardapioActivity extends AppCompatActivity {

    public NavController navController;
    private TextView contador_carrinho;
    private LinearLayout endereco_changed;
    @SuppressLint("StaticFieldLeak")
    private static CardapioActivity instance;
    public static CardapioActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        instance = this;
        endereco_changed = findViewById(R.id.endereco_changed);
        contador_carrinho = findViewById(R.id.contador_carrinho);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> endereco_changed.setVisibility(destination.getId() == R.id.navigation_menu_principal ? View.VISIBLE : View.GONE));
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

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId() != R.id.navigation_menu_principal){
            navController.popBackStack();
            return;
        }
        super.onBackPressed();
    }

    public void oncarrinho(View view) {
        if(HelperManager.isClicked()){
            startActivity(new Intent(this, CarrinhoActivity.class));
            if(navController.getCurrentDestination().getId() != R.id.navigation_menu_principal){
                navController.popBackStack();
            }
            HelperManager.clieckTime = 0;
        }
    }

    @Override
    protected void onResume() {
        ((TextView)findViewById(R.id.localizacao)).setText(Usuario.getEndereco(this));
        super.onResume();
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
}