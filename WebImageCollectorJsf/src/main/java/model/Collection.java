package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the images that are in one collection, it has a name and the list of images
 */
public class Collection {

    private String name;
    private List<Image> images;

    public Collection(String name) {
        this.setName(name);
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        if (!images.contains(image)) {
            images.add(image);
        }
    }

    public void removeImage(Image image) {
        if (images == null)
            return;

        images.remove(image);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
