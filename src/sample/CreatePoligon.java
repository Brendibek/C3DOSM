package sample;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class CreatePoligon {

    private Controller controller;
    public CreatePoligon(Controller controller){
        this.controller = controller;
    }

    /*public void createPoligon(Pane rootPane) {
        /*Polygon polygon = new Polygon();
        polygon = setPoint(polygon, 10, 10);
        polygon = setPoint(polygon, 10, 30);
        polygon = setPoint(polygon, 30, 30);
        polygon = setPoint(polygon, 30, 10);
        setPolygon(polygon, rootPane);
        Polygon polygon2 = new Polygon();
        polygon2 = setPoint(polygon2, -20, 20);
        polygon2 = setPoint(polygon2, 20, 40);
        polygon2 = setPoint(polygon2, 40, 40);
        polygon2 = setPoint(polygon2, 40, 20);
        setPolygon(polygon2, rootPane);
    }*/

    public void  setPolygon(Polygon polygon){
        setDefaultStyle(polygon);
        Polygon tempPolygon = polygon;
        polygon.setOnMouseClicked(e -> test(tempPolygon));
        controller.pane.getChildren().add(polygon);
    }

    public Polygon setPoint(Polygon polygon, double pointX, double pointY){
        polygon.getPoints().add(pointY);
        polygon.getPoints().add(-pointX);
        return polygon;
    }

    public Polygon setDefaultStyle(Polygon polygon){
        polygon.setFill(Color.valueOf("#999999"));
        polygon.setStroke(Color.valueOf("#333333"));
        polygon.setStrokeWidth(2);
        return polygon;
    }

    public Polygon setClickedStyle(Polygon polygon){
        polygon.setStroke(Color.valueOf("#ee6f00"));
        polygon.setStrokeWidth(2);
        return polygon;
    }

    public void test(Polygon polygon){
        if(clickedPolygon == polygon){
            clickedPolygon = null;
            setDefaultStyle(polygon);
        }else{
            if(clickedPolygon != null) setDefaultStyle(clickedPolygon);
            clickedPolygon = polygon;
            setClickedStyle(polygon);
        }
    }

    Polygon clickedPolygon = null;
}
