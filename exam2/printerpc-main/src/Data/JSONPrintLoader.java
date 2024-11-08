package Data;

import Models.Print;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONPrintLoader implements PrintLoader{
    @Override
    public List<Print> getPrintList() {
        List<Print> prints = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Print>> ref = new TypeReference<List<Print>>() {};
        try {
            prints = mapper.readValue(new File("resources/prints.json"), ref);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prints;
    }
}
