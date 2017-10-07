package main.controller;

import main.model.Image;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the major controller for managing different actions related to images such as:
 * Creating the list of images
 * Adding images
 * Removing images
 */

@ManagedBean (name = "imageService")
@SessionScoped
public class ImageService {
    private List<Image> allImages;

    /*
    Read imageData.txt file and initialize list of images
     */
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
            if(readLine.isEmpty())
                continue;
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

    /*
    Upload the image
     */
    public void uploadImage(UploadedFile file) {
        try {
            InputStream in = file.getInputstream();
            String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            projectPath = projectPath.replace("out\\artifacts\\JavaEESample\\","web\\resources\\images\\");
            projectPath = "c:/images/";
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

    /*
    Add the Image to the text file list
     */
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

    /*
    Delete the image from the list and remove the corresponding data from the imageData.txt
     */
    public List<Image> deleteSelectedImage(Image image) {
        allImages.remove(image);

        String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        projectPath = projectPath.replace("out\\artifacts\\JavaEESample\\","");
        File f = new File(projectPath + "imageData.txt");

        File newFile = new File(f.getAbsolutePath()+".tmp");

    try {
        BufferedReader b = new BufferedReader(new FileReader(f));
        PrintWriter writer = new PrintWriter(new FileWriter(newFile));
        String readLine = "";

        while ((readLine = b.readLine()) != null) {
            if(readLine.contains(image.getName()))
                continue;
            if(!readLine.isEmpty()) {
                writer.println(readLine);
                writer.flush();
            }
            readLine = null;
        }

        b.close();
        writer.close();
        System.gc();

        if(!f.delete()) {
            throw new IOException("Cannot delete old imageData file");
        }

        if(!newFile.renameTo(f)) {
            throw new IOException("Cannot rename new imageData file");
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

        return allImages;
    }
}