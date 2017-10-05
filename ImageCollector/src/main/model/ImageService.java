package main.model;

import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminko on 03/10/17.
 */

@ManagedBean (name = "imageService")
@SessionScoped
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

    public void uploadImage(UploadedFile file) {
        try {
            InputStream in = file.getInputstream();
            String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            projectPath = projectPath.replace("out\\artifacts\\JavaEESample\\","web\\resources\\images\\");
            File f = new File(projectPath + file.getFileName());
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

            String imageName = f.getName();
            int dotIndex = imageName.lastIndexOf(".");
            imageName = imageName.replace(imageName.substring(dotIndex),"");

            String imagePath = f.getPath();
            imagePath = imagePath.substring(imagePath.indexOf("\\images"));
            imagePath = imagePath.replace("\\","/");
            Image image = new Image(imageName,imagePath,null);
            addImageToList(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImageToList(Image image)
    {
        String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        projectPath = projectPath.replace("out\\artifacts\\JavaEESample\\","");
        File f = new File(projectPath + "imageData.txt");

        try {
            PrintWriter out = new PrintWriter(new FileWriter(f, true));
            out.append("\n"+image.getName()+";"+image.getPath()+";"+"None");
            out.close();
        } catch (IOException e) {
            System.out.println("Unable to find an imageData.txt. Make sure to have it!");
            e.printStackTrace();
        }

    }
}
