package app.birdsoft.meurestaurante.model;

import java.util.List;

public class CarrinhoElements {
    private int vazioVisibility, layoutConexao, listaVisibility, layoutWifiOffline, progressVisibility;
    private boolean isCarrinho;
    private List<Carrinho> carrinhos;
    private int count;

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public int getLayoutWifiOffline() {
        return layoutWifiOffline;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        this.layoutWifiOffline = layoutWifiOffline;
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

    public boolean isCarrinho() {
        return isCarrinho;
    }

    public void setCarrinho(boolean carrinho) {
        isCarrinho = carrinho;
    }

    public List<Carrinho> getCarrinhos() {
        return carrinhos;
    }

    public void setCarrinhos(List<Carrinho> carrinhos) {
        this.carrinhos = carrinhos;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
