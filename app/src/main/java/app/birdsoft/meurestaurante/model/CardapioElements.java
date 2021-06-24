package app.birdsoft.meurestaurante.model;

import java.util.List;

public class CardapioElements {
    private int vazioVisibility, layoutConexao, listaVisibility, progressVisibility, LayoutWifiOffline;
    public List<Cardapio> cardapios;

    public int getLayoutWifiOffline() {
        return LayoutWifiOffline;
    }

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        LayoutWifiOffline = layoutWifiOffline;
    }

    public List<Cardapio> getCardapios() {
        return cardapios;
    }

    public void setCardapios(List<Cardapio> cardapios) {
        this.cardapios = cardapios;
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
