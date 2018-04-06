package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import resource.Resource;
import formats.*;

public class Controller {
    public Button editorBtn;
    public AnchorPane rootPane;
    @FXML
    public Label textBottomBar;
    public Pane pane;

    public double tempMouseX = 0, tempMouseY = 0;

    private GeoJSON geojson;

    public Controller(){
        this.geojson = new GeoJSON(this);
    }

    public void showEditor(){
    }

    public void dragPane(javafx.scene.input.MouseEvent e){
        pane.setTranslateX(pane.getTranslateX() + (e.getX() - tempMouseX));
        pane.setTranslateY(pane.getTranslateY() + (e.getY() - tempMouseY));
        setTempMouseXY(e.getX(), e.getY());
    }

    public void updateTempMouseXY(javafx.scene.input.MouseEvent e){
        setTempMouseXY(e.getX(), e.getY());
    }

    public void setTempMouseXY(double x, double y){
        tempMouseX = x;
        tempMouseY = y;
    }

    public void scrollPane(ScrollEvent e){
        if((e.getDeltaY() < 0)&&(pane.getScaleX() > 0.5)){
            //zoom map -
            pane.setScaleX(pane.getScaleX() - 0.1);
            pane.setScaleY(pane.getScaleY() - 0.1);
            //zoom move map
            pane.setTranslateX(pane.getTranslateX() + (e.getX() - Resource.stage.getWidth() / 2) / 10);
            pane.setTranslateY(pane.getTranslateY() + (e.getY() - Resource.stage.getHeight() / 2) / 10);
        }else if(((e.getDeltaY() > 0)&&(pane.getScaleX() < 5))){
            //zoom map +
            pane.setScaleX(pane.getScaleX() + 0.1);
            pane.setScaleY(pane.getScaleY() + 0.1);
            //zoom move map
            pane.setTranslateX(pane.getTranslateX() - (e.getX() - Resource.stage.getWidth() / 2) / 10);
            pane.setTranslateY(pane.getTranslateY() - (e.getY() - Resource.stage.getHeight() / 2) / 10);
        }

    }

    public void open(){
        FileChooser file = new FileChooser();
        file.setTitle("Open File");
        file.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All file", "*.geojson",  "*.osm"),
            new FileChooser.ExtensionFilter("GeoJSON (*.geojson)", "*.geojson"),
            new FileChooser.ExtensionFilter("OSM (*.osm)", "*.osm")
        );
        Resource.link = file.showOpenDialog(Resource.stage);
        if (Resource.link != null){
            textBottomBar.setText("Loading...");
            String[] fileFormat = Resource.link.toString().split("\\.");
            switch (fileFormat[fileFormat.length - 1].toLowerCase()){
                case "geojson":{

                    new Thread(() -> geojson.loadFile()).start();
                    break;
                }
                case "osm":{

                    break;
                }
                case "shp":{

                    break;
                }
            }
        }
    }

    public void exit(){
        System.exit(0);
    }
}