package nl.saxion;

import nl.saxion.Models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final PrintQueueManager printQueueManager = new PrintQueueManager();
    private final PrintManagerFacade printManagerFacade = new PrintManagerFacade();


    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        initializeData(args);

        int choice;
        do {
            showMenu();
            choice = menuChoice(9);

            switch (choice) {
                case 1:
                    printManagerFacade.addNewPrintTask();
                    break;
                case 2:
                    printManagerFacade.registerPrintCompletion();
                    break;
                case 3:
                    printManagerFacade.registerPrinterFailure();
                    break;
                case 4:
                    //Strategy Pattern
                    printManagerFacade.changePrintStrategy();
                    break;
                case 5:
                    printManagerFacade.startPrintQueue();
                    break;
                case 6:
                    printManagerFacade.showPrints();
                    break;
                case 7:
                    printManagerFacade.showPrinters();
                    break;
                case 8:
                    printManagerFacade.showSpools();
                    break;
                case 9:
                    printManagerFacade.showPendingPrintTasks();
                    break;
            }

            System.out.println("-----------------------------------");
        } while (choice > 0 && choice < 10);

        exit();
    }

    private void initializeData(String[] args) {
        if (args.length > 0) {
            readPrintsFromFile(args[0]);
            readSpoolsFromFile(args[1]);
            readPrintersFromFile(args[2]);
        } else {
            readPrintsFromFile("");
            readSpoolsFromFile("");
            readPrintersFromFile("");
        }
    }

    private void showMenu() {
        System.out.println("------------- Menu ----------------");
        System.out.println("- 1) Add new Print Task");
        System.out.println("- 2) Register Printer Completion");
        System.out.println("- 3) Register Printer Failure");
        System.out.println("- 4) Change printing style");
        System.out.println("- 5) Start Print Queue");
        System.out.println("- 6) Show prints");
        System.out.println("- 7) Show printers");
        System.out.println("- 8) Show spools");
        System.out.println("- 9) Show pending print tasks");
        System.out.println("- 0) Exit");
    }

    private void startPrintQueue() {
        System.out.println("---------- Starting Print Queue ----------");
        printQueueManager.startInitialQueue();
        System.out.println("-----------------------------------");
    }

    private void exit() {
        // Handle cleanup or any other exit-related logic
    }
    // Start Strategy Pattern
    private void changePrintStrategy() {
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
    // Finish Strategy Pattern
    private void registerPrintCompletion() {
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

    private void registerPrinterFailure() {
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

    private void addNewPrintTask() {
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

    private void showPrints() {
        List<Print> prints = printQueueManager.getPrints();
        System.out.println("---------- Available prints ----------");
        prints.forEach(System.out::println);
        System.out.println("--------------------------------------");
    }

    private void showSpools() {
        List<Spool> spools = printQueueManager.getSpools();
        System.out.println("---------- Spools ----------");
        spools.forEach(System.out::println);
        System.out.println("----------------------------");
    }

    private void showPrinters() {
        List<Printer> printers = printQueueManager.getPrinters();
        System.out.println("--------- Available printers ---------");
        printers.forEach(this::displayPrinterInfo);
        System.out.println("--------------------------------------");
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

    private void showPendingPrintTasks() {
        List<PrintTask> printTasks = printQueueManager.getPendingPrintTasks();
        System.out.println("--------- Pending Print Tasks ---------");
        printTasks.forEach(System.out::println);
        System.out.println("--------------------------------------");
    }

    private void readPrintsFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "prints.json";
        }
        URL printResource = getClass().getResource("/" + filename);
        if (printResource == null) {
            System.err.println("Warning: Could not find prints.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(printResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray prints = (JSONArray) jsonParser.parse(reader);
            for (Object p : prints) {
                JSONObject print = (JSONObject) p;
                String name = (String) print.get("name");
                int height = ((Long) print.get("height")).intValue();
                int width = ((Long) print.get("width")).intValue();
                int length = ((Long) print.get("length")).intValue();
                //int filamentLength = ((Long) print.get("filamentLength")).intValue();
                JSONArray fLength = (JSONArray) print.get("filamentLength");
                int printTime = ((Long) print.get("printTime")).intValue();
                ArrayList<Double> filamentLength = new ArrayList();
                for(int i = 0; i < fLength.size(); i++) {
                    filamentLength.add(((Double) fLength.get(i)));
                }
                printQueueManager.addPrint(name, height, width, length, filamentLength, printTime);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readPrintersFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "printers.json";
        }
        URL printersResource = getClass().getResource("/" + filename);
        if (printersResource == null) {
            System.err.println("Warning: Could not find printers.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(printersResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray printers = (JSONArray) jsonParser.parse(reader);
            for (Object p : printers) {
                JSONObject printer = (JSONObject) p;
                int id = ((Long) printer.get("id")).intValue();
                int type = ((Long) printer.get("type")).intValue();
                String name = (String) printer.get("name");
                String manufacturer = (String) printer.get("manufacturer");
                int maxX = ((Long) printer.get("maxX")).intValue();
                int maxY = ((Long) printer.get("maxY")).intValue();
                int maxZ = ((Long) printer.get("maxZ")).intValue();
                int maxColors = ((Long) printer.get("maxColors")).intValue();
                printQueueManager.addPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readSpoolsFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "spools.json";
        }
        URL spoolsResource = getClass().getResource("/" + filename);
        if (spoolsResource == null) {
            System.err.println("Warning: Could not find spools.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(spoolsResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray spools = (JSONArray) jsonParser.parse(reader);
            for (Object p : spools) {
                JSONObject spool = (JSONObject) p;
                int id = ((Long) spool.get("id")).intValue();
                String color = (String) spool.get("color");
                String filamentType = (String) spool.get("filamentType");
                double length = (Double) spool.get("length");
                FilamentType type;
                switch (filamentType) {
                    case "PLA":
                        type = FilamentType.PLA;
                        break;
                    case "PETG":
                        type = FilamentType.PETG;
                        break;
                    case "ABS":
                        type = FilamentType.ABS;
                        break;
                    default:
                        System.out.println("- Not a valid filamentType, bailing out");
                        return;
                }
                printQueueManager.addSpool(new Spool(id, color, type, length));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    private void readJsonFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "printers.json";
        }
        URL printersResource = getClass().getResource("/" + filename);
        if (printersResource == null) {
            System.err.println("Warning: Could not find printers.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(printersResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray printers = (JSONArray) jsonParser.parse(reader);
            for (Object p : printers) {
                JSONObject printer = (JSONObject) p;
                int id = ((Long) printer.get("id")).intValue();
                int type = ((Long) printer.get("type")).intValue();
                String name = (String) printer.get("name");
                String manufacturer = (String) printer.get("manufacturer");
                int maxX = ((Long) printer.get("maxX")).intValue();
                int maxY = ((Long) printer.get("maxY")).intValue();
                int maxZ = ((Long) printer.get("maxZ")).intValue();
                int maxColors = ((Long) printer.get("maxColors")).intValue();
                printQueueManager.addPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private int menuChoice(int max) {
        int choice = -1;
        while (choice < 0 || choice > max) {
            System.out.print("- Choose an option: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                // Try again after consuming the current line
                System.out.println("- Error: Invalid input");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private int numberInput() {
        return scanner.nextInt();
    }

    private int numberInput(int min, int max) {
        int input = numberInput();
        while (input < min || input > max) {
            input = numberInput();
        }
        return input;
    }
}
