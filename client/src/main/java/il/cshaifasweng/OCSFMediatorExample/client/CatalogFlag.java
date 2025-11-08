package il.cshaifasweng.OCSFMediatorExample.client;

/**
 * A simple static flag used to store catalog state across different parts of the
 * client application.  The class name now matches the filename (CatalogFlag.java)
 * so that Java compilers will not complain about the class/file mismatch.
 */
public class CatalogFlag {
    private static int flagg = 0;

    /**
     * Retrieve the current catalog flag value.
     *
     * @return the current flag value (0 or 1)
     */
    public static int getFlagg() {
        return flagg;
    }

    /**
     * Set the catalog flag value.
     *
     * @param flagg the new flag value
     */
    public static void setFlagg(int flagg) {
        CatalogFlag.flagg = flagg;
    }
}