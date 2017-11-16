# Web Image Collector

This project is the practice of creating a RESTful Web Application based on Java Server Faces (JSF) framework. 

The features of the app:
- User can upload PNG images
- User can search images by three categores: by name of image, by specific tag, by collection
- User can add/remove tags to the images.
- User can make/remove a collections of images.
- User can add images to collections

**Feature: User can upload PNG images**   
Given: the app in the initial state having no images or preuploaded images discribed in a text file   
When: user clicks on "Upload Image", chooses one or more images   
Then: those images should be uploaded all together to a table of images on the main page   

**Feature: User can search images by three categores: by name of image, by specific tag, by collection**   
Given: the app in the initial state having selected the category in the search bar  
When: user clicks on "search" button with some text typed in the search box (either name of image, tag name or a collection)  
Then: the image(s) should be displayed to the user if the result was found  

When: user clicks on "search" button with no text typed and selected any category  
Then: The Image table will reset and show all of the images  

**Feature: User can add/remove tags to the images**  
Given: the app has an image with no tags   
When: user clicks on "Option" and selects "Add tag" and types one or multiple tags as suggested in the note   
Then: The new tags should be applied to the given image   

Given: the app has an image with any tags   
When: user clicks on "Option" and selects "Remove tag" and types one tag as suggested in the note   
Then: The tag should be deleted from the given image   

**Feature: User can make/remove a collections of images**   
Given: the app has some images, but no collections   
When: user clicks on "Collections" bar and clicks on "Add a collection", types the name and hits "Add"   
Then: A new collection should be created and added to the list of collections   

Given: the app has some images and collections   
When: user clicks on arrow near any Collection and clicks on "Delete"   
Then: A collection should be deleted from the list of collections   

**Feature: User can add images to collections**  
Given: the app has some images and collections with images   
When: user clicks on arrow near any Collection and clicks on "Add Image"   
Then: The checkboxes appear near every image along with "Save" and "Cancel" buttons on the bottom. Checking checboxes and Saving will add the Images, cancelling will reset the process of adding the images to collection.   
