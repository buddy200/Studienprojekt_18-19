package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldStates;

/**
 * sopra_priv
 * Created by Felix B on 19.11.17.
 * Mail: felix.burk@gmail.com
 */

public class FieldPolygon extends Polygon {

    Context context;
    Paint textPaint;

    public FieldPolygon(Context context){
        super(context);

        this.context = context;

        //init default values
        this.setFillColor(ContextCompat.getColor(context, R.color.stateDefault));
        this.setTitle("Field");
        this.setStrokeColor(Color.argb(0,0,0,0));

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * overwriting the normal draw method, to display the name
     * in the center of gravity of the polygon
     * (this is the quickest way to get the title in a good position relative to the polygon
     * better would be a center point inside of it, but this might be a bit more difficult)
     */
    double lowX0, lowY0, highX1, highY1;
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow){
        //only draw names if zoomed in to certain level
        //TODO: show name depending to polygon size and zoom level
        if(mapView.getZoomLevel() < 16){
            super.draw(canvas, mapView, shadow);
            return;
        }

        //calculate centroid ( = center of gravity) of polygon
        lowX0 = lowY0 = Double.MAX_VALUE;
        highX1 = highY1 = Double.MIN_VALUE;
        for(GeoPoint point : this.getPoints()){
            if(lowX0 > point.getLatitude()){
                lowX0 = point.getLatitude();
            }
            if(lowY0 > point.getLongitude()){
                lowY0 = point.getLongitude();
            }
            if(highX1 < point.getLatitude()){
                highX1 = point.getLatitude();
            }
            if(highY1 < point.getLongitude()){
                highY1 = point.getLongitude();
            }
        }
        double centerX = lowX0 + ((highX1 - lowX0) / 2);
        double centerY = lowY0 + ((highY1 - lowY0) / 2);

        Point polyCentroidPoint = new Point();
        mapView.getProjection().toPixels(new GeoPoint(centerX, centerY), polyCentroidPoint);

        canvas.drawText(this.getTitle(), polyCentroidPoint.x, polyCentroidPoint.y, textPaint);

        super.draw(canvas,mapView, shadow);
    }

    /**
     * map field state to color
     * @param field
     * @return
     */
    private int stateToPolygonColor(FieldStates field) {
        switch (field){
            case NoDamage:
                return ContextCompat.getColor(context, R.color.stateNoDamage);
            case LightDamage:
                return ContextCompat.getColor(context, R.color.stateLightDamage);
            case HighDamage:
                return ContextCompat.getColor(context, R.color.stateHighDamage);
            default:
                return ContextCompat.getColor(context, R.color.stateDefault);
        }
    }

    /**
     * changes the state the fill color depening on the state
     * @param state
     */
    public void setState(FieldStates state){
        this.setFillColor(stateToPolygonColor(state));
    }

}






















