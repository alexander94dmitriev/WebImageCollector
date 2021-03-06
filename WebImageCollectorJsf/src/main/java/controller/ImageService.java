package controller;

import model.Collection;
import model.Image;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
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

    /**
    Read imageData.txt file and initialize list of images
     */
    public List<Image> initializeImageList() throws IOException {
        allImages = new ArrayList<>();
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //filePath = filePath.replace("src\\main\\webapp\\","\\imageData.txt");
        File f = new File(filePath+"\\imageData.txt");
        if (!f.exists())
        {
            f.createNewFile();
            return allImages;
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

    /**
     * Create the tags list
     *
     * @return
     */
    public List<String> initializeAllTagsList() {
        List<String> allTags = new ArrayList<>();

        for(Image image: allImages) {
            for(String tag: image.getTags()) {
                if(!allTags.contains(tag)) {
                    allTags.add(tag);
                }
            }
        }

        return allTags;
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

    /**
     Upload the image to an app, store the info about it in textData
     */
    public void uploadImage(UploadedFile file) {
        try {
            InputStream in = file.getInputstream();
            String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            //projectPath = "c:/images/images/";
            projectPath = System.getProperty("user.home") + "/images/";

            for (Image imageInList : allImages) {
                String name = file.getFileName().replaceFirst("[.][^.]+$", "");

                if (imageInList.getName().equals(name)) {
                    FacesMessage message = new FacesMessage("The image already exists in the table");
                    FacesContext.getCurrentInstance().addMessage(null, message);

                    in.close();
                    return;
                }
            }

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

            //for(allImages)

            int dotIndex = f.getName().lastIndexOf(".");
            imageName = imageName.replace(imageName.substring(dotIndex),"");
            int anotherDotIndex = f.getPath().lastIndexOf(".");
            //String imagePath = "/images/" + f.getName();
            String imagePath = f.getName();
            Image image = new Image(imageName,imagePath,null);
            addImageToList(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    Add the Image to the text file list
     */
    public void addImageToList(Image image)
    {
        String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //projectPath = projectPath.replace("out\\artifacts\\JavaEESample\\","");
        File f = new File(projectPath + "\\imageData.txt");

        try {
            PrintWriter out = new PrintWriter(new FileWriter(f, true));
            out.append("\n" + image.getName() + ";/" + image.getPath() + ";");
            out.close();
        } catch (IOException e) {
            System.out.println("Unable to find an imageData.txt. Make sure to have it!");
            e.printStackTrace();
        }

    }

    /**
    Delete the image from the list and remove the corresponding data from the imageData.txt
     */
    public List<Image> deleteSelectedImage(Image image){

        //remove from the list
        allImages.remove(image);

        try {
            //remove from the folder
            String filePath = System.getProperty("user.home") + "/images/";
            File imageFile = new File(filePath + image.getPath());

            if (!imageFile.exists()) {
                throw new IOException("Unable to delete file: " + imageFile.getName());
            } else {
                imageFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //remove from the file. Should make a new text file
        String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(projectPath + "\\imageData.txt");

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

        FacesMessage message = new FacesMessage("The image: " + image.getName() + " deleted");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return allImages;
    }

    /**
     * add a tag to an Image and store the info in textData
     * @param image
     * @param tag
     */
    public void addTagToImage(Image image, String tag) {
        String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(projectPath + "\\imageData.txt");

        File newFile = new File(f.getAbsolutePath()+".tmp");

        try {
            BufferedReader b = new BufferedReader(new FileReader(f));
            PrintWriter writer = new PrintWriter(new FileWriter(newFile));
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                if(readLine.contains(image.getName())) {
                    String temp = b.readLine();
                    String finalString = readLine + tag + ";" + "\n";
                    writer.write(finalString);
                    writer.flush();
                    continue;
                }

                if (readLine.isEmpty())
                    continue;

                writer.println(readLine);
                writer.flush();
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

            FacesMessage message = new FacesMessage("The tag: " + tag + " was added to image: " + image.getName());
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the tag from inage and remove the info in textData
     * @param image
     * @param tag
     */
    public void removeTagFromImage(Image image, String tag) {
        String projectPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(projectPath + "\\imageData.txt");

        if (tag.isEmpty()) {
            FacesMessage message = new FacesMessage("Nothing is selected to delete");
            FacesContext.getCurrentInstance().addMessage("", message);
            return;
        }

        File newFile = new File(f.getAbsolutePath()+".tmp");

        try {
            BufferedReader b = new BufferedReader(new FileReader(f));
            PrintWriter writer = new PrintWriter(new FileWriter(newFile));
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                if(readLine.contains(image.getName())) {
                    String newReadLine = readLine.replace(tag+";","");
                    writer.println(newReadLine);
                    writer.flush();
                    continue;
                }
                writer.println(readLine);
                writer.flush();
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


            FacesMessage message = new FacesMessage("The tag: " + tag + " deleted from image: " + image.getName());
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find an image using selected search criteria
     * @param searchString
     * @param searchStringSelected
     * @param collections
     * @return
     */
    public List<Image> searchImages(String searchString, String searchStringSelected, List<Collection> collections) {
        List<Image> currentImages = new ArrayList<>();
        if(searchString == null || searchString.isEmpty() || searchStringSelected == null) {
            currentImages = allImages;
        } else if(searchStringSelected.contains("tags")) {
            currentImages = new ArrayList<>();
            String[] split = searchString.split(", ");
            for(Image image: allImages) {
                for(int i = 0; i < split.length; ++i)
                    if(image.getTags().contains(split[i])) {
                        currentImages.add(image);
                    }
            }
        } else if(searchStringSelected.contains("collections")) {
            for (Collection collection : collections) {
                if (collection.getName().contains(searchString)) {
                    currentImages = collection.getImages();
                    break;
                }
            }
        } else if(searchStringSelected.contains("name")) {
            for(Image image: allImages) {
                if(image.getName().contains(searchString)) {
                    currentImages.add(image);
                }
            }
        }

        return currentImages;
    }
}