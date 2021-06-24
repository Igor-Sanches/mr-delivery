package app.birdsoft.meurestaurante.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.adaptador.AdaptadorFragments;
import app.birdsoft.meurestaurante.login.fragments.LoginFragment;
import app.birdsoft.meurestaurante.login.fragments.RegistrarFragment;

public class LoginActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private static LoginActivity instance;
    public static LoginActivity getInstance() {
        return instance;
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 1){
            onPage(0);
        }else{
            finish();
        }
    }

    public void onPage(int page){
        viewPager.setCurrentItem(page);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_login);
            instance = this;
            viewPager = findViewById(R.id.viewpager);
            viewPager.beginFakeDrag();
            AdaptadorFragments adaptador = new AdaptadorFragments(getSupportFragmentManager());
            adaptador.adicionar(new LoginFragment(), "");
            adaptador.adicionar(new RegistrarFragment(), "");
            viewPager.setAdapter(adaptador);
        }catch (Exception c){
            Toast.makeText(this, c.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}