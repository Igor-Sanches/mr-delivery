package app.birdsoft.meurestaurante.model;

public class ClienteElements {
    private String endereco;
    private String nome;
    private Cliente clinte;
    private boolean bloqueado;

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cliente getClinte() {
        return clinte;
    }

    public void setClinte(Cliente clinte) {
        this.clinte = clinte;
    }
}
