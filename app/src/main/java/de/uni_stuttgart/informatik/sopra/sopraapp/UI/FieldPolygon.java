package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Polygon;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 19.11.17.
 * Mail: felix.burk@gmail.com
 */

public class FieldPolygon extends Polygon {

    Context context;
    Paint textPaint;
    Field field;

    public FieldPolygon(Context context, Field field){
        super(context);

        this.field = field;
        this.context = context;

        //init default values
        this.setFillColor(ContextCompat.getColor(context, R.color.stateDefault));
        this.setTitle("");
        //invisible borders
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
    Point polyCentroidPoint;
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow){
        //only draw names if zoomed in to certain level
        //TODO: show name depending to polygon size and zoom level
        if(mapView.getZoomLevel() < 16){
            super.draw(canvas, mapView, shadow);
            return;
        }

        polyCentroidPoint = new Point();
        mapView.getProjection().toPixels(field.getCentroid(), polyCentroidPoint);

        canvas.drawText(this.getTitle(), polyCentroidPoint.x, polyCentroidPoint.y, textPaint);

        super.draw(canvas, mapView, shadow);
    }


    @Override public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView){
        boolean tapped = contains(event);
        //only show detail if map is zoomed in enough
        if (tapped && mapView.getZoomLevel() > 13){
            Projection pj = mapView.getProjection();
            GeoPoint position = (GeoPoint)pj.fromPixels((int)event.getX(), (int)event.getY());

            try {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                BottomSheetDetailDialogFragment.newInstance(field).show(fragmentManager, "test");
            } catch (ClassCastException e) {
                Log.e("FieldPolygon", "Can't get fragment manager");
            }
        }
        return tapped;
    }


}
