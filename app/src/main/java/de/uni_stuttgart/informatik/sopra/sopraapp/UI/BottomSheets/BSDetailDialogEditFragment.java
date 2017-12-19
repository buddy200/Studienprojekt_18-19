package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.AddFieldActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.FieldType;

/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 *
 * A custom BottomSheetDetailDialogFragment to edit Fields
 */

public class BSDetailDialogEditFragment extends BottomSheetDialogFragment implements BSEditContract.BottomSheet, View.OnClickListener{

    private static final String TAG = "BSDetailDialogEditFragment";

    private BSEditContract.Presenter mPresenter;
    private Field mFieldToChange;

    private TextView headingText;
    private EditText fieldName;
    private ViewFlipper viewFlipper;
    private EditText fieldRegion;
    private DatePicker datePicker;
    private Spinner fieldSpinner;
    private TextView fieldSize;
    private EditText fieldPolicyHolder;
    private Button finishButton;
    private Button deleteButton;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BSDetailDialogEditFragment newInstance(Field field) {
        final BSDetailDialogEditFragment fragment = new BSDetailDialogEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("mField", field);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog_edit, container, false);
        configureBottomSheetBehaviour(view);

        headingText = view.findViewById(R.id.heading);
        fieldName = view.findViewById(R.id.field_detail_name_edit);
        fieldRegion = view.findViewById(R.id.field_detail_region_edit);
        datePicker = view.findViewById(R.id.field_detail_date_picker);
        fieldSpinner = view.findViewById(R.id.field_detail_state_spinner);
        fieldSize = view.findViewById(R.id.field_detail_size);
        fieldPolicyHolder = view.findViewById(R.id.field_detail_policyholder_edit);

        finishButton = view.findViewById(R.id.edit_finish_button);
        deleteButton = view.findViewById(R.id.delete_button);

        finishButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        finishButton.setText("Finish");
        deleteButton.setText("Delete");

        return view;
    }

    private void configureBottomSheetBehaviour(View view) {
        //Not implemented yet - TODO
    }


    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
        mFieldToChange = (Field) getArguments().getSerializable("mField");
    }

    /**
     * handle clicks on buttons
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(this.mPresenter != null) {
            switch (v.getId()) {
                case R.id.edit_finish_button:
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
    public void setPresenter(BSEditContract.Presenter p) {
        mPresenter = p;
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        //Not implemented yet TODO
    }

    @Override
    public void fillData(Field f) {
        if(this.getActivity() instanceof AddFieldActivity){
            deleteButton.setVisibility(View.INVISIBLE);
        }

        if(f instanceof AgrarianField){
            headingText.setText("AgrarFeld");
            datePicker.setVisibility(View.INVISIBLE);
            datePicker.removeAllViews();
            fieldRegion.setText(f.getCounty());

            List<AgrarianFieldType> statusCheck;
            statusCheck = Arrays.asList(AgrarianFieldType.values());
            fieldSpinner.setAdapter(new ArrayAdapter<AgrarianFieldType>(getContext(), android.R.layout.simple_spinner_item, AgrarianFieldType.values()));
            //TODO not working right now..
            fieldSpinner.setSelection(statusCheck.indexOf(f.getType()));

            fieldPolicyHolder.setText(((AgrarianField)f).getOwner());

        }else if(f instanceof DamageField){
            headingText.setText("DamageFeld");
            fieldRegion.setVisibility(View.INVISIBLE);

            List<DamageFieldType> statusCheck;
            statusCheck = Arrays.asList(DamageFieldType.values());
            fieldSpinner.setAdapter(new ArrayAdapter<DamageFieldType>(getContext(), android.R.layout.simple_spinner_item, DamageFieldType.values()));
            //TODO not working right now..
            fieldSpinner.setSelection(statusCheck.indexOf(f.getType()));

            fieldPolicyHolder.setText(((DamageField)f).getEvaluator());
        }else{
            headingText.setText("Feld");
        }


        fieldName.setText(f.getName());
        fieldSize.setText(String.valueOf(f.getSize()));

    }

    /**
     * add the changed data to a new Field
     * @return
     */
    public Field changedField(){

        mFieldToChange = (Field) getArguments().getSerializable("mField");
        mFieldToChange.setName(fieldName.getText().toString());
        mFieldToChange.setType((FieldType) fieldSpinner.getSelectedItem());

        if(!fieldRegion.getText().toString().equals("")) {
            mFieldToChange.setCounty(fieldRegion.getText().toString());
        }else{
            mFieldToChange.setAutomaticCounty();
        }

        if(mFieldToChange instanceof AgrarianField){
            ((AgrarianField) mFieldToChange).setOwner(fieldPolicyHolder.getText().toString());
        }else{
            ((DamageField) mFieldToChange).setEvaluator(fieldPolicyHolder.getText().toString());
        }

        return mFieldToChange;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }
}
