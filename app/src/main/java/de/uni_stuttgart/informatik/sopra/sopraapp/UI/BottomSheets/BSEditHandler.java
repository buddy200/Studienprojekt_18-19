package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.support.annotation.NonNull;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.AppDataManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 15.12.17.
 * Mail: felix.burk@gmail.com
 */

public class BSEditHandler implements BSEditContract.Presenter {

    @NonNull
    private final AppDataManager mDataManager;

    @NonNull
    private final BSDetailDialogEditFragment mEditFragment;

    private final Field mField;

    /**
     * @param field may be null for a new field
     * @param dataManager
     * @param editFragment
     */
    public BSEditHandler(Field field, @NonNull AppDataManager dataManager, @NonNull BSDetailDialogEditFragment editFragment){
        mDataManager = dataManager;
        mEditFragment = editFragment;
        mField = field;

        mEditFragment.setPresenter(this);
    }

    @Override
    public void start() {
        if(mField != null){
            populateBS(mField);
        }
    }

    @Override
    public void populateBS(Field f) {
        mEditFragment.fillData(f);
    }

    @Override
    public void deleteCurrentField() {
        mDataManager.removeField(mField);
    }

    @Override
    public void changeField(Field f) {
        mDataManager.removeField(mField);
        mDataManager.addField(f);
    }

}
