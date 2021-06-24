package app.birdsoft.meurestaurante.model;

import java.util.List;

public class AnunciosElements {
    private int vazioVisibility, layoutConexao, listaVisibility, progressVisibility;
    public List<Anuncio> anuncios;

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public List<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
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
