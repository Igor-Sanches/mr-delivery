package app.birdsoft.meurestaurante.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static boolean isRunningLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static double roundHalfUp(double d, int i) {
        if (i >= 0) {
            return new BigDecimal(d).setScale(i, RoundingMode.HALF_UP).doubleValue();
        }
        throw new IllegalArgumentException();
    }

    public static int getWidthFromScreenSize(Integer num, Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        double d = displayMetrics.widthPixels;
        double intValue = num;
        Double.isNaN(intValue);
        double d2 = intValue / 100.0d;
        Double.isNaN(d);
        return (int) (d * d2);
    }

    public static void toggleArrow(boolean z, View view, boolean z2) {
        long j = 200;
        if (z) {
            ViewPropertyAnimator animate = view.animate();
            if (!z2) {
                j = 0;
            }
            animate.setDuration(j).rotation(180.0f);
            return;
        }
        ViewPropertyAnimator animate2 = view.animate();
        if (!z2) {
            j = 0;
        }
        animate2.setDuration(j).rotation(0.0f);
    }

    public static void toggleArrow(boolean z, View view) {
        toggleArrow(z, view, true);
    }
}
