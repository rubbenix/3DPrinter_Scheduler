package nl.saxion.Models;

import nl.saxion.PrinterManager;
import nl.saxion.Models.Printer;

import java.util.ArrayList;
import java.util.List;

public class DefaultPrintTaskSelectionStrategy implements PrintTaskSelectionStrategy{
    @Override
    public PrintTask selectPrintTask(PrinterManager printerManager, Printer printer) {
        Spool[] spools = printer.getCurrentSpools();
        PrintTask chosenTask = null;

        // Primero, buscamos una tarea que coincida con el spool actual de la impresora.
        if (spools[0] != null) {
            for (PrintTask printTask : printerManager.getPendingPrintTasks()) {
                if (printer.printFits(printTask.getPrint())) {
                    if (printer instanceof StandardFDM && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
                        if (spools[0].spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                            chosenTask = printTask;
                            break;
                        }
                    } else if (printer instanceof HousedPrinter && printTask.getColors().size() == 1) {
                        if (spools[0].spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                            chosenTask = printTask;
                            break;
                        }
                    } else if (printer instanceof MultiColor && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= ((MultiColor) printer).getMaxColors()) {
                        boolean printWorks = true;
                        for (int i = 0; i < spools.length && i < printTask.getColors().size(); i++) {
                            if (!spools[i].spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                                printWorks = false;
                                break;
                            }
                        }
                        if (printWorks) {
                            chosenTask = printTask;
                            break;
                        }
                    }
                }
            }
        }

        // Si no encontramos una tarea con el spool actual, buscamos con los spools libres.
        if (chosenTask == null) {
            for (PrintTask printTask : printerManager.getPendingPrintTasks()) {
                if (printer.printFits(printTask.getPrint()) && printerManager.getPrinterCurrentTask(printer) == null) {
                    if (printer instanceof StandardFDM && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
                        Spool chosenSpool = findFreeSpool(printerManager.getFreeSpools(), printTask.getColors().get(0), printTask.getFilamentType());
                        if (chosenSpool != null) {
                            chosenTask = printTask;
                            break;
                        }
                    } else if (printer instanceof HousedPrinter && printTask.getColors().size() == 1) {
                        Spool chosenSpool = findFreeSpool(printerManager.getFreeSpools(), printTask.getColors().get(0), printTask.getFilamentType());
                        if (chosenSpool != null) {
                            chosenTask = printTask;
                            break;
                        }
                    } else if (printer instanceof MultiColor && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= ((MultiColor) printer).getMaxColors()) {
                        List<Spool> chosenSpools = findFreeSpools(printerManager.getFreeSpools(), printTask.getColors(), printTask.getFilamentType());
                        if (chosenSpools.size() == printTask.getColors().size()) {
                            chosenTask = printTask;
                            break;
                        }
                    }
                }
            }
        }
        if (chosenTask != null) {
            if (printer.printFits(chosenTask.getPrint())) {
                printerManager.getRunningPrintTasks().put(printer, chosenTask);
                printerManager.getFreePrinters().remove(printer);
                System.out.println("- Started task: " + chosenTask + " on printer " + printer.getName());
            }
        }

        return chosenTask;
    }

    private Spool findFreeSpool(List<Spool> freeSpools, String color, FilamentType filamentType) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(color, filamentType)) {
                return spool;
            }
        }
        return null;
    }

    private List<Spool> findFreeSpools(List<Spool> freeSpools, List<String> colors, FilamentType filamentType) {
        List<Spool> chosenSpools = new ArrayList<>();
        for (String color : colors) {
            for (Spool spool : freeSpools) {
                if (spool.spoolMatch(color, filamentType) && !chosenSpools.contains(spool)) {
                    chosenSpools.add(spool);
                    break;
                }
            }
        }
        return chosenSpools;
    }
}
