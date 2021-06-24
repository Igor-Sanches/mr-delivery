package app.birdsoft.meurestaurante.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Carrinho implements Serializable {
    public ArrayList<Double> listaValores;
    private String displayName, uid, uid_client, id_produto;
    private long data;
    private Double valorTotal;
    private int quantidade;
    private ArrayList<ItemCardapioLista> listas;

    public ArrayList<Double> getListaValores() {
        return listaValores;
    }

    public void setListaValores(ArrayList<Double> listaValores) {
        this.listaValores = listaValores;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid_client() {
        return uid_client;
    }

    public void setUid_client(String uid_client) {
        this.uid_client = uid_client;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public ArrayList<ItemCardapioLista> getListas() {
        return listas;
    }

    public void setListas(ArrayList<ItemCardapioLista> listas) {
        this.listas = listas;
    }
}
