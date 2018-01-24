package de.uni_stuttgart.informatik.sopra.fieldManager.data;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.ProgressStatus;

/**
 * Created by larsb on 22.11.2017.
 * <p>
 * a custom field class, containing fields that represent damage
 */

public class DamageField extends Field implements Serializable {
    private static final String TAG = "DamageField";

    private static final long serialVersionUID = 8L;

    private String parsedDate = "";
    private String evaluator = "";
    private ProgressStatus progressStatus;
    private double insuranceMoney;
    private ArrayList<PictureData> paths;
    private DamageFieldType defaultType = DamageFieldType.Storm;
    private AgrarianField parentField;

    /**
     * constructor
     *
     * @param context
     * @param cPoints
     */
    public DamageField(Context context, List<CornerPoint> cPoints, AgrarianField parentField) {
        super(context, cPoints);
        super.setType(defaultType);
        this.setColor(damageFieldToColor());
        this.setDate(new Date(0));
        this.paths = new ArrayList<>();
        this.parentField = parentField;
        calcInsuranceAmount();
        progressStatus = ProgressStatus.sent;
    }

    /**
     * map type of damage to color
     *
     * @return
     */
    private int damageFieldToColor() {
        return R.color.fieldDefaultColor;
    }

    public String getParsedDate() {
        return parsedDate;
    }

    /**
     * return the date in a readable format
     *
     * @param date
     */
    public void setDate(Date date) {
        //store date as a string - is probably easier
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String parsedDate =
                context.getResources().getString(R.string.date_label)
                        + " " + format1.format(date);
        this.parsedDate = parsedDate;
    }

    public void setDate(String date) {
        this.parsedDate = date;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public ArrayList<PictureData> getPaths() {
        return paths;
    }

    public void setPath(String path) {
        PictureData pictureData = new PictureData((new Integer(paths.size())).toString(), path);
        paths.add(pictureData);
    }

    public void setPath(PictureData pictureData) {
        paths.add(pictureData);
    }

    public void setPhotoName(String name, PictureData pictureData) {
        pictureData.setImage_title(name);
    }

    public void calcInsuranceAmount() {
        insuranceMoney = this.getSize() * this.getType().getInsuranceMoneyPerSquareMeter() * parentField.getType().getInsuranceMoneyPerSquareMeter();
    }

    public double getInsuranceMoney() {
        return insuranceMoney;
    }

    public AgrarianField getParentField() {
        return parentField;
    }

    public void setType(DamageFieldType type) {
        super.setType(type);
        this.calcInsuranceAmount();
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    /**
     * delete the given photo from the damage field and from the internal storage
     *
     * @param pos give the position of the photo in the paths ArrayList
     */
    public void deletePhoto(int pos) {
        File photo = new File(getPaths().get(pos).getImage_path());
        photo.delete();
        paths.remove(pos);
    }

    /**
     * this method delete all photos from the damageField and from the internal storage
     */
    public void deleteAllPhotos() {
        for (PictureData pictureData : paths) {
            File photo = new File(pictureData.getImage_path());
            photo.delete();
        }
        paths.clear();
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }
}


