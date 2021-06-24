package app.birdsoft.meurestaurante.model;

public class Anuncio {

    private String UrlImage, NumeroDeImagens;

    public String getNumeroDeImagens() {
        return NumeroDeImagens;
    }

    public void setNumeroDeImagens(String numeroDeImagens) {
        this.NumeroDeImagens = numeroDeImagens;
    }

    public Anuncio(){}

    public Anuncio(String UrlImage) {
        this.UrlImage = UrlImage;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        this.UrlImage = urlImage;
    }

}
