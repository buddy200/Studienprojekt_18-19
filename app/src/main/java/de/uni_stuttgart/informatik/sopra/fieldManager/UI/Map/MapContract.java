package de.uni_stuttgart.informatik.sopra.fieldManager.UI.Map;

import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BaseView;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 19.12.17.
 * Mail: felix.burk@gmail.com
 */

public interface MapContract {

    interface MapFragment extends BaseView {

    }

    interface MapHandler extends BasePresenter {
        Polygon fieldToPolygon(Field mField);
        void addFields(List<Field> fields);
        void addField(Field field);
        void setCurrLocMarker(double lat, double lon);
        void invalidateMap();
        void reloadWithData(ArrayList<Field> fields);
        void animateAndZoomTo(double lat, double lon);
        void addPolyline(Polyline p);
    }
}
