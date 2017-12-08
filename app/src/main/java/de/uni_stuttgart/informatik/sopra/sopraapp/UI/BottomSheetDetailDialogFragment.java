package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
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

public class BottomSheetDetailDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetDetail";

    protected FragmentInteractionListener mListener;

    Field changedField;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BottomSheetDialogFragment newInstance(Field field) {
        final BottomSheetDialogFragment fragment = new BottomSheetDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("mField", field);
        fragment.setArguments(args);

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

    EditText nameEdit;
    EditText countyEdit;
    Spinner type;
    EditText ownerOrEvaluatorEdit;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView name = (TextView) view.findViewById(R.id.field_detail_name);
        Button editFinish = (Button) view.findViewById(R.id.edit_finish_button);
        editFinish.setOnClickListener(this);

        TextView size = (TextView) view.findViewById(R.id.field_detail_size);

        Field mField = (Field) getArguments().getSerializable("mField");
        mField.finish();

        size.setText(mField.getSize() + "m" + "\u00B2");


       setupView(view, mField, name, editFinish);

    }

    /**
     * sets up the view, will be overwritten in custom BottomSheetDetailDialogFragments
     * @param view
     * @param mField
     * @param name
     * @param editFinish
     */
    protected void setupView(View view, Field mField, TextView name, Button editFinish){
        noEditSetup(view, mField, name, editFinish);
    }

    /**
     * the UI setup for this dialog, containg TextViews, nothing to edit
     * @param view
     * @param mField
     * @param name
     * @param editFinish
     */
    private void noEditSetup(View view, Field mField, TextView name, Button editFinish) {
        TextView state = (TextView) view.findViewById(R.id.field_detail_state);
        TextView size = (TextView) view.findViewById(R.id.field_detail_size);
        TextView county = (TextView) view.findViewById(R.id.field_detail_region);
        TextView ownerOrEvaluator = (TextView) view.findViewById(R.id.field_detail_policyholder);
        TextView date = (TextView) view.findViewById(R.id.field_detail_date);
        Button addDmg = (Button) view.findViewById(R.id.add_damageField_button);
        addDmg.setOnClickListener(this);

        name.setText(mField.getName());
        county.setText(mField.getCounty());
        editFinish.setText(getContext().getResources().getString(R.string.button_edit_name));

        state.setText(mField.getType().toString());
        state.setTextColor(mField.getColor());

        //is field agrarian?
        if (mField instanceof AgrarianField) {
            ownerOrEvaluator.setText(((AgrarianField)mField).getOwner());
            date.setText("");
        }
        //is field damage?
        if (mField instanceof DamageField) {
            addDmg.setVisibility(View.INVISIBLE);
            date.setText(((DamageField)mField).getParsedDate());
            ownerOrEvaluator.setText(((DamageField)mField).getEvaluator());
        }
    }

    public void setId(int id) {
        this.getView().setId(id);
    }

    /**
     * handle button clicks
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(mListener != null) {
            switch (v.getId()) {
                case R.id.edit_finish_button:
                    mListener.onFragmentMessage(TAG, "startEdit", getArguments().getSerializable("mField"));
                    this.dismiss();
                    break;
                case R.id.add_damageField_button:
                    mListener.onFragmentMessage(TAG, "addDmgField", getArguments().getSerializable("mField"));
            }
        }
    }

}
