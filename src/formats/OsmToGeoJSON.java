package formats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.layout.AnchorPane;

import org.json.JSONObject;
import org.json.XML;

public class OsmToGeoJSON {

    public void osmToGeoJson(AnchorPane rootPane) {
        try{
            //подсчёт зданий
            String text = "";
            String line;
            BufferedReader reader = new BufferedReader( new FileReader("g:\\Diplom\\map.osm"));
            while((line = reader.readLine()) != null) text += line;
            reader.close();
            JSONObject json = XML.toJSONObject(text).getJSONObject("osm");
        }
        catch (IOException e){}
    }
}