package nl.saxion.interfaces;

import nl.saxion.Models.PrintTask;
import nl.saxion.PrintQueueManager;

public interface PrintStrategy {
    void addToQueue(PrintQueueManager printQueueManager, PrintTask printTask);
}
