package app.birdsoft.meurestaurante.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.adaptador.AdaptadorCardapioPrincipal;
import app.birdsoft.meurestaurante.view.CardapioActivity;
import app.birdsoft.meurestaurante.viewModel.CardapioViewModel;
import app.birdsoft.meurestaurante.widget.MyToast;

public class CardapioPrincipalFragment extends Fragment {

    private LinearLayout vazio, listaLayout, layout_wifi_error, lyt_progress;
    private AdaptadorCardapioPrincipal adaptador;
    private CardapioViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cardapio_principal, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CardapioViewModel.class);
        viewModel.init(getActivity());
        adaptador = new AdaptadorCardapioPrincipal(new ArrayList<>(), getActivity());
        vazio = root.findViewById(R.id.vazio);
        listaLayout = root.findViewById(R.id.listaLayout);
        layout_wifi_error = root.findViewById(R.id.layout_wifi_error);
        lyt_progress = root.findViewById(R.id.lyt_progress);
        RecyclerView mRecyclerView = root.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adaptador);
        viewModel.getMutableLiveData().observe(requireActivity(), cardapioElements -> {
            if(cardapioElements!=null){
                adaptador.insert(cardapioElements.getCardapios());
                vazio.setVisibility(cardapioElements.getVazioVisibility());
                listaLayout.setVisibility(cardapioElements.getListaVisibility());
                lyt_progress.setVisibility(cardapioElements.getProgressVisibility());
                layout_wifi_error.setVisibility(cardapioElements.getLayoutWifiOffline());
            }
        });
        adaptador.setOnClickItemListener((v, item, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("cardapio", item);
            CardapioActivity.getInstance().navController.navigate(item.getTipoLanche() == 2 ? R.id.navigation_menu_secundario_unico : R.id.navigation_menu_secundario, bundle);
        });
        root.findViewById(R.id.sem_conexao_1).setOnClickListener((v -> onUpdate()));
        root.findViewById(R.id.sem_conexao_2).setOnClickListener((v -> onUpdate()));
        return root;
    }

    private void onUpdate() {
        MyToast.makeText(requireActivity(), R.string.atualizando).show();
        viewModel.update(requireActivity());
    }

    @Override
    public void onResume() {
        viewModel.update(getActivity());
        super.onResume();
    }
}