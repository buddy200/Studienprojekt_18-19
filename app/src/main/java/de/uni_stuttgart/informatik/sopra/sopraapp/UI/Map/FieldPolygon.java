package de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Polygon;

import de.uni_stuttgart.informatik.sopra.sopraapp.MainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 19.11.17.
 * Mail: felix.burk@gmail.com
 *
 * A custom Polygon overlay for the MapView
 */

public class FieldPolygon extends Polygon {

    private Context context;
    private Paint textPaint;
    private Field field;
    private BitmapShader bitmapShader;

    public FieldPolygon(Context context, Field field){
        super(context);

        this.field = field;
        this.context = context;

        //init default values
        this.setTitle("");

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);

    }


    Point polyCentroidPoint;

    /**
     * overwriting the normal draw method, to display the name
     * in the center of gravity of the polygon
     * (this is the quickest way to get the title in a good position relative to the polygon
     * better would be a center point inside of it, but this might be a bit more difficult)
     *
     * TODO: instead of a color use tile overlays from .png files
     * @param canvas
     * @param mapView
     * @param shadow
     */
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow){
        //only draw names if zoomed in to certain level
        //TODO: show name depending to polygon size and zoom level
        if(field instanceof AgrarianField){
            if(mapView.getZoomLevel() < 18){
                super.draw(canvas, mapView, shadow);
                return;
            }
            textPaint.setTextSize(50);
            textPaint.setColor(Color.BLACK);
            this.setStrokeColor(Color.argb(0,0,0,0));

            //handle damage fields
        }else if(field instanceof DamageField){
            if(mapView.getZoomLevel() < 19){
                super.draw(canvas, mapView, shadow);
                return;
            }
            textPaint.setTextSize(40);
            textPaint.setColor(Color.RED);
            this.setStrokeColor(Color.argb(255,0,0,0));
            this.setStrokeWidth(1.0f);

        }

        polyCentroidPoint = new Point();
        mapView.getProjection().toPixels(field.getCentroid(), polyCentroidPoint);

        canvas.drawText(this.getTitle(), polyCentroidPoint.x, polyCentroidPoint.y, textPaint);

        super.draw(canvas, mapView, shadow);
    }



}
