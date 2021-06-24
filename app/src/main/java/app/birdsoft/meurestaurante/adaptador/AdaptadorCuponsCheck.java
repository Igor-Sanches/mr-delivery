package app.birdsoft.meurestaurante.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.Cupom;
import app.birdsoft.meurestaurante.tools.Mask;

public class AdaptadorCuponsCheck extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cupom> mCupons;
    private final Context context;
    public boolean on_atach = true;
    private OnItemCheckedChanged onItemCheckedChanged;

    public interface OnItemCheckedChanged{
        void onChanged(View view, Cupom cupom, CheckBox checked, int position);
    }

    public void setOnItemCheckedChanged(OnItemCheckedChanged onItemCheckedChanged) {
        this.onItemCheckedChanged = onItemCheckedChanged;
    }

    public void setInsert(List<Cupom> cupon) {
        this.mCupons = cupon;
        notifyDataSetChanged();
    }

    public AdaptadorCuponsCheck(Context context, List<Cupom> mCupons) {
        this.context = context;
        this.mCupons = mCupons;
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout container;
        public CardView layout;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            container= root.findViewById(R.id.containeContents);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.chupom_check, parent, false));
    }

    private final List<CheckBox> boxes = new ArrayList<>();
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                Cupom cupom = mCupons.get(position);
                CheckBox btn = new CheckBox(context);
                btn.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btn.setButtonDrawable(R.drawable.menu_selector);
                btn.setPadding(10, 0, 0, 10);
                btn.setText(getCupomName(cupom));
                btn.setTag(position);
                boxes.add(btn);
                btn.setOnClickListener(view -> {
                    CheckBox btn12 = (CheckBox)view;
                    for (CheckBox checkBox : boxes){
                        if(btn12.getTag() != checkBox.getTag()){
                            checkBox.setChecked(false);
                        }
                    }
                    if(onItemCheckedChanged != null){
                        onItemCheckedChanged.onChanged(view, cupom, btn12, position);
                    }
                });
                viewHolder.container.addView(btn);
            }catch (Exception x){
                Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCupomName(Cupom cupom) {
        if(cupom.getDescontoType() == 0)
            return context.getString(R.string.frete_gratis);
        else if(cupom.getDescontoType() == 1){
            if(cupom.isMininum()){
                return ((long)cupom.getValorDesconto())+"% " + context.getString(R.string.cupom_por_minimu) + " " + Mask.formatarValor(cupom.getValorMinimo());
            }else return  ((long)cupom.getValorDesconto())+"% " + context.getString(R.string.cupom_por);
        }else{
            if(cupom.isMininum()){
                return Mask.formatarValor(cupom.getValorDesconto())+" " + context.getString(R.string.cupom_por_minimu) + " " + Mask.formatarValor(cupom.getValorMinimo());
            }else return Mask.formatarValor(cupom.getValorDesconto()) +" " + context.getString(R.string.cupom_por);
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
