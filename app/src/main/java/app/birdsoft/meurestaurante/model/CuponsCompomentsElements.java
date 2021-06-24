package app.birdsoft.meurestaurante.model;

public class CuponsCompomentsElements {
    private String mensagem;
    private boolean ativo, mensagemType;
    private Cupom cupom;

    public void setMensagemType(boolean mensagemType) {
        this.mensagemType = mensagemType;
    }

    public boolean isMensagemType() {
        return mensagemType;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Cupom getCupom() {
        return cupom;
    }

    public void setCupom(Cupom cupom) {
        this.cupom = cupom;
    }
}
