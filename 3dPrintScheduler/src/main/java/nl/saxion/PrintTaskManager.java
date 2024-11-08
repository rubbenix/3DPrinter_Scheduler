package nl.saxion;

import nl.saxion.Models.Print;
import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Printer;
import java.util.ArrayList;
import java.util.List;

public class PrintTaskManager {
    private final List<PrintTask> printTasks;

    public PrintTaskManager() {
        this.printTasks = new ArrayList<>();
    }

    public void addPrintTask(PrintTask printTask) {
        printTasks.add(printTask);
    }

    public PrintTask getPrinterCurrentTask(Printer printer) {
        return printTasks.stream()
                .filter(printTask -> printTask.getPrint().printFits(printer) && !printTask.isAssigned())
                .findFirst()
                .orElse(null);
    }

    public List<PrintTask> getPendingPrintTasks() {
        return printTasks.stream()
                .filter(printTask -> !printTask.isAssigned())
                .toList();
    }

    public void assignPrintTask(Printer printer, PrintTask printTask) {
        if (!printTask.isAssigned() && printTask.getPrint().printFits(printer)) {
            printTask.assignToPrinter(printer);
            System.out.println("Assigned task to printer " + printer.getName() + ": " + printTask);
        } else {
            System.out.println("Cannot assign task to printer " + printer.getName());
        }
    }

    public void completePrintTask(PrintTask printTask) {
        if (printTask.isAssigned() && !printTask.isCompleted()) {
            printTask.complete();
            System.out.println("Print task completed: " + printTask);
        } else {
            System.out.println("Print task cannot be completed: " + printTask);
        }
    }

    public void failPrintTask(PrintTask printTask) {
        if (printTask.isAssigned() && !printTask.isFailed()) {
            printTask.fail();
            System.out.println("Print task failed: " + printTask);
        } else {
            System.out.println("Print task cannot fail: " + printTask);
        }
    }
}
