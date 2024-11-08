package Data;

public abstract class Loader {
    private static PrintLoader printLoader = new JSONPrintLoader();

    public static PrintLoader getPrintLoader() {
        return printLoader;
    }

    public static void setPrintLoader(PrintLoader newPrintLoader) {
        printLoader = newPrintLoader;
    }
}
