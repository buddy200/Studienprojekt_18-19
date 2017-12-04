package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldStates;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 */

public class BottomSheetDetailDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String KEY_NAME = "name";
    private static final String KEY_STATE = "state";
    private static final String KEY_COLOR = "color";
    private static final String KEY_COUNTY = "county";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_SIZE = "size";
    private static final String KEY_DATE = "date";
    private static final String KEY_EVALUATOR = "evaluator";


    private static boolean mEdit = false;

    private OnButtonInteraction mListener;


    public static BottomSheetDialogFragment newInstance(Field field, boolean edit) {
        final BottomSheetDialogFragment fragment = new BottomSheetDetailDialogFragment();
        Bundle args = new Bundle();
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
        if (context instanceof OnButtonInteraction) {
            mListener = (OnButtonInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMenuFragmentInteractionListener");
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
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView name = (TextView) view.findViewById(R.id.field_detail_name);
        Button editFinish = (Button) view.findViewById(R.id.edit_finish_button);
        editFinish.setOnClickListener(this);

        if(!mEdit) noEditSetup(view, name, editFinish);
        else editSetup(view, name, editFinish);

    }

    private void editSetup(View view, TextView name, Button editFinish) {
        LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.bottomSheet);
        RelativeLayout topPanel = (RelativeLayout) view.findViewById(R.id.topPanel);

        name.setText("Set up new Field");

        // bottomSheet.removeView(name);
        EditText nameEdit = view.findViewById(R.id.field_detail_name_edit);
        nameEdit.setText("Name..");

        EditText countyEdit = view.findViewById(R.id.field_detail_region_edit);
        countyEdit.setText("Address..");

        Spinner state = view.findViewById(R.id.field_detail_state_spinner);
        state.setAdapter(new ArrayAdapter<FieldStates>(getContext(), android.R.layout.simple_spinner_item, FieldStates.values()));

        EditText ownerOrEvaluatorEdit = view.findViewById(R.id.field_detail_policyholder_edit);
        ownerOrEvaluatorEdit.setText("Owner or Evaluator Name");


        editFinish.setText(getContext().getResources().getString(R.string.button_finish_name));
    }

    private void noEditSetup(View view, TextView name, Button editFinish) {
        TextView stateOrDate = (TextView) view.findViewById(R.id.field_detail_state);
        TextView county = (TextView) view.findViewById(R.id.field_detail_region);
        TextView ownerOrEvaluator = (TextView) view.findViewById(R.id.field_detail_policyholder);
        TextView size = (TextView) view.findViewById(R.id.field_detail_size);

        name.setText(getArguments().getString(KEY_NAME));
        county.setText(getArguments().getString(KEY_COUNTY));
        size.setText(String.valueOf(getArguments().getDouble(KEY_SIZE)));
        editFinish.setText(getContext().getResources().getString(R.string.button_edit_name));

        //is field agrarian?
        if (getArguments().getString(KEY_OWNER) != null) {
            stateOrDate.setText(getArguments().getSerializable(KEY_STATE).toString());
            stateOrDate.setTextColor(getArguments().getInt(KEY_COLOR));
            ownerOrEvaluator.setText(getArguments().getString(KEY_OWNER));
        }
        //is field damage?
        if (getArguments().getString(KEY_DATE) != null) {
            stateOrDate.setText(getArguments().getString(KEY_DATE));
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
                    mListener.onButtonInteraction();
                    break;
            }
        }
    }


    public interface OnButtonInteraction {
        public void onButtonInteraction();
    }
}
