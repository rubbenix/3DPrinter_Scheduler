package nl.saxion.Models;

import nl.saxion.interfaces.PrinterAdapter;

public class PrinterAdapterImpl implements PrinterAdapter {

    private PrinterAdapter printerAdapter;

    public PrinterAdapterImpl(PrinterAdapter printable) {
        this.printerAdapter = printable;
    }

    @Override
    public void printTask(PrintTask task) {
        PrinterAdapter.print(task);
    }
}
