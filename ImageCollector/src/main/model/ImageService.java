package main.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminko on 03/10/17.
 */

@ManagedBean (name = "imageService")
@ApplicationScoped
public class ImageService {
    private List<Image> allImages;

    public List<Image> initializeImageList() throws IOException {
        allImages = new ArrayList<>();
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        filePath = filePath.replace("out\\artifacts\\JavaEESample\\","\\imageData.txt");
        File f = new File(filePath);
        if (!f.exists())
        {
            throw new IOException("File not found");
        }
        BufferedReader b = new BufferedReader(new FileReader(f));

        String readLine = "";


        while ((readLine = b.readLine()) != null) {
            String[] split = readLine.split(";|\n");
            String imagename = split[0];
            String imagePath = split[1];
            List<String> imageTags = new ArrayList<>();
            for(int i = 2; i < split.length; ++i)
            {
                imageTags.add(split[i]);
            }
            Image image = new Image(imagename,imagePath,imageTags);
            allImages.add(image);
            readLine = null;
        }

        b.close();
        System.gc();

        return allImages;
    }

    public List<Image> getAllImages() {
        return allImages;
    }

    public void setAllImages(List<Image> allImages) {
        this.allImages = allImages;
    }

    public void addImage(Image image) {
        allImages.add(image);
    }
}
