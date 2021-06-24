package app.birdsoft.meurestaurante.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.birdsoft.meurestaurante.MainActivity;
import app.birdsoft.meurestaurante.betaScreen.FzerLoginComoClienteActivity;
import app.birdsoft.meurestaurante.manager.UpdateAllViewModel;
import app.birdsoft.meurestaurante.settings.Settings;

public abstract class BaseViewActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if(Settings.getUID(this).equals("null")){
            startActivity(new Intent(this, FzerLoginComoClienteActivity.class));
        }else{
            if (WelcomeActivity.shouldDisplay(this)) {
                startActivity(new Intent(this, WelcomeActivity.class));
            } else{
                new UpdateAllViewModel(this).execute(this);
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        finish();

    }
}
