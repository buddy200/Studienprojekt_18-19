package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.PictureData;

/**
 * This class creates the photo gallery from one damageField
 */


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<PictureData> galleryList;
    private Context context;
    private BottomSheetDialogFragment bottomSheet;
    private final BitmapFactory.Options options;
    private static final int IMAGE_SCALE = 10;

    public GalleryAdapter(Context context, ArrayList<PictureData> galleryList, BottomSheetDialogFragment bottomSheet) {
        this.bottomSheet = bottomSheet;
        this.galleryList = galleryList;
        this.context = context;
        options = new BitmapFactory.Options();

    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    /*
     * this method display all Pictures from oen damageField
     */
    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(galleryList.get(i).getImage_title());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        File imgFile = new File(galleryList.get(i).getImage_path());
        if (imgFile.exists()) {
            options.inSampleSize = IMAGE_SCALE;
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            viewHolder.img.setImageBitmap(myBitmap);
        }
        imgFile = null;
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    /**
     * set up the Recyler View for the pictures
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateDeleteDialog().show();
            }
        };

        public ViewHolder(View view) {
            super(view);
            if (bottomSheet instanceof BSDetailDialogEditDmgField || bottomSheet instanceof BottomSheetAddPhoto) {
                view.setOnClickListener(mOnClickListener);
            }

            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
        }

        /**
         * generate a delete Dialog
         * @return
         */
        private AlertDialog.Builder generateDeleteDialog() {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            if (bottomSheet instanceof BSDetailDialogEditDmgField) {
                                ((BSDetailDialogEditDmgField) bottomSheet).removePicture(getAdapterPosition());
                            } else if (bottomSheet instanceof BottomSheetAddPhoto) {
                                ((BottomSheetAddPhoto) bottomSheet).removePicture(getAdapterPosition());
                            }
                            dialog.dismiss();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getResources().getString(R.string.dialogmessage_want_delete_photo)).setPositiveButton(context.getResources().getString(R.string.word_yes), dialogClickListener)
                    .setNegativeButton(context.getResources().getString(R.string.word_no), dialogClickListener);

            return builder;
        }
    }
}