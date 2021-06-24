package app.birdsoft.meurestaurante.login.fragments;

import android.content.Context;

import app.birdsoft.meurestaurante.login.WelcomeActivity;

public class IntroFragment extends WelcomeFragment implements WelcomeActivity.WelcomeContent {
    public boolean shouldDisplay(Context context) {
        return false;
    }

}
