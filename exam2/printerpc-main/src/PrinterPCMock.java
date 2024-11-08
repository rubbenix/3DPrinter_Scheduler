import Data.Loader;
import Models.Print;
import Models.ActivePrint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/**
 * Mock implementation of the PrinterPC, pretends to accept jobs and simply runs a timer to 'complete' prints.
 * This can be used in your specific component.
 */
public class PrinterPCMock implements PrinterPC {
    private List<ActivePrint> activePrints = new ArrayList<>();

    private List<ActivePrint> completedPrints = new ArrayList<>();

    /**
     * Add a new print to the queue, returns a message if this succeeded or not.
     * @param printName
     * @param material
     * @param colors
     * @return unique identifier of new print
     */
    public String addPrintToQueue(String printName, String material, String[] colors) {
        if(activePrints.size() > 25) {
            throw new IllegalStateException("There are no available printers");
        }

        List<String> materials = Arrays.asList("PLA", "PETG", "ABS");

        if(!materials.contains(material)) {
            throw new IllegalArgumentException("Unknown material");
        }

        List<Print> possiblePrints = Loader.getPrintLoader().getPrintList()
                .stream()
                .filter(p -> p.name().equals(printName))
                .toList();

        if(possiblePrints.size() != 1) {
            throw new IllegalArgumentException("Print not found");
        }

        Print print = possiblePrints.get(0);

        if(colors.length != print.nrColors()) {
            throw new IllegalArgumentException("Wrong number of colors supplied");
        }

        ActivePrint ap = new ActivePrint(print.name(), print.time());

        activePrints.add(ap);

        return ap.getID();
    };

    /**
     * Get the list of completed prints.
     * @return List of completed prints with ID and name.
     */
    public String[] getActivePrints() {
        return activePrints.stream().map(ActivePrint::toStringWithTime).toArray(String[]::new);
    }

    /**
     * Get the list of completed prints.
     * @return List of completed prints with ID and name.
     */
    public String[] getCompletedPrints() {
        return completedPrints.stream().map(ActivePrint::toString).toArray(String[]::new);
    }

    /**
     * Clear the list of completed prints.
     */
    public void clearCompletedPrints() {
        completedPrints.clear();
    }

    /**
     * Tick calls the timer causing the prints to count down their remaining timer.
     */
    public void tick() {
        for (ActivePrint ap : activePrints) {
            ap.reduceTimer();
            if (ap.getTimeRemaining() == 0) {
                completedPrints.add(ap);
            }
        }
        activePrints = activePrints.stream()
                .filter(ap -> ap.getTimeRemaining() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    /*
    This section exists so the printers can be tested while working with it.
     */

    Thread printerRunner = new Thread(new Runnable() { public void run() {
        while(printersActive) {
            tick();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    } });

    private boolean printersActive = false;

    /**
     * Start the printers causing a tick to happen each second.
     */
    public void startPrinters() {
        printersActive = true;
        printerRunner.start();
    }

    /**
     * Stop the printers from ticking down.
     */
    public void stopPrinters() {
        printersActive = false;
    }
}
