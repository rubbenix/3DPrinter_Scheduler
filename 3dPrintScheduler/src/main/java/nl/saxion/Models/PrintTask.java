package nl.saxion.Models;

import java.util.List;

public class PrintTask {
    private Print print;
    private List<String> colors;
    private FilamentType filamentType;
    private boolean assigned;
    private boolean completed;
    private boolean failed;

    public PrintTask(Print print, List<String> colors, FilamentType filamentType) {
        this.print = print;
        this.colors = colors;
        this.filamentType = filamentType;
        this.assigned = false;
        this.completed = false;
        this.failed = false;
    }

    public List<String> getColors() {
        return colors;
    }

    public FilamentType getFilamentType() {
        return filamentType;
    }

    public Print getPrint() {
        return print;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void assignToPrinter(Printer printer) {
        if (!assigned && print.printFits(printer)) {
            assigned = true;
            System.out.println("Assigned task to printer " + printer.getName() + ": " + this);
        } else {
            System.out.println("Cannot assign task to printer " + printer.getName());
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        if (assigned && !completed) {
            completed = true;
            System.out.println("Print task completed: " + this);
        } else {
            System.out.println("Print task cannot be completed: " + this);
        }
    }

    public boolean isFailed() {
        return failed;
    }

    public void fail() {
        if (assigned && !failed) {
            failed = true;
            System.out.println("Print task failed: " + this);
        } else {
            System.out.println("Print task cannot fail: " + this);
        }
    }

    @Override
    public String toString() {
        return "< " + print.getName() + " " + filamentType + " " + colors.toString() + " >";
    }
}
