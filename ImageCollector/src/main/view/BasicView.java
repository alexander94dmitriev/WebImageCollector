package main.view;

import main.model.Car;
import main.model.CarService;
import main.model.Image;
import main.model.ImageService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.File;
import java.io.FileInputStream;
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

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String filename = context.getExternalContext().getRequestParameterMap().get("filename");
            return new DefaultStreamedContent(new FileInputStream(new File("C:/", filename)));
        }
    }
}
