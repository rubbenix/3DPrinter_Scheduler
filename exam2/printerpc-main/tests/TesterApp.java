import Data.Loader;
import Models.Print;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This application exists purely to test the threading and that everything works reasonably well.
 */
public class TesterApp {
    public static void main(String[] args) {
        new TesterApp().run();
    }

    PrinterPCMock printerPC = new PrinterPCMock();

    public void run() {
        Loader.setPrintLoader(() -> {
            List<Print> prints = new ArrayList<>();
            prints.add(new Print("Stegosaurus Pickholder", 30, 1));
            return prints;
        });

        printerPC.startPrinters();
        System.out.println("Printers started.");

        Scanner scanner = new Scanner(System.in);

        int menuChoice = -1;

        while(menuChoice != 0) {
            System.out.println("1) Add a print to queue.");
            System.out.println("2) Show active prints.");
            System.out.println("3) Show completed prints.");
            System.out.println("4) Clear completed prints.");
            System.out.println("0) Exit.");
            System.out.print("Choice: ");
            menuChoice = scanner.nextInt();
            if(menuChoice == 1) {
                try {
                    printerPC.addPrintToQueue("Stegosaurus Pickholder", "PLA", new String[]{"blue"});
                } catch (IllegalStateException e) {
                    // This error needs to be caught or the program breaks down.
                    System.out.println(e.getMessage());
                }
            } else if (menuChoice == 2) {
                for(String print: printerPC.getActivePrints()) {
                    System.out.println(print);
                }
            } else if (menuChoice == 3) {
                for(String print: printerPC.getCompletedPrints()) {
                    System.out.println(print);
                }
            } else if (menuChoice == 4) {
                printerPC.clearCompletedPrints();
                System.out.println("Completed prints cleared.");
            }
        }

        printerPC.stopPrinters();
        System.out.println("Printers stopped.");

    }

}
