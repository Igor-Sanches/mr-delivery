package app.birdsoft.meurestaurante.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.dialogo.DialogInput;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cliente;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.settings.Settings;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.tools.Email;
import app.birdsoft.meurestaurante.tools.Mask;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.viewModel.SettingsViewModel;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class AppSettingsActivity extends AppCompatActivity {

    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;
    private LoadingDialog dialog;
    private SettingsViewModel viewModel;
    private static AppSettingsActivity instance;
    public static AppSettingsActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        instance = this;
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.init(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        dialog = new LoadingDialog(this);
        View sheetBottom = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
    }

    @Override
    protected void onResume() {
        viewModel.update(this);
        super.onResume();
    }

    public void trocarSenha(){
        try{
            if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.dialog_redefinir_senha, null);

            final TextInputLayout emailInput = _view.findViewById(R.id.emailUser);
            emailInput.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    emailInput.setError("");
                    emailInput.setErrorEnabled(false);
                }
            });
            FrameLayout btnRec= _view.findViewById(R.id.buttonGo);
            btnRec.setOnClickListener(v -> {
                final String _emailB2 = emailInput.getEditText().getText().toString();
                if (Conexao.isConnected(AppSettingsActivity.this)) {
                    if (!_emailB2.equals("")) {

                        if (Email.validar(_emailB2)) {
                            recuperar(emailInput);
                        } else {
                            emailInput.setErrorEnabled(true);
                            emailInput.setError(getString(R.string.digite_um_email_valido));
                            emailInput.getEditText().requestFocus();
                        }

                    } else {
                        emailInput.setErrorEnabled(true);
                        emailInput.setError(getString(R.string.digite_seu_email));
                        emailInput.getEditText().requestFocus();
                    }


                } else {
                    MySnackbar.makeText(this, R.string.sem_conexao, ModoColor._falha).show();
                }
            });

            sheetDialog = new BottomSheetDialog(AppSettingsActivity.this);
            sheetDialog.setContentView(_view);
            if(Build.VERSION.SDK_INT >= 21){
                sheetDialog.getWindow().addFlags(67108864);
            }
            ((View)_view.getParent()).setBackgroundColor(getResources().getColor(R.color.transparent));
            sheetDialog.show();
        }catch (Exception x){
            System.out.println(x.getMessage());
        }
    }

    private void recuperar(TextInputLayout emailInput) {
        dialog.show();
        MySnackbar.makeText(this, R.string.verificando_email).show();
        String email = emailInput.getEditText().getText().toString().trim();
        Conexao.getFirebaseAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        MySnackbar.makeText(this, R.string.verificado_email).show();
                    }else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (FirebaseAuthInvalidUserException user) {
                            emailInput.getEditText().requestFocus();
                            emailInput.setErrorEnabled(true);
                            emailInput.setError(getString(R.string.firebaseAuthInvalidUserException));
                        }catch (Exception e) {
                            MySnackbar.makeText(this, R.string.falha_verificado_email).show();
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                    sheetDialog.dismiss();
                });
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private ListPreference formato;
        private Cliente cliente;
        private LoadingDialog loading;
        private SettingsViewModel viewModel;
        private Preference nome;
        private Preference email;
        private Preference senha;
        private Preference telefone;
        private Preference endereco;

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("FORMATO_DATA")){
                Settings.setFormateDate(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(key, "dd/MM/yyyy"), getActivity());
                iniciarVisualizacaoData();
            }else{
                Settings.put(key, (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(key, true)), getActivity());
            }
        }

        private void iniciarVisualizacaoData() {
            formato.setSummary(DateTime.toDateString());
        }

        public void onPause() {
            super.onPause();
            this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onResume() {
            super.onResume();
            this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            viewModel = AppSettingsActivity.getInstance().viewModel;
            viewModel.init(getActivity());
            Preference notificationSettings = findPreference("CONFIGURATION_PUSH");
            notificationSettings.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getActivity().getPackageName());
                intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
                intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
                startActivity(intent);
                return false;
            });
            findPreference("BIRDSOFT_KEY").setOnPreferenceClickListener((click -> {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.birdsoft.com.br")));
                return false;
            }));
            loading = new LoadingDialog(getActivity());
            formato = findPreference("FORMATO_DATA");
            nome = findPreference("PROFILE_NAME");
            email = findPreference("PROFILE_EMAIL");
            senha = findPreference("PROFILE_SENHA");
            telefone = findPreference("PROFILE_TELEFONE");
            endereco = findPreference("PROFILE_ENDERECO");
            Preference btn_exit = findPreference("PROFILE_EXIT");
            nome.setOnPreferenceClickListener(preference -> {
                if(checkInfo(nome.getSummary().toString())){
                    trocarNome();
                }
                return false;
            });
            email.setOnPreferenceClickListener(preference -> {
                if(checkInfo(email.getSummary().toString())){
                    trocaEmail();
                }
                return false;
            });
            senha.setOnPreferenceClickListener(preference -> {
                if(checkInfo(senha.getSummary().toString())){
                    AppSettingsActivity.getInstance().trocarSenha();
                }
                return false;
            });
            telefone.setOnPreferenceClickListener(preference -> {
                if(checkInfo(telefone.getSummary().toString())){
                    trocaTelefone();
                }
                return false;
            });
            endereco.setOnPreferenceClickListener(preference -> {
                if(checkInfo(endereco.getSummary().toString())){
                    HelperManager.inputEnderecoCliente(getActivity()).observe(this, (endereco1 -> {
                        if(endereco1 != null){
                            LoadingDialog dialog = new LoadingDialog(getActivity());
                            dialog.show();

                            new ViewModelProvider(this).get(SettingsViewModel.class).trocaEndereco(endereco1, getActivity()).observe(this, (sucesso -> {
                                if(sucesso){
                                    Usuario.setEndereco(endereco1.getEndereco(), getActivity());
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
                                    Settings.setEndereco(_endereco, getActivity());
                                    this.endereco.setSummary(Usuario.getEndereco(getActivity()));
                                    cliente.setEndereco(Usuario.getEndereco(getActivity()));
                                    update();
                                    MyToast.makeText(getActivity(), R.string.troca_endereco, ModoColor._success).show();
                                }else{
                                    MyToast.makeText(getActivity(), R.string.error_troca_endereco, ModoColor._falha).show();
                                }
                                dialog.dismiss();
                            }));
                        }
                    }));
                    //trocaEndereco();
                }
                return false;
            });
            btn_exit.setOnPreferenceClickListener(preference -> {
                HelperManager.exitApp(getActivity());
                return false;
            });
            viewModel.getMutableLiveData().observe(this, (settingsElements -> {
                if(settingsElements.isSuccess()){
                    cliente = settingsElements.getCliente();
                    nome.setSummary(settingsElements.getCliente().getNome());
                    email.setSummary(settingsElements.getCliente().getEmail());
                    senha.setSummary(getString(R.string.clique_senha));
                    telefone.setSummary(settingsElements.getCliente().getTelefone());
                    endereco.setSummary(settingsElements.getCliente().getEndereco());
                }else{
                    cliente = null;
                    nome.setSummary(R.string.falha_settings_info);
                    email.setSummary(R.string.falha_settings_info);
                    senha.setSummary(R.string.falha_settings_info);
                    telefone.setSummary(R.string.falha_settings_info);
                    endereco.setSummary(R.string.falha_settings_info);
                }
            }));
            //iniciarVisualizacaoData();
        }

        private void update() {
            viewModel.update(getActivity());
        }

        private void trocaTelefone() {
            @SuppressLint("InflateParams") View root = LayoutInflater.from(getActivity()).inflate(R.layout.card_contato, null);
            DialogInput input = new DialogInput(getActivity(), root, getString(R.string.telefone));
            EditText number = ((TextInputLayout)root.findViewById(R.id.zapUser)).getEditText();
            number.addTextChangedListener(Mask.insertTelefoneCelular(number));
            number.setText(cliente.getTelefone());
            number.setSelection(cliente.getTelefone().length());
            Button cancel = root.findViewById(R.id.cancel);
            Button enter = root.findViewById(R.id.enter);
            cancel.setOnClickListener(v -> input.dismiss());
            enter.setOnClickListener((v -> {
                if(!cliente.getTelefone().equals(telefone)){
                    if(isConnected()){
                        loading.show();
                        String telefone = number.getText().toString().trim();
                        if(!telefone.equals("")){
                            if("(##) ####-####".length() == telefone.length() || "(##) #####-####".length() == telefone.length()){
                                viewModel.trocaTelefone(telefone, getActivity()).observe(this, (success ->{
                                    if(success){
                                        this.telefone.setSummary(telefone);
                                        cliente.setTelefone(telefone);
                                        input.dismiss();
                                        update();
                                        MySnackbar.makeText(getActivity(), R.string.troca_telefone, ModoColor._success).show();
                                    }else{
                                        MySnackbar.makeText(getActivity(), R.string.error_troca_telefone, ModoColor._falha).show();
                                    }
                                    loading.dismiss();
                                }));
                            }else{
                                MySnackbar.makeText(getActivity(), R.string.digite_o_seu_telefone_correto, ModoColor._falha).show();
                            }
                        }else{
                            MySnackbar.makeText(getActivity(), R.string.digite_o_seu_telefone, ModoColor._falha).show();
                        }
                    }
                }else input.dismiss();
            }));

            input.show();
        }

        private void trocaEmail() {
            @SuppressLint("InflateParams") View root = LayoutInflater.from(getActivity()).inflate(R.layout.card_email, null);
            DialogInput input = new DialogInput(getActivity(), root, getString(R.string.email));
            TextInputLayout mTextInputLayout = root.findViewById(R.id.user_editor);
            EditText editor = mTextInputLayout.getEditText();
            editor.setText(cliente.getEmail());
            editor.setSelection(cliente.getEmail().length());
            Button cancel = root.findViewById(R.id.cancel);
            Button enter = root.findViewById(R.id.enter);
            cancel.setOnClickListener(v -> input.dismiss());
            enter.setOnClickListener((v -> {
                if(!cliente.getEmail().equals(email)){
                    if(isConnected()){
                        loading.show();
                        String email = editor.getText().toString().trim();
                        if(!email.equals("")){
                            viewModel.trocaEmail(email, getActivity()).observe(this, (success ->{
                                if(success){
                                    this.email.setSummary(email);
                                    cliente.setEmail(email);
                                    input.dismiss();
                                    update();
                                    MySnackbar.makeText(getActivity(), R.string.troca_email, ModoColor._success).show();
                                }else{
                                    MySnackbar.makeText(getActivity(), R.string.error_troca_email, ModoColor._falha).show();
                                }
                                loading.dismiss();
                            }));
                        }else{
                            MySnackbar.makeText(getActivity(), R.string.digite_o_seu_email, ModoColor._falha).show();
                        }
                    }
                }else input.dismiss();
            }));

            input.show();
        }

        private void trocarNome() {
            @SuppressLint("InflateParams") View root = LayoutInflater.from(getActivity()).inflate(R.layout.card_display_name, null);
            DialogInput input = new DialogInput(getActivity(), root, getString(R.string.nome_sobrenome));
            TextInputLayout mTextInputLayout = root.findViewById(R.id.pinUser);
            EditText editor = mTextInputLayout.getEditText();
            editor.setText(cliente.getNome());
            editor.setSelection(cliente.getNome().length());
            Button cancel = root.findViewById(R.id.cancel);
            Button enter = root.findViewById(R.id.enter);
            cancel.setOnClickListener(v -> input.dismiss());
            enter.setOnClickListener((v -> {
                if(!cliente.getNome().equals(nome)){
                    if(isConnected()){
                        loading.show();
                        String nome = editor.getText().toString().trim();
                        if(!nome.equals("")){
                            viewModel.trocaNome(nome, getActivity()).observe(this, (success ->{
                                if(success){
                                    this.nome.setSummary(nome);
                                    cliente.setNome(nome);
                                    input.dismiss();
                                    update();
                                    MySnackbar.makeText(getActivity(), R.string.troca_nome, ModoColor._success).show();
                                }else{
                                    MySnackbar.makeText(getActivity(), R.string.error_troca_nome, ModoColor._falha).show();
                                }
                                loading.dismiss();
                            }));
                        }else{
                            MySnackbar.makeText(getActivity(), R.string.digite_o_seu_nome, ModoColor._falha).show();
                        }
                    }
                }else input.dismiss();

            }));

            input.show();

        }

        private boolean checkInfo(String summary) {
            if(summary.equals(getString(R.string.falha_settings_info)) || summary.equals("...")){
                MySnackbar.makeText(getActivity(), R.string.tente_atualizar_info).show();
                viewModel.update(getActivity());
                return false;
            }
            return true;
        }

        private boolean isConnected() {
            if(!Conexao.isConnected(getActivity())){
                MySnackbar.makeText(getActivity(), R.string.erro_na_conexao, ModoColor._falha).show();
                return false;
            }
            return true;
        }
    }

}