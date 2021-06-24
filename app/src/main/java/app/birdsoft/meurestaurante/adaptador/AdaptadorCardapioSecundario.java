package app.birdsoft.meurestaurante.adaptador;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.model.ItemCardapio;
import app.birdsoft.meurestaurante.tools.ItemAnimation;
import app.birdsoft.meurestaurante.tools.Mask;
import app.birdsoft.meurestaurante.tools.Utils;
import app.birdsoft.meurestaurante.tools.ViewAnimation;

public class AdaptadorCardapioSecundario extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemCardapio> itemCardapios;
    private final Context context;
    public boolean on_attach = true;
    private int lastPosition = -1;
    private OnItemCheckedChanged onItemCheckedChanged;

    public List<ItemCardapio> getLista() {
        return itemCardapios;
    }

    public void insert(List<ItemCardapio> itemCardapios) {
        this.itemCardapios = itemCardapios;
        notifyDataSetChanged();
    }

    public interface OnItemCheckedChanged{
        void onChanged(View v, String title, Double preco, ItemCardapio itemCardapio, int position, CheckBox checkBox, LinearLayout layout, ImageButton btnRemove, ImageButton btnAdd, TextView number, int maxItensAdicionais);
    }

    public void setOnItemCheckedChanged(OnItemCheckedChanged onItemCheckedChanged) {
        this.onItemCheckedChanged = onItemCheckedChanged;
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        private final TextView categoria;
        private final LinearLayout container;
        public ImageButton bt_expand;
        public View lyt_expand;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            bt_expand = root.findViewById(R.id.bt_expand);
            lyt_expand = root.findViewById(R.id.lyt_expand);
            categoria = root.findViewById(R.id.titleCategory);
            container= root.findViewById(R.id.containeContents);
        }
    }

    public AdaptadorCardapioSecundario(List<ItemCardapio> itemCardapios, Context context) {
        this.itemCardapios = itemCardapios;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.itens_cardapio, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                final OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                final ItemCardapio itemCardapio = itemCardapios.get(position);
                viewHolder.categoria.setText(itemCardapio.getDispayTitulo());
                final ArrayList<String> textos = itemCardapio.getTextos();
                final ArrayList<Boolean> texts = itemCardapio.getText();
                final ArrayList<String> contents = itemCardapio.getContents();
                final ArrayList<Double> precos = itemCardapio.getValores();
                final ArrayList<Integer> maxItensAdicionais = itemCardapio.getMaxItensAdicionais();
                if(itemCardapio.isItensAdicionais()){
                    for(int i = 0; i < contents.size(); i++){
                        LinearLayout.LayoutParams layoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                        LinearLayout layout = new LinearLayout(context);
                        layout.setLayoutParams(layoutP);
                        layout.setGravity(Gravity.CENTER_VERTICAL);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        TextView texto = null;
                        CheckBox btn = null;
                        if(texts == null){
                            btn = new CheckBox(context);
                            btn.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                            btn.setButtonDrawable(R.drawable.menu_selector);
                            btn.setPadding(20, 0, 0, 0);
                            btn.setText(contents.get(i));
                            btn.setTag(i);
                            itemCardapio.allCheckBox.add(btn);
                        }else{
                            if(texts.get(i)){
                                texto = new TextView(context);
                                texto.setTextAppearance(context, R.style.text_style);
                                //texto.setTextSize(30);
                                texto.setGravity(Gravity.CENTER_VERTICAL);
                                texto.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                                texto.setPadding(20, 0, 0, 0);
                                texto.setText(textos.get(i));
                            }else{
                                btn = new CheckBox(context);
                                btn.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                                btn.setButtonDrawable(R.drawable.menu_selector);
                                btn.setPadding(20, 0, 0, 0);
                                btn.setText(contents.get(i));
                                btn.setTag(i);
                                itemCardapio.allCheckBox.add(btn);
                            }
                        }

                        LinearLayout layout2 = new LinearLayout(context);
                        layout2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        layout2.setGravity(Gravity.CENTER_VERTICAL);
                        layout2.setOrientation(LinearLayout.HORIZONTAL);
                        TextView preco = new TextView(context);
                        FrameLayout.LayoutParams lText = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lText.setMargins(0,0,10,0);
                        preco.setLayoutParams(lText);
                        preco.setText(String.format("+ %s", Mask.formatarValor(precos.get(i))));
                        LinearLayout layoutBtn = new LinearLayout(context);
                        layoutBtn.setVisibility(View.GONE);
                        layoutBtn.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0,0,40,0);
                        layoutBtn.setLayoutParams(layoutParams);
                        layoutBtn.setGravity(Gravity.CENTER_VERTICAL);
                        TypedValue value = new TypedValue();
                        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, value, true);
                        ImageButton btnRemove = new ImageButton(context);
                        TableLayout.LayoutParams btns = new TableLayout.LayoutParams(100, 100);
                        btnRemove.setColorFilter(context.getResources().getColor(R.color.preto));
                        btnRemove.setLayoutParams(btns);
                        btnRemove.setEnabled(false);
                        btnRemove.setBackgroundResource(value.resourceId);
                        btnRemove.setImageResource(R.drawable.ic_action_remove);
                        TextView number = new TextView(context);
                        TableLayout.LayoutParams paramsNumber = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsNumber.setMargins(20,0,20,0);
                        number.setGravity(Gravity.CENTER_HORIZONTAL);
                        number.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        number.setLayoutParams(paramsNumber);
                        number.setText("1");
                        ImageButton btnAdd = new ImageButton(context);
                        if(maxItensAdicionais.get(i) == 1){
                            layoutBtn.setVisibility(View.GONE);
                        }
                        btnAdd.setColorFilter(context.getResources().getColor(R.color.preto));
                        btnAdd.setLayoutParams(btns);
                        btnAdd.setBackgroundResource(value.resourceId);
                        btnAdd.setImageResource(R.drawable.ic_action_add);
                        layoutBtn.addView(btnRemove);
                        layoutBtn.addView(number);
                        layoutBtn.addView(btnAdd);
                        if(texts == null){
                            layout2.addView(preco);
                        }else{
                            if(!texts.get(i)){
                                layout2.addView(preco);
                            }
                        }
                        layout2.addView(layoutBtn);
                        if(texts == null){
                            layout.addView(btn);
                        }else layout.addView(texts.get(i) ? texto : btn);
                        layout.addView(layout2);

                        if(texts == null){
                            btn.setOnClickListener(view -> {
                                CheckBox btn1 = (CheckBox)view;
                                if(onItemCheckedChanged != null){
                                    onItemCheckedChanged.onChanged(view, contents.get((int) btn1.getTag()), precos.get((int) btn1.getTag()), itemCardapio, position, btn1, layoutBtn, btnRemove, btnAdd, number, maxItensAdicionais.get((int) btn1.getTag()));
                                }
                            });
                        }else{
                            if(!texts.get(i)){
                                assert btn != null;
                                btn.setOnClickListener(view -> {
                                    CheckBox btn1 = (CheckBox)view;
                                    if(onItemCheckedChanged != null){
                                        onItemCheckedChanged.onChanged(view, contents.get((int) btn1.getTag()), precos.get((int) btn1.getTag()), itemCardapio, position, btn1, layoutBtn, btnRemove, btnAdd, number, maxItensAdicionais.get((int) btn1.getTag()));
                                    }
                                });
                            }
                        }

                        viewHolder.container.addView(layout);
                    }

                }else{
                    for(int i = 0; i < contents.size(); i++){
                        LinearLayout.LayoutParams layoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                        LinearLayout layout = new LinearLayout(context);
                        layout.setLayoutParams(layoutP);
                        layout.setGravity(Gravity.CENTER_VERTICAL);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        TextView texto = null;
                        CheckBox btn = null;
                        if(texts == null){
                            btn = new CheckBox(context);
                            btn.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                            btn.setButtonDrawable(R.drawable.menu_selector);
                            btn.setPadding(20, 0, 0, 0);
                            btn.setText(contents.get(i));
                            btn.setTag(i);
                            itemCardapio.allCheckBox.add(btn);
                            btn.setOnClickListener(view -> {
                                CheckBox btn12 = (CheckBox)view;
                                if(onItemCheckedChanged != null){
                                    onItemCheckedChanged.onChanged(view, contents.get((int) btn12.getTag()), precos.get((int) btn12.getTag()), itemCardapio, position, btn12, null, null, null, null, 0);
                                }
                            });
                        }else{
                            if(texts.get(i)){
                                texto = new TextView(context);
                                texto.setTextAppearance(context, R.style.text_style);
                                texto.setGravity(Gravity.CENTER_VERTICAL);
                                texto.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                                texto.setPadding(20, 0, 0, 0);
                                texto.setText(textos.get(i));
                            }else{
                                btn = new CheckBox(context);
                                btn.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                                btn.setButtonDrawable(R.drawable.menu_selector);
                                btn.setPadding(20, 0, 0, 0);
                                btn.setText(contents.get(i));
                                btn.setTag(i);
                                itemCardapio.allCheckBox.add(btn);
                                btn.setOnClickListener(view -> {
                                    CheckBox btn12 = (CheckBox)view;
                                    if(onItemCheckedChanged != null){
                                        onItemCheckedChanged.onChanged(view, contents.get((int) btn12.getTag()), precos.get((int) btn12.getTag()), itemCardapio, position, btn12, null, null, null, null, 0);
                                    }
                                });
                            }
                        }

                        TextView preco = new TextView(context);
                        preco.setText(String.format("+ %s", Mask.formatarValor(precos.get(i))));

                        if(texts == null){
                            layout.addView(btn);
                            layout.addView(preco);
                        }else{
                            layout.addView(texts.get(i) ? texto : btn);
                            if(!texts.get(i)){
                                layout.addView(preco);
                            }
                        }
                        viewHolder.container.addView(layout);
                    }
                }
                /*
                if(itemCardapio.isItensAdicionais()){
                    for(int i = 0; i < contents.size(); i++){
                        LinearLayout.LayoutParams layoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                        LinearLayout layout = new LinearLayout(context);
                        layout.setLayoutParams(layoutP);
                        layout.setGravity(Gravity.CENTER_VERTICAL);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        CheckBox btn = new CheckBox(context);
                        btn.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                        btn.setButtonDrawable(R.drawable.menu_selector);
                        btn.setPadding(20, 0, 0, 0);
                        btn.setText(contents.get(i));
                        btn.setTag((int)i);
                        itemCardapio.allCheckBox.add(btn);
                        LinearLayout layout2 = new LinearLayout(context);
                        layout2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        layout2.setGravity(Gravity.CENTER_VERTICAL);
                        layout2.setOrientation(LinearLayout.HORIZONTAL);
                        TextView preco = new TextView(context);
                        FrameLayout.LayoutParams lText = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lText.setMargins(0,0,10,0);
                        preco.setLayoutParams(lText);
                        preco.setText(String.format("+ %s", Mask.formatarValor(precos.get(i))));
                        LinearLayout layoutBtn = new LinearLayout(context);
                        layoutBtn.setVisibility(View.GONE);
                        layoutBtn.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0,0,40,0);
                        layoutBtn.setLayoutParams(layoutParams);
                        layoutBtn.setGravity(Gravity.CENTER_VERTICAL);
                        TypedValue value = new TypedValue();
                        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, value, true);
                        ImageButton btnRemove = new ImageButton(context);
                        TableLayout.LayoutParams btns = new TableLayout.LayoutParams(100, 100);
                        btnRemove.setColorFilter(context.getResources().getColor(R.color.preto));
                        btnRemove.setLayoutParams(btns);
                        btnRemove.setEnabled(false);
                        btnRemove.setBackgroundResource(value.resourceId);
                        btnRemove.setImageResource(R.drawable.ic_action_remove);
                        TextView number = new TextView(context);
                        TableLayout.LayoutParams paramsNumber = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsNumber.setMargins(20,0,20,0);
                        number.setGravity(Gravity.CENTER_HORIZONTAL);
                        number.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        number.setLayoutParams(paramsNumber);
                        number.setText("1");
                        ImageButton btnAdd = new ImageButton(context);
                        if(maxItensAdicionais.get(i) == 1){
                            layoutBtn.setVisibility(View.GONE);
                        }
                        btnAdd.setColorFilter(context.getResources().getColor(R.color.preto));
                        btnAdd.setLayoutParams(btns);
                        btnAdd.setBackgroundResource(value.resourceId);
                        btnAdd.setImageResource(R.drawable.ic_action_add);
                        layoutBtn.addView(btnRemove);
                        layoutBtn.addView(number);
                        layoutBtn.addView(btnAdd);
                        layout2.addView(preco);
                        layout2.addView(layoutBtn);
                        layout.addView(btn);
                        layout.addView(layout2);

                        btn.setOnClickListener(view -> {
                            CheckBox btn1 = (CheckBox)view;
                            if(onItemCheckedChanged != null){
                                onItemCheckedChanged.onChanged(view, contents.get((int) btn1.getTag()), precos.get((int) btn1.getTag()), itemCardapio, position, btn1, layoutBtn, btnRemove, btnAdd, number, maxItensAdicionais.get((int) btn1.getTag()));
                            }
                        });
                        viewHolder.container.addView(layout);
                    }

                }else{
                    for(int i = 0; i < contents.size(); i++){
                        LinearLayout.LayoutParams layoutP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                        LinearLayout layout = new LinearLayout(context);
                        layout.setLayoutParams(layoutP);
                        layout.setGravity(Gravity.CENTER_VERTICAL);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        CheckBox btn = new CheckBox(context);
                        btn.setLayoutParams(new TableLayout.LayoutParams(0, 90, 1.0f));
                        btn.setButtonDrawable(R.drawable.menu_selector);
                        btn.setPadding(20, 0, 0, 0);
                        btn.setText(contents.get(i));
                        btn.setTag((int)i);
                        itemCardapio.allCheckBox.add(btn);
                        btn.setOnClickListener(view -> {
                            CheckBox btn12 = (CheckBox)view;
                            if(onItemCheckedChanged != null){
                                onItemCheckedChanged.onChanged(view, contents.get((int) btn12.getTag()), precos.get((int) btn12.getTag()), itemCardapio, position, btn12, null, null, null, null, 0);
                            }
                        });
                        TextView preco = new TextView(context);
                        preco.setText(String.format("+ %s", Mask.formatarValor(precos.get(i))));
                        layout.addView(btn);
                        layout.addView(preco);
                        viewHolder.container.addView(layout);
                    }
                }*/



               /* if(bloco.isMultiselect() || bloco.getSelectMax() >= 2){
                    for(int i = 0; i < contents.size(); i++){
                        CheckBox btn = new CheckBox(context);
                        btn.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                        btn.setButtonDrawable(R.drawable.menu_selector);
                        btn.setPadding(20, 0, 0, 0);
                        btn.setText(contents.get(i));
                        btn.setTag((int)i);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CheckBox btn = (CheckBox)view;
                                if(onItemCheckedChanged != null){
                                    onItemCheckedChanged.onChanged(view, contents.get((int)btn.getTag()), precos.get((int)btn.getTag()), bloco, position, btn);
                                }
                            }
                        });
                        viewHolder.container.addView(btn);
                    }
                }else {
                    RadioGroup radioGroup = new RadioGroup(context);
                    radioGroup.setOrientation(LinearLayout.VERTICAL);
                    for(int i = 0; i < contents.size(); i++){
                        RadioButton btn = new RadioButton(context);
                        btn.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                        btn.setButtonDrawable(R.drawable.menu_selector);
                        btn.setPadding(10, 0, 0, 0);
                        btn.setText(contents.get(i));
                        btn.setTag((int)i);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RadioButton btn = (RadioButton)view;
                                if(onItemCheckedChanged != null){
                                    onItemCheckedChanged.onChanged(view, contents.get((int)btn.getTag()), precos.get((int)btn.getTag()), bloco, position, null);
                                }
                            }
                        });
                        radioGroup.addView(btn);
                    }
                    viewHolder.container.addView(radioGroup);
                }
                */
                viewHolder.bt_expand.setOnClickListener(view -> itemCardapios.get(position).expanded = toggleLayoutExpand(!itemCardapio.expanded, view, viewHolder.lyt_expand));
                if (itemCardapio.expanded) {
                    viewHolder.lyt_expand.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.lyt_expand.setVisibility(View.GONE);
                }
                Utils.toggleArrow(itemCardapio.expanded, viewHolder.bt_expand, false);
                setAnimation(viewHolder.itemView, position);
            }catch (Exception x){
                new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
            }
        }

    }

    public boolean toggleLayoutExpand(boolean z, View view, View view2) {
        Utils.toggleArrow(z, view);
        if (z) {
            ViewAnimation.expand(view2);
        } else {
            ViewAnimation.collapse(view2);
        }
        return z;
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
        return itemCardapios.size();
    }
}



