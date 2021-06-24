package app.birdsoft.meurestaurante.model;

public class Endereco {
    private boolean sn;
    private String nomeRuaNumero, bairro, cidade, referencia, numeroCasa, endereco, tipo_lugar, complemento, bloco_n_ap;

    public boolean isSn() {
        return sn;
    }

    public void setSn(boolean sn) {
        this.sn = sn;
    }

    public String getBloco_n_ap() {
        return bloco_n_ap;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getTipo_lugar() {
        return tipo_lugar;
    }

    public void setBloco_n_ap(String bloco_n_ap) {
        this.bloco_n_ap = bloco_n_ap;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setTipo_lugar(String tipo_lugar) {
        this.tipo_lugar = tipo_lugar;
    }

    public String getNomeRuaNumero() {
        return nomeRuaNumero;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setNomeRuaNumero(String nomeRuaNumero) {
        this.nomeRuaNumero = nomeRuaNumero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }
}
