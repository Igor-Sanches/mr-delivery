package app.birdsoft.meurestaurante.model;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;

import app.birdsoft.meurestaurante.R;

public class LocalLocal {
    LatLng finish;
    LatLng inicio;

    public LocalLocal() {
    }

    public LocalLocal(LatLng latLng, LatLng latLng2) {
        this.inicio = latLng;
        this.finish = latLng2;
    }

    public LatLng getInicio() {
        return this.inicio;
    }

    public void setInicio(LatLng latLng) {
        this.inicio = latLng;
    }

    public LatLng getFinish() {
        return this.finish;
    }

    public void setFinish(LatLng latLng) {
        this.finish = latLng;
    }

    public String getDistancia(){
        LatLng posicaoInicial = new LatLng(getInicio().latitude,getInicio().longitude);
        LatLng posicaiFinal = new LatLng(getFinish().latitude,getFinish().longitude);
        double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
        double calculo = distance / 1000;

        if(distance == 0.0) {
            return "Próximo de você";
        }else if((long)distance <= 1000){
            String _return = new DecimalFormat("#.00").format(calculo) + " metros";;
            return _return.replace(",", "").replace(" metros","").length() >= 3 ? _return : _return.replace(",","");
        }else{
            return new DecimalFormat("#.00").format(calculo) + " Km";
        }
    }

    public long getDistanciaLong() {
        LatLng posicaoInicial = new LatLng(getInicio().latitude,getInicio().longitude);
        LatLng posicaiFinal = new LatLng(getFinish().latitude,getFinish().longitude);
        double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
        return (long)distance;
    }

    public double getDistanciaDouble() {
        LatLng posicaoInicial = new LatLng(getInicio().latitude,getInicio().longitude);
        LatLng posicaiFinal = new LatLng(getFinish().latitude,getFinish().longitude);
        double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
        return distance / 1000;
    }

    public String getTempo(Context c) {
        double distancia = getDistanciaDouble();
        double tempoPreparo = 15;
        if(distancia == 0.0){
            return (long)tempoPreparo + " " + c.getString(R.string.minutos);
        }else{
            double tempo = 2.5 * distancia;
            double inlong = tempo + tempoPreparo;
            return (long)inlong + " " + c.getString(R.string.minutos);
        }
    }


    public long getTempoLong(Context c) {
        double distancia = getDistanciaDouble();
        double tempoPreparo = 10;
        if(distancia == 0.0){
            return (long)tempoPreparo;
        }else{
            double tempo = 2.5 * distancia;
            double inlong = tempo + tempoPreparo;
            return (long)inlong;
        }
    }

    public int getTempoConvertForInt() {
        double distancia = getDistanciaDouble();
        double tempoPreparo = 15;
        if(distancia == 0.0){
            return (int)tempoPreparo;
        }else{
            double tempo = 2.5 * distancia;
            double inlong = tempo + tempoPreparo;
            return (int)inlong;
        }
    }
}
