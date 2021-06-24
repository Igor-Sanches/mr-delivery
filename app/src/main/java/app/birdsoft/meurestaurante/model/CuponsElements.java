package app.birdsoft.meurestaurante.model;

import java.util.List;

public class CuponsElements {
    private int vazioVisibility, layoutConexao, layoutWifiOffline, listaVisibility, progressVisibility;
    public List<Cupom> cupoms;

    public int getLayoutWifiOffline() {
        return layoutWifiOffline;
    }

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        this.layoutWifiOffline = layoutWifiOffline;
    }

    public List<Cupom> getCupoms() {
        return cupoms;
    }

    public void setCupoms(List<Cupom> cupoms) {
        this.cupoms = cupoms;
    }

    public int getVazioVisibility() {
        return vazioVisibility;
    }

    public void setVazioVisibility(int vazioVisibility) {
        this.vazioVisibility = vazioVisibility;
    }

    public int getListaVisibility() {
        return listaVisibility;
    }

    public void setListaVisibility(int listaVisibility) {
        this.listaVisibility = listaVisibility;
    }

    public int getProgressVisibility() {
        return progressVisibility;
    }

    public void setProgressVisibility(int progressVisibility) {
        this.progressVisibility = progressVisibility;
    }
}
