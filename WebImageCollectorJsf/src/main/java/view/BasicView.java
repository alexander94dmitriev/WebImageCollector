package view;

import controller.ImageService;
import model.Collection;
import model.Image;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ManagedBean(name="BasicView")
@SessionScoped
public class BasicView implements Serializable {

    @ManagedProperty("#{imageService}")
    private ImageService imageService;
    private List<Collection> allCollections;
    private List<String> allTags;
    private List<Image> currentImages;
    private List<Image> allImages;
    private List<String> tagsList;

    private String addTag;
    private String removeTag;
    private Image selectedImage;

    private String searchString;
    private String searchStringSelected;
    private List<String> searchStringListSelected;

    private String newCollection;
    private String removeCollection;
    private List<String> collectionNames;
    private Collection selectedCollection;
    private Boolean imageToCollectionMode;

    @PostConstruct
    public void init() {
        try {
            currentImages = imageService.initializeImageList();
            setAllImages(currentImages);
            searchStringListSelected = Collections.unmodifiableList(Arrays.asList(new String[] {"By tags", "By collections", "By name"}));
            allTags = imageService.initializeAllTagsList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void uploadFile(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        imageService.uploadImage(file);
        try {
            currentImages = imageService.initializeImageList();
            FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchAction(ActionEvent actionEvent) {
        currentImages = imageService.searchImages(searchString, searchStringSelected, allCollections);
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
        try {
            setAllImages(imageService.initializeImageList());
            allTags = imageService.initializeAllTagsList();


            FacesMessage message = new FacesMessage("New tags were added to selected image");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            e.printStackTrace();
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
        try {
            setAllImages(imageService.initializeImageList());
            allTags = imageService.initializeAllTagsList();

            FacesMessage message = new FacesMessage("New tags were deleted from selected image");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            e.printStackTrace();
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

    public List<String> getSearchStringListSelected() {
        return searchStringListSelected;
    }

    public List<Collection> getAllCollections() {
        return allCollections;
    }

    public void setAllCollections(List<Collection> allCollections) {
        this.allCollections = allCollections;
    }

    public List<String> getAllTags() {
        return allTags;
    }

    public void setAllTags(List<String> allTags) {
        this.allTags = allTags;
    }

    public List<Image> getAllImages() {
        return allImages;
    }

    public void setAllImages(List<Image> allImages) {
        this.allImages = allImages;
    }

    public String getNewCollection() {
        return newCollection;
    }

    public void setNewCollection(String newCollection) {
        this.newCollection = newCollection;
    }

    public void addNewCollection() {
        if (allCollections == null) {
            allCollections = new ArrayList<>();
        }

        allCollections.add(new Collection(newCollection));

        FacesMessage message = new FacesMessage("Added new collection: " + newCollection);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void removeCollection() {
        for (Collection collection : allCollections) {
            if (collection.getName().equals(removeCollection)) {
                allCollections.remove(collection);

                FacesMessage message = new FacesMessage("Removed the collection: " + removeCollection);
                FacesContext.getCurrentInstance().addMessage(null, message);

                return;
            }
        }
    }

    public Boolean getImageToCollectionMode() {
        return imageToCollectionMode;
    }

    public void setImageToCollectionMode(Boolean imageToCollectionMode) {
        this.imageToCollectionMode = imageToCollectionMode;
    }

    public void deleteSelectedCollection(Collection collection) {
        allCollections.remove(collection);
    }

    public void addImagetoSelectedList(Image image) {
        List<Image> images = selectedCollection.getImages();

        if (images == null || !images.contains(image)) {
            selectedCollection.addImage(image);
        } else {
            selectedCollection.removeImage(image);
        }
    }

    public Collection getSelectedCollection() {
        return selectedCollection;
    }

    public void setSelectedCollection(Collection selectedCollection) {
        this.selectedCollection = selectedCollection;
    }

    public void addNewCollectionToList() {
        for (Collection collection : allCollections) {
            if (collection.getName().equals(selectedCollection.getName())) {
                allCollections.remove(collection);
                allCollections.add(selectedCollection);
                break;
            }
        }
    }

    public void cancelCollectionUpdate() {
        selectedCollection = null;
    }

    public String getRemoveCollection() {
        return removeCollection;
    }

    public void setRemoveCollection(String removeCollection) {
        this.removeCollection = removeCollection;
    }

    public List<String> getCollectionNames() {
        List<String> result = new ArrayList<>();

        if (allCollections == null)
            return result;

        for (Collection collection : allCollections) {
            result.add(collection.getName());
        }

        return result;
    }

}
