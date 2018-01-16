package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BaseView;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.PictureData;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;

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

        void deltePhotFromDatabase(PictureData pd);

    }
}
