package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BaseView;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 17.12.17.
 * Mail: felix.burk@gmail.com
 */

public interface BSEditContract {

    interface BottomSheet extends BaseView {
        void setPresenter(Presenter p);

        boolean isVisible();

        void setLoadingIndicator(boolean active);

        void fillData(Field f);

    }

    interface Presenter extends BasePresenter {
        void start();

        void populateBS(Field f);

        void deleteCurrentField();

        void changeField(Field f);
    }
}
