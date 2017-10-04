package main.view;

import main.model.Car;
import main.model.CarService;
import main.model.ImageService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;

@ManagedBean(name="dtBasicView")
@ViewScoped
public class BasicView implements Serializable {

    @ManagedProperty("#{imageService}")
    private ImageService imageService;

    private List<Car> cars;
    private Part file;
    private String text;

    @ManagedProperty("#{carService}")
    private CarService service;

    @PostConstruct
    public void init() {
        cars = service.createCars(10);
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setService(CarService service) {
        this.service = service;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void upload() {
        try {
            InputStream in = file.getInputStream();

            File f = new File("C:\\"+ file.getSubmittedFileName());
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            String test = f.getAbsolutePath();
            byte[] buffer= new byte[1024];
            int length;

            while((length=in.read(buffer))>0)
            {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            InputStream in = file.getInputstream();

            File f = new File("C:\\"+ file.getFileName());
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            String test = f.getAbsolutePath();
            byte[] buffer= new byte[1024];
            int length;

            while((length=in.read(buffer))>0)
            {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
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
}
