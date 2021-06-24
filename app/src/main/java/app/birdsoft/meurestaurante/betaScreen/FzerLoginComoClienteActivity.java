package app.birdsoft.meurestaurante.betaScreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.dialogo.DialogMessage;
import app.birdsoft.meurestaurante.dialogo.DialogPerfectScan;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.login.SplashActivity;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.navigation.Navigate;
import app.birdsoft.meurestaurante.servicos.SendNotification;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class FzerLoginComoClienteActivity extends AppCompatActivity {

    private TextInputLayout user_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fzer_login_como_cliente);
        user_editor = findViewById(R.id.user_editor);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void onEscanearCodigo(View view) {
        view.setEnabled(false);
        try{
            Intent intent = new Intent("perfect.scan.action.SCAN");
            startActivityForResult(intent, 0);
        }catch (ActivityNotFoundException X){
            view.setEnabled(true);
            DialogPerfectScan scan = new DialogPerfectScan(this);
            scan.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            findViewById(R.id.btn_escanear_call).setEnabled(true);
            if(requestCode == 0){
                if(resultCode == 0 || resultCode == -1){
                    assert data != null;
                    String formato = data.getStringExtra("codeFormat");
                    if(formato.equals("QR_CODE")){
                        String codigo = data.getStringExtra("result");
                        if(codigo.length() != 28){
                            DialogMessage message = new DialogMessage(this, getString(R.string.message_qr_not_lenght), false, "");
                            message.show();
                        }else{
                            Objects.requireNonNull(user_editor.getEditText()).setText(codigo);
                            iniciarEscaneamento(codigo);
                        }
                    }else{
                        DialogMessage message = new DialogMessage(this, getString(R.string.message_qr_not), false, "");
                        message.show();
                    }
                }
            }
        }catch (Exception ignore){

        }
    }

    private void iniciarEscaneamento(String codigo) {
       try{
          if(Conexao.isConnected(this)){
              LoadingDialog dialog = new LoadingDialog(this);
              dialog.show();
              new ViewModelProvider(this)
                      .get(LoginViewModel.class)
                      .isContaExist(codigo)
                      .observe(this, (result -> {
                          dialog.dismiss();
                          if(result == R.string.sucesso){
                              MyToast.makeText(this, R.string.entrando_como_cliente, ModoColor._success).show();
                              Settings.setUID(codigo, this);
                              if(Usuario.getUid(this) != null)
                                  SendNotification.UpdateToken(codigo, this);
                              Navigate.activity(this).navigate(SplashActivity.class);
                              finish();
                          }else{
                              DialogMessage message = new DialogMessage(this, getString(result), false, "");
                              message.show();
                          }
                      }));
          }else{
              DialogMessage message = new DialogMessage(this, getString(R.string.sem_conexao), false, "");
              message.show();

          }
       }catch (Exception ignore){

       }
    }

    public void onIniciarEscaneamento(View view) {
        String codigo = Objects.requireNonNull(user_editor.getEditText()).getText().toString().trim();
        if(!codigo.equals("")){
            if(codigo.length() != 28){
                DialogMessage message = new DialogMessage(this, getString(R.string.message_qr_not_lenght), false, "");
                message.show();
            }else{
                user_editor.getEditText().setText(codigo);
                iniciarEscaneamento(codigo);
            }
        }else{
            DialogMessage message = new DialogMessage(this, getString(R.string.message_digite), false, "");
            message.show();

        }
    }

    public void onContratarServico(View view) {
        if(Conexao.isConnected(this)){
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
            HelperManager.getContrato(this).observe(this, (result -> {
                if(!result)
                    MySnackbar.makeText(this, R.string.contato_n_feito, ModoColor._falha).show();

                dialog.dismiss();
            }));
        }else{
            DialogMessage message = new DialogMessage(this, getString(R.string.sem_conexao), false, "");
            message.show();

        }
    }
}