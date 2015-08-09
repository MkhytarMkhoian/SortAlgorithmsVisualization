package main;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Created by Mkhitar on 29.03.2015.
 */
public class VisualElement {

    private Rectangle mRectangle;
    private Polygon mPolygon;
    private Double[] mCoordinates;

    public VisualElement() {
    }

    public VisualElement(Rectangle rectangle, Polygon polygon, Double[] coordinates) {
        this.mRectangle = rectangle;
        this.mPolygon = polygon;
        mCoordinates = coordinates;
    }

    public void refreshCoordinates(){
        mPolygon.getPoints().clear();
        mPolygon.getPoints().addAll(mCoordinates);
    }

    public Rectangle getRectangle() {
        return mRectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.mRectangle = rectangle;
    }

    public Polygon getPolygon() {
        return mPolygon;
    }

    public void setPolygon(Polygon polygon) {
        this.mPolygon = polygon;
    }
}
