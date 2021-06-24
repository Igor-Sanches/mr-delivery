package app.birdsoft.meurestaurante.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.Anuncio;
import app.birdsoft.meurestaurante.tools.ItemAnimation;

public class AdaptadorAnuncios extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Anuncio> mAnuncio;
    private final Context context;
    public boolean on_attach = true;
    private int lastPosition = -1;

    public void insert(List<Anuncio> anuncios){
        this.mAnuncio = anuncios;
        notifyDataSetChanged();
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        private final ImageView banner;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            banner = root.findViewById(R.id.banner);
        }
    }

    public AdaptadorAnuncios(List<Anuncio> mAnuncio, Context context) {
        this.mAnuncio = mAnuncio;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_anuncio, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                final OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                final Anuncio anuncio = mAnuncio.get(position);
                Glide.with(context).load(anuncio.getUrlImage()).into(viewHolder.banner);
                setAnimation(viewHolder.itemView, position);
            }catch (Exception x){
                new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
            }
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, i);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View view, int i) {
        if (i > this.lastPosition) {
            int animation_type = 1;
            ItemAnimation.animate(view, this.on_attach ? i : -1, animation_type);
            this.lastPosition = i;
        }
    }

    @Override
    public int getItemCount() {
        return mAnuncio.size();
    }
}



