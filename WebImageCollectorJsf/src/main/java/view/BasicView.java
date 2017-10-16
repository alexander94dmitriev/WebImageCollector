package view;

import controller.ImageService;
import model.Image;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="BasicView")
@SessionScoped
public class BasicView implements Serializable {

    @ManagedProperty("#{imageService}")
    private ImageService imageService;
    private List<Image> currentImages;
    private List<Image> allImages;
    private List<String> tagsList;
    private String addTag;
    private String removeTag;
    private Image selectedImage;
    private String searchString;
    private String searchStringSelected;

    @PostConstruct
    public void init() {
        try {
            currentImages = imageService.initializeImageList();
            allImages = currentImages;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        imageService.uploadImage(file);
        try {
            currentImages = imageService.initializeImageList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchAction(ActionEvent actionEvent) {
        if(searchStringSelected.equals("tags"))
        {
            currentImages = new ArrayList<>();
            for(Image image: allImages)
            {
                if(image.getTags().contains(searchString))
                {
                    currentImages.add(image);
                }
            }
        }
        else if(searchStringSelected.equals("collections"))
        {

        }
        else if (searchStringSelected.equals("name"))
        {
            currentImages = allImages;
        }
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void handleKeyEvent () {
        this.searchString = this.searchString.toUpperCase();
    }

    public ImageService getImageService() {
        return imageService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public List<Image> getCurrentImages() {
        return currentImages;
    }

    public void setCurrentImages(List<Image> currentImages) {
        this.currentImages = currentImages;
    }

    public Image getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void deleteSelectedImage(Image image) {
        currentImages = imageService.deleteSelectedImage(image);
    }

    public void addTagToSelectedImage() {
        for(Image image: currentImages)
        {
            if(selectedImage.getName().equals(image.getName()))
            {
                String[] split = addTag.split(",");
                for(int i = 0;  i < split.length; ++i)
                {
                    if(split[i].isEmpty())
                        continue;

                    image.addNewTag(split[i]);
                    imageService.addTagToImage(image, split[i]);
                }
            }
        }
    }

    public void removeTagFromSelectedImage() {
        for(Image image: currentImages)
        {
            if(selectedImage.getName().equals(image.getName()))
            {
                String[] split = addTag.split(",");
                for(int i = 0;  i < split.length; ++i)
                {
                    if(split[i].isEmpty())
                        continue;

                    image.removeTag(split[i]);
                    imageService.removeTagFromImage(image, split[i]);
                }
            }
        }
    }

    public String getSearchStringSelected() {
        return searchStringSelected;
    }

    public void setSearchStringSelected(String searchStringSelected) {
        if(searchStringSelected.isEmpty())
            return;
        this.searchStringSelected = searchStringSelected;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public String getAddTag() {
        return addTag;
    }

    public void setAddTag(String addTag) {
        if(addTag.isEmpty())
            return;
        this.addTag = addTag;
    }

    public String getRemoveTag() {
        return removeTag;
    }

    public void setRemoveTag(String removeTag) {
        this.removeTag = removeTag;
    }
}
