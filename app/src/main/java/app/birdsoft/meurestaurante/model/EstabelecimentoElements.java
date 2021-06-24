package app.birdsoft.meurestaurante.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class EstabelecimentoElements {
    private boolean aberto;
    private List<EstabelecimentoHorario> horarios;
    private String whatsapp;
    private String ligacao;
    private GeoPoint geoPoint;
    private String endereco;
    private int layoutWifiOffline, layoutFechado, layoutConexao;
    private int km;
    private Integer prazo;
    private Integer prazoMinutoFixo;
    private int taxa;
    private double valorPerKm;
    private double valorFixo;

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setPrazoMinutoFixo(Integer prazoMinutoFixo) {
        this.prazoMinutoFixo = prazoMinutoFixo;
    }

    public void setPrazo(Integer prazo) {
        this.prazo = prazo;
    }

    public Integer getPrazo() {
        return prazo;
    }

    public Integer getPrazoMinutoFixo() {
        return prazoMinutoFixo;
    }

    public int getLayoutWifiOffline() {
        return layoutWifiOffline;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        this.layoutWifiOffline = layoutWifiOffline;
    }

    public int getLayoutFechado() {
        return layoutFechado;
    }

    public void setLayoutFechado(int layoutFechado) {
        this.layoutFechado = layoutFechado;
    }

    public int getTaxa() {
        return taxa;
    }

    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }

    public double getValorPerKm() {
        return valorPerKm;
    }

    public void setValorPerKm(double valorPerKm) {
        this.valorPerKm = valorPerKm;
    }

    public double getValorFixo() {
        return valorFixo;
    }

    public void setValorFixo(double valorFixo) {
        this.valorFixo = valorFixo;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public int getKm() {
        return km;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public List<EstabelecimentoHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<EstabelecimentoHorario> horarios) {
        this.horarios = horarios;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getLigacao() {
        return ligacao;
    }

    public void setLigacao(String ligacao) {
        this.ligacao = ligacao;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

}
