package app.birdsoft.meurestaurante.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.UUID;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.dialogo.DialogInput;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cardapio;
import app.birdsoft.meurestaurante.model.Carrinho;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.tools.Mask;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.view.CardapioActivity;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.widget.ImageUtils;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class CardapioSecundarioUnicoFragment extends Fragment {

    private CoordinatorLayout lista_layout;
    private LinearLayout vazio, layout_wifi_error, lyt_progress;
    private RobotoTextView valorText;
    private TextView summary, displayName;
    private ImageView app_bar_image;
    private Cardapio cardapio;
    private double valor = 0, subValor = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cardapio_secundario_unico, container, false);
        assert getArguments() != null;
        cardapio = (Cardapio)getArguments().getSerializable("cardapio");
        if(cardapio == null){
            MyToast.makeText(getActivity(), R.string.error_layout).show();
            requireActivity().finish();
            return null;
        }
        vazio = root.findViewById(R.id.vazio);
        layout_wifi_error = root.findViewById(R.id.layout_wifi_error);
        summary = root.findViewById(R.id.summary);
        app_bar_image = root.findViewById(R.id.app_bar_image);
        lista_layout = root.findViewById(R.id.layout_principal);
        lyt_progress = root.findViewById(R.id.lyt_progress);
        displayName = root.findViewById(R.id.displayName);
        valorText = root.findViewById(R.id.valorText);
        input();
        root.findViewById(R.id.adicionar_carrinho).setOnClickListener((v -> adicionar()));
        return root;
    }

    private void adicionar() {
        try {

            @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_carrinho_quantidade, null);
            DialogInput dialog = new DialogInput(requireActivity(), view, getString(R.string.quant_max_titulo));
            TextView numero = view.findViewById(R.id.numero);
            TextView valorText = view.findViewById(R.id.valorText);
            ImageButton remover = view.findViewById(R.id.btn_remover);
            ImageButton adicionar = view.findViewById(R.id.btn_adicionar);
            remover.setEnabled(false);
            subValor = this.valor;
            onValue(Integer.parseInt(numero.getText().toString()), valorText);
            remover.setOnClickListener((v -> {
                int n = Integer.parseInt(numero.getText().toString());
                n -= 1;
                numero.setText(String.valueOf(n));
                remover.setEnabled(n > 1);
                adicionar.setEnabled(n < 10);
                onValue(n, valorText);
            }));
            adicionar.setOnClickListener((v -> {
                int n = Integer.parseInt(numero.getText().toString());
                n += 1;
                numero.setText(String.valueOf(n));
                remover.setEnabled(n > 1);
                adicionar.setEnabled(n < 10);
                onValue(n, valorText);
            }));
            view.findViewById(R.id.cancel).setOnClickListener((v -> dialog.dismiss()));
            view.findViewById(R.id.confirmar).setOnClickListener((v -> {
                dialog.dismiss();
                onIniciar(Integer.parseInt(numero.getText().toString()));
            }));
            dialog.show();

        }catch (Exception x){
            // message(x.getMessage());
        }

    }

    private void onIniciar(int parseInt) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId_produto(cardapio.getUid());
        carrinho.setDisplayName(cardapio.getName());
        carrinho.setUid(UUID.randomUUID().toString());
        carrinho.setValorTotal(subValor);
        carrinho.setQuantidade(parseInt);
        carrinho.setUid_client(Usuario.getUid(getActivity()));
        carrinho.setData(DateTime.getTime());
        carrinho.setListas(null);
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();
        new ViewModelProvider(requireActivity()).get(CarrinhoViewModel.class).insert(carrinho, getActivity()).observe(requireActivity(), success -> {
            if(success){
                MySnackbar.makeText(getActivity(), R.string.adicionado_ao_carrinho, ModoColor._success).show();
                CardapioActivity.getInstance().navController.navigateUp();
            }else{
                MySnackbar.makeText(getActivity(), R.string.error_adicionar_carrinho).show();
            }
            loadingDialog.dismiss();
        });
    }

    private void onValue(int parseInt, TextView valorText) {
        subValor = parseInt * valor;
        valorText.setText(Mask.formatarValor(subValor));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void input() {
        ImageUtils.displayImageFromUrl(getActivity(), cardapio.getImageUrl(), app_bar_image, getResources().getDrawable(R.drawable.image_add_ft));
        displayName.setText(cardapio.getName());
        if(cardapio.getReceita() != null){
            summary.setText(cardapio.getReceita());
        }
        valor = cardapio.getValor();
        valorText.setText(Mask.formatarValor(valor));
        lyt_progress.setVisibility(View.GONE);
        layout_wifi_error.setVisibility(View.GONE);
        vazio.setVisibility(View.GONE);
        lista_layout.setVisibility(View.VISIBLE);
    }

}