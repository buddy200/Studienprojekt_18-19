package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 *
 * A generic List Dialog Fragment containing all Fields on the Map
 */
public class ItemListDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "ItemListDialogFragment";

//    private static final String ARG_ITEM_LIST_BUNDLE = "list_bundle";
//    private static final String ARG_ITEM_LIST = "list";

    private FragmentInteractionListener mListener;
    private AppDataManager dataManager;
    private static List<Field> fieldData;

 //   private static ArrayList<Field> fieldList;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment ItemListDialogFragment.
     */
    public static ItemListDialogFragment newInstance(List<Field> fields) {
        final ItemListDialogFragment fragment = new ItemListDialogFragment();
        fieldData = fields;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
        configureBottomSheetBehavior(view);
        return view;
    }

    /**
     * method to configure the behaviour of the bottom sheet
     * @param view
     */
    private void configureBottomSheetBehavior(View view) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    /**
     * the ViewHolder holding the Field Objects
     */
    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final TextView state;
        final TextView county;
        final LinearLayout layout;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            //add the item layout xml
            super(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false));

            //the item is shown as a text view
            text = (TextView) itemView.findViewById(R.id.item_field_name);
            state = (TextView) itemView.findViewById(R.id.item_field_state);
            county = (TextView) itemView.findViewById(R.id.item_field_county);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_item);

            // the on click listener for the item that is being clicked
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                            mListener.onFragmentMessage(TAG, "itemClick", fieldData.get(getAdapterPosition()));
                        dismiss();
                    }
                }
            });
        }

    }

    /**
     * Item Adapter for the different Fields
     */
    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

       // private AppDataManager dataManager;

    /*    private final Bundle  mFieldBundle;
        private final ArrayList<Bundle> bundleListFields;*/

        private ItemAdapter() {
        //    dataManager = new AppDataManager(getContext());
            /*
            mFieldBundle = fieldBundles;
            bundleListFields = new ArrayList<>();
            for(int i=0; i<fieldBundles.getParcelableArrayList(ARG_ITEM_LIST).size(); i++){
                bundleListFields.add((Bundle) fieldBundles.getParcelableArrayList(ARG_ITEM_LIST).get(i));
            }
            */
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        /**
         * sets the text of our items
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(fieldData.get(position).getName());
            if(fieldData.get(position).getType() != null){
                holder.state.setText(fieldData.get(position).getType().toString());
                holder.state.setTextColor(fieldData.get(position).getColor());
            }else {
                holder.state.setText(" ");
                holder.state.setTextColor(fieldData.get(position).getColor());
            }
            if(fieldData.get(position) instanceof AgrarianField) {
                holder.county.setText(fieldData.get(position).getCounty());
            }
            else{
                if(fieldData.get(position) instanceof DamageField)
                holder.county.setText(((DamageField) fieldData.get(position)).getParentField().getName());
            }
        }

        @Override
        public int getItemCount() {
            return fieldData.size();
        }

    }

}
