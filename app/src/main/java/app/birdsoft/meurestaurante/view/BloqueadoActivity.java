package app.birdsoft.meurestaurante.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.tools.Conexao;
import app.birdsoft.meurestaurante.widget.MySnackbar;

public class BloqueadoActivity extends AppCompatActivity {

    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloqueado);
        String msg = getIntent().getStringExtra("msg");
        TextView _msg = findViewById(R.id.msg);
        _msg.setText(String.format("%s %s", getString(R.string.block_msg), msg));
        Conexao.getFirebaseAuth().signOut();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - count > 2000){
            count= System.currentTimeMillis();
            MySnackbar.makeText(this, R.string.clique_para_sair).show();
            return;
        }
        finishAffinity();
    }
}