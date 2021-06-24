package app.birdsoft.meurestaurante.betaScreen;

import androidx.annotation.StringRes;
import androidx.lifecycle.MutableLiveData;

import app.birdsoft.meurestaurante.R;
import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;

public class LoginRepository {

    public static void isContaExist(MutableLiveData<Integer> data, String codigo) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.DADOS)
                .document(codigo)
                .collection(Chave.DADOS)
                .document("autenticacao")
                .get().addOnCompleteListener((result -> {
            if (result.isSuccessful()) {
                data.setValue(result.getResult().exists() ? R.string.sucesso : R.string.message_not_account);
            } else data.setValue(R.string.erro_connectar);
        }));
    }

}
