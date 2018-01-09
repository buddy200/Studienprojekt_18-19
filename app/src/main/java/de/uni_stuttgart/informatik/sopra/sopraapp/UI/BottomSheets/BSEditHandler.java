package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 15.12.17.
 * Mail: felix.burk@gmail.com
 */

public class BSEditHandler implements BSEditContract.Presenter {

    private static final String TAG = "BSEditHandler";

    @NonNull
    private final AppDataManager mDataManager;

    @NonNull
    private final BSEditContract.BottomSheet mEditFragment;

    private Field mField;
    private AgrarianField parentField;

    /**
     * @param field may be null for a new field
     * @param dataManager
     * @param editFragment
     */
    public BSEditHandler(Field field, @NonNull AppDataManager dataManager, @NonNull BSEditContract.BottomSheet editFragment){
        mDataManager = dataManager;
        mEditFragment = editFragment;
        mField = field;
        Log.e(TAG, String.valueOf(mField.getName()));

        mEditFragment.setPresenter(this);
    }

    public BSEditHandler(Field field, AgrarianField parent, @NonNull AppDataManager dataManager, @NonNull BSEditContract.BottomSheet editFragment){
        mDataManager = dataManager;
        mEditFragment = editFragment;
        mField = field;
        Log.e(TAG, "parent: " + String.valueOf(mField.getName()));
        parentField = parent;
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
        if(mField != null) {
            mDataManager.removeField(mField);
            Log.e(TAG, "IMPORTTANT removing.. " + mField.getName());
        }
    }

    @Override
    public void changeField(Field f) {
        this.deleteCurrentField();
        if(parentField == null){
            mDataManager.addAgrarianField(f);
        }else {
            mDataManager.addDamageField((DamageField) f, parentField);
        }
        mField = f;
    }

    public Field getVisibleField() {
        return mField;
    }
}
