package app.birdsoft.meurestaurante.dialogo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.widget.MySnackbar;

public class DialogEndereco  extends AlertDialog {
    private final Activity context;
    private final Endereco _endereco;
    private final MutableLiveData<Endereco> data;

    public DialogEndereco(@NonNull Activity context, Endereco _endereco, MutableLiveData<Endereco> data) {
        super(context);
        this.context = context;
        this._endereco = _endereco;
        this.data = data;
    }

    @Override
    public void show() {
        super.show();
        Button spinner_button_selector = findViewById(R.id.spinner_button_selector);
        CheckBox sem_numero_check = findViewById(R.id.sem_numero_check);
        TextInputLayout bloco_n_app = findViewById(R.id.bloco_n_app);
        TextInputLayout nome_rua_numeroInput = findViewById(R.id.nome_rua_numero);
        TextInputLayout numero_casaInput = findViewById(R.id.numero_casa);
        TextInputLayout bairroInput = findViewById(R.id.bairro);
        TextInputLayout referenciaInput = findViewById(R.id.referencia);
        TextInputLayout cidadeInput = findViewById(R.id.cidade);
        TextInputLayout complementosInput = findViewById(R.id.complementos);
        assert bairroInput != null;
        assert complementosInput != null;
        assert bloco_n_app != null;
        assert nome_rua_numeroInput != null;
        assert numero_casaInput != null;
        assert referenciaInput != null;
        assert cidadeInput != null;

        spinner_button_selector.setOnClickListener((v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.inflate(R.menu.lugares_options);
            menu.setOnMenuItemClickListener((item -> {
                spinner_button_selector.setText(item.getTitle());
                bloco_n_app.setVisibility(item.getItemId() == R.id.predio || item.getItemId() == R.id.condominios_casas ? View.VISIBLE : View.GONE);
                return false;
            }));
            menu.show();
        }));
        Endereco endereco;
        if (_endereco == null) {
            endereco = Settings.getEndereco(context);
        } else endereco = _endereco;
        if (endereco != null) {
            spinner_button_selector.setText(endereco.getTipo_lugar() != null ? endereco.getTipo_lugar() : "");
            if(endereco.getTipo_lugar() != null) {
                bloco_n_app.setVisibility(endereco.getTipo_lugar().equals(context.getString(R.string.predio)) || endereco.getTipo_lugar().equals(context.getString(R.string.condominios_casas)) ? View.VISIBLE : View.GONE);
            }
            String numero_casa = endereco.getNumeroCasa().replace("Casa N° ", "");
            Objects.requireNonNull(nome_rua_numeroInput.getEditText()).setText(endereco.getNomeRuaNumero());
            Objects.requireNonNull(complementosInput.getEditText()).setText(endereco.getComplemento() != null ? endereco.getComplemento() : "");
            Objects.requireNonNull(numero_casaInput.getEditText()).setText(numero_casa);
            Objects.requireNonNull(bairroInput.getEditText()).setText(endereco.getBairro());
            Objects.requireNonNull(bloco_n_app.getEditText()).setText(endereco.getBloco_n_ap() != null ? endereco.getBloco_n_ap() : "");
            Objects.requireNonNull(referenciaInput.getEditText()).setText(endereco.getReferencia());
            Objects.requireNonNull(cidadeInput.getEditText()).setText(endereco.getCidade());
            sem_numero_check.setChecked(endereco.isSn());
            sem_numero_check.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                numero_casaInput.setError("");
                numero_casaInput.setEnabled(false);
                numero_casaInput.setEnabled(!isChecked);
            }));
            nome_rua_numeroInput.getEditText().setSelection(endereco.getNomeRuaNumero().length());
            if(!endereco.isSn()){
                numero_casaInput.getEditText().setSelection(numero_casa.length());
            }
            bairroInput.getEditText().setSelection(endereco.getBairro().length());
            if(endereco.getBloco_n_ap() != null){
                bloco_n_app.getEditText().setSelection(endereco.getBloco_n_ap().length());
            }
            if(endereco.getComplemento() != null){
                complementosInput.getEditText().setSelection(endereco.getComplemento().length());
            }
            referenciaInput.getEditText().setSelection(endereco.getReferencia().length());
            cidadeInput.getEditText().setSelection(endereco.getCidade().length());

        }

        Objects.requireNonNull((View) findViewById(R.id.confirmar)).setOnClickListener((v -> {
            if (Conexao.isConnected(context)) {
                String nome_rua_numero = Objects.requireNonNull(nome_rua_numeroInput.getEditText()).getText().toString().trim();
                String numero_casa = Objects.requireNonNull(numero_casaInput.getEditText()).getText().toString().trim();
                String bairro = Objects.requireNonNull(bairroInput.getEditText()).getText().toString().trim();
                String cidade = Objects.requireNonNull(cidadeInput.getEditText()).getText().toString().trim();
                String referencia = Objects.requireNonNull(referenciaInput.getEditText()).getText().toString().trim();
                String _complemento = Objects.requireNonNull(complementosInput.getEditText()).getText().toString().trim();
                String _bloco_ap = Objects.requireNonNull(bloco_n_app.getEditText()).getText().toString().trim();

                if (!nome_rua_numero.equals("")) {
                    if (!numero_casa.equals("") || sem_numero_check.isChecked()) {
                        if (!bairro.equals("")) {
                            if (!referencia.equals("")) {
                                if (!cidade.equals("")) {
                                    if(!_complemento.equals("")){
                                        dismiss();
                                        Endereco endereco_ = new Endereco();
                                        endereco_.setBloco_n_ap(_bloco_ap);
                                        endereco_.setSn(sem_numero_check.isChecked());
                                        endereco_.setTipo_lugar(spinner_button_selector.getText().toString());
                                        endereco_.setComplemento(_complemento);
                                        endereco_.setBairro(bairro);
                                        endereco_.setCidade(cidade);
                                        endereco_.setNomeRuaNumero(nome_rua_numero);
                                        endereco_.setNumeroCasa(sem_numero_check.isChecked() ? "" : "Casa N° " + numero_casa);
                                        endereco_.setReferencia(referencia);
                                        String endereco_string;
                                        if(endereco_.getNumeroCasa().equals("")){
                                            if(bloco_n_app.getVisibility() == View.VISIBLE){
                                                endereco_string = String.format("%s, S/N, %s, %s, %s, %s, %s", endereco_.getNomeRuaNumero(), endereco_.getReferencia(), endereco_.getBloco_n_ap(), endereco_.getComplemento(), endereco_.getBairro(), endereco_.getCidade());
                                            }else endereco_string = String.format("%s, S/N, %s, %s, %s, %s", endereco_.getNomeRuaNumero(), endereco_.getReferencia(), endereco_.getComplemento(), endereco_.getBairro(), endereco_.getCidade());
                                        }else{
                                            if(bloco_n_app.getVisibility() == View.VISIBLE){
                                                endereco_string = String.format("%s, %s, %s, %s, %s, %s, %s", endereco_.getNomeRuaNumero(), endereco_.getNumeroCasa(), endereco_.getReferencia(), endereco_.getBloco_n_ap(), endereco_.getComplemento(), endereco_.getBairro(), endereco_.getCidade());
                                            }else endereco_string = String.format("%s, %s, %s, %s, %s, %s", endereco_.getNomeRuaNumero(), endereco_.getNumeroCasa(), endereco_.getReferencia(), endereco_.getComplemento(), endereco_.getBairro(), endereco_.getCidade());
                                        }
                                        endereco_.setEndereco(String.format("%s: %s", spinner_button_selector.getText().toString(), endereco_string));
                                        data.setValue(endereco_);
                                    } else {
                                        complementosInput.setErrorEnabled(true);
                                        complementosInput.setError(context.getString(R.string.digite_complemento));
                                    }

                                } else {
                                    cidadeInput.setErrorEnabled(true);
                                    cidadeInput.setError(context.getString(R.string.digite_cidade));
                                }
                            } else {
                                referenciaInput.setErrorEnabled(true);
                                referenciaInput.setError(context.getString(R.string.digite_referencia));
                            }
                        } else {
                            bairroInput.setErrorEnabled(true);
                            bairroInput.setError(context.getString(R.string.digite_bairro));
                        }
                    } else {
                        numero_casaInput.setErrorEnabled(true);
                        numero_casaInput.setError(context.getString(R.string.digite_numero_casa));
                    }
                } else {
                    nome_rua_numeroInput.setErrorEnabled(true);
                    nome_rua_numeroInput.setError(context.getString(R.string.digite_nome_rua_numero));
                }

            } else {
                MySnackbar.makeText(context, R.string.sem_conexao, ModoColor._falha).show();
            }
        }));
        Objects.requireNonNull((View) findViewById(R.id.cancel)).setOnClickListener((v -> dismiss()));
        Objects.requireNonNull(referenciaInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (referenciaInput.isErrorEnabled()) {
                    referenciaInput.setError("");
                    referenciaInput.setErrorEnabled(false);
                }
            }
        });
        Objects.requireNonNull(bairroInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bairroInput.isErrorEnabled()) {
                    bairroInput.setError("");
                    bairroInput.setErrorEnabled(false);
                }
            }
        });
        Objects.requireNonNull(numero_casaInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numero_casaInput.isErrorEnabled()) {
                    numero_casaInput.setError("");
                    numero_casaInput.setErrorEnabled(false);
                }
            }
        });
        Objects.requireNonNull(nome_rua_numeroInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nome_rua_numeroInput.isErrorEnabled()) {
                    nome_rua_numeroInput.setError("");
                    nome_rua_numeroInput.setErrorEnabled(false);
                }
            }
        });
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_editor_endereco);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}