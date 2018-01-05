package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 *
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
    private Button addDmg;
    private Button edit;
    private Button navButton;
    private ImageView imageView;
    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        name = (TextView) view.findViewById(R.id.field_detail_name);
        edit = (Button) view.findViewById(R.id.edit_finish_button);
        edit.setOnClickListener(this);


        size = (TextView) view.findViewById(R.id.field_detail_size);
        state = (TextView) view.findViewById(R.id.field_detail_state);
        size = (TextView) view.findViewById(R.id.field_detail_size);
        county = (TextView) view.findViewById(R.id.field_detail_region);
        ownerOrEvaluator = (TextView) view.findViewById(R.id.field_detail_policyholder);
        date = (TextView) view.findViewById(R.id.field_detail_date);
        addDmg = (Button) view.findViewById(R.id.add_damageField_button);
        addDmg.setOnClickListener(this);
        navButton = (Button) view.findViewById(R.id.button_nav);
        navButton.setOnClickListener(this);

    }

    @Override
    public void fillData(Field mField) {
        this.mField = mField;
        name.setText(mField.getName());
        county.setText(mField.getCounty());
        edit.setText(getContext().getResources().getString(R.string.button_edit_name));

        state.setText(mField.getType().toString());
        state.setTextColor(mField.getColor());

        size.setText(mField.getSize() + "m" + "\u00B2");

        //is field agrarian?
        if (mField instanceof AgrarianField) {
            ownerOrEvaluator.setText(((AgrarianField)mField).getOwner());
            date.setText("");
        }
        //is field damage?
        if (mField instanceof DamageField) {
            addDmg.setVisibility(View.INVISIBLE);
            county.setVisibility(View.INVISIBLE);
            date.setText(((DamageField)mField).getParsedDate());
            ownerOrEvaluator.setText(((DamageField)mField).getEvaluator());

            if(((DamageField) mField).getpaths() != null) {
                GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), ((DamageField) mField).getpaths());
                recyclerView.setAdapter(galleryAdapter);
            }
            else{
            }
        }

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }



    /**
     * handle button clicks
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
                    mListener.onFragmentMessage(TAG, "addDmgField", mPresenter.getVisibleField());
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
