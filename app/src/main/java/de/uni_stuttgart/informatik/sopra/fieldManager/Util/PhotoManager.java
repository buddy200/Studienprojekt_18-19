package de.uni_stuttgart.informatik.sopra.fieldManager.Util;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BottomSheetAddPhoto;


/**
 * Created by larsb on 20.12.2017.
 */

public class PhotoManager {
    public static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;
    Activity activity;
    BottomSheetAddPhoto fragment;

    public PhotoManager(Activity a, BottomSheetAddPhoto fragment) {
        this.activity = a;
        this.fragment = fragment;
    }

    public String dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(activity, "Could not open file!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity, " de.uni_stuttgart.informatik.sopra.fieldManager.Util.fileprovider"
                        ,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                fragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                return photoFile.getAbsolutePath();
            }
        }
        return null;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("Path", mCurrentPhotoPath);
        return image;
    }
}
