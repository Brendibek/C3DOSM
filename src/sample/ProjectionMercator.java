package sample;

import org.json.JSONObject;

public class ProjectionMercator {
    public String convert(double lon, double lat) {
        double a = 6378137; //полуось эллипса
        if(lat > 89.5) lat = 89.5;
        else if(lat < -89.5) lat = -89.5;
        double x = a * Math.log(Math.tan(Math.PI / 4 + Math.toRadians(lat)/2));
        double y = a * Math.toRadians(lon);
        JSONObject coord = new JSONObject();
        coord.put("x", x);
        coord.put("y", y);
        //System.out.println(coord);
        return coord.toString();
    }
}