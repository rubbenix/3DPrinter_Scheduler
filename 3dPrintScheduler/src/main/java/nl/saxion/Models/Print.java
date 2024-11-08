package nl.saxion.Models;

import nl.saxion.interfaces.PrinterAdapter;

import java.util.ArrayList;

public class Print implements PrinterAdapter {
    private String name;
    private int height;
    private int width;
    private int length;
    private ArrayList<Double> filamentLength;
    private int printTime;

    public Print(String name, int height, int width, int length, ArrayList<Double> filamentLength, int printTime) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;
        this.filamentLength = filamentLength;
        this.printTime = printTime;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Double> getFilamentLength() {
        return filamentLength;
    }

    public boolean printFits(Printer printer) {
        return getHeight() <= printer.getMaxZ() && getWidth() <= printer.getMaxX() && getLength() <= printer.getMaxY();
    }

    @Override
    public String toString() {
        return "--------" + System.lineSeparator() +
                "- Name: " + name + System.lineSeparator() +
                "- Height: " + height + System.lineSeparator() +
                "- Width: " + width + System.lineSeparator() +
                "- Length: " + length + System.lineSeparator() +
                "- FilamentLength: " + filamentLength + System.lineSeparator() +
                "- Print Time: " + printTime + System.lineSeparator() +
                "--------";
    }

    @Override
    public void printTask(PrintTask task) {
        PrinterAdapter.print(task);

    }
}
