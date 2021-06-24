package app.birdsoft.meurestaurante.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.Cardapio;
import app.birdsoft.meurestaurante.tools.ItemAnimation;
import app.birdsoft.meurestaurante.widget.ImageUtils;

public class AdaptadorCardapioPrincipal extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public boolean on_attach = true;
    private int lastPosition = -1;
    private final List<Cardapio> str;
    private final Context context;
    public boolean loading;
    private OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener{
        void onClick(View v, Cardapio item, int position);
    }

    public AdaptadorCardapioPrincipal(List<Cardapio> str, Context context) {
        this.str =str;
        this.context =context;
    }

    public void clear() {
        if(str.size() > 0){
            str.clear();
            notifyDataSetChanged();
        }
    }

    public void insert(List<Cardapio> list) {
        if(list != null){
            setLoaded();
            clear();
            this.str.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setLoaded() {
        this.loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (this.str.get(i) == null) {
                this.str.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView displayName, summary;
        public View layout;
        private final ImageView iconeProduto;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            iconeProduto = root.findViewById(R.id.iconeProduto);
            displayName = root.findViewById(R.id.displayName);
            summary = root.findViewById(R.id.summary);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cardapio, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                Cardapio string = str.get(position);
                viewHolder.displayName.setText(string.getName());
                ImageUtils.displayImageFromUrl(context, string.getImageUrl(), viewHolder.iconeProduto, context.getResources().getDrawable(R.drawable.image_add_ft));
                if(string.getReceita() != null) {
                        viewHolder.summary.setText(string.getReceita());
                }else{
                    viewHolder.summary.setText("");
                }
                viewHolder.layout.setOnClickListener(view -> {
                    if(onClickItemListener != null){
                        onClickItemListener.onClick(view, string, position);
                    }
                });
                setAnimation(viewHolder.itemView, position);
            }catch (Exception x){
                new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
            }
        }
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
        return str.size();
    }
}



