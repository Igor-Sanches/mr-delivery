package app.birdsoft.meurestaurante.model;

public class NotificationSender {
    public NotificationData data;
    public String to;

    public NotificationSender(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {
    }
}
