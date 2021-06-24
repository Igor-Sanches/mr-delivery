package app.birdsoft.meurestaurante.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import app.birdsoft.meurestaurante.manager.Chave;
import app.birdsoft.meurestaurante.manager.FireStoreUtils;
import app.birdsoft.meurestaurante.manager.Usuario;
import app.birdsoft.meurestaurante.model.Cliente;
import app.birdsoft.meurestaurante.model.ClienteElements;
import app.birdsoft.meurestaurante.tools.Conexao;

public class ClienteRepository {
    public static MutableLiveData<ClienteElements> getInstance(Context context) {
        return GetElements(context);
    }

    private synchronized static MutableLiveData<ClienteElements> GetElements(Context context) {
        MutableLiveData<ClienteElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<ClienteElements> data, Context context) {
        ClienteElements clienteElements = new ClienteElements();
        if(Conexao.isConnected(context)) {
            FireStoreUtils
                    .getDatabaseOnline()
                    .collection(Chave.Usuario)
                    .document(Usuario.getUid(context))
                    .get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot.exists()) {
                        Cliente cliente = documentSnapshot.toObject(Cliente.class);
                        if (cliente != null) {
                            Usuario.setEndereco(cliente.getEndereco(), context);
                            clienteElements.setClinte(cliente);
                            clienteElements.setEndereco(cliente.getEndereco());
                            clienteElements.setNome(cliente.getNome());
                            clienteElements.setBloqueado(cliente.isBlock());
                        } else {
                            clienteElements.setClinte(null);
                            clienteElements.setEndereco(null);
                            clienteElements.setBloqueado(false);
                            clienteElements.setNome(null);
                        }

                    } else {
                        clienteElements.setClinte(null);
                        clienteElements.setEndereco(null);
                        clienteElements.setBloqueado(false);
                        clienteElements.setNome(null);
                    }
                    data.setValue(clienteElements);
                } catch (Exception x) {
                    clienteElements.setClinte(null);
                    clienteElements.setEndereco(null);
                    clienteElements.setBloqueado(false);
                    clienteElements.setNome(null);
                    data.setValue(clienteElements);
                }
            });
        }else{
            clienteElements.setClinte(null);
            clienteElements.setEndereco(null);
            clienteElements.setBloqueado(false);
            clienteElements.setNome(null);
            data.setValue(clienteElements);
        }
    }

    public static void update(MutableLiveData<ClienteElements> data, Context context) {
        getFirebaseData(data, context);
    }
}
