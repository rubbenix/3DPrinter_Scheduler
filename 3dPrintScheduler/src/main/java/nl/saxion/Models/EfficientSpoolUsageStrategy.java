package nl.saxion.Models;

import nl.saxion.PrintQueueManager;
import nl.saxion.interfaces.PrintStrategy;

public class EfficientSpoolUsageStrategy implements PrintStrategy {

    @Override
    public void addToQueue(PrintQueueManager printQueueManager, PrintTask printTask) {
        // Implement strategy specific logic
        // ...
        printQueueManager.addPrintTaskToQueue(printTask);
    }
}
