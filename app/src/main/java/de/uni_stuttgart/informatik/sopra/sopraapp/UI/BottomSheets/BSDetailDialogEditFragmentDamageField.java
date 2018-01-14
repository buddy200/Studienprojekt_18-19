package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.AddFieldActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.PhotoManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.ProgressStatus;

/**
 * sopra_priv
 * Created by Felix B on 07.12.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * A custom BottomSheetDetailDialogDamageFieldFragment to edit Fields
 */

public class
BSDetailDialogEditFragmentDamageField extends BottomSheetDialogFragment implements BSEditContract.BottomSheet, View.OnClickListener {

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
    public static BSDetailDialogEditFragmentDamageField newInstance() {
        final BSDetailDialogEditFragmentDamageField fragment = new BSDetailDialogEditFragmentDamageField();

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
        recyclerView = (RecyclerView) view.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        finishButton = view.findViewById(R.id.edit_finish_button);
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
    public void onAttach(Context context){
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
                case R.id.edit_finish_button:
                    mPresenter.changeField(changedField());
                    mListener.onFragmentMessage(TAG, "done", null);
                    this.dismiss();
                    break;
                case R.id.delete_button:
                    mPresenter.deleteCurrentField();
                    this.dismiss();
                    break;
                case R.id.add_Photo_Button:
                    mPresenter.changeField(changedField());
                    takePhoto();
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
                    DatePickerDialog datePicker = new DatePickerDialog(getContext(), listener, 2017, 1, 8);
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
        headingText.setText("DamageFeld");
        fieldestimatedCosts.setText(getResources().getString(R.string.detailItem_estimatedpayment) + String.valueOf(((DamageField) field).getInsuranceMoney()));
        dateText.setText(field.getParsedDate());
        List<DamageFieldType> statusCheck;
        statusCheck = Arrays.asList(DamageFieldType.values());
        fieldSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, DamageFieldType.values()));
        fieldSpinner.setSelection(statusCheck.indexOf(field.getType()));
        progressSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ProgressStatus.values()));
        progressSpinner.setSelection(statusCheck.indexOf(field.getProgressStatus()));
        fieldPolicyHolder.setText(field.getEvaluator());
        fieldName.setText(field.getName());
        fieldSize.setText(field.getConvertedSize());
        if ((field).getpaths() != null) {
            galleryAdapter = new GalleryAdapter(getContext(), field.getpaths(), this);
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
        mFieldToChange.setType((DamageFieldType) fieldSpinner.getSelectedItem());
        mFieldToChange.setProgressStatus((ProgressStatus) progressSpinner.getSelectedItem());
        return mFieldToChange;
    }

    /*
     * create a PhotoManager object and save the fielpath from the picture in the damageField
     */
    public void takePhoto() {
        PhotoManager photoManager = new PhotoManager(getActivity());
        if (mPresenter.getVisibleField() instanceof DamageField) {
            String s = photoManager.dispatchTakePictureIntent();
            ((DamageField) mPresenter.getVisibleField()).setpath(s);
        }
    }

    public void removePicture(int position){
        //delete the foto from the internal storage
        File temp = new File(((DamageField) mPresenter.getVisibleField()).getpaths().get(position).getImage_path());
        temp.delete();
        //remove the image data from the damage field and refresh the recycler view
        ((DamageField) mPresenter.getVisibleField()).getpaths().remove(position);
        recyclerView.removeViewAt(position);
        galleryAdapter.notifyItemRemoved(position);
        galleryAdapter.notifyItemRangeChanged(position, ((DamageField) mPresenter.getVisibleField()).getpaths().size());
    }

}
