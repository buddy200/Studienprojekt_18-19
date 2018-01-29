package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * A custom BottomSheetDialogFragment to display information of Fields
 */

public class BSDetailDialogDmgField extends BottomSheetDialogFragment implements View.OnClickListener, BSEditContract.BottomSheet {

    private static final String TAG = "BottomSheetDetail";

    private FragmentInteractionListener mListener;
    private BSEditContract.Presenter mPresenter;

    private DamageField mField;

    private TextView name;
    private TextView state;
    private TextView size;
    private TextView ownerOrEvaluator;
    private TextView date;
    private ImageButton edit;
    private ImageButton navButton;
    private RecyclerView recyclerView;
    private TextView estimatedCosts;
    private TextView progressState;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BSDetailDialogDmgField newInstance() {
        final BSDetailDialogDmgField fragment = new BSDetailDialogDmgField();
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
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog_damagefield, container, false);
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
        recyclerView = view.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        name = view.findViewById(R.id.field_detail_name);
        edit = view.findViewById(R.id.finish_edit_button_agr);
        edit.setOnClickListener(this);
        navButton = view.findViewById(R.id.pick_form_gallery);
        navButton.setOnClickListener(this);

        progressState = view.findViewById(R.id.progress_state);
        size = view.findViewById(R.id.field_detail_size);
        state = view.findViewById(R.id.field_detail_state);
        size = view.findViewById(R.id.field_detail_size);
        ownerOrEvaluator = view.findViewById(R.id.field_detail_policyholder);
        date = view.findViewById(R.id.field_detail_date);
        estimatedCosts = view.findViewById(R.id.field_cost);

        if (!GlobalConstants.isAdmin) {
            edit.setVisibility(View.GONE);
        }
    }

    @Override
    public void fillData(Field mField) {
        this.mField = (DamageField) mField;
        name.setText(this.mField.getName());
        edit.setImageResource(R.drawable.ic_mode_edit_black_24px);
        state.setText(this.mField.getType().toString(getContext()));
        size.setText(this.mField.getConvertedSize());
        date.setText(this.mField.getParsedDate());
        progressState.setText( this.mField.getProgressStatus().toString(getContext()));
        ownerOrEvaluator.setText(this.mField.getEvaluator());
        estimatedCosts.setText( String.valueOf(this.mField.getInsuranceMoney()));

        if ((this.mField).getPaths() != null) {
            GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), this.mField.getPaths(), this);
            recyclerView.setAdapter(galleryAdapter);
        }
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
                case R.id.pick_form_gallery:
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
