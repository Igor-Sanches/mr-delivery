package app.birdsoft.meurestaurante.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.dialogo.DialogEndereco;
import app.birdsoft.meurestaurante.dialogo.DialogSair;
import app.birdsoft.meurestaurante.model.BlocoPublicar;
import app.birdsoft.meurestaurante.model.Endereco;
import app.birdsoft.meurestaurante.model.EstabelecimentoHorario;
import app.birdsoft.meurestaurante.model.ItemCardapio;
import app.birdsoft.meurestaurante.model.LocalLocal;
import app.birdsoft.meurestaurante.model.Pedido;
import app.birdsoft.meurestaurante.tools.DateTime;
import app.birdsoft.meurestaurante.widget.MyToast;

public class HelperManager {

    public static LiveData<Boolean> getContrato(Context context){
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        FireStoreUtils
                .getDatabaseOnline()
                .collection("Contato")
                .document("contrato")
                .get()
                .addOnCompleteListener((result -> {
                    if(result.isSuccessful()){
                        if(result.getResult().exists()){
                            Map<String, Object> map = result.getResult().getData();
                            if(map != null){
                                if(map.get("numero") != null){
                                    data.setValue(true);
                                    startZap((String) Objects.requireNonNull(map.get("numero")), context);
                                }else data.setValue(false);
                            }else data.setValue(false);
                        }else data.setValue(false);
                    }else data.setValue(false);
                }));
        return data;
    }

