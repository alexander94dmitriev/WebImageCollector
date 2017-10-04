import main.Car;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

@ManagedBean(name="dtBasicView")
@ViewScoped
public class BasicView implements Serializable {

    private List<Car> cars;
    private Part file;

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

            File f = new File("C:\\" + file.getSubmittedFileName());
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

 /* InputStream in = file.getInputStream();
            Files.copy(in,new File("uploads","pic.png").toPath());
 */       } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
