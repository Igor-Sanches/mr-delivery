package app.birdsoft.meurestaurante.login.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;


public abstract class WelcomeFragment extends Fragment {
    protected Activity mActivity;

    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Nullable
    public View onCreateView(@NotNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

}
