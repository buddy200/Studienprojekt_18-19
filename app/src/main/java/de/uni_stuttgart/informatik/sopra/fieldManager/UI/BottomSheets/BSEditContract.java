package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BaseView;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.PictureData;

/**
 * sopra_priv
 * Created by Felix B on 17.12.17.
 * Mail: felix.burk@gmail.com
 */

interface BSEditContract {

    interface BottomSheet extends BaseView {

        boolean isVisible();

        void setLoadingIndicator(boolean active);

        void fillData(Field f);

    }

    interface Presenter extends BasePresenter {
        void start();

        void populateBS(Field f);

        void deleteCurrentField();

        void changeField(Field f);

        Field getVisibleField();

        void addPhotoToDatabase(PictureData pd);

        void deletePhotoFromDatabase(PictureData pd);

    }
}
