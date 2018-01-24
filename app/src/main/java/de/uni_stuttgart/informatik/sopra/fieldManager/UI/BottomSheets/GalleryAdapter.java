package de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import de.uni_stuttgart.informatik.sopra.fieldManager.MainActivity;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.PictureData;

/**
 * This class creates the photo gallery from one damageField
 */


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private static final String TAG = "GalleryAdapter";

    private ArrayList<PictureData> galleryList;
    private Context context;
    private BottomSheetDialogFragment bottomSheet;

    public GalleryAdapter(Context context, ArrayList<PictureData> galleryList, BottomSheetDialogFragment bottomSheet) {
        this.bottomSheet = bottomSheet;
        this.galleryList = galleryList;
        this.context = context;
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
        new LoadImage(viewHolder.img, galleryList.get(i).getImage_path()).execute();
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(galleryList.get(getAdapterPosition()).getImage_path())), "image/*");
                MainActivity.getmContext().startActivity(intent);
            }
        };

        private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                generateDeleteDialog().show();
                return true;
            }
        };

        public ViewHolder(View view) {
            super(view);
            if (bottomSheet instanceof BSDetailDialogEditDmgField || bottomSheet instanceof BottomSheetAddPhoto) {
                view.setOnLongClickListener(mOnLongClickListener);
            }else {
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

class LoadImage extends AsyncTask<Object, Void, Bitmap> {
    private static final String TAG = "GalleryAdapterAsync";
    private static final int IMAGE_SCALE = 7;
    private  BitmapFactory.Options options;



    private ImageView imv;
    private String path;

    public LoadImage(ImageView imv, String path) {
        this.imv = imv;
        this.path = path;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap bitmap = null;
        options = new BitmapFactory.Options();
        File file = new File(path);

        if(file.exists()){
            options.inSampleSize = IMAGE_SCALE;
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        }

        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null && imv != null){
            imv.setVisibility(View.VISIBLE);
            imv.setImageBitmap(result);
        }else{
            imv.setVisibility(View.GONE);
        }
    }

}