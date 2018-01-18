package de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BasePresenter;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.PhotoManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.PictureData;

import static android.app.Activity.RESULT_OK;

/**
 * Created by larsb on 14.01.2018.
 */

public class BottomSheetAddPhoto extends BottomSheetDialogFragment implements View.OnClickListener, BSEditContract.BottomSheet {

    private static final String TAG = "BottomSheetAddPhoto";
    private static final int RESULT_LOAD_IMAGE = 13;

    protected FragmentInteractionListener mListener;
    private BSEditContract.Presenter mPresenter;

    private DamageField mField;

    /**
     * this factory method is used to generate an instance
     * using the provided parameters
     *
     * @return A new instance of fragment BottomSheetDialogFragment.
     */
    public static BottomSheetAddPhoto newInstance() {
        final BottomSheetAddPhoto fragment = new BottomSheetAddPhoto();
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
        View view = inflater.inflate(R.layout.fragment_add_fotos, container, false);
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

    private RecyclerView recyclerView;
    private EditText photoName;
    private ImageButton addPhotoFromGallery;
    private ImageButton addPhotoFromCamera;
    private ImageButton finish;
    private GalleryAdapter galleryAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        photoName = (EditText) view.findViewById(R.id.edit_photo_name);
        addPhotoFromCamera = (ImageButton) view.findViewById(R.id.button_add_photo);
        addPhotoFromGallery = (ImageButton) view.findViewById(R.id.button_add_photo_from_gallery);
        finish = (ImageButton) view.findViewById(R.id.finish_edit_button_agr);
        addPhotoFromGallery.setOnClickListener(this);
        addPhotoFromCamera.setOnClickListener(this);
        finish.setOnClickListener(this);
//        GlobalConstants.setCurrentPhotoField((DamageField) mPresenter.getVisibleField());

    }

    @Override
    public void fillData(Field mField) {
        this.mField = (DamageField) mField;

        if ((this.mField).getPaths() != null) {
            galleryAdapter = new GalleryAdapter(getContext(), this.mField.getPaths(), this);
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
                    mPresenter.changeField(mPresenter.getVisibleField());
                    dismiss();
                    break;
                case R.id.button_add_photo:
                    takePhoto();
                    mPresenter.changeField(mPresenter.getVisibleField());
                    break;
                case R.id.button_add_photo_from_gallery:
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    break;
            }
        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (BSEditContract.Presenter) presenter;
    }

    /*
 * create a PhotoManager object and save the fielpath from the picture in the damageField
 */
    public void takePhoto() {
        PhotoManager photoManager = new PhotoManager(getActivity(), this);
        if (mPresenter.getVisibleField() instanceof DamageField) {
            String s = photoManager.dispatchTakePictureIntent();
            PictureData pictureData = new PictureData(photoName.getText().toString(), s);
            mPresenter.addPhotoToDatabase(pictureData);
            galleryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * remove picture from the clicked position and refresh the gallery
     *
     * @param position
     */
    public void removePicture(int position) {
        DamageField damageField = (DamageField) mPresenter.getVisibleField();
        //remove the image data from the damage field and refresh the recycler view
        PictureData pd = damageField.getPaths().get(position);
        mPresenter.deltePhotFromDatabase(pd);
        //remove the image data from the damage field and refresh the recycler view
        ((DamageField) mPresenter.getVisibleField()).deletePhoto(position);
        mPresenter.changeField(mPresenter.getVisibleField());
        galleryAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK && requestCode == PhotoManager.REQUEST_TAKE_PHOTO) {
            DamageField fieldPhotoToDelete = (DamageField) mPresenter.getVisibleField();
            mPresenter.deltePhotFromDatabase(fieldPhotoToDelete.getPaths().get(fieldPhotoToDelete.getPaths().size() - 1));
            fieldPhotoToDelete.deletePhoto(fieldPhotoToDelete.getPaths().size() - 1);
            mPresenter.changeField(fieldPhotoToDelete);
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            if(picturePath == null){
                return;
            }
            cursor.close();
            PictureData pictureData = new PictureData(photoName.getText().toString(), picturePath);
            mPresenter.addPhotoToDatabase(pictureData);
            galleryAdapter.notifyDataSetChanged();
        }
    }
}
