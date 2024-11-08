package nl.saxion.interfaces;

import nl.saxion.Models.PrintTask;

public interface PrinterAdapter{

    static void print(PrintTask task) {

    }

    void printTask(PrintTask task);

}
