package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
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
 */

public class BSDetailDialogEditFragment extends BottomSheetDetailDialogFragment {

    private static final String TAG = "BSDetailDialogEditFragment";

    public static BottomSheetDialogFragment newInstance(Field field) {
        final BottomSheetDialogFragment fragment = new BSDetailDialogEditFragment();
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
        return view;
    }

    EditText nameEdit;
    EditText countyEdit;
    Spinner type;
    EditText ownerOrEvaluatorEdit;
    @Override
    protected void setupView(View view, Field mField, TextView name, Button editFinish) {
        LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.bottomSheet);
        RelativeLayout topPanel = (RelativeLayout) view.findViewById(R.id.topPanel);

        Button delete = (Button) view.findViewById(R.id.delete_button);
        delete.setText("Delete");
        delete.setOnClickListener(this);

        nameEdit = view.findViewById(R.id.field_detail_name_edit);
        nameEdit.setText(mField.getName());

        countyEdit = view.findViewById(R.id.field_detail_region_edit);
        countyEdit.setText(mField.getCounty());

        type = view.findViewById(R.id.field_detail_state_spinner);
        ownerOrEvaluatorEdit = view.findViewById(R.id.field_detail_policyholder_edit);

        if(mField instanceof AgrarianField){
            name.setText("Edit AgrarianField");

            List<AgrarianFieldType> statusCheck;
            statusCheck = Arrays.asList(AgrarianFieldType.values());
            type.setAdapter(new ArrayAdapter<AgrarianFieldType>(getContext(), android.R.layout.simple_spinner_item, AgrarianFieldType.values()));
            type.setSelection(statusCheck.indexOf(mField.getType()));

            ownerOrEvaluatorEdit.setText(((AgrarianField)mField).getOwner());

        }else if(mField instanceof DamageField){
            name.setText("Edit DamageField");

            List<DamageFieldType> statusCheck;
            statusCheck = Arrays.asList(DamageFieldType.values());
            type.setAdapter(new ArrayAdapter<DamageFieldType>(getContext(), android.R.layout.simple_spinner_item, DamageFieldType.values()));
            type.setSelection(statusCheck.indexOf(mField.getType()));

            ownerOrEvaluatorEdit.setText(((DamageField)mField).getEvaluator());

        }

        nameEdit.setOnClickListener(this);
        countyEdit.setOnClickListener(this);
        ownerOrEvaluatorEdit.setOnClickListener(this);

        editFinish.setText(getContext().getResources().getString(R.string.button_finish_name));

        changeData();
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null) {
            switch (v.getId()) {
                case R.id.edit_finish_button:
                    this.mListener.onFragmentMessage(TAG, "finishEdit", changeData());
                    this.dismiss();
                    break;
                case R.id.delete_button:
                    this.mListener.onFragmentMessage(TAG, "delete", changedField);
                    this.dismiss();
                    break;
            }
        }
    }

    public Field changeData(){

        changedField = (Field) getArguments().getSerializable("mField");
        changedField.setName(nameEdit.getText().toString());
        changedField.setType((FieldType) type.getSelectedItem());

        if(!countyEdit.getText().toString().equals("")) {
            changedField.setCounty(countyEdit.getText().toString());
        }else{
            changedField.setAutomaticCounty();
        }

        if(changedField instanceof AgrarianField){
            ((AgrarianField) changedField).setOwner(ownerOrEvaluatorEdit.getText().toString());
        }else{
            ((DamageField) changedField).setEvaluator(ownerOrEvaluatorEdit.getText().toString());
        }

        return changedField;
    }

}
