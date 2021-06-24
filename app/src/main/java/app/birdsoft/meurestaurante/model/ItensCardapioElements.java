package app.birdsoft.meurestaurante.model;

import java.util.List;

public class ItensCardapioElements {
    private int vazioVisibility, layoutConexao, listaVisibility, LayoutWifiOffline, progressVisibility;
    private List<ItemCardapio> itemCardapios;

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public List<ItemCardapio> getItemCardapios() {
        return itemCardapios;
    }

    public void setItemCardapios(List<ItemCardapio> itemCardapios) {
        this.itemCardapios = itemCardapios;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        LayoutWifiOffline = layoutWifiOffline;
    }

    public int getLayoutWifiOffline() {
        return LayoutWifiOffline;
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
