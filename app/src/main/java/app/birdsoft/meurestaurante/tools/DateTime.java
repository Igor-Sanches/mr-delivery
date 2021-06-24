package app.birdsoft.meurestaurante.tools;
 
import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTime {
    @SuppressLint("ConstantLocale")
    private static final Locale locale = Locale.getDefault();
    
    /**
     *
     * Retorna uma String com a data e hora atual do aparelho
     * @return
     */
    public static String toDateString(){
       SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
       return date.format(new Date());
    }

    /**
     *
     * @param format = String onde voc� coloca o formato da data ex: dd/MM/yyyy
     * @return
     */
    public static String toDateString(String format){
       SimpleDateFormat date = new SimpleDateFormat(format, locale);
       return date.format(new Date());
    }

    /**
     *
     * @param format = String onde voc� coloca o formato da data ex: dd/MM/yyyy
    * @param time = Adicione o time que deseja para retorna a String com o formado desejado
     * @return
     */
    public static String toDateString(String format, long time){
       SimpleDateFormat date = new SimpleDateFormat(format, locale);
       return date.format(time);
    }

    /**
     *
     * Pegue o time da data e hora atual no formato dd/MM/yyyy HH:mm
     * @return
     */
    public static long getTime(){
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(date.format(new Date())).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(DateTime.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static Date getDate(long date){
        return new Date(date);
    }

    public static boolean isAtrasado(String data) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parse = new SimpleDateFormat("HH:mm");
        try{
            Date horaPedidoChegaR = parse.parse(data);
            Date horaAtual = parse.parse(parse.format(new Date()));
            return horaPedidoChegaR.before(horaAtual);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValido(long dataValidade) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date horaPedidoChegaR = parse.parse(toDateString("dd/MM/yyyy", dataValidade));
            Date horaAtual = parse.parse(parse.format(new Date()));
            return horaPedidoChegaR.before(horaAtual);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
