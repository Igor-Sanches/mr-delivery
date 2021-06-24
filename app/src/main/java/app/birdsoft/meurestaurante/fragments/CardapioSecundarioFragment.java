package app.birdsoft.meurestaurante.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.adaptador.AdaptadorCardapioSecundario;
import app.birdsoft.meurestaurante.dialogo.DialogInput;
import app.birdsoft.meurestaurante.dialogo.DialogMessage;
import app.birdsoft.meurestaurante.dialogo.LoadingDialog;
import app.birdsoft.meurestaurante.manager.HelperManager;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cardapio;
import app.birdsoft.meurestaurante.model.Carrinho;
import app.birdsoft.meurestaurante.model.ItemCardapio;
import app.birdsoft.meurestaurante.model.ItemCardapioLista;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.tools.Mask;
import app.birdsoft.meurestaurante.tools.ModoColor;
import app.birdsoft.meurestaurante.view.CardapioActivity;
import app.birdsoft.meurestaurante.viewModel.CarrinhoViewModel;
import app.birdsoft.meurestaurante.widget.ImageUtils;
import app.birdsoft.meurestaurante.widget.MySnackbar;
import app.birdsoft.meurestaurante.widget.MyToast;

public class CardapioSecundarioFragment extends Fragment {

    private RobotoTextView valorText;
    private TextView summary, displayName;
    private ImageView app_bar_image;
    private AdaptadorCardapioSecundario adaptador;
    private Cardapio cardapio;
    private double valor = 0, subValor = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cardapio_secundario, container, false);
        assert getArguments() != null;
        cardapio = (Cardapio)getArguments().getSerializable("cardapio");
        if(cardapio == null){
            MyToast.makeText(getActivity(), R.string.error_layout).show();
            requireActivity().finish();
            return null;
        }
        adaptador = new AdaptadorCardapioSecundario(new ArrayList<>(), getActivity());
        LinearLayout vazio = root.findViewById(R.id.vazio);
        LinearLayout layout_wifi_error = root.findViewById(R.id.layout_wifi_error);
        summary = root.findViewById(R.id.summary);
        app_bar_image = root.findViewById(R.id.app_bar_image);
        CoordinatorLayout lista_layout = root.findViewById(R.id.layout_principal);
        LinearLayout lyt_progress = root.findViewById(R.id.lyt_progress);
        displayName = root.findViewById(R.id.displayName);
        valorText = root.findViewById(R.id.valorText);
        RecyclerView mRecyclerView = root.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adaptador);
        input();
        adaptador = new AdaptadorCardapioSecundario(HelperManager.convert(cardapio.getCardapio()), getActivity());
        mRecyclerView.setAdapter(adaptador);
        adaptador.setOnItemCheckedChanged((v, title, preco, itemCardapio, position, checkBox, layout, btnRemove, btnAdd, number, maxItensAdicionais) -> {
            try{
                boolean isAdd = false;

                if(layout != null){
                    isAdd = true;
                    btnAdd.setOnClickListener(view -> {
                        if(checkBox.isChecked()){
                            try{
                                int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                                itensAdicionaisCount += 1;
                                number.setText(String.format("%s", itensAdicionaisCount));
                                btnRemove.setEnabled(itensAdicionaisCount > 1);
                                if(maxItensAdicionais > 0){
                                    btnAdd.setEnabled(itensAdicionaisCount < maxItensAdicionais);
                                }
                                itemCardapio.valor += preco;
                                for (int i = 0; i < itemCardapio.lista.contents.size(); i++) {
                                    if(itemCardapio.lista.contents.get(i).equals(title) && itemCardapio.lista.valores.get(i).equals(preco)){
                                        itemCardapio.lista.quantidate.set(i, itensAdicionaisCount);
                                    }
                                }
                                valorView();
                            }catch (Exception x){
                                // message(x.getMessage());
                            }
                        }
                    });
                    btnRemove.setOnClickListener(view -> {
                        if(checkBox.isChecked()){
                            int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                            itensAdicionaisCount -= 1;
                            number.setText(String.format("%s", itensAdicionaisCount));
                            itemCardapio.valor -= preco;
                            for (int i = 0; i < itemCardapio.lista.contents.size(); i++) {
                                if(itemCardapio.lista.contents.get(i).equals(title) && itemCardapio.lista.valores.get(i).equals(preco)){
                                    itemCardapio.lista.quantidate.set(i, itensAdicionaisCount);
                                }
                            }
                            btnRemove.setEnabled(itensAdicionaisCount >= 2);
                            if(maxItensAdicionais != 0){
                                if(itensAdicionaisCount < maxItensAdicionais){
                                    btnAdd.setEnabled(true);
                                }
                            }
                            valorView();
                        }
                    });

                    if(checkBox.isChecked()){
                        int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                        Double y = itemCardapio.valor;
                        for (int i = 0; i < itensAdicionaisCount; i++) {
                            y += preco;
                        }
                        itemCardapio.valor = y;
                        if(maxItensAdicionais > 1){
                            layout.setVisibility(View.VISIBLE);
                        }
                        itemCardapio.lista.displayName = itemCardapio.getDispayTitulo() + "";
                        itemCardapio.lista.contents.add(title);
                        itemCardapio.lista.valores.add(preco);
                        itemCardapio.lista.quantidate.add(itensAdicionaisCount);
                        valorView();
                        layout.setVisibility(maxItensAdicionais > 1 ? View.VISIBLE : View.GONE);
                    }else{

                        int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                        Double y = itemCardapio.valor;
                        for (int i = 0; i < itensAdicionaisCount; i++) {
                            y -= preco;
                        }
                        itemCardapio.valor = y;
                        if(maxItensAdicionais > 1){
                            layout.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < itemCardapio.lista.contents.size(); i++) {
                            if(itemCardapio.lista.contents.get(i).equals(title) && itemCardapio.lista.valores.get(i).equals(preco)){
                                itemCardapio.lista.valores.remove(i);
                                itemCardapio.lista.contents.remove(i);
                                itemCardapio.lista.quantidate.remove(i);
                                if(itemCardapio.lista.contents.size() == 0){
                                    itemCardapio.lista.displayName = "";
                                }
                            }
                        }
                        valorView();
                        layout.setVisibility(View.GONE);
                    }
                }

                if (!itemCardapio.isMultiselect()) {
                    if (checkBox.isChecked()) {
                        itemCardapio.max.add(checkBox);
                        if (itemCardapio.max.size() == itemCardapio.getSelectMax()) {
                            for (int i = 0; i < itemCardapio.allCheckBox.size(); i++){
                                CheckBox box = itemCardapio.allCheckBox.get(i);
                                for (int j = 0; j < itemCardapio.max.size(); j++) {
                                    CheckBox check = itemCardapio.max.get(j);
                                    if (check != box) {
                                        box.setEnabled(false);
                                    }
                                }
                            }

                            for (CheckBox x : itemCardapio.max) {
                                x.setEnabled(true);
                            }

                            //bloco.max.get(0).setChecked(false);
                            //bloco.max.remove(0)
                        }
                    } else {
                        for (int i = 0; i < itemCardapio.max.size(); i++) {
                            if (itemCardapio.max.get(i) == checkBox) {
                                itemCardapio.max.remove(i);
                                for (CheckBox check : itemCardapio.allCheckBox) {
                                    check.setEnabled(true);
                                }
                            }
                        }
                    }

                }

                if(!isAdd){
                    if(checkBox.isChecked()){
                        itemCardapio.lista.displayName = itemCardapio.getDispayTitulo() + "";
                        itemCardapio.lista.contents.add(title);
                        itemCardapio.lista.valores.add(preco);
                        itemCardapio.lista.quantidate.add(1);
                    }else{
                        for (int i = 0; i < itemCardapio.lista.contents.size(); i++) {
                            if(itemCardapio.lista.contents.get(i).equals(title) && itemCardapio.lista.valores.get(i).equals(preco)){
                                itemCardapio.lista.valores.remove(i);
                                itemCardapio.lista.contents.remove(i);
                                itemCardapio.lista.quantidate.remove(i);
                                if(itemCardapio.lista.contents.size() == 0){
                                    itemCardapio.lista.displayName = "";
                                }
                            }
                        }
                    }
                    if(checkBox.isChecked()){
                        if(itemCardapio.isValorMaior()){
                            if(itemCardapio.valor < preco) {
                                itemCardapio.valor = preco;
                            }
                        }else{
                            itemCardapio.valor += preco;
                        }
                    }else{
                        if(itemCardapio.isValorMaior()){
                            double newValor = 0;
                            for (int i = 0; i < itemCardapio.lista.valores.size(); i++) {
                                Double x = itemCardapio.lista.valores.get(i);
                                if(newValor < x){
                                    newValor = x;
                                }
                            }

                            itemCardapio.valor = newValor;
                        }else{
                            itemCardapio.valor -= preco;
                        }
                    }
                }



                valorView();
                itemCardapio.modificate = itemCardapio.lista.contents.size() > 0;
                itemCardapio.selected_obg = itemCardapio.modificate;
            }catch (Exception x){
                //message(x.getMessage());
            }
        });
        vazio.setVisibility(View.GONE);
        lista_layout.setVisibility(View.VISIBLE);
        lyt_progress.setVisibility(View.GONE);
        layout_wifi_error.setVisibility(View.GONE);
        root.findViewById(R.id.adicionar_carrinho).setOnClickListener((v -> adicionar()));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void adicionar() {
        try {
            boolean isValid = true;
            boolean modificate = false;
            List<ItemCardapio> lista = adaptador.getLista();
            for (int i = 0; i < lista.size(); i++) {
                ItemCardapio itemCardapio = lista.get(i);
                if(itemCardapio.modificate){
                    modificate = true;
                }
                if(itemCardapio.isObgdSelect()){
                    if(!itemCardapio.selected_obg){
                        isValid = false;
                        String msg;
                        msg = HelperManager.messageView(itemCardapio, requireActivity());
                        new DialogMessage(requireActivity(), msg, getString(R.string.inportante)).show();
                        break;
                    }
                }
            }

            if(cardapio.getTipoLanche() != 0){
                modificate = true;
            }

            if(isValid){
                if(modificate){
                    iniciarDialog(lista);
                }else{
                   String msg;
                    msg = getString(R.string.selecione_para_continuar);
                    new DialogMessage(requireActivity(), msg, getString(R.string.inportante)).show();

                }
            }
        }catch (Exception x){
            // message(x.getMessage());
        }

    }

    private void onValue(int parseInt, TextView valorText) {
        subValor = parseInt * valor;
        valorText.setText(Mask.formatarValor(subValor));
    }

    private void iniciarDialog(List<ItemCardapio> lista) {
        try {
            boolean isSelectedQunat = false;
            for(ItemCardapio item : lista){
                if(item.isItensAdicionais()){
                    isSelectedQunat = true;
                    break;
                }
            }
            subValor = this.valor;
            if(isSelectedQunat){
                onIniciar(Integer.parseInt("1"), lista);
            }else{
                @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_carrinho_quantidade, null);
                DialogInput dialog = new DialogInput(requireActivity(), view, getString(R.string.quant_max_titulo));
                TextView numero = view.findViewById(R.id.numero);
                TextView valorText = view.findViewById(R.id.valorText);
                ImageButton remover = view.findViewById(R.id.btn_remover);
                ImageButton adicionar = view.findViewById(R.id.btn_adicionar);
                remover.setEnabled(false);
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
                    onIniciar(Integer.parseInt(numero.getText().toString()), lista);
                }));
                dialog.show();
            }

        }catch (Exception x){
            // message(x.getMessage());
        }
    }

    private void onIniciar(int parseInt, List<ItemCardapio> lista) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId_produto(cardapio.getUid());
        carrinho.setDisplayName(cardapio.getName());
        carrinho.setUid(UUID.randomUUID().toString());
        carrinho.setValorTotal(subValor);
        carrinho.setQuantidade(parseInt);
        carrinho.setUid_client(Usuario.getUid(getActivity()));
        carrinho.setData(DateTime.getTime());
        ArrayList<ItemCardapioLista> list = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            list.add(lista.get(i).lista);
        }
        carrinho.setListas(list);
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void input() {
        ImageUtils.displayImageFromUrl(requireActivity(), cardapio.getImageUrl(), app_bar_image, getResources().getDrawable(R.drawable.image_add_ft));
        displayName.setText(cardapio.getName());
        if(cardapio.getReceita() != null){
            summary.setText(cardapio.getReceita());
        }
        valor = cardapio.getValor();
        valorText.setText(Mask.formatarValor(valor));
    }

    private void valorView() {
        try{
            List<ItemCardapio> itemCardapioList = adaptador.getLista();
            double x = 0;
            for (int i = 0; i < itemCardapioList.size(); i++) {
                x += itemCardapioList.get(i).valor;
            }

            if(cardapio.getTipoLanche() != 0){
                x += cardapio.getValor();
            }
            valor = x;
            valorText.setText(Mask.formatarValor(valor));

        }catch (Exception x){
            //message(x.getMessage());
        }
    }

}