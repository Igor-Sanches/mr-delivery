package app.birdsoft.meurestaurante.servicos;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.birdsoft.meurestaurante.model.NotificationType;
import app.birdsoft.meurestaurante.settings.Settings;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message, id_pedido, state_pedido, notificationType;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(Settings.isNotification(this)){
            title=remoteMessage.getData().get("title");
            message=remoteMessage.getData().get("message");
            id_pedido=remoteMessage.getData().get("uid_pedido");
            state_pedido=remoteMessage.getData().get("state_pedido");
            notificationType=remoteMessage.getData().get("notificationType");

            switch (NotificationType.valueOf(notificationType)){
                case pedido:
                    NotificationSend.sendPedidosAtualizacao(title, message, state_pedido, this);
                    break;
                case prazo:
                    NotificationSend.sendAlertaPrazo(title, message, this);
                    break;
                case alterar_pedido:
                    NotificationSend.sendAlterarPedido(title, message, this);
                    break;
                case cupon:
                    NotificationSend.onCupom(title, message, this);
                    break;
            }
        }
    }

}
