package formats;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.shape.Polygon;
import org.json.JSONArray;
import org.json.JSONObject;
import resource.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import sample.*;

public class GeoJSON {
    private ProjectionMercator mecrator = new ProjectionMercator();

    private boolean minMaxCoordCheck = false;
    private double minCoordX, minCoordY, maxCoordX, maxCoordY ;

    private Controller controller;
    public GeoJSON(Controller controller){
        this.controller = controller;
    }


    public void loadFile() {
        String str = "", line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Resource.link));
            while((line = reader.readLine()) != null) str += line;
            reader.close();
        }catch (IOException e){}
        JSONObject geoJSONobj = new JSONObject(str);
        JSONArray buildingsLonLat = buildingFilter(geoJSONobj);
        JSONArray buildingsProection = buildingToProection(buildingsLonLat);
        Platform.runLater(() -> {
            createPolygon(buildingsProection);
            controller.textBottomBar.setText("Open: " + Resource.link.toString());
        });
    }

    private JSONArray buildingFilter(JSONObject geoJSONobj){
        JSONArray building = new JSONArray();
        JSONArray featuresArr = (JSONArray) geoJSONobj.get("features");
        for(int featuresArrN = 0; featuresArrN < featuresArr.length(); featuresArrN++) {
            JSONObject featuresObj = (JSONObject)featuresArr.get(featuresArrN);
            JSONObject propertiesObj = (JSONObject)featuresObj.get("properties");
            JSONObject tagsObj = (JSONObject)propertiesObj.get("tags");
            if(!tagsObj.isNull("building")) building.put(featuresObj);
        }
        return building;
    }

    private JSONArray buildingToProection(JSONArray buildingArr){
        for(int buildingArrN = 0; buildingArrN < buildingArr.length(); buildingArrN++) {
            JSONObject buildingObj = (JSONObject)buildingArr.get(buildingArrN);
            JSONObject geometryObj = (JSONObject)buildingObj.get("geometry");
            JSONArray coordinatesArr = (JSONArray)geometryObj.get("coordinates");

            JSONArray proectionCoordinates = new JSONArray();
            for(int coordinatesArrN = 0; coordinatesArrN < coordinatesArr.length(); coordinatesArrN++){
                JSONArray inOut = new JSONArray();
                JSONArray coordinates = (JSONArray)coordinatesArr.get(coordinatesArrN);
                if(coordinatesArrN == 0) {
                    for (int coordinatesN = 0; coordinatesN < coordinates.length(); coordinatesN++) {
                        JSONArray proectionCoordinate = new JSONArray();
                        JSONArray coordinate = (JSONArray) coordinates.get(coordinatesN);
                        JSONObject XY = new JSONObject(mecrator.convert(Double.parseDouble(coordinate.get(0).toString()), Double.parseDouble(coordinate.get(1).toString())));
                        double x = Double.parseDouble(XY.get("x").toString());
                        double y = Double.parseDouble(XY.get("y").toString());
                        System.out.println(x + " " + y);
                        proectionCoordinate.put(x);
                        proectionCoordinate.put(y);
                        inOut.put(proectionCoordinate);
                        if(!minMaxCoordCheck){
                            minCoordX = x;
                            minCoordY = y;
                            maxCoordX = x;
                            maxCoordY = y;
                            minMaxCoordCheck = true;
                        }else {
                            if (x < minCoordX) minCoordX = x;
                            else if (x > maxCoordX) maxCoordX = x;
                            if (y < minCoordY) minCoordY = y;
                            else if (y > maxCoordY) maxCoordY = y;
                        }
                    }
                    proectionCoordinates.put(inOut);
                }
            }
            geometryObj.remove("coordinates");
            geometryObj.put("coordinates",proectionCoordinates);
        }
        return buildingArr;
    }

    private void createPolygon(JSONArray buildingArr){
        for(int buildingArrN = 0; buildingArrN < buildingArr.length(); buildingArrN++) {
            JSONObject buildingObj = (JSONObject)buildingArr.get(buildingArrN);
            JSONObject geometryObj = (JSONObject)buildingObj.get("geometry");
            JSONArray coordinatesArr = (JSONArray)geometryObj.get("coordinates");

            CreatePoligon createPol = new CreatePoligon(controller);
            Polygon polygon = new Polygon();
            for(int coordinatesArrN = 0; coordinatesArrN < coordinatesArr.length(); coordinatesArrN++){
                JSONArray coordinates = (JSONArray)coordinatesArr.get(coordinatesArrN);
                if(coordinatesArrN == 0) {
                    for (int coordinatesN = 0; coordinatesN < coordinates.length(); coordinatesN++) {
                        JSONArray coordinate = (JSONArray) coordinates.get(coordinatesN);
                        double x = Double.parseDouble(coordinate.get(0).toString());
                        double y = Double.parseDouble(coordinate.get(1).toString());
                        createPol.setPoint(polygon, x - maxCoordX, y - minCoordY);
                    }
                }
            }
            createPol.setPolygon(polygon);
        }
    }
}
