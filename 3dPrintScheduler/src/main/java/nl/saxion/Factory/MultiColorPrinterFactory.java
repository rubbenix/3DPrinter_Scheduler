package nl.saxion.Factory;

import nl.saxion.Models.MultiColor;
import nl.saxion.Models.Printer;
import nl.saxion.interfaces.PrinterFactory;

public class MultiColorPrinterFactory implements PrinterFactory {
    @Override
    public Printer createPrinter(int id, String name, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        return new MultiColor(id, name, manufacturer, maxX, maxY, maxZ, maxColors);
    }
}
