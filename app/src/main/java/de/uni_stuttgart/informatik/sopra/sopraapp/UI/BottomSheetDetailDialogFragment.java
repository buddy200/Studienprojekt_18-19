package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;


/**
 * sopra_priv
 * Created by Felix B on 20.11.17.
 * Mail: felix.burk@gmail.com
 */

public class BottomSheetDetailDialogFragment extends BottomSheetDialogFragment {

    private static final String KEY_NAME = "name";


    public static BottomSheetDialogFragment newInstance(Field field) {
        final BottomSheetDialogFragment fragment = new BottomSheetDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_NAME, field.getName());
        fragment.setArguments(args);

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
        return inflater.inflate(R.layout.fragment_item_detail_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView name = (TextView) view.findViewById(R.id.field_detail_name);
        name.setText(getArguments().getString(KEY_NAME));
    }
}