    public static void startZap(String zap2, Context context) {
        StringBuilder numero = new StringBuilder();
        for(char c : zap2.toCharArray()){
            if("0123456789".contains(String.valueOf(c))){
                numero.append(c);
            }
        }

        String zap = PhoneNumberUtils.formatNumber(numero.toString());
        if("###########".length() == zap.length() || "##########".length() == zap.length()){
            zap = "+55" + zap;
        }
        String link = "https://api.whatsapp.com/send?phone=" + zap;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    public static String messageView(ItemCardapio itemCardapio, Context context) {
        String msg;
        String start;
        if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.escolha)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.escolar))){
            start = context.getString(R.string.e_nescessario_que);
        }else if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.selecione)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.selaciona))){
            start = context.getString(R.string.e_nescessario_voce);
        }else if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.bedida)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.bebidas))){
            start = context.getString(R.string.e_nescessario_voce_seleciona_uma);
        }else if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.sabo)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.sabor))){
            start = context.getString(R.string.e_nescessario_voce_selecione_um);
        }else{
            start = context.getString(R.string.e_nescessario);
        }
        if(!itemCardapio.isMultiselect()){
            String f = itemCardapio.getSelectMax() == 1 ? context.getString(R.string.item) : context.getString(R.string.itens);

            msg = start + " " + itemCardapio.getDispayTitulo().toLowerCase() + " " + context.getString(R.string.para_continuar_vc_selecione_ate) + " " + itemCardapio.getSelectMax() + " " + f;
        }else{
            msg = start + " " + itemCardapio.getDispayTitulo().toLowerCase() + " " + context.getString(R.string.para_continuar);
        }

        return msg;
    }

    public static String getNumero(String numero) {
        StringBuilder _numero = new StringBuilder();
        for(char c : numero.toCharArray()){
            if("0123456789".contains(String.valueOf(c))){
                _numero.append(c);
            }
        }
        return _numero.toString();
    }

    public static String getPrazo(int prazo, long dataPedido){
        Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
        calendar.setTime(DateTime.getDate(dataPedido));
        calendar.add(Calendar.MINUTE, prazo);
        String hora = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minuto = String.valueOf(calendar.get(Calendar.MINUTE));
        if(hora.length() == 1){
            hora = "0" + hora;
        }
        if(minuto.length() == 1){
            minuto = "0" + minuto;
        }

        return hora + ":" + minuto;
    }

    public static boolean isAtrasado(String text) {
        return DateTime.isAtrasado(text);
    }

    public static void startCall(Pedido pedido, Context context) {
        StringBuilder numero = new StringBuilder();
        for(char c : pedido.getTelefone().toCharArray()){
            if("0123456789".contains(String.valueOf(c))){
                numero.append(c);
            }
        }

        String _numero = PhoneNumberUtils.formatNumber(numero.toString());
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _numero)));

    }

    public static void startZap(Pedido pedido, Context context) {
        StringBuilder numero = new StringBuilder();
        for(char c : pedido.getTelefone().toCharArray()){
            if("0123456789".contains(String.valueOf(c))){
                numero.append(c);
            }
        }

        String zap = PhoneNumberUtils.formatNumber(numero.toString());
        if("###########".length() == zap.length() || "##########".length() == zap.length()){
            zap = "+55" + zap;
        }
        String link = "https://api.whatsapp.com/send?phone=" + zap;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static void startGPS(Pedido pedido, Context context) {
        String coordenadas = pedido.getCoordenadas();
        String link = "https://www.google.com.br/maps/dir//" + coordenadas + "/@" + coordenadas + ",16z/data=!4m4!4m3!1m1!4e2!1m0";
        Uri gmmIntentUri = Uri.parse(link);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }else{
            MyToast.makeText(context, R.string.erro_gps_rota).show();
        }
    }

    public static void exitApp(Activity activity) {
        new DialogSair(activity).show();
    }

    public static boolean isVencido(long dataValidade) {
        return !DateTime.isValido(dataValidade);
    }

    public static long clieckTime = 0;
    public static boolean isClicked(){
        if(System.currentTimeMillis() - clieckTime > 4000){
            clieckTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public static String getPrazoEst(int prazoType, int minutoPrazo, LocalLocal localLocal, Context context) {
        if(prazoType == 0){
            return localLocal.getTempo(context);
        }else{
            return minutoPrazo + " " + context.getString(R.string.minutos);
        }
    }

    public static int getPrazoEstConvertForInt(int prazoType, int minutoPrazo, LocalLocal localLocal) {
        if(prazoType == 0){
            return localLocal.getTempoConvertForInt();
        }else{
            return minutoPrazo;
        }
    }

    public static List<EstabelecimentoHorario> drive(List<EstabelecimentoHorario> horarios) {
        if(horarios == null){
            List<EstabelecimentoHorario> _horarios = new ArrayList<>();
            _horarios.add(new EstabelecimentoHorario("dom", false, "--:--", "--:--"));
            _horarios.add(new EstabelecimentoHorario("seg", false, "--:--", "--:--"));
            _horarios.add(new EstabelecimentoHorario("ter", false, "--:--", "--:--"));
            _horarios.add(new EstabelecimentoHorario("qua", false, "--:--", "--:--"));
            _horarios.add(new EstabelecimentoHorario("qui", false, "--:--", "--:--"));
            _horarios.add(new EstabelecimentoHorario("set", false, "--:--", "--:--"));
            _horarios.add(new EstabelecimentoHorario("sab", false, "--:--", "--:--"));
            return _horarios;
        }
        return horarios;
    }

    public static ItemCardapio convert(BlocoPublicar publicar) {
        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setContents(publicar.getContents());
        itemCardapio.setDispayTitulo(publicar.getDispayTitulo());
        itemCardapio.setText(publicar.getText());
        itemCardapio.setTextos(publicar.getTextos());
        itemCardapio.setItensAdicionais(publicar.isItensAdicionais());
        itemCardapio.setMaxItensAdicionais(publicar.getMaxItensAdicionais());
        itemCardapio.setMultiselect(publicar.isMultiselect());
        itemCardapio.setObgdSelect(publicar.isObgdSelect());
        itemCardapio.setSelectMax(publicar.getSelectMax());
        itemCardapio.setValores(publicar.getValores());
        itemCardapio.setValorMaior(publicar.isValorMaior());
        return itemCardapio;
    }

    public static LiveData<Endereco> inputEnderecoCliente(Activity context){
        MutableLiveData<Endereco> data = new MutableLiveData<>();
        inputEnderecoCliente(context, null, data);
        return data;
    }

    public static LiveData<Endereco> inputEnderecoCliente(Activity context, Endereco endereco){
        MutableLiveData<Endereco> data = new MutableLiveData<>();
        inputEnderecoCliente(context, endereco, data);
        return data;
    }

    public static void inputEnderecoCliente(Activity context, Endereco _endereco, MutableLiveData<Endereco> data) {
        DialogEndereco dialogEndereco = new DialogEndereco(context, _endereco, data);
        dialogEndereco.show();
    }

    public static List<ItemCardapio> convert(ArrayList<BlocoPublicar> cardapio) {
        List<ItemCardapio> cardapioList = new ArrayList<>();
        for(BlocoPublicar blocoPublicar : cardapio){
            cardapioList.add(convert(blocoPublicar));
        }
        return cardapioList;
    }
}
