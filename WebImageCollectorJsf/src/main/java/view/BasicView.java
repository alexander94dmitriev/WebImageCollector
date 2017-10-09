package main.java.view;

import main.java.model.Image;
import main.java.controller.ImageService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name="BasicView")
@SessionScoped
public class BasicView implements Serializable {

    @ManagedProperty("#{imageService}")
    private ImageService imageService;

    private List<Image> images;
    private Image selectedImage;
    private String text;

    @PostConstruct
    public void init() {
        try {
            images = imageService.initializeImageList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        imageService.uploadImage(file);
        try {
            images = imageService.initializeImageList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void handleKeyEvent () {
        this.text = this.text.toUpperCase();
    }

    public ImageService getImageService() {
        return imageService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Image getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void deleteSelectedImage(Image image) {
        images = imageService.deleteSelectedImage(image);
    }
}
