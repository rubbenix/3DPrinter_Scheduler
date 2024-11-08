package nl.saxion.Models;

import nl.saxion.PrinterManager;

public interface PrintTaskSelectionStrategy {
    PrintTask selectPrintTask(PrinterManager printerManager, Printer printer);
}

