package app.birdsoft.meurestaurante.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.Carrinho;
import app.birdsoft.meurestaurante.tools.ItemAnimation;
import app.birdsoft.meurestaurante.tools.Mask;

public class AdaptadorCarrinho extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Carrinho> mCarrinho;
    private final Context context;
    public boolean on_attach = true;
    private int lastPosition = -1;
    private OnDeleteItemListener onDeleteItemListener;

    public List<Carrinho> getLista() {
        return mCarrinho;
    }

    public double getValor(){
        double valor = 0;
        for (Carrinho carrinho : mCarrinho){
            valor += carrinho.getValorTotal();
        }
        return valor;
    }

    public void insert(List<Carrinho> carrinhos) {
        mCarrinho = carrinhos;
        notifyDataSetChanged();
    }

    public interface OnDeleteItemListener{
        void onDelete(View v, Carrinho carrinho, int position);
    }

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton delete;
        public TextView display, valor;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            delete = root.findViewById(R.id.delete);
            display = root.findViewById(R.id.displayName);
            valor = root.findViewById(R.id.valorText);
        }
    }

    public AdaptadorCarrinho(List<Carrinho> mcarrinho, Context context) {
        this.mCarrinho = mcarrinho;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                final OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                final Carrinho carrinho = mCarrinho.get(position);
                viewHolder.display.setText(getName(carrinho));
                viewHolder.valor.setText(Mask.formatarValor(carrinho.getValorTotal()));
                viewHolder.delete.setOnClickListener(view -> {
                    if(onDeleteItemListener!=null){
                        onDeleteItemListener.onDelete(view, carrinho, position);
                    }
                });

                setAnimation(viewHolder.itemView, position);
            }catch (Exception x){
                new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
            }
        }
    }

    private String getName(Carrinho carrinho) {
        String name = carrinho.getDisplayName();
        if(carrinho.getQuantidade() > 1){
            name += " (" + carrinho.getQuantidade() + ")";
        }
        return name;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int i) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, i);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View view, int i) {
        if (i > this.lastPosition) {
            int animation_type = 0;
            ItemAnimation.animate(view, this.on_attach ? i : -1, animation_type);
            this.lastPosition = i;
        }
    }

    @Override
    public int getItemCount() {
        return mCarrinho.size();
    }
}



