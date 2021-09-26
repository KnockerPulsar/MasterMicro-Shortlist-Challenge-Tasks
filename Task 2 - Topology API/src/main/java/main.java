import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try {
            File helper = new File("src/main");

            FileReader r = new FileReader(helper.getAbsolutePath().concat(("/resources/topology.json")));
            JsonReader reader = new JsonReader(r);
            reader.setLenient(true);

            Object json = gson.fromJson(reader, Object.class);
            System.out.println("KAK");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
