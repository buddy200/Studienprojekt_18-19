package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * A custom BottomSheetDialogFragment to display information of Fields
 */

public class BSDetailDialogAgrField extends BottomSheetDialogFragment implements View.OnClickListener, BSEditContract.BottomSheet {

    private static final String TAG = "BottomSheetDetail";

    protected FragmentInteractionListener mListener;
    private BSEditContract.Presenter mPresenter;

    private AgrarianField mField;

    private TextView name;
    private TextView state;
    private TextView size;
    private TextView county;
    private TextView owner;
    private ImageButton addDmg;
    private ImageButton edit;
    private ImageButton navButton;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BSDetailDialogAgrField newInstance() {
        final BSDetailDialogAgrField fragment = new BSDetailDialogAgrField();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog_agrarianfield, container, false);
        configureBottomSheetBehaviour(view);
        return view;
    }

    /**
     * method to configure the behaviour of the bottom sheet
     *
     * @param view
     */
    void configureBottomSheetBehaviour(View view) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        name = view.findViewById(R.id.field_detail_name);
        edit = view.findViewById(R.id.finish_edit_button_agr);
        edit.setOnClickListener(this);
        addDmg = view.findViewById(R.id.add_damageField_button);
        addDmg.setOnClickListener(this);
        navButton = view.findViewById(R.id.button_navigate_google_maps);
        navButton.setOnClickListener(this);

        size = view.findViewById(R.id.field_detail_size);
        state = view.findViewById(R.id.field_detail_state);
        size = view.findViewById(R.id.field_detail_size);
        county = view.findViewById(R.id.field_detail_region);
        owner = view.findViewById(R.id.field_detail_policyholder);

        if (!GlobalConstants.isAdmin) {
            addDmg.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }
    }

    @Override
    public void fillData(Field mField) {
        this.mField = (AgrarianField) mField;
        name.setText(getResources().getString(R.string.dialogItem_Name) + " " + this.mField.getName() + "");
        county.setText(getResources().getString(R.string.dialogItem_Location) + " " + this.mField.getCounty());
        edit.setImageResource(R.drawable.ic_mode_edit_black_24px);
        state.setText(getResources().getString(R.string.dialogItem_Type) + " " + this.mField.getType().toString());
        state.setTextColor(mField.getColor());
        size.setText(getResources().getString(R.string.dialogItem_Size) + " " + this.mField.getConvertedSize());
        owner.setText(getResources().getString(R.string.dialogItem_Owner) + " " + this.mField.getOwner());
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    /**
     * handle button clicks
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.finish_edit_button_agr:
                    mListener.onFragmentMessage(TAG, "startEdit", mPresenter.getVisibleField());
                    this.dismiss();
                    break;
                case R.id.add_damageField_button:
                    GlobalConstants.setLastLocationOnMap(mPresenter.getVisibleField().getCentroid());
                    mListener.onFragmentMessage(TAG, "addDmgField", mPresenter.getVisibleField());
                    dismiss();
                    break;
                case R.id.button_navigate_google_maps:
                    callGoogleMapsIntent();
                    break;
            }
        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (BSEditContract.Presenter) presenter;
    }

    /**
     * call a googlemaps intent with the position of the centroid point from the field object
     */
    private void callGoogleMapsIntent() {
        String geoString = "geo:" + String.valueOf(mField.getCentroid().getLatitude()) + "," + String.valueOf(mField.getCentroid().getLongitude()) + "?q=" + String.valueOf(mField.getCentroid().getLatitude()) + "," + String.valueOf(mField.getCentroid().getLongitude());
        Uri gmmIntentUri = Uri.parse(geoString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
