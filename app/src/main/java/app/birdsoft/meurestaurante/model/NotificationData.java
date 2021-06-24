package app.birdsoft.meurestaurante.model;

public class NotificationData {
    private String title, message, uid_pedido, state_pedido, uid_cliente, notificationType;

    public void setUid_cliente(String uid_cliente) {
        this.uid_cliente = uid_cliente;
    }

    public String getUid_cliente() {
        return uid_cliente;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getState_pedido() {
        return state_pedido;
    }

    public String getUid_pedido() {
        return uid_pedido;
    }

    public void setState_pedido(String state_pedido) {
        this.state_pedido = state_pedido;
    }

    public void setUid_pedido(String uid_pedido) {
        this.uid_pedido = uid_pedido;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NotificationData(){

    }

    public NotificationData(String title, String message, String uid_cliente, String uid_pedido, String state_pedido, NotificationType typeo) {
        this.title = title;
        this.notificationType = typeo.name();
        this.message = message;
        this.uid_cliente=uid_cliente;
        this.uid_pedido = uid_pedido;
        this.state_pedido = state_pedido;
    }
}
