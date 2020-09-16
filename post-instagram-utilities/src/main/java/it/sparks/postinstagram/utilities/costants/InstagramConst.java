package it.sparks.postinstagram.utilities.costants;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Umberto Zollo
 */
public class InstagramConst {

    public static final long GENERIC_ERROR = 9999;

    public static final NumberFormat NF1 = NumberFormat.getNumberInstance(Locale.ITALY);

    public static final String PATH_IG = "C:\\CLIENTI\\temp\\IG";

    static {
        NF1.setGroupingUsed(false);
    }

    private InstagramConst() {

    }
}
