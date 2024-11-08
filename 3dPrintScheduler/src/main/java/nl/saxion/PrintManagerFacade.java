package nl.saxion;

import nl.saxion.Models.*;
import nl.saxion.interfaces.PrinterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PrintManagerFacade {
    private final Scanner scanner;
    private PrinterFactory factory;



    public Printer addPrinter(int id, int type, String name, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        // Use factory to create the printer
        return factory.createPrinter(id, name, manufacturer, maxX, maxY, maxZ, maxColors);
    }
    private PrintQueueManager printQueueManager;

    public PrintManagerFacade() {
        this.scanner = new Scanner(System.in);
        this.printQueueManager = new PrintQueueManager();
    }

    public void addNewPrintTask() {
        List<String> colors = new ArrayList<>();
        List<Print> prints = printQueueManager.getPrints();
        System.out.println("---------- New Print Task ----------");
        System.out.println("---------- Available prints ----------");

        int counter = 1;
        for (Print print : prints) {
            System.out.println("- " + counter + ": " + print.getName());
            counter++;
        }

        System.out.print("- Print number: ");
        int printNumber = numberInput(1, prints.size());
        System.out.println("--------------------------------------");

        Print selectedPrint = prints.get(printNumber - 1);
        String printName = selectedPrint.getName();
        FilamentType selectedFilamentType = chooseFilamentType();
        List<Spool> availableSpools = printQueueManager.getSpools().stream()
                .filter(spool -> spool.getFilamentType() == selectedFilamentType)
                .collect(Collectors.toList());

        System.out.println("---------- Colors ----------");
        selectColors(colors, availableSpools, selectedPrint.getFilamentLength().size());
        System.out.println("--------------------------------------");

        printQueueManager.addPrintTask(printName, colors, selectedFilamentType);
        System.out.println("----------------------------");
    }

    public void registerPrintCompletion() {
        List<Printer> printers = printQueueManager.getPrintersWithCurrentTask();
        System.out.println("---------- Currently Running Printers ----------");

        for (Printer printer : printers) {
            PrintTask currentTask = printQueueManager.getPrinterCurrentTask(printer);
            if (currentTask != null) {
                System.out.println("- " + printer.getId() + ": " + printer.getName() + " - " + currentTask);
            }
        }

        System.out.print("- Printer that is done (ID): ");
        int printerId = numberInput(-1, printers.size());
        System.out.println("-----------------------------------");

        printQueueManager.registerCompletion(printerId);
    }

    public void registerPrinterFailure() {
        List<Printer> printers = printQueueManager.getPrintersWithCurrentTask();
        System.out.println("---------- Currently Running Printers ----------");

        for (Printer printer : printers) {
            PrintTask currentTask = printQueueManager.getPrinterCurrentTask(printer);
            if (currentTask != null) {
                System.out.println("- " + printer.getId() + ": " + printer.getName() + " > " + currentTask);
            }
        }

        System.out.print("- Printer ID that failed: ");
        int printerId = numberInput(1, printers.size());

        printQueueManager.registerPrinterFailure(printerId);
        System.out.println("-----------------------------------");
    }

    public void changePrintStrategy() {
        System.out.println("---------- Change Strategy -------------");
        System.out.println("- Current strategy: " + printQueueManager.getPrintStrategy());
        System.out.println("- 1: Less Spool Changes");
        System.out.println("- 2: Efficient Spool Usage");
        System.out.print("- Choose strategy: ");
        int strategyChoice = numberInput(1, 2);

        if (strategyChoice == 1) {
            printQueueManager.setPrintStrategy("Less Spool Changes");
        } else if (strategyChoice == 2) {
            printQueueManager.setPrintStrategy("Efficient Spool Usage");
        }

        System.out.println("-----------------------------------");
    }

    public void startPrintQueue() {
        System.out.println("---------- Starting Print Queue ----------");
        printQueueManager.startInitialQueue();
        System.out.println("-----------------------------------");
    }

    public void showPrints() {
        List<Print> prints = printQueueManager.getPrints();
        System.out.println("---------- Available prints ----------");
        prints.forEach(System.out::println);
        System.out.println("--------------------------------------");
    }

    public void showSpools() {
        List<Spool> spools = printQueueManager.getSpools();
        System.out.println("---------- Spools ----------");
        spools.forEach(System.out::println);
        System.out.println("----------------------------");
    }

    public void showPrinters() {
        List<Printer> printers = printQueueManager.getPrinters();
        System.out.println("--------- Available printers ---------");
        printers.forEach(this::displayPrinterInfo);
        System.out.println("--------------------------------------");
    }

    public void showPendingPrintTasks() {
        List<PrintTask> printTasks = printQueueManager.getPendingPrintTasks();
        System.out.println("--------- Pending Print Tasks ---------");
        printTasks.forEach(System.out::println);
        System.out.println("--------------------------------------");
    }

    private FilamentType chooseFilamentType() {
        System.out.println("---------- Filament Type ----------");
        System.out.println("- 1: PLA");
        System.out.println("- 2: PETG");
        System.out.println("- 3: ABS");
        System.out.print("- Filament type number: ");
        int filamentTypeChoice = numberInput(1, 3);
        System.out.println("--------------------------------------");

        switch (filamentTypeChoice) {
            case 1:
                return FilamentType.PLA;
            case 2:
                return FilamentType.PETG;
            case 3:
                return FilamentType.ABS;
            default:
                System.out.println("- Not a valid filamentType, bailing out");
                throw new IllegalArgumentException("Invalid filamentType choice");
        }
    }

    private void selectColors(List<String> colors, List<Spool> availableSpools, int numberOfColors) {
        int counter = 1;
        for (Spool spool : availableSpools) {
            System.out.println("- " + counter + ": " + spool.getColor() + " (" + spool.getFilamentType() + ")");
            counter++;
        }

        for (int i = 0; i < numberOfColors; i++) {
            System.out.print("- Color number: ");
            int colorChoice = numberInput(1, availableSpools.size());
            colors.add(availableSpools.get(colorChoice - 1).getColor());
        }
    }

    private void displayPrinterInfo(Printer printer) {
        String output = printer.toString();
        PrintTask currentTask = printQueueManager.getPrinterCurrentTask(printer);

        if (currentTask != null) {
            output = output.replace("--------", "- Current Print Task: " + currentTask + System.lineSeparator() +
                    "--------");
        }

        System.out.println(output);
    }

    private int numberInput(int min, int max) {
        int input = numberInput();
        while (input < min || input > max) {
            input = numberInput();
        }
        return input;
    }

    private int numberInput() {
        return scanner.nextInt();
    }
}
