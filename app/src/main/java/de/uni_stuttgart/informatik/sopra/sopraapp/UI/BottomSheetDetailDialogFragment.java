package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
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

import java.io.Serializable;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 */

public class BottomSheetDetailDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetDetail";

    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_COLOR = "color";
    private static final String KEY_COUNTY = "county";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_SIZE = "size";
    private static final String KEY_DATE = "date";
    private static final String KEY_EVALUATOR = "evaluator";


    private static boolean mEdit = false;

    private FragmentInteractionListener mListener;

    private static Field field;

    public static BottomSheetDialogFragment newInstance(Field field, boolean edit) {
        final BottomSheetDialogFragment fragment = new BottomSheetDetailDialogFragment();
        Bundle args = new Bundle();
        field = field;
        fragment.setArguments(field.getBundle());

        mEdit = edit;
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
        if(mEdit){
            view = inflater.inflate(R.layout.fragment_item_detail_dialog_edit, container, false);
        }
        configureBottomSheetBehaviour(view);
        return view;
    }

    private void configureBottomSheetBehaviour(View view) {

    }

    String[] s = new String[10];
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
        size.setText(String.valueOf(getArguments().getDouble(KEY_SIZE)) + "m" + "\u00B2");


        if(!mEdit) noEditSetup(view, name, editFinish);
        else editSetup(view, name, editFinish);

    }

    private void editSetup(View view, TextView name, Button editFinish) {
        LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.bottomSheet);
        RelativeLayout topPanel = (RelativeLayout) view.findViewById(R.id.topPanel);

        name.setText("Set up new Field");

        nameEdit = view.findViewById(R.id.field_detail_name_edit);
        nameEdit.setText("Name..");

        countyEdit = view.findViewById(R.id.field_detail_region_edit);
        countyEdit.setText("Address..");

        type = view.findViewById(R.id.field_detail_state_spinner);
        type.setAdapter(new ArrayAdapter<AgrarianFieldType>(getContext(), android.R.layout.simple_spinner_item, AgrarianFieldType.values()));

        ownerOrEvaluatorEdit = view.findViewById(R.id.field_detail_policyholder_edit);
        ownerOrEvaluatorEdit.setText("Owner or Evaluator Name");

        editFinish.setText(getContext().getResources().getString(R.string.button_finish_name));
    }

    private void noEditSetup(View view, TextView name, Button editFinish) {
        TextView state = (TextView) view.findViewById(R.id.field_detail_state);
        TextView county = (TextView) view.findViewById(R.id.field_detail_region);
        TextView ownerOrEvaluator = (TextView) view.findViewById(R.id.field_detail_policyholder);
        TextView date = (TextView) view.findViewById(R.id.field_detail_date);

        name.setText(getArguments().getString(KEY_NAME));
        county.setText(getArguments().getString(KEY_COUNTY));
        editFinish.setText(getContext().getResources().getString(R.string.button_edit_name));

        state.setText(getArguments().getSerializable(KEY_TYPE).toString());
        state.setTextColor(getArguments().getInt(KEY_COLOR));

        //is field agrarian?
        if (getArguments().getString(KEY_OWNER) != null) {
            ownerOrEvaluator.setText(getArguments().getString(KEY_OWNER));
            date.setText("");
        }
        //is field damage?
        if (getArguments().getString(KEY_DATE) != null) {
            date.setText(getArguments().getString(KEY_DATE));
            ownerOrEvaluator.setText(getArguments().getString(KEY_EVALUATOR));
        }
    }

    public void setId(int id) {
        this.getView().setId(id);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) {
            switch (v.getId()) {
                case R.id.edit_finish_button:
                    if(mEdit) mListener.onFragmentMessage(TAG, "edit", null);
                    else mListener.onFragmentMessage(TAG, "noEdit", null);

                    break;
            }
        }
    }

    public Bundle getData(){
        Bundle b = new Bundle();
        b.putString("name", nameEdit.getText().toString());
        b.putSerializable("type", (Serializable) type.getSelectedItem());
        if(countyEdit.getText() != null){
            b.putString("address", countyEdit.getText().toString());
        }
        b.putString("ownerOrEvaluator", ownerOrEvaluatorEdit.getText().toString());

        return b;
    }

}
