package nl.saxion.interfaces;

import nl.saxion.Models.Printer;

public interface PrinterFactory {
    Printer createPrinter(int id, String name, String manufacturer, int maxX, int maxY, int maxZ, int maxColors);
}
