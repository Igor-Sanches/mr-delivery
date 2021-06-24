package app.birdsoft.meurestaurante.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import app.birdsoft.meurestaurante.R;

public class MensagemDialogPedidoFragment extends BottomSheetDialogFragment {
    public AppBarLayout app_bar_layout;
    private BottomSheetBehavior mBehavior;
    public boolean isShowing = false;
    private BottomSheetDialog bottomSheetDialog;
    private final String mensagemView;

    public MensagemDialogPedidoFragment(String mensagemView) {
        this.mensagemView = mensagemView;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        try{
            bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(bundle);
            View inflate = View.inflate(getContext(), R.layout.fragment_mensagem_dialog_pedido, null);
            bottomSheetDialog.setContentView(inflate);
            TextView mensagem = inflate.findViewById(R.id.mensagem);
            ((TextView) inflate.findViewById(R.id.name_toolbar)).setText(getString(R.string.alteracao));
            mensagem.setText(mensagemView);
        this.mBehavior = BottomSheetBehavior.from((View) inflate.getParent());
            this.mBehavior.setPeekHeight(-1);
            this.app_bar_layout = inflate.findViewById(R.id.app_bar_layout);
            inflate.findViewById(R.id.bt_close).setOnClickListener(view -> MensagemDialogPedidoFragment.this.dismiss());

        }catch (Exception x){
            message(x.getMessage());
        }
        return bottomSheetDialog;
    }

    private void message(String message) {
        new AlertDialog.Builder(requireActivity()).setMessage(message).setPositiveButton("OK", null).show();
    }

    public void onStart() {
        super.onStart();
       this.mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        isShowing = false;
        super.onDismiss(dialog);
    }

}
