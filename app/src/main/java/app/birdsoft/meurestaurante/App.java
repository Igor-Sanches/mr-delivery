package app.birdsoft.meurestaurante;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.servicos.SendNotification;
import app.birdsoft.meurestaurante.settings.Settings;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Settings.delete(this);
        //if(Usuario.getUid(this) != null)
            //SendNotification.UpdateToken(this);
    }
}
