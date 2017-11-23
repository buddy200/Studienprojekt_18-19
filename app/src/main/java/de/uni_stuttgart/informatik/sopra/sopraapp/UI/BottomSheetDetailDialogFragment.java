package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.ArgrarianField;
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


    public static BottomSheetDialogFragment newInstance(Field argrarianField) {
        final BottomSheetDialogFragment fragment = new BottomSheetDetailDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(argrarianField.getBundle());
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
        TextView state = (TextView) view.findViewById(R.id.field_detail_state);
        TextView county = (TextView) view.findViewById(R.id.field_detail_region);
        TextView owner = (TextView) view.findViewById(R.id.field_detail_policyholder);

        name.setText(getArguments().getString(KEY_NAME));
        state.setText(getArguments().getSerializable(KEY_STATE).toString());
        state.setTextColor(getArguments().getInt(KEY_COLOR));
        county.setText(getArguments().getString(KEY_COUNTY));
        owner.setText(getArguments().getString(KEY_OWNER));
    }
}
