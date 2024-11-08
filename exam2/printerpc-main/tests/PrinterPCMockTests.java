import Data.Loader;
import Models.Print;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PrinterPCMockTests {
    PrinterPCMock printerPC = new PrinterPCMock();

    @BeforeEach
    public void startClient() {
        // Reset the printerPC before each test.
        Loader.setPrintLoader(() -> {
            List<Print> prints = new ArrayList<>();
            prints.add(new Print("Stegosaurus Pickholder", 30, 1));
            prints.add(new Print("Earth Globe", 30, 2));
            return prints;
        });
        printerPC = new PrinterPCMock();
    }

    @Test
    public void correctPrint_addPrintToQueue_UUID() {
        String id = printerPC.addPrintToQueue("Stegosaurus Pickholder" , "PLA", new String[]{"blue"});
        assertDoesNotThrow(() -> {
            UUID.fromString(id);
        });
    }

    @Test
    public void correctPrint_addPrintToQueue_PrintInActiveQueue() {
        String id = printerPC.addPrintToQueue("Stegosaurus Pickholder" , "PLA", new String[]{"blue"});
        assertEquals(id + " - " + "Stegosaurus Pickholder" + " - " + "30", printerPC.getActivePrints()[0]);
    }

    @Test
    public void correctPrint_WaitForCompletion_PrintCompleted() {
        String printID = printerPC.addPrintToQueue("Stegosaurus Pickholder" , "PLA", new String[]{"blue"});
        for(int i=0; i<=30;i++) {
            printerPC.tick();
        }
        String[] response = printerPC.getCompletedPrints();
        assertEquals(printID + " - Stegosaurus Pickholder", response[0]);
    }

    @Test
    public void correctPrint_ClearCompletedPrinters_CompletedListEmpty() {
        String printID = printerPC.addPrintToQueue("Stegosaurus Pickholder" , "PLA", new String[]{"blue"});
        for(int i=0; i<=30;i++) {
            printerPC.tick();
        }
        printerPC.clearCompletedPrints();
        assertEquals(0, printerPC.getCompletedPrints().length);
    }

    @Test
    public void incorrectMaterial_addPrintToQueue_ErrorWrongMaterial() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            printerPC.addPrintToQueue("Stegosaurus Pickholder" , "rubber", new String[]{"blue"});
        });
        assertEquals("Unknown material",exception.getMessage());
    }

    @Test
    public void incorrectColorCount_addPrintToQueue_ErrorIcorrectNrColors() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            printerPC.addPrintToQueue("Earth Globe", "PLA", new String[]{"blue"});
        });
        assertEquals("Wrong number of colors supplied", exception.getMessage());
    }

    @Test
    public void incorrectPrintName_addPrintToQueue_ErrorUnknownPrint() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            printerPC.addPrintToQueue("Guitar", "PLA", new String[]{"blue"});
        });
        assertEquals("Print not found", exception.getMessage());
    }

    @Test
    public void tooManyPrints_addPrintToQueue_ErrorNoCapacity() {
        //Fill up the queue
        for(int i =0; i<=25; i++)
            printerPC.addPrintToQueue("Stegosaurus Pickholder" , "PLA", new String[]{"blue"});
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            String response = printerPC.addPrintToQueue("Stegosaurus Pickholder", "PLA", new String[]{"blue"});
        });
        assertEquals("There are no available printers", exception.getMessage());
    }

}


