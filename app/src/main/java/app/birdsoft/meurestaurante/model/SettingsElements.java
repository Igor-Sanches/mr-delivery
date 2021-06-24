package app.birdsoft.meurestaurante.model;

public class SettingsElements {
     private Cliente cliente;
     private boolean success;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
