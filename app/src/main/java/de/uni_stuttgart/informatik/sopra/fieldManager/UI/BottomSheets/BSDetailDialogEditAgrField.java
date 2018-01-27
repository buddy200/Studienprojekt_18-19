package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
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

import de.uni_stuttgart.informatik.sopra.fieldManager.AddFieldActivity;
import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.AgrarianFieldType;

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
    public void onAttach(Context context) {
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
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog_agrarianfield_edit, container, false);
        configureBottomSheetBehaviour(view);

        headingText = view.findViewById(R.id.heading);
        fieldName = view.findViewById(R.id.field_detail_name_edit);
        fieldRegion = view.findViewById(R.id.field_detail_region_edit);
        fieldSpinner = view.findViewById(R.id.field_detail_state_spinner);
        fieldSize = view.findViewById(R.id.field_detail_size);
        fieldPolicyHolder = view.findViewById(R.id.field_detail_policyholder_edit);

        finishButton = view.findViewById(R.id.finish_edit_button_agr);
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
                case R.id.finish_edit_button_agr:
                    mListener.onFragmentMessage(TAG, "done", mPresenter.getVisibleField());
                    mPresenter.changeField(changedField());
                    this.dismiss();
                    break;
                case R.id.delete_button:
                    generateDeleteDialog().show();
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
        headingText.setText(getResources().getString(R.string.agrarian_field));
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
        }
        return mFieldToChange;
    }

    /**
     * generate a delete Dialog
     *
     * @return
     */
    private AlertDialog.Builder generateDeleteDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mPresenter.deleteCurrentField();
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getResources().getString(R.string.dialogmessage_want_delete_Field)).setPositiveButton(getContext().getResources().getString(R.string.word_yes), dialogClickListener)
                .setNegativeButton(getContext().getResources().getString(R.string.word_no), dialogClickListener);

        return builder;
    }
}