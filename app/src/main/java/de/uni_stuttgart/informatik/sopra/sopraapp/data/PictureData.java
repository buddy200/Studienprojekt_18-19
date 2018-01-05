package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import java.io.Serializable;

/**
 * This class save a title and the file-path of one picture
 */

public class PictureData implements Serializable {
    private static final long serialVersionUID = 21L;
    private String image_title;
    private String image_path;


public PictureData(String image_title, String image_path){
    this.image_title = image_title;
    this.image_path = image_path;
}

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String image_title) {
        this.image_title = image_title;
    }


}
