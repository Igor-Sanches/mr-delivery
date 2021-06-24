package app.birdsoft.meurestaurante.servicos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import app.birdsoft.meurestaurante.manager.FirebaseUtils;
import app.birdsoft.meurestaurante.model.MyResponse;
import app.birdsoft.meurestaurante.model.NotificationData;
import app.birdsoft.meurestaurante.model.NotificationSender;
import app.birdsoft.meurestaurante.model.Token;
import app.birdsoft.meurestaurante.settings.Settings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotification {
    private final APIService apiService;
    public SendNotification(){
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }

    public static void UpdateToken(String uid, Context context){
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseUtils.getDatabaseRef().child("Tokens").child(Settings.getUID(context)).child(uid).setValue(token);
    }

    public void onPush(NotificationData data, Context context){
        MutableLiveData<Boolean> data1 = new MutableLiveData<>();
        FirebaseUtils.getDatabaseRef().child("AdminTokens").child(Settings.getUID(context)).child("Tokens").child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String usertoken = dataSnapshot.getValue(String.class);
                    sendNotifications(usertoken, data, data1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, NotificationData data, MutableLiveData<Boolean> data1) {
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    data1.setValue(response.body().success == 1);
                }else{
                    data1.setValue(false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                data1.setValue(false);
            }
        });
    }

}
