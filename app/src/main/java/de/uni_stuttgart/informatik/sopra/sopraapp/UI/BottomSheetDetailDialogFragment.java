package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 */

public class BottomSheetDetailDialogFragment extends BottomSheetDialogFragment {

    private static final String KEY_NAME = "name";
    private static final String KEY_STATE = "state";
    private static final String KEY_COLOR = "color";
    private static final String KEY_COUNTY = "county";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_SIZE = "size";
    private static final String KEY_DATE = "date";
    private static final String KEY_EVALUATOR = "evaluator";


    private static boolean mEdit = false;


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail_dialog, container, false);
        configureBottomSheetBehaviour(view);
        return view;
    }

    private void configureBottomSheetBehaviour(View view) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView name = (TextView) view.findViewById(R.id.field_detail_name);
        TextView stateOrDate = (TextView) view.findViewById(R.id.field_detail_state);
        TextView county = (TextView) view.findViewById(R.id.field_detail_region);
        TextView ownerOrEvaluator = (TextView) view.findViewById(R.id.field_detail_policyholder);
        TextView size = (TextView) view.findViewById(R.id.field_detail_size);

        name.setText(getArguments().getString(KEY_NAME));
        county.setText(getArguments().getString(KEY_COUNTY));
        size.setText(String.valueOf(getArguments().getDouble(KEY_SIZE)));

        //is field agrarian?
        if(getArguments().getString(KEY_OWNER) != null){
            stateOrDate.setText(getArguments().getSerializable(KEY_STATE).toString());
            stateOrDate.setTextColor(getArguments().getInt(KEY_COLOR));
            ownerOrEvaluator.setText(getArguments().getString(KEY_OWNER));
        }
        //is field damage?
        if(getArguments().getString(KEY_DATE) != null){
            stateOrDate.setText(getArguments().getString(KEY_DATE));
            ownerOrEvaluator.setText(getArguments().getString(KEY_EVALUATOR));
        }


        if(mEdit){
            LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.bottomSheet);
            bottomSheet.removeAllViews();

            bottomSheet.addView(name);
            name.setText("Set up new Field");

           // bottomSheet.removeView(name);
            EditText nameEdit = new EditText(getContext());
            nameEdit.setText("Name..");
            bottomSheet.addView(nameEdit);

          //  bottomSheet.removeView(stateOrDate);
            EditText stateOrDateEdit = new EditText(getContext());
            stateOrDateEdit.setText("Date..");
            stateOrDateEdit.setInputType(InputType.TYPE_CLASS_DATETIME);
            bottomSheet.addView(stateOrDateEdit);

           // bottomSheet.removeView(county);
            EditText countyEdit = new EditText(getContext());
            countyEdit.setText("Address..");
            bottomSheet.addView(countyEdit);

          //  bottomSheet.removeView(ownerOrEvaluator);
            EditText ownerOrEvaluatorEdit = new EditText(getContext());
            ownerOrEvaluatorEdit.setText("Owner or Evaluator Name");
            bottomSheet.addView(ownerOrEvaluatorEdit);
        }

    }

    public boolean getSth(){
        return true;
    }

    public void setId(int id) {
        this.getView().setId(id);
    }
}
