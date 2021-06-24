package app.birdsoft.meurestaurante.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devspark.robototextview.widget.RobotoTextView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.dialogo.DialogAtrasado;
import app.birdsoft.meurestaurante.dialogo.DialogNaoAtrasado;
import app.birdsoft.meurestaurante.dialogo.DialogRecebido;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.model.NotificationData;
import app.birdsoft.meurestaurante.model.NotificationType;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.servicos.SendNotification;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.tools.Status;
import app.birdsoft.meurestaurante.viewModel.PedidosViewModel;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;
import pl.droidsonroids.gif.GifImageView;

public class AcompanhamentoPedidoActivity extends AppCompatActivity {

    private BottomSheetDialog sheetDialog;
    private Status status;
    private Pedido pedido;
    private PedidosViewModel viewModel;
    private boolean isAtrasado;
    private LinearLayout layout_principal, layout_conexao_error, layout_wifi_error, layout_relogio, error_dados, lyt_progress;
    private RobotoTextView endereco;
    private TextView previsao_de_entrega, status_pedido;
    private GifImageView imageProgress;
    private Button btn_alteracao_pedido, btn_ver_msg_cancelamento_pedido, btn_comfirmar_cancelamento, btn_finalizar_pedido, btn_recebi_pedido, btn_relatar_problemas;
    private GifImageView progress_preparando, progress_transporte, progress_final;
    private BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhamento_pedido);
        View sheetBottom = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
        viewModel = new ViewModelProvider(this).get(PedidosViewModel.class);
        runOnUiThread(()->viewModel.init(this));
        layout_principal = findViewById(R.id.layout_principal);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        btn_finalizar_pedido = findViewById(R.id.btn_finalizar_pedido);
        btn_recebi_pedido = findViewById(R.id.btn_recebi_pedido);
        error_dados = findViewById(R.id.error_dados);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        layout_relogio = findViewById(R.id.layout_relogio);
        btn_relatar_problemas = findViewById(R.id.btn_relatar_problemas);
        btn_comfirmar_cancelamento = findViewById(R.id.btn_comfirmar_cancelamento);
        lyt_progress = findViewById(R.id.lyt_progress);
        progress_preparando = findViewById(R.id.progress_preparando);
        btn_ver_msg_cancelamento_pedido = findViewById(R.id.btn_ver_msg_cancelamento_pedido);
        progress_transporte = findViewById(R.id.progress_transporte);
        progress_final = findViewById(R.id.progress_final);
        endereco = findViewById(R.id.endereco);
        previsao_de_entrega = findViewById(R.id.previsao_de_entrega);
        imageProgress = findViewById(R.id.imageProgress);
        btn_alteracao_pedido = findViewById(R.id.btn_alteracao_pedido);
        status_pedido = findViewById(R.id.status_pedido);
        runOnUiThread(() -> viewModel.getMutableLiveData().observe(this, (pedidosElements -> {
            if(sheetDialog != null){
                sheetDialog.dismiss();
            }
            if(pedidosElements.isPedido()){
                endereco.setText(pedidosElements.getEndereco());
                pedido = pedidosElements.getPedido();
                status_pedido.setText(pedidosElements.getStatus());
                status = pedidosElements.getStatusPedido();
                isAtrasado = pedidosElements.isAtrasado();
                progress_preparando.setImageResource(pedidosElements.getProgressPreparando());
                progress_transporte.setImageResource(pedidosElements.getProgressTransporte());
                progress_final.setImageResource(pedidosElements.getProgressFinal());
                previsao_de_entrega.setText(pedidosElements.getPrevisaoDeEntrega());
                Glide.with(AcompanhamentoPedidoActivity.this).load(pedidosElements.getImageLogo()).into(imageProgress);
                layout_principal.setVisibility(pedidosElements.getLayoutPrincipal());
                lyt_progress.setVisibility(pedidosElements.getLytProgress());
                layout_relogio.setVisibility(pedidosElements.getRelogioView());
                btn_ver_msg_cancelamento_pedido.setVisibility(pedidosElements.getBtnVerMsgCancelamentoPedido());
                error_dados.setVisibility(pedidosElements.getErrorDados());
                btn_alteracao_pedido.setVisibility(pedidosElements.getBtnAlteracaoPedido());
                btn_comfirmar_cancelamento.setVisibility(pedidosElements.getBtnComfirmarCancelamento());
                btn_finalizar_pedido.setVisibility(pedidosElements.getBtnFinalizarPedido());
                btn_recebi_pedido.setVisibility(pedidosElements.getBtnRecebiPedido());
                layout_conexao_error.setVisibility(pedidosElements.getLayoutConexao());
                btn_relatar_problemas.setVisibility(pedidosElements.getBtnRelatarProblemas());
            }else{
                layout_principal.setVisibility(pedidosElements.getLayoutPrincipal());
                lyt_progress.setVisibility(pedidosElements.getLytProgress());
                layout_conexao_error.setVisibility(pedidosElements.getLayoutConexao());
                error_dados.setVisibility(pedidosElements.getErrorDados());
            }
            layout_wifi_error.setVisibility(pedidosElements.getLayoutWifiOffline());

        })));
    }

    @Override
    protected void onResume() {
        viewModel.update(this);
        super.onResume();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onPedidoFinalizado(View view) {
        LoadingDialog loading = new LoadingDialog(this);
        loading.show();
      viewModel.confirmarpedido(pedido, this).observe(this, (aBoolean -> {
            if(aBoolean){
                MyToast.makeText(this, R.string.msg_confirmacao, ModoColor._success).show();
                finish();
            }else{
                MyToast.makeText(this, R.string.erro_confirmacao, ModoColor._falha).show();
            }
        }));
    }

    public void onPedidoRecebido(View view) {
        DialogRecebido recebido = new DialogRecebido(this);
        recebido.show();
        recebido.setOnClickListener(() -> {
            LoadingDialog loading = new LoadingDialog(this);
            viewModel.recebi(pedido, this).observe(this, (aBoolean -> {
                loading.dismiss();
                if(aBoolean){
                    MyToast.makeText(this, R.string.msg_confirmacao, ModoColor._success).show();
                    new SendNotification().onPush(new NotificationData(
                            getString(R.string.notification_title_confirma_recebimento),
                            getString(R.string.notification_msg_confirma_recebimento),
                            pedido.getUid_client(),
                            pedido.getUid(),
                            pedido.getStatusPedido(),
                            NotificationType.prazo), this);
                    finish();
                }else{
                    MySnackbar.makeText(this, R.string.erro_confirmacao, ModoColor._falha).show();
                }
            }));
        });
    }

    public void onConfirmarCancelamento(View view) {
       LoadingDialog loading = new LoadingDialog(this);
       loading.show();
        viewModel.confirmarpedido(pedido, this).observe(this, (aBoolean -> {
            if(aBoolean){
                MyToast.makeText(this, R.string.msg_cancelamento_confirmacao, ModoColor._success).show();
                new SendNotification().onPush(new NotificationData(
                        getString(R.string.notification_title_confirma_cancelamento),
                        getString(R.string.notification_msg_confirma_cancelamento),
                        pedido.getUid_client(),
                        pedido.getUid(),
                        pedido.getStatusPedido(),
                        NotificationType.prazo), this);
                finish();
            }else{
                MySnackbar.makeText(this, R.string.erro_confirmacao, ModoColor._falha).show();
            }
        }));

    }

    public void onVerCancelamentoDoPedido(View view) {
        showDialog(pedido.getMsgCancelamento(), 1);
    }

    public void onVerAlteracaoDoPedido(View view) {
        showDialog(pedido.getMsgAlteracao(), 0);
    }

    private void showDialog(String msg, int type){
         if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.fragment_mensagem_dialog_pedido, null);
        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(_view);
        TextView mensagem = _view.findViewById(R.id.mensagem);
        ((TextView)_view.findViewById(R.id.name_toolbar)).setText(type == 0 ? getString(R.string.alteracao) : getString(R.string.mensagem));
        mensagem.setText(msg);
        _view.findViewById(R.id.bt_close).setOnClickListener(view -> sheetDialog.dismiss());

        if(Build.VERSION.SDK_INT >= 21){
            sheetDialog.getWindow().addFlags(67108864);
        }

        sheetDialog.show();
    }

    public void onRelatarProblemas(View view){
        btn_relatar_problemas.setEnabled(false);
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.dialog_relatar_problemas, null);
        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(_view);
        _view.findViewById(R.id.btn_pedido_cancelar).setEnabled(status != Status.pedidoCancelado && status == Status.novoPedido);
        _view.findViewById(R.id.btn_pedido_n_chegou).setEnabled(status != Status.pedidoCancelado && !isAtrasado);
        _view.findViewById(R.id.btn_cancelar_dialog).setOnClickListener((v)-> sheetDialog.dismiss());
        _view.findViewById(R.id.btn_ir_ao_estabelecimento).setOnClickListener((v -> {
            sheetDialog.dismiss();
            HelperManager.startGPS(pedido, this);
        }));
        _view.findViewById(R.id.btn_chamar_whastapp).setOnClickListener((v -> {
            sheetDialog.dismiss();
            HelperManager.startZap(pedido, this);
        }));
        _view.findViewById(R.id.btn_ligar_estabelecimento).setOnClickListener((v -> {
            sheetDialog.dismiss();
            HelperManager.startCall(pedido, this);
        }));
        _view.findViewById(R.id.btn_pedido_cancelar).setOnClickListener((v -> {
            sheetDialog.dismiss();
            onCancelarPedido();
        }));
         _view.findViewById(R.id.btn_pedido_n_chegou).setOnClickListener((v -> {
            sheetDialog.dismiss();
            onRelatarAtraso();
        }));

        if(Build.VERSION.SDK_INT >= 21){
            sheetDialog.getWindow().addFlags(67108864);
        }
        ((View)_view.getParent()).setBackgroundColor(getResources().getColor(R.color.color_dialog));
        sheetDialog.setOnDismissListener(dialog -> btn_relatar_problemas.setEnabled(true));
        sheetDialog.show();
    }

    private void onCancelarPedido() {
        if(isConnected()){
           @SuppressLint("InflateParams") View layout = LayoutInflater.from(this).inflate(R.layout.dialog_cancelar_pedido, null);
            DialogInput input = new DialogInput(this, layout, null);
            input.show();
           
            EditText messageCancel = layout.findViewById(R.id.message_cancel);
            layout.findViewById(R.id.btn_comfirmar_cancelamento).setOnClickListener((v -> {
                if(isConnected()){
                    String mensagem = messageCancel.getText().toString().trim();
                    if(!mensagem.equals("")){
                        
                        viewModel.cancelamento(pedido, mensagem, this).observe(this, (a ->{
                            dialog.dismiss();
                            if(a){
                                MySnackbar.makeText(this, R.string.send_cancelamento, ModoColor._success).show();
                                new SendNotification().onPush(new NotificationData(
                                        getString(R.string.notification_title_cancelamento) + pedido.getDisplayName(),
                                        getString(R.string.notification_msg_cancelamento) + " " + mensagem,
                                        pedido.getUid_client(),
                                        pedido.getUid(),
                                        pedido.getStatusPedido(),
                                        NotificationType.pedido), this);
                            }else{
                                MySnackbar.makeText(this, R.string.erro_cancelamento, ModoColor._falha).show();
                            }
                            loading.dismiss();
                        }));
                    }else{
                        MyToast.makeText(this, R.string.digite_msg_cancelamento, ModoColor._falha).show();
                    }
                }
            }));
            layout.findViewById(R.id.btn_finish).setOnClickListener((v -> dialog.dismiss()));
            layout.findViewById(R.id.btn_cancelar).setOnClickListener((v -> dialog.dismiss()));
            dialog.show();

        }
    }

    private void onRelatarAtraso() {
        if(isConnected()){
           if(HelperManager.isAtrasado(previsao_de_entrega.getText().toString())) {
               DialogAtrasado atrasado = new DialogAtrasado(this);
               atrasado.show();
               atrasado.setOnClickListener(()-> {
                   LoadingDialog loading = new LoadingDialog(this);
                  loading.show();
                         viewModel.relatarAtraso(pedido, this).observe(this, (a -> {
                       loading.dismiss();
                       if (a) {
                           MySnackbar.makeText(this, R.string.send_relata_atraso, ModoColor._success).show();
                           new SendNotification().onPush(new NotificationData(
                                   getString(R.string.notification_title_atraso),
                                   getString(R.string.notification_msg_atraso) + " " + pedido.getDisplayName(),
                                   pedido.getUid_client(),
                                   pedido.getUid(),
                                   pedido.getStatusPedido(),
                                   NotificationType.prazo), this);
                           atrasado.dismiss;
                       } else {
                           MySnackbar.makeText(this, R.string.erro_relata_atraso, ModoColor._falha).show();
                       }
                   }
                   ));
               });

               }else{
               new DialogNaoAtrasado(this).show();
            }
        }

    }

    private boolean isConnected() {
        if(!Conexao.isConnected(this)){
            MySnackbar.makeText(this, R.string.erro_na_conexao, ModoColor._falha).show();
            return false;
        }
        return true;
    }

    public void onUpdate(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        viewModel.update(this);
    }
}