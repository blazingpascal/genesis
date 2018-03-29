package model.genetics;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;


public class JSONTraits {
    private static String file;
    private static HashMap<String, TraitData> traits;

    public static void loadJSONTraits(String f) {
        try {
            Gson gson = new Gson();
            JsonObject o = new JsonParser().parse(new FileReader(f)).getAsJsonObject();
            JsonArray t = o.getAsJsonArray("traits");

            file = f;
            traits = new HashMap<>();
            for(JsonElement tr : t) {
                if(!tr.isJsonNull()) {
                    JsonObject obj = tr.getAsJsonObject();
                    String name = obj.get("name").getAsString();
                    int split = obj.get("split").getAsInt();
                    String[] values = gson.fromJson(obj.getAsJsonArray("values"), String[].class);
                    traits.put(name, new TraitData(split, values));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, TraitData> getTraits() {
        return traits;
    }

    public static String getName(String s, int i) {
        return traits.get(s).getName(i);
    }

    public static int getRandomValue(String s, Random r) {
        return traits.get(s).random(r);
    }

    public static int getRandomSkewRecessValue(String s, Random r) {
        return traits.get(s).randomSkewRecess(r);
    }
}
