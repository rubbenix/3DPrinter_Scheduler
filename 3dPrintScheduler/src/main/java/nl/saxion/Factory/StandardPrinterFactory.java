package nl.saxion.Factory;

import nl.saxion.Models.Printer;
import nl.saxion.Models.StandardFDM;
import nl.saxion.interfaces.PrinterFactory;

public class StandardPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(int id, String name, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new StandardFDM(id, name, manufacturer, maxX, maxY, maxZ) {
        };
    }
}