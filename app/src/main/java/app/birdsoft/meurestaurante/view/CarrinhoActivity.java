package app.birdsoft.meurestaurante.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.adaptador.AdaptadorCarrinho;
import app.birdsoft.meurestaurante.adaptador.AdaptadorCuponsCheck;
import app.birdsoft.meurestaurante.dialogo.DialogMessage;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.dialogo.MainDialog;
import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.FirebaseUtils;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cliente;
import app.birdsoft.meurestaurante.model.Cupom;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.model.EstabelecimentoHorario;
import app.birdsoft.meurestaurante.model.LocalLocal;
import app.birdsoft.meurestaurante.model.NotificationData;
import app.birdsoft.meurestaurante.model.NotificationType;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.servicos.SendNotification;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.tools.Mask;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.tools.Pagamento;
import app.birdsoft.meurestaurante.tools.Status;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.viewModel.ClienteViewModel;
import app.birdsoft.meurestaurante.viewModel.CuponsViewModel;
import app.birdsoft.meurestaurante.viewModel.EstabelecimentoViewModel;
import app.birdsoft.meurestaurante.viewModel.PedidosViewModel;
import app.birdsoft.meurestaurante.viewModel.SettingsViewModel;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class CarrinhoActivity extends AppCompatActivity {

    private CarrinhoViewModel viewModel;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private EditText msg_observacao;
    private TextView valorText, valor_text_antigo, cupom_codigo_ticked, cupom_mensagem_ticked, domingo, segunda, terca, quarta, quinta, sexta, sabado, error_localization, endereco_cliente, valor_troco, previsao_de_entrega, distancia_pedido, frete_pedido;
    private RadioButton op_card_credito, op_card_debito, op_dinheiro;
    private int taxa;
    private boolean isBloqueado;
    private AdaptadorCarrinho adaptador;
    private int km;
    private double valorPerKm;
    private double valorFixo;
    private LocalLocal localLocal;
    private Cliente cliente;
    private double valorTroco = 0;
    private Cupom cupom;
    private int prazoType = 0;
    private int minutoPrazo = 0;
    private RelativeLayout recido_antigo;
    private String enderecoCliente;
    private double valorFrete = 0, valorTotal = 0, valorFreteReserve = 0;
    private LatLng minhaLocalizacao, localizacaoDoEstalecimento;
    private NestedScrollView layout_estabelecimento_fechado;
    private String numeroTelefone;
    private LinearLayout listaLayout, layout_conexao_error, layout_cupom_ticked, lyt_progress_endereco, layout_endereco_visualizador, layout_troco, vazio, lyt_progress, layout_wifi_error, layout_localization_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        CuponsViewModel cuponsViewModel = new ViewModelProvider(this).get(CuponsViewModel.class);
        runOnUiThread(()-> cuponsViewModel.init(this));
        cuponsViewModel.update(this);
        viewModel = new ViewModelProvider(this).get(CarrinhoViewModel.class);
        runOnUiThread(()-> viewModel.init(this));
        EstabelecimentoViewModel esViewModel = new ViewModelProvider(this).get(EstabelecimentoViewModel.class);
        runOnUiThread(()-> esViewModel.init(this));
        ClienteViewModel clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        runOnUiThread(()-> clienteViewModel.init(this));
        adaptador = new AdaptadorCarrinho(new ArrayList<>(), this);
        domingo = findViewById(R.id.domingo);
        recido_antigo = findViewById(R.id.recido_antigo);
        segunda = findViewById(R.id.segunda);
        layout_conexao_error  =findViewById(R.id.layout_conexao_error);
        layout_cupom_ticked = findViewById(R.id.layout_cupom_ticked);
        cupom_codigo_ticked = findViewById(R.id.cupom_codigo_ticked);
        cupom_mensagem_ticked = findViewById(R.id.cupom_mensagem_ticked);
        valorText = findViewById(R.id.valorText);
        valor_text_antigo = findViewById(R.id.valor_text_antigo);
        terca = findViewById(R.id.terca);
        quarta = findViewById(R.id.quarta);
        quinta = findViewById(R.id.quinta);
        sexta = findViewById(R.id.sexta);
        sabado = findViewById(R.id.sabado);
        layout_endereco_visualizador = findViewById(R.id.layout_endereco_visualizador);
        op_dinheiro = findViewById(R.id.op_dinheiro);
        layout_troco = findViewById(R.id.layout_troco);
        endereco_cliente = findViewById(R.id.endereco_cliente);
        layout_estabelecimento_fechado = findViewById(R.id.layout_estabelecimento_fechado);
        lyt_progress_endereco = findViewById(R.id.lyt_progress_endereco);
        op_card_debito = findViewById(R.id.op_card_debito);
        op_card_credito = findViewById(R.id.op_card_credito);
        msg_observacao = findViewById(R.id.msg_observacao);
        previsao_de_entrega = findViewById(R.id.previsao_de_entrega);
        frete_pedido = findViewById(R.id.frete_pedido);
        distancia_pedido = findViewById(R.id.distancia_pedido);
        valor_troco = findViewById(R.id.valor_troco);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaLayout = findViewById(R.id.listaLayout);
        lyt_progress = findViewById(R.id.lyt_progress);
        vazio = findViewById(R.id.vazio);
        error_localization = findViewById(R.id.error_localization);
        layout_localization_error = findViewById(R.id.layout_localization_error);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        LinearLayout layout_cupons = findViewById(R.id.layout_cupons);
        RecyclerView lista_cupons = findViewById(R.id.lista_cupons);
        lista_cupons.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorCuponsCheck check = new AdaptadorCuponsCheck(this, new ArrayList<>());
        lista_cupons.setAdapter(check);
        check.setOnItemCheckedChanged((view, cupom, checked, position)->{
            if(checked.isChecked()){
                if(cupom.isMininum()){
                    if(cupom.getValorMinimo() <= valorTotal){
                        removerCupom();
                        adicionarCupom(cupom);
                    }else{
                        if(cupom.getDescontoType() != 0){
                            MyToast.makeText(this, getString(R.string.valor_minimo_utrapasado) + " " + Mask.formatarValor(cupom.getValorMinimo()), ModoColor._falha).show();
                            checked.setChecked(false);
                            removerCupom();
                        }else {
                            removerCupom();
                            adicionarCupom(cupom);
                        }
                    }
                }else adicionarCupom(cupom);
           }else removerCupom();
        });
        cuponsViewModel.getMutableLiveData().observe(this, (cuponsElements -> {
            check.setInsert(cuponsElements.getCupoms());
            layout_cupons.setVisibility(cuponsElements.getListaVisibility());
        }));
        mRecyclerView.setAdapter(adaptador);
        esViewModel.getMutableLiveData().observe(this, (estabelecimentoElements -> {
            if(estabelecimentoElements.isAberto()){
                localizacaoDoEstalecimento = new LatLng(estabelecimentoElements.getGeoPoint().getLatitude(), estabelecimentoElements.getGeoPoint().getLongitude());
                numeroTelefone = HelperManager.getNumero(estabelecimentoElements.getLigacao());
                km = estabelecimentoElements.getKm();
                prazoType = estabelecimentoElements.getPrazo();
                minutoPrazo = estabelecimentoElements.getPrazoMinutoFixo();
                taxa = estabelecimentoElements.getTaxa();
                valorFixo = estabelecimentoElements.getValorFixo();
                valorPerKm = estabelecimentoElements.getValorPerKm();
                onBuscarMinhaLocalizacao();
            }else{
                inflatelayoutEstabelecimentoFechado(estabelecimentoElements.getHorarios());
                listaLayout.setVisibility(View.GONE);
                vazio.setVisibility(View.GONE);
                lyt_progress.setVisibility(View.GONE);
                layout_conexao_error.setVisibility(estabelecimentoElements.getLayoutConexao());
                layout_wifi_error.setVisibility(estabelecimentoElements.getLayoutWifiOffline());
                layout_localization_error.setVisibility(View.GONE);
                layout_estabelecimento_fechado.setVisibility(View.VISIBLE);
            }
        }));
        clienteViewModel.getMutableLiveData().observe(this, (clienteElements -> {
            if(clienteElements.getClinte() != null){
                cliente = clienteElements.getClinte();
                isBloqueado = clienteElements.isBloqueado();
                enderecoCliente = clienteElements.getEndereco();
                endereco_cliente.setText(enderecoCliente);
                lyt_progress_endereco.setVisibility(View.GONE);
                layout_endereco_visualizador.setVisibility(View.VISIBLE);
            }
        }));

        op_dinheiro.setOnCheckedChangeListener((buttonView, isChecked) -> layout_troco.setVisibility(isChecked ? View.VISIBLE : View.GONE));
    }

    private void removerCupom() {
        this.cupom = null;
        cupom_codigo_ticked.setText("");
        String msg = "";
        cupom_mensagem_ticked.setText(msg);
        layout_cupom_ticked.setVisibility(View.GONE);
        inputValor();
    }

    private void adicionarCupom(Cupom cupom) {
        this.cupom = cupom;
        cupom_codigo_ticked.setText(String.format("%s (%s)", getString(R.string.cupom), cupom.getCodigo()));
        String msg;
        if(cupom.getDescontoType() == 0){
            msg = getString(R.string.cupom_frete_gratis_apricado);
        }else if(cupom.getDescontoType() == 1){
            if(cupom.isMininum()){
                msg = ((long)cupom.getValorDesconto())+"% " + getString(R.string.cupom_por_acima) + " " + Mask.formatarValor(cupom.getValorMinimo());
            }else msg = ((long)cupom.getValorDesconto())+"% " + getString(R.string.cupom_por);
        }else{
            if(cupom.isMininum()){
                msg = Mask.formatarValor(cupom.getValorDesconto())+" " + getString(R.string.cupom_por_acima) + " " + Mask.formatarValor(cupom.getValorMinimo());
            }else msg = Mask.formatarValor(cupom.getValorDesconto()) +" " + getString(R.string.cupom_por);
        }
        cupom_mensagem_ticked.setText(msg);
        layout_cupom_ticked.setVisibility(View.VISIBLE);
        inputValor();
    }

    private synchronized void onIniciarGeolocalizacao() {
        if(Conexao.isConnected(this)){
            final LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder =
                    new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(this);

            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(this,
                    locationSettingsResponse -> onIniciarBuscar());

            task.addOnFailureListener(this, e -> {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(CarrinhoActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                            onIniciarBuscar();
                        } catch (IntentSender.SendIntentException sendEx) {
                            new AlertDialog.Builder(CarrinhoActivity.this)
                                    .setMessage(getString(R.string.gps_nescessario))
                                    .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss()).setOnDismissListener(dialog -> finish()).show();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        onIniciarBuscar();
                        break;
                }
            });
        }else{
            listaLayout.setVisibility(View.GONE);
            vazio.setVisibility(View.GONE);
            lyt_progress.setVisibility(View.GONE);
            layout_wifi_error.setVisibility(View.VISIBLE);
            layout_localization_error.setVisibility(View.GONE);
            layout_estabelecimento_fechado.setVisibility(View.GONE);
        }
    }

    private void onBuscarMinhaLocalizacao() {
        if (Build.VERSION.SDK_INT < 23) {
            onIniciarGeolocalizacao();
            return;
        }
        try {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } catch (Exception x) {
            onIniciarGeolocalizacao();
        }
    }

    private void onIniciarBuscar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            System.out.println("onComplete");
            try {
                if (task.isSuccessful()) {
                    Location result = task.getResult();
                    minhaLocalizacao = new LatLng(result.getLatitude(), result.getLongitude());
                    calcularDistanciaAsync(minhaLocalizacao);
                }
            } catch (Exception x) {
                falhaGPS();
            }
        });
    }

    private void falhaGPS() {
        listaLayout.setVisibility(View.GONE);
        vazio.setVisibility(View.GONE);
        lyt_progress.setVisibility(View.GONE);
        layout_wifi_error.setVisibility(View.GONE);
        layout_localization_error.setVisibility(View.VISIBLE);
        layout_estabelecimento_fechado.setVisibility(View.GONE);
        error_localization.setText(getString(R.string.erro_gps));
    }

    private void calcularDistanciaAsync(LatLng minhaLocalizacao) {
        new calcularDistanciaAsyncTask(this).execute(minhaLocalizacao);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onIniciarGeolocalizacao();
            }
        } catch (Exception x) {
            //message(x.getMessage());
        }
    }

    private void inflatelayoutEstabelecimentoFechado(List<EstabelecimentoHorario> horarios) {
        List<EstabelecimentoHorario> horarioList;
        if(horarios == null){
            horarioList = new EstabelecimentoHorario().getList();
        }else horarioList = horarios;
        String domingo = horarioList.get(0).isAberto() ? horarioList.get(0).getHorarioAbrir() + " - " + horarioList.get(0).getHorarioFechar() : null;
        this.domingo.setText(domingo == null ? getString(R.string.fechado) : domingo);
        String segunda = horarioList.get(1).isAberto() ? horarioList.get(1).getHorarioAbrir() + " - " + horarioList.get(1).getHorarioFechar() : null;
        this.segunda.setText(segunda == null ? getString(R.string.fechado) : segunda);
        String terca = horarioList.get(2).isAberto() ? horarioList.get(2).getHorarioAbrir() + " - " + horarioList.get(2).getHorarioFechar() : null;
        this.terca.setText(terca == null ? getString(R.string.fechado) : terca);
        String quarta = horarioList.get(3).isAberto() ? horarioList.get(3).getHorarioAbrir() + " - " + horarioList.get(3).getHorarioFechar() : null;
        this.quarta.setText(quarta == null ? getString(R.string.fechado) : quarta);
        String quinta = horarioList.get(4).isAberto() ? horarioList.get(4).getHorarioAbrir() + " - " + horarioList.get(4).getHorarioFechar() : null;
        this.quinta.setText(quinta == null ? getString(R.string.fechado) : quinta);
        String sexta = horarioList.get(5).isAberto() ? horarioList.get(5).getHorarioAbrir() + " - " + horarioList.get(5).getHorarioFechar() : null;
        this.sexta.setText(sexta == null ? getString(R.string.fechado) : sexta);
        String sabado = horarioList.get(6).isAberto() ? horarioList.get(6).getHorarioAbrir() + " - " + horarioList.get(6).getHorarioFechar() : null;
        this.sabado.setText(sabado == null ? getString(R.string.fechado) : sabado);
    }

    public void onBack(View view) { onBackPressed(); }

    public void onInputTroco(View view) {
        MainDialog dialog = new MainDialog();
        dialog.generateCurrencyAmountKeyboardDialog(this, valorTroco);
        dialog.setOnConfirmationClick((valor, real) -> {
            valor_troco.setText(real);
            valorTroco = valor;
        });
    }

    public void onEditarEndereco(View view) {
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
                        this.endereco_cliente.setText(Usuario.getEndereco(this));
                        enderecoCliente = Usuario.getEndereco(this);
                        cliente.setEndereco(Usuario.getEndereco(this));
                        MyToast.makeText(this, R.string.troca_endereco, ModoColor._success).show();
                        new ViewModelProvider(this).get(SettingsViewModel.class).update(this);
                    }else{
                        MyToast.makeText(this, R.string.error_troca_endereco, ModoColor._falha).show();
                    }
                    dialog.dismiss();
                }));
            }
        }));
    }

    public void onFinalizarPedido(View view) {
        if(isBloqueado){
            Intent intent = new Intent(this, BloqueadoActivity.class);
            intent.putExtra("msg", cliente.getMsgBlock());
            startActivity(intent);
            finish();
            return;
        }
        if(op_dinheiro.isChecked() || op_card_credito.isChecked() || op_card_debito.isChecked()){

            if(localLocal == null) localLocal = new LocalLocal(minhaLocalizacao, localizacaoDoEstalecimento);

            Pedido pedido = new Pedido();
            String uid = UUID.randomUUID().toString();
            pedido.setClienteNome(cliente.getNome());
            pedido.setDataPedido(System.currentTimeMillis());
            pedido.setDisplayName("Pedido: " + DateTime.toDateString("dd MMM yyyy"));
            pedido.setEndereco(enderecoCliente);
            pedido.setItensPedido(adaptador.getLista());
            if(cliente.getLocal() != null){
                pedido.setCoordenadas(minhaLocalizacao.latitude + ", " + minhaLocalizacao.longitude);
            }
            pedido.setPagamento(getPagamentos());
            pedido.setObservacao(msg_observacao.getText().toString().trim());
            pedido.setStatusPedido(Status.novoPedido.toString());
            pedido.setTelefone(numeroTelefone);
            pedido.setTroco(op_dinheiro.isChecked() && valorTroco != (double) 0);
            pedido.setValorTroco(valorTroco);
            pedido.setValorTotal(valorTotal);
            pedido.setUid_client(cliente.getUuid());
            pedido.setPrazo(HelperManager.getPrazoEstConvertForInt(prazoType, minutoPrazo, localLocal));
            pedido.setUid(uid);
            pedido.setFreteGratis(valorFrete == 0);
            pedido.setDistancia(distancia_pedido.getText().toString().trim());
            pedido.setFrete(valorFrete);
            pedido.setCoordenadas(minhaLocalizacao.latitude +", "+ minhaLocalizacao.longitude);
            pedido.setValorComFrete((valorFrete + valorTotal));
            pedido.setCupom(cupom);
            PedidosViewModel pedidosViewModel = new ViewModelProvider(this).get(PedidosViewModel.class);
            pedidosViewModel.init(this);
            LoadingDialog loading = new LoadingDialog(this);
            loading.show();
            pedidosViewModel.isPedido(this).observe(this, (existe -> {
                if(!existe){
                    new ViewModelProvider(this).get(PedidosViewModel.class).putPedido(pedido, this).observe(this, (aBoolean -> {
                        if(aBoolean){
                            startActivity(new Intent(CarrinhoActivity.this, AcompanhamentoPedidoActivity.class));
                            finish();
                            if(CardapioActivity.getInstance() != null)
                                CardapioActivity.getInstance().finish();

                            FireStoreUtils
                                    .getDatabase()
                                    .collection(Chave.Usuario)
                                    .document(pedido.getUid_client())
                                    .update("local", new GeoPoint(minhaLocalizacao.latitude, minhaLocalizacao.longitude));
                            FirebaseUtils.getDatabaseRef().child(Chave.CARRINHO).child(Settings.getUID(this)).child(pedido.getUid_client()).removeValue();
                            MyToast.makeText(CarrinhoActivity.this, R.string.meg_pedido_enviado, ModoColor._success).show();
                            NotificationData data = new NotificationData(getString(R.string.novo_pedido), getString(R.string.novo_pedido_recebido), pedido.getUid_client(), uid, pedido.getStatusPedido(), NotificationType.pedido);
                            new SendNotification().onPush(data, this);
                        }else{
                            MySnackbar.makeText(this, R.string.falha_enviar_pedido, ModoColor._falha).show();
                            loading.dismiss();
                        }
                    }));
                }else{
                    loading.dismiss();
                    DialogMessage message
                            = new DialogMessage(this, getString(R.string.falha_enviar_pedido_aguarde), false, "");
                    message.show();
                }
            }));

        }else{
            MySnackbar.makeText(this, R.string.error_pagamento_selecionar, ModoColor._falha).show();
        }
    }

    private String getPagamentos() {
        String res;
        if(op_dinheiro.isChecked()){
            res = Pagamento.dinheiro.toString();
        }else if(op_card_credito.isChecked()){
            res  = Pagamento.cartao_credito.toString();
        }else{ res = Pagamento.cartao_debido.toString(); }
        return res;
    }

    public void onUpdateConnection(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        new ViewModelProvider(this).get(CuponsViewModel.class).update(this);
        new ViewModelProvider(this).get(EstabelecimentoViewModel.class).update(this);
        new ViewModelProvider(this).get(ClienteViewModel.class).update(this);
        viewModel.update(this);
    }

    @SuppressLint("StaticFieldLeak")
    private class calcularDistanciaAsyncTask extends AsyncTask<LatLng, Void, Address> {
        private final Context context;
        public calcularDistanciaAsyncTask(Context context) {
            this.context = context;
        }

        public Address doInBackground(LatLng... latLngArr) {
            LatLng latLng = latLngArr[0];
            try {
                List fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (fromLocation != null && fromLocation.size() > 0) {
                    return (Address) fromLocation.get(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Address address) {
            if (address != null) {
                minhaLocalizacao = new LatLng(address.getLatitude(), address.getLongitude());
                iniciarDados();
            }
        }
    }

    private void iniciarDados() {
        localLocal = new LocalLocal(minhaLocalizacao, localizacaoDoEstalecimento);
        long distancia = localLocal.getDistanciaLong();
        if(distancia <= ((km * 1000) + 200)) {
            if(taxa == 0){
                frete_pedido.setText(getString(R.string.frete_gratis));
                valorFrete = 0;
                valorFreteReserve = valorFrete;
            }else if(taxa == 1){
                valorFrete = valorFixo;
                valorFreteReserve = valorFrete;
                frete_pedido.setText(Mask.formatarValor(valorFrete));
            }else{
                if(localLocal.getDistanciaDouble() == 0.0){
                    frete_pedido.setText(getString(R.string.frete_gratis));
                    valorFrete = 0;
                    valorFreteReserve = valorFrete;
                } else{
                    valorFrete = valorPerKm * localLocal.getDistanciaDouble();
                    valorFreteReserve = valorFrete;
                    frete_pedido.setText(Mask.formatarValor(valorFrete));
                }
            }
            distancia_pedido.setText(localLocal.getDistancia());
            previsao_de_entrega.setText(HelperManager.getPrazoEst(prazoType, minutoPrazo, localLocal, this));
            viewModel.getMutableLiveData().observe(this, (carrinhoElements -> {
                if(carrinhoElements.isCarrinho()){
                    adaptador.insert(carrinhoElements.getCarrinhos());
                    adaptador.setOnDeleteItemListener((v, carrinho, position) -> viewModel.delete(carrinho, this).observe(this, (integer -> {
                        switch (integer){
                            case 1:
                                MySnackbar.makeText(this, R.string.removido_carrinho_erro, ModoColor._falha).show();
                                break;
                            case 2:
                                MySnackbar.makeText(this, R.string.removido_carrinho_sucesso, ModoColor._success).show();
                                inputValor();
                                break;
                        }
                    })));
                }
                inputValor();
                listaLayout.setVisibility(carrinhoElements.getListaVisibility());
                vazio.setVisibility(carrinhoElements.getVazioVisibility());
                lyt_progress.setVisibility(carrinhoElements.getProgressVisibility());
                layout_conexao_error.setVisibility(carrinhoElements.getLayoutConexao());
                layout_wifi_error.setVisibility(carrinhoElements.getLayoutWifiOffline());
                layout_localization_error.setVisibility(View.GONE);
                layout_estabelecimento_fechado.setVisibility(View.GONE);
            }));
        }else{
            listaLayout.setVisibility(View.GONE);
            vazio.setVisibility(View.GONE);
            lyt_progress.setVisibility(View.GONE);
            layout_wifi_error.setVisibility(View.GONE);
            layout_localization_error.setVisibility(View.VISIBLE);
            layout_estabelecimento_fechado.setVisibility(View.GONE);
            error_localization.setText(String.format("%s%s Km %s", getString(R.string.erro_gps_distancia), km, getString(R.string.erro_gps_distancia_2)));
        }
    }

    private void inputValor(){
        valorTotal = adaptador.getValor();
        if(cupom != null){
            if(cupom.getDescontoType() == 2){
                valorTotal = valorTotal - cupom.getValorDesconto();
                valorFrete = valorFreteReserve;
                valor_text_antigo.setText(Mask.formatarValor(adaptador.getValor()));
                valor_text_antigo.setVisibility(View.VISIBLE);
                recido_antigo.setVisibility(View.VISIBLE);
            }else if(cupom.getDescontoType() == 1){
                double percentual = cupom.getValorDesconto() / 100;
                double valorFinal = percentual * valorTotal;
                valorTotal = valorTotal - valorFinal;
                valorFrete = valorFreteReserve;
                valor_text_antigo.setText(Mask.formatarValor(adaptador.getValor()));
                valor_text_antigo.setVisibility(View.VISIBLE);
                recido_antigo.setVisibility(View.VISIBLE);
            }else {
                valorFrete = 0;
                frete_pedido.setText(Mask.formatarValor(valorFrete));
                valor_text_antigo.setText("");
                valor_text_antigo.setVisibility(View.GONE);
                layout_cupom_ticked.setVisibility(View.GONE);
                recido_antigo.setVisibility(View.GONE);
            }
        }else {
            valorFrete = valorFreteReserve;
            frete_pedido.setText(Mask.formatarValor(valorFrete));
            valor_text_antigo.setText("");
            recido_antigo.setVisibility(View.GONE);
            layout_cupom_ticked.setVisibility(View.GONE);
            valor_text_antigo.setVisibility(View.GONE);
        }
        if(valorFrete == 0.0 || valorFrete == 0){
            frete_pedido.setText(R.string.frete_gratis);
        }
        //valorTotal += valorFrete;
        valorText.setText(Mask.formatarValor(valorTotal + valorFrete));
    }

}