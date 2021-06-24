package app.birdsoft.meurestaurante.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.Cupom;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.tools.Mask;

public class AdaptadorCupons extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cupom> mCupons;
    private final Context context;
    public boolean on_atach = true;
    private OnRemoveCupom onRemoveCupom;

    public void setOnRemoveCupom(OnRemoveCupom onRemoveCupom) {
        this.onRemoveCupom = onRemoveCupom;
    }

    public interface OnRemoveCupom{
        void onRemove(Cupom cupom, int position);
    }

    public void setInsert(List<Cupom> cupon) {
        this.mCupons = cupon;
        notifyDataSetChanged();
    }

    public AdaptadorCupons(Context context, List<Cupom> mCupons) {
        this.context = context;
        this.mCupons = mCupons;
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView codigo, displayName, data, desconto_valor;
        public CardView layout;
        private Button remover_item;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            displayName = root.findViewById(R.id.displayName);
            data = root.findViewById(R.id.data);
            codigo = root.findViewById(R.id.codigo);
            remover_item = root.findViewById(R.id.remover_item);
            desconto_valor = root.findViewById(R.id.desconto_valor);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cupom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                Cupom cupom = mCupons.get(position);
                viewHolder.displayName.setText(getName(cupom));
                viewHolder.codigo.setText(cupom.getCodigo());
                viewHolder.desconto_valor.setText(getDesconto(cupom));

                viewHolder.data.setText(getValido(cupom));
                viewHolder.remover_item.setOnClickListener((v -> onRemoveCupom.onRemove(cupom, position)));
            }catch (Exception x){
                Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getValido(Cupom cupom) {
        if(cupom.isVencimento()){
            return context.getString(R.string.vencimento) + " " + DateTime.toDateString("dd/MM/yyyy", cupom.getDataValidade());
        }else{
            return context.getString(R.string.cupom_valido);
        }
    }

    private String getDesconto(Cupom cupom) {
        if(cupom.getDescontoType() != 0){
            if(cupom.getDescontoType() == 2){
                return Mask.formatarValor(cupom.getValorDesconto()) + " Off";
            }else return ((int)cupom.getValorDesconto()) + "% Off";
        }else return "";
    }

    private String getName(Cupom cupon) {
        if(cupon.getDescontoType() == 0){
            return context.getString(R.string.cupom_frete_grates);
        }else{
            return context.getString(R.string.cupom_desconto);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_atach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mCupons.size();
    }

}
