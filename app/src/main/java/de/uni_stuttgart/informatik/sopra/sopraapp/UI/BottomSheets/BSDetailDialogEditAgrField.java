package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.AddFieldActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;

/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * A custom BSDetailDialogDmgField to edit Fields
 */

public class
BSDetailDialogEditAgrField extends BottomSheetDialogFragment implements BSEditContract.BottomSheet, View.OnClickListener {

    private static final String TAG = "BSDetailDialogEditFragmentAgrarianField";

    private BSEditContract.Presenter mPresenter;

    private TextView headingText;
    private EditText fieldName;
    private EditText fieldRegion;
    private Spinner fieldSpinner;
    private TextView fieldSize;
    private EditText fieldPolicyHolder;
    private ImageButton finishButton;
    private ImageButton deleteButton;
    private FragmentInteractionListener mListener;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BSDetailDialogEditAgrField newInstance() {
        final BSDetailDialogEditAgrField fragment = new BSDetailDialogEditAgrField();

        return fragment;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog_agrarianfieldedit, container, false);
        configureBottomSheetBehaviour(view);

        headingText = view.findViewById(R.id.heading);
        fieldName = view.findViewById(R.id.field_detail_name_edit);
        fieldRegion = view.findViewById(R.id.field_detail_region_edit);
        fieldSpinner = view.findViewById(R.id.field_detail_state_spinner);
        fieldSize = view.findViewById(R.id.field_detail_size);
        fieldPolicyHolder = view.findViewById(R.id.field_detail_policyholder_edit);

        finishButton = view.findViewById(R.id.edit_finish_button);
        deleteButton = view.findViewById(R.id.delete_button);
        finishButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        return view;
    }

    private void configureBottomSheetBehaviour(View view) {
        //Not implemented yet - TODO
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    /**
     * handle clicks on buttons
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (this.mPresenter != null) {
            switch (v.getId()) {
                case R.id.edit_finish_button:
                    mListener.onFragmentMessage(TAG, "done", null);
                    mPresenter.changeField(changedField());
                    this.dismiss();
                    break;
                case R.id.delete_button:
                    mPresenter.deleteCurrentField();
                    this.dismiss();
                    break;
            }
        }
    }

    @Override
    public void setPresenter(BasePresenter p) {
        mPresenter = (BSEditContract.Presenter) p;
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        //Not implemented yet TODO
    }

    @Override
    public void fillData(Field f) {
        AgrarianField field = (AgrarianField) f;
        if (this.getActivity() instanceof AddFieldActivity) {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        headingText.setText("AgrarFeld");
        fieldRegion.setText(field.getCounty());
        List<AgrarianFieldType> statusCheck;
        statusCheck = Arrays.asList(AgrarianFieldType.values());
        fieldSpinner.setAdapter(new ArrayAdapter<AgrarianFieldType>(getContext(), android.R.layout.simple_spinner_item, AgrarianFieldType.values()));
        fieldSpinner.setSelection(statusCheck.indexOf(field.getType()));
        fieldPolicyHolder.setText(field.getOwner());
        fieldName.setText(field.getName());
        fieldSize.setText(field.getConvertedSize());
    }

    /**
     * add the changed data to a new Field
     *
     * @return
     */
    public Field changedField() {
        AgrarianField mFieldToChange = (AgrarianField) mPresenter.getVisibleField();
        mFieldToChange.setOwner(fieldPolicyHolder.getText().toString());
        mFieldToChange.setLinesFormField(((AgrarianField) mPresenter.getVisibleField()).getLinesFormField());
        mFieldToChange.setName(fieldName.getText().toString());
        mFieldToChange.setType((AgrarianFieldType) fieldSpinner.getSelectedItem());
        if (!fieldRegion.getText().toString().equals(getResources().getString(R.string.county_default_name))) {
            mFieldToChange.setCounty(fieldRegion.getText().toString());
        } else {
            //    mFieldToChange.setAutomaticCounty();
        }
        return mFieldToChange;
    }

}
