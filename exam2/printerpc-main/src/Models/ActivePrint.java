package Models;

import java.util.UUID;

public class ActivePrint {
    private final UUID id;
    private final String printName;
    private int timeRemaining;

    public ActivePrint(String printName, int timeRemaining) {
        this.id = UUID.randomUUID();
        this.printName = printName;
        this.timeRemaining = timeRemaining;
    }

    public void reduceTimer() {
        if(timeRemaining > 0)
            timeRemaining--;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public String getID() {
        return id.toString();
    }

    public String getPrintName() {
        return printName;
    }

    public String toString() {
        return getID() + " - " + getPrintName();
    }

    public String toStringWithTime() {
        return getID() + " - " + getPrintName() + " - " + timeRemaining;
    }
}
