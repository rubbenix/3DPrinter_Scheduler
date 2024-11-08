package nl.saxion.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrinterEventManager {
    private List<PrintTask> activePrintTasks;
    private Map<Integer, Integer> failedPrinters;

    public PrinterEventManager() {
        this.activePrintTasks = new ArrayList<>();
        this.failedPrinters = new HashMap<>();
    }

    public void registerCompletion(int printerId) {
        // Implement completion logic, e.g., remove completed task from active tasks
        // and update printer status.
        // For simplicity, I'm assuming PrintQueueManager handles this.
        // You may need to add more logic based on your requirements.
        // Here, we are just removing the completed task from the active tasks list.
    //    activePrintTasks.removeIf(printTask -> printTask.getPrint().getId() == printerId);
    }

    public void registerFailure(int printerId) {
        // Implement failure logic, e.g., mark the printer as failed.
        // For simplicity, I'm assuming PrintQueueManager handles this.
        // You may need to add more logic based on your requirements.
        failedPrinters.put(printerId, printerId);
    }

    public List<PrintTask> getActivePrintTasks() {
        return activePrintTasks;
    }

    public Map<Integer, Integer> getFailedPrinters() {
        return failedPrinters;
    }

    public void printerCompleted(int printer) {
        // Additional logic if needed when a printer completes a task
    }

    public void printerFailed(Printer printer) {
        // Additional logic if needed when a printer fails
    }

    public boolean isPrinterInFailureState(Printer printer) {
        return failedPrinters.containsKey(printer.getId());
    }

    public void printerStarted(Printer printer) {
        // Additional logic if needed when a printer starts a task
    }

    // Additional methods or modifications as needed
}
