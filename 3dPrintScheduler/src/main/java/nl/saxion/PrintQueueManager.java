package nl.saxion;
import nl.saxion.interfaces.PrintStrategy;
import nl.saxion.Models.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PrintQueueManager {
    private final List<Printer> printers;
    private final List<Spool> spools;
    private List<Spool> freeSpools = new ArrayList<>();
    private final List<Print> prints;
    private final PrintTaskManager printTaskManager;
    private List<Printer> freePrinters = new ArrayList<>();
    private final PrinterEventManager printerEventManager;
    private List<PrintTask> pendingPrintTasks;
    private String printStrategy = "Less Spool Changes";
    private PrintStrategy IprintStrategy;
    public PrintQueueManager() {
        this.printers = new ArrayList<>();
        this.spools = new ArrayList<>();
        this.prints = new ArrayList<>();
        this.printTaskManager = new PrintTaskManager();
        this.printerEventManager = new PrinterEventManager();
        this.pendingPrintTasks = new ArrayList<>();

    }

    public void setPrintStrategy(PrintStrategy IprintStrategy) {
        this.IprintStrategy = IprintStrategy;
    }

    public void addPrintTaskToQueue(PrintTask printTask) {
        // Delegate to the selected strategy
        if (IprintStrategy != null) {
            IprintStrategy.addToQueue(this, printTask);
        } else {
            // Default behavior if no strategy is set
            // ...
        }
    }
    public void addPrinters(List<Printer> printers) {
        this.printers.addAll(printers);
    }

    public void addSpools(List<Spool> spools) {
        this.spools.addAll(spools);
    }

    public void addPrints(List<Print> prints) {
        this.prints.addAll(prints);
    }
    public void addSpool(Spool spool) {
        spools.add(spool);
        freeSpools.add(spool);
    }

    public List<Print> getPrints() {
        return new ArrayList<>(prints);
    }

    public List<Spool> getSpools() {
        return new ArrayList<>(spools);
    }

    public List<Printer> getPrinters() {
        //return new ArrayList<>(printers);
        return printers;
    }

    public List<Printer> getPrintersWithCurrentTask() {
        return printers.stream()
                .filter(printer -> getPrinterCurrentTask(printer) != null)
                .collect(Collectors.toList());
    }

    public PrintTask getPrinterCurrentTask(Printer printer) {
        return printTaskManager.getPrinterCurrentTask(printer);
    }

    public void addPrintTask(String printName, List<String> colors, FilamentType filamentType) {
        Print print = findPrintByName(printName);
        if (print != null) {
            PrintTask printTask = new PrintTask(print, colors, filamentType);
            printTaskManager.addPrintTask(printTask);
        } else {
            System.out.println("Print not found");
        }
    }

    public void registerCompletion(int printerId) {
        Printer printer = findPrinterById(printerId);
        if (printer != null) {
            PrintTask currentTask = getPrinterCurrentTask(printer);
            if (currentTask != null) {
                printTaskManager.completePrintTask(currentTask);
                printerEventManager.printerCompleted(printer.getId());
            } else {
                System.out.println("Printer has no current task");
            }
        } else {
            System.out.println("Printer not found");
        }
    }

    public void registerPrinterFailure(int printerId) {
        Printer printer = findPrinterById(printerId);
        if (printer != null) {
            PrintTask currentTask = getPrinterCurrentTask(printer);
            if (currentTask != null) {
                printTaskManager.failPrintTask(currentTask);
                printerEventManager.printerFailed(printer);
            } else {
                System.out.println("Printer has no current task");
            }
        } else {
            System.out.println("Printer not found");
        }
    }

    public void startInitialQueue() {
        List<PrintTask> pendingTasks = printTaskManager.getPendingPrintTasks();
        for (Printer printer : printers) {
            if (!printerEventManager.isPrinterInFailureState(printer)) {
                if (!pendingTasks.isEmpty()) {
                    PrintTask nextTask = pendingTasks.remove(0);
                    printTaskManager.assignPrintTask(printer, nextTask);
                    printerEventManager.printerStarted(printer);
                } else {
                    System.out.println("No pending tasks to assign");
                }
            } else {
                System.out.println("Printer " + printer.getName() + " is in a failure state.");
            }
        }
    }

    public void setPrintStrategy(String strategy) {
        this.printStrategy = strategy;
    }

    public String getPrintStrategy() {
        return printStrategy;
    }

    private Print findPrintByName(String printName) {
        return prints.stream()
                .filter(print -> print.getName().equals(printName))
                .findFirst()
                .orElse(null);
    }

    private Printer findPrinterById(int printerId) {
        return printers.stream()
                .filter(printer -> printer.getId() == printerId)
                .findFirst()
                .orElse(null);
    }

    public List<PrintTask> getPendingPrintTasks() {
        return Collections.unmodifiableList(pendingPrintTasks);
    }

    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        if (printerType == 1) {
            StandardFDM printer = new StandardFDM(id, printerName, manufacturer, maxX, maxY, maxZ);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 2) {
            HousedPrinter printer = new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 3) {
            MultiColor printer = new MultiColor(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
            printers.add(printer);
            freePrinters.add(printer);
        }
    }

    public void addPrint(String name, int height, int width, int length, ArrayList<Double> filamentLength, int printTime) {
        Print p = new Print(name, height, width, length, filamentLength, printTime);
        prints.add(p);
    }
}
