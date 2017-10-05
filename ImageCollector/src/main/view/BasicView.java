package main.view;

import main.model.Car;
import main.model.CarService;
import main.model.Image;
import main.model.ImageService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name="dtBasicView")
@SessionScoped
public class BasicView implements Serializable {

    @ManagedProperty("#{imageService}")
    private ImageService imageService;

    private List<Image> images;

    private List<Car> cars;
    private String text;

    @ManagedProperty("#{carService}")
    private CarService service;



    @PostConstruct
    public void init() {
        cars = service.createCars(10);
        try {
            images = imageService.initializeImageList();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<Car> getCars() {
        return cars;
    }

    public void setService(CarService service) {
        this.service = service;
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
}
