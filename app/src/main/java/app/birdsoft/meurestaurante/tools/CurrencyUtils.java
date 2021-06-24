package app.birdsoft.meurestaurante.tools;

import java.text.NumberFormat;

public class CurrencyUtils {
    public static String formatCurrencyAmount(Object obj) {
        return NumberFormat.getCurrencyInstance().format(Double.valueOf(Utils.roundHalfUp((Double) obj, 2)));
    }
}
