package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * A custom BottomSheetDialogFragment to display information of Fields
 */

public class BottomSheetDetailDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, BSEditContract.BottomSheet {

    private static final String TAG = "BottomSheetDetail";

    protected FragmentInteractionListener mListener;
    private BSEditContract.Presenter mPresenter;


    private Field mField;

    Field changedField;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BottomSheetDetailDialogFragment newInstance() {
        final BottomSheetDetailDialogFragment fragment = new BottomSheetDetailDialogFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog, container, false);
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

    private TextView name;
    private TextView state;
    private TextView size;
    private TextView county;
    private TextView ownerOrEvaluator;
    private TextView date;
    private ImageButton addDmg;
    private ImageButton edit;
    private ImageButton navButton;
    private RecyclerView recyclerView;
    private TextView estimatedCosts;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        name = (TextView) view.findViewById(R.id.field_detail_name);
        edit = (ImageButton) view.findViewById(R.id.edit_finish_button);
        edit.setOnClickListener(this);


        size = (TextView) view.findViewById(R.id.field_detail_size);
        state = (TextView) view.findViewById(R.id.field_detail_state);
        size = (TextView) view.findViewById(R.id.field_detail_size);
        county = (TextView) view.findViewById(R.id.field_detail_region);
        ownerOrEvaluator = (TextView) view.findViewById(R.id.field_detail_policyholder);
        date = (TextView) view.findViewById(R.id.field_detail_date);
        estimatedCosts = (TextView) view.findViewById(R.id.field_cost);
        addDmg = (ImageButton) view.findViewById(R.id.add_damageField_button);
        addDmg.setOnClickListener(this);
        navButton = (ImageButton) view.findViewById(R.id.button_nav);
        navButton.setOnClickListener(this);

    }


    @Override
    public void fillData(Field mField) {
        this.mField = mField;
        name.setText(getResources().getString(R.string.dialogItem_Name) + " " + mField.getName());
        county.setText(getResources().getString(R.string.dialogItem_Location) + " " + mField.getCounty());
        edit.setImageResource(R.drawable.ic_mode_edit_black_24px);

        state.setText(getResources().getString(R.string.dialogItem_Type) + " " + mField.getType().toString());
        state.setTextColor(mField.getColor());

        size.setText(getResources().getString(R.string.dialogItem_Size) + " " + mField.getConvertedSize());

        //is field agrarian?
        if (mField instanceof AgrarianField) {
            ownerOrEvaluator.setText(getResources().getString(R.string.detailItem_evaluator) + " " + ((AgrarianField) mField).getOwner());
            date.setText("");
            estimatedCosts.setVisibility(View.INVISIBLE);
        }
        //is field damage?
        if (mField instanceof DamageField) {
            addDmg.setVisibility(View.INVISIBLE);
            county.setVisibility(View.INVISIBLE);
            date.setText(getResources().getString(R.string.dialogItem_Date) + " " + ((DamageField) mField).getParsedDate());
            ownerOrEvaluator.setText(getResources().getString(R.string.dialogItem_Owner) + " " + ((DamageField) mField).getEvaluator());
            estimatedCosts.setText(getResources().getString(R.string.detailItem_estimatedpayment) + " " + String.valueOf(((DamageField) mField).getInsuranceMoney()));

            if (((DamageField) mField).getpaths() != null) {
                GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), ((DamageField) mField).getpaths());
                recyclerView.setAdapter(galleryAdapter);
            } else {
            }
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
                case R.id.edit_finish_button:
                    mListener.onFragmentMessage(TAG, "startEdit", mPresenter.getVisibleField());
                    this.dismiss();
                    break;
                case R.id.add_damageField_button:
                    GlobalConstants.setLastLocationOnMap(mPresenter.getVisibleField().getCentroid());
                    mListener.onFragmentMessage(TAG, "addDmgField", mPresenter.getVisibleField());
                    dismiss();
                    break;
                case R.id.button_nav:
                    //call a googlemaps intent with the position of the centroid point from the field object
                    String geoString = "geo:" + String.valueOf(mField.getCentroid().getLatitude()) + "," + String.valueOf(mField.getCentroid().getLongitude()) + "?q=" + String.valueOf(mField.getCentroid().getLatitude()) + "," + String.valueOf(mField.getCentroid().getLongitude());
                    Uri gmmIntentUri = Uri.parse(geoString);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }

                    break;
            }
        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (BSEditContract.Presenter) presenter;
    }
}
