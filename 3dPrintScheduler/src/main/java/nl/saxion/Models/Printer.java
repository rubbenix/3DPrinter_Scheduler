package nl.saxion.Models;

import nl.saxion.interfaces.PrinterAdapter;

import java.awt.print.Printable;
import java.util.ArrayList;

public abstract class Printer implements PrinterAdapter {
    private int id;
    private String name;
    private String manufacturer;
    private int maxX;
    private int maxY;
    private int maxZ;
    private Spool currentSpool;

    public Printer(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getId() {
        return id;
    }

    public abstract int CalculatePrintTime(Print print);

    public abstract Spool[] getCurrentSpools();

    public abstract void setCurrentSpools(ArrayList<Spool> spools);

    public abstract boolean printFits(Print print);

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setCurrentSpool(Spool spool) {
        this.currentSpool = spool;
    }

    public Spool getCurrentSpool() {
        return currentSpool;
    }

    @Override
    public String toString() {
        return "--------" + System.lineSeparator() +
                "- ID: " + id + System.lineSeparator() +
                "- Name: " + name + System.lineSeparator() +
                "- Manufacturer: " + manufacturer + System.lineSeparator() +
                "- maxX: " + maxX + System.lineSeparator() +
                "- maxY: " + maxY + System.lineSeparator() +
                "- maxZ: " + maxZ + System.lineSeparator() +
                "- Current Spool: " + (currentSpool != null ? currentSpool.getId() : "None") + System.lineSeparator() +
                "--------";
    }
}
