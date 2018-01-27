package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.AddFieldActivity;
import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.ProgressStatus;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.PictureData;

/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * A custom BSDetailDialogDmgField to edit Fields
 */

public class
BSDetailDialogEditDmgField extends BottomSheetDialogFragment implements BSEditContract.BottomSheet, View.OnClickListener {

    private static final String TAG = "BSDetailDialogEditFragmentDamageField";

    private BSEditContract.Presenter mPresenter;

    private TextView headingText;
    private TextView dateText;
    private TextView fieldestimatedCosts;
    private EditText fieldName;
    private Spinner fieldSpinner;
    private Spinner progressSpinner;
    private TextView fieldSize;
    private EditText fieldPolicyHolder;
    private Button pickDate;
    private ImageButton finishButton;
    private ImageButton deleteButton;
    private ImageButton addPhotoButton;
    private RecyclerView recyclerView;
    private FragmentInteractionListener mListener;
    private GalleryAdapter galleryAdapter;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BSDetailDialogEditDmgField newInstance() {
        final BSDetailDialogEditDmgField fragment = new BSDetailDialogEditDmgField();

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
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog_damagefield_edit, container, false);
        configureBottomSheetBehaviour(view);

        headingText = view.findViewById(R.id.heading);
        dateText = view.findViewById(R.id.text_date);
        fieldName = view.findViewById(R.id.field_detail_name_edit);
        fieldSpinner = view.findViewById(R.id.field_detail_state_spinner);
        progressSpinner = view.findViewById(R.id.progress_state_spinner);
        fieldSize = view.findViewById(R.id.field_detail_size);
        fieldPolicyHolder = view.findViewById(R.id.field_detail_policyholder_edit);
        fieldestimatedCosts = view.findViewById(R.id.field_cost);

        pickDate = view.findViewById(R.id.button_pick_date);
        recyclerView = view.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        finishButton = view.findViewById(R.id.finish_edit_button_agr);
        deleteButton = view.findViewById(R.id.delete_button);
        addPhotoButton = view.findViewById(R.id.add_Photo_Button);

        pickDate.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        addPhotoButton.setOnClickListener(this);

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
                    mPresenter.changeField(changedField());
                    mListener.onFragmentMessage(TAG, "done", mPresenter.getVisibleField());
                    this.dismiss();
                    break;
                case R.id.delete_button:
                    generateDeleteDialog().show();
                    this.dismiss();
                    break;
                case R.id.add_Photo_Button:
                    mListener.onFragmentMessage(TAG, "addPhoto", mPresenter.getVisibleField());
                    mPresenter.changeField(changedField());
                    mPresenter.changeField(mPresenter.getVisibleField());
                    this.dismiss();
                    break;
                case R.id.button_pick_date:
                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int selectedYear,
                                              int selectedMonth, int selectedDay) {
                            dateText.setText(selectedDay + " - " + (selectedMonth + 1) + " - "
                                    + selectedYear);
                        }
                    };
                    DatePickerDialog datePicker = new DatePickerDialog(getContext(), listener, Calendar.getInstance().get(Calendar.YEAR) , Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    datePicker.show();
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
        DamageField field = (DamageField) f;
        if (this.getActivity() instanceof AddFieldActivity) {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        headingText.setText(getResources().getString(R.string.damage_field));
        fieldestimatedCosts.setText(String.valueOf(((DamageField) field).getInsuranceMoney()));
        dateText.setText(field.getParsedDate());
        List<DamageFieldType> statusCheck;
        statusCheck = Arrays.asList(DamageFieldType.values());
        fieldSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, DamageFieldType.getAllString(getContext())));
        fieldSpinner.setSelection(statusCheck.indexOf(field.getType()));
        progressSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ProgressStatus.getAllString(getContext())));
        progressSpinner.setSelection(statusCheck.indexOf(field.getProgressStatus()));
        if(field.getEvaluator().equals("")) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            fieldPolicyHolder.setText(prefs.getString(getContext().getString(R.string.pref_username), ""));
        }
        else{
            fieldPolicyHolder.setText(field.getEvaluator());
        }
        fieldName.setText(field.getName());
        fieldSize.setText(field.getConvertedSize());
        if ((field).getPaths() != null) {
            galleryAdapter = new GalleryAdapter(getContext(), field.getPaths(), this);
            recyclerView.setAdapter(galleryAdapter);
        }

    }

    /**
     * add the changed data to a new Field
     *
     * @return
     */
    public Field changedField() {
        DamageField mFieldToChange = (DamageField) mPresenter.getVisibleField();
        mFieldToChange.setEvaluator(fieldPolicyHolder.getText().toString());
        mFieldToChange.setDate(dateText.getText().toString());
        mFieldToChange.setName(fieldName.getText().toString());
        mFieldToChange.setType(DamageFieldType.fromString((String)  fieldSpinner.getSelectedItem(), getContext()));
        mFieldToChange.setProgressStatus(ProgressStatus.fromString((String)progressSpinner.getSelectedItem(), getContext()));
        return mFieldToChange;
    }

    /**
     * remove picture from the clicked position and refresh the gallery
     *
     * @param position
     */
    public void removePicture(int position) {
        DamageField damageField = (DamageField) mPresenter.getVisibleField();
        //remove the image data from the damage field and refresh the recycler view
        PictureData pd = damageField.getPaths().get(position);
        mPresenter.deletePhotoFromDatabase(pd);
        //remove the image data from the damage field and refresh the recycler view
        ((DamageField) mPresenter.getVisibleField()).deletePhoto(position);
        mPresenter.changeField(mPresenter.getVisibleField());
        galleryAdapter.notifyItemRemoved(position);
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
        builder.setMessage(getContext().getResources().getString(R.string.dialogmessage_want_delete_damage)).setPositiveButton(getContext().getResources().getString(R.string.word_yes), dialogClickListener)
                .setNegativeButton(getContext().getResources().getString(R.string.word_no), dialogClickListener);

        return builder;
    }
}
