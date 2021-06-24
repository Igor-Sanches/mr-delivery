package app.birdsoft.meurestaurante.model;

import com.google.firebase.firestore.GeoPoint;

public class Cliente {

    private String nomeRuaNumero, bairro, cidade, referencia, numeroCasa, tipo_lugar, complemento, bloco_n_ap;;
    private boolean sn;
    private GeoPoint local;
    private boolean block;
    private String msgBlock;

    public void setSn(boolean sn) {
        this.sn = sn;
    }

    public void setTipo_lugar(String tipo_lugar) {
        this.tipo_lugar = tipo_lugar;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setBloco_n_ap(String bloco_n_ap) {
        this.bloco_n_ap = bloco_n_ap;
    }

    public String getTipo_lugar() {
        return tipo_lugar;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBloco_n_ap() {
        return bloco_n_ap;
    }

    public boolean isSn() {
        return sn;
    }

    public String getNomeRuaNumero() {
        return nomeRuaNumero;
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

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void setMsgBlock(String msgBlock) {
        this.msgBlock = msgBlock;
    }

    public String getMsgBlock() {
        return msgBlock;
    }

    public GeoPoint getLocal() {
        return local;
    }

    public void setLocal(GeoPoint local) {
        this.local = local;
    }

    private String uuid, nome, email, telefone, endereco, data;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
