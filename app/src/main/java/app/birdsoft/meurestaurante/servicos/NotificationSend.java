package app.birdsoft.meurestaurante.servicos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.login.SplashActivity;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.tools.Status;
import app.birdsoft.meurestaurante.view.AcompanhamentoPedidoActivity;
import app.birdsoft.meurestaurante.view.CuponsActivity;

public class NotificationSend {

    public static void sendPedidosAtualizacao(String titulo, String texto, String state_pedido, Context context){
            try{
               NotificationCompat.Builder builder =
                       new NotificationCompat.Builder(context, "notif_0009");
               Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Status.valueOf(state_pedido);
                intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : AcompanhamentoPedidoActivity.class);
                switch (Status.valueOf(state_pedido)){
                    case novoPedido:
                    case preparandoPedido:
                        builder.setSmallIcon(R.drawable.cooking_icon);
                        break;
                    case pedidoEntregue:
                        builder.setSmallIcon(R.drawable.ic_action_checked);
                        break;
                    case pedidoEmTransido:
                        builder.setSmallIcon(R.drawable.delivery_icon);
                        break;
                    case pedidoCancelado:
                        builder.setSmallIcon(R.drawable.ic_action_block);
                        break;
                }

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                builder.setWhen(System.currentTimeMillis());
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setContentIntent(pendingIntent);
                builder.setFullScreenIntent(pendingIntent, true);
                builder.setContentText(texto);
                builder.setAutoCancel(true);
                builder.setContentTitle(titulo);
                builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                builder.setVibrate(new long[]{3000});
                builder.setPriority(Notification.PRIORITY_MAX);
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

                NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                   String channel_id = "Channel_Delivey";
                   NotificationChannel channel = new NotificationChannel(channel_id, "Alerta de pedidos", NotificationManager.IMPORTANCE_HIGH);
                   manager.createNotificationChannel(channel);
                   builder.setChannelId(channel_id);
               }

                //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                manager.notify(0, builder.build());

            }catch (Exception x){
               new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
           }
     }

    public static void sendAlertaPrazo(String titulo, String texto, Context context){
        try{
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "notif_0010");
            Intent intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : AcompanhamentoPedidoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.ic_action_timer);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentText(texto);
            builder.setAutoCancel(true);
            builder.setContentTitle(titulo);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(new long[]{3000});
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channel_id = "Channel_Delivey_Prazo";
                NotificationChannel channel = new NotificationChannel(channel_id, "Prazo da entrega", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channel_id);
            }

            //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            manager.notify(1, builder.build());

        }catch (Exception x){
            new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
        }
    }

    public static void sendAlterarPedido(String titulo, String texto, Context context){
        try{
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "notif_0011");
            Intent intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : AcompanhamentoPedidoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.ic_action_menu);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentText(texto);
            builder.setAutoCancel(true);
            builder.setContentTitle(titulo);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(new long[]{3000});
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channel_id = "Channel_Delivey_Alterar";
                NotificationChannel channel = new NotificationChannel(channel_id, "Alterar pedido", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channel_id);
            }

            //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            manager.notify(2, builder.build());

        }catch (Exception x){
            new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
        }
    }

    public static void onCupom(String titulo, String texto, Context context){
        try{
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "notif_0011");
            Intent intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : CuponsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.cupom);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentText(texto);
            builder.setAutoCancel(true);
            builder.setContentTitle(titulo);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(new long[]{3000});
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channel_id = "Channel_Delivey_Cupom";
                NotificationChannel channel = new NotificationChannel(channel_id, "Cupons", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channel_id);
            }

            //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            manager.notify(4, builder.build());

        }catch (Exception x){
            new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
        }

    }
}
