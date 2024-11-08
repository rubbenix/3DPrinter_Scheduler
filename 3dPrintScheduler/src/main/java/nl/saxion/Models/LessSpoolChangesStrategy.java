package nl.saxion.Models;

import nl.saxion.PrintQueueManager;
import nl.saxion.interfaces.PrintStrategy;
public class LessSpoolChangesStrategy implements PrintStrategy {
    @Override
    public void addToQueue(PrintQueueManager printQueueManager, PrintTask printTask) {
        printQueueManager.addPrintTaskToQueue(printTask);
    }
}
