package pandemic;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

class JSONLoadSave {
    double INFECTED_CREATION_PROBABILITY=0.008;
    double HEALTHY_CREATION_PROBABILITY=0.08;

    public static void main(String... args) {
        JSONLoadSave jls = new JSONLoadSave();
        // writes into a file in the current directory
        String filename = System.getProperty("user.dir") + "/data.json";
        System.out.println("Saving to " + filename);
        jls.writeJSON(jls.JSONifyAttributes(), filename);
        JSONObject jobj = jls.readJSON(filename);
        jobj.forEach((k, v) -> System.out.println(k + ": " + v));
        // all this casting is ugly, but it is what it is
        jls.HEALTHY_CREATION_PROBABILITY = (double) jobj.get("HEALTHY_CREATION_PROBABILITY");
        jls.INFECTED_CREATION_PROBABILITY = (double) jobj.get("INFECTED_CREATION_PROBABILITY");
    }

    private JSONObject JSONifyAttributes() {
        JSONObject jsonObj = new JSONObject(new HashMap<String, Double>());
        jsonObj.put("HEALTHY_CREATION_PROBABILITY", HEALTHY_CREATION_PROBABILITY);
        jsonObj.put("INFECTED_CREATION_PROBABILITY", INFECTED_CREATION_PROBABILITY);
        return jsonObj;
    }

    private void writeJSON(JSONObject jobj, String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(jobj.toJSONString());
            fw.flush();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    private JSONObject readJSON(String filename) {
        JSONParser parser = new JSONParser();
        try (FileReader fr = new FileReader(filename)) {
            return (JSONObject) parser.parse(fr);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
