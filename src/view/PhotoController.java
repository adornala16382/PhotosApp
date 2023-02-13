//Aditya Rangavajhala, Aryan Dornala

/**
 * Controller class for photoView page 
 * @author Aditya Rangavajhala Aryan Dornala
 */
package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import model.*;
import javax.swing.Action;
import app.PhotosLibrary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.CurrentSession;

public class PhotoController extends SceneSwitcher {
    /**
     * Manages the Photo Display page where a User can view all the photos in an album
     */

    @FXML
    private Button backBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label selectedAlbumLabel;

    @FXML
    private ImageView currentImage;

    @FXML
    private TextField captionField;

    @FXML
    private ListView<String> displayTagsField;

    @FXML
    private TextField tagNameField;

    @FXML
    private Button removePhotoBtn;

    @FXML
    private Button addTagBtn;

    @FXML
    private TextField tagValueField;

    @FXML
    private Button removeTagBtn;

    @FXML
    private Button addCaption;

    @FXML
    private TextField deleteTagLabelField;

    @FXML
    private Button movePhotoBtn;

    @FXML
    private Button copyPhotoBtn;

    @FXML
    private Button scrollLeftBtn;

    @FXML
    private Button scrollRightBtn;

    @FXML
    private Label dateTextField;

    @FXML
    private ChoiceBox<String> albumCopyDropdown;

    @FXML
    private ChoiceBox<String> albumMoveDropdown;

    @FXML
    private TextField deleteTagNameField;

    public ObservableList<String> tagsList = FXCollections.observableArrayList();


    /**
     * @param e is the ActionEvent when the button is clicked
     * Allows user to logout of application
     */
    public void logout(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == logoutBtn) {
            switchScene("login.fxml", logoutBtn);
            CurrentSession.save();
        }
    }
    /**
     * Allows user to backtrack to previous page
     * @param e is the ActionEvent for the goBack button
     * @throws IOException
     */

    public void goBack(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == backBtn) {
            switchScene("albumView.fxml", logoutBtn);
            CurrentSession.save();
        }
    }
    /**
     * Allows user to view photos in an album as slideshow (to the right)
     * @param e is ActionEvent for button
     */

    public void scrollRight(ActionEvent e){
        Button b = (Button) e.getSource();
        if (b == scrollRightBtn) {
            if(PhotosLibrary.session.getSelectedAlbum().getPhotoSet().indexOf(PhotosLibrary.session.getSelectedPhoto()) < PhotosLibrary.session.getSelectedAlbum().getPhotoSet().size()-1){
                int index=  PhotosLibrary.session.getSelectedAlbum().getPhotoSet().indexOf(PhotosLibrary.session.getSelectedPhoto());
                PhotosLibrary.session.setSelectedPhoto(PhotosLibrary.session.getSelectedAlbum().getPhotoSet().get(index+1));
                start();
            }
            
        }

    }
    /**
     * Allows user to view photos in an album as slideshow (to the left)
     * @param e is ActionEvent for button
     */
    public void scrollLeft(ActionEvent e){
        Button b = (Button) e.getSource();
        if (b == scrollLeftBtn) {
            if(PhotosLibrary.session.getSelectedAlbum().getPhotoSet().indexOf(PhotosLibrary.session.getSelectedPhoto()) > 0){
                int index=  PhotosLibrary.session.getSelectedAlbum().getPhotoSet().indexOf(PhotosLibrary.session.getSelectedPhoto());
                PhotosLibrary.session.setSelectedPhoto(PhotosLibrary.session.getSelectedAlbum().getPhotoSet().get(index-1));
                start();
            }
            
        }
        
    }
    /**
     * Allows for Captioning/Recaptioning
     * @param e
     * @throws IOException
     */

    public void setCaption(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == addCaption) {
            PhotosLibrary.session.getSelectedPhoto().setCaption(captionField.getText());
            successAlert((Stage)b.getScene().getWindow());
            CurrentSession.save();
        }
    }
    /**
     * Reinitalizes the ListView for tags on the page based on the selected photo and its given tags 
     * @throws IOException
     */

    public void setTagView() throws IOException {
        tagsList.clear();
        Set tagvals = PhotosLibrary.session.getSelectedPhoto().getTags().keySet();
        Iterator<String> tagsIterator = tagvals.iterator();
        String insertion = "";
        while (tagsIterator.hasNext()) {
            String temp = tagsIterator.next();
            insertion = temp + ":";
            ArrayList values = PhotosLibrary.session.getSelectedPhoto().getTags().get(temp);

            insertion += String.join(", ", values);
            if (!values.isEmpty()) {
                tagsList.add(insertion);
            }
        }
        CurrentSession.save();
    }
    /**
     * Allows the user to add a tag to a photo 
     * @param e
     * @throws IOException
     */

    public void addTag(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == addTagBtn) {
            if (tagNameField.getText().isEmpty() || tagValueField.getText().isEmpty()) {
                missingTagFieldAlert((Stage)b.getScene().getWindow());
            } 
            else {

                if((PhotosLibrary.session.getSelectedPhoto().getTags().containsKey(tagNameField.getText()) && (PhotosLibrary.session.getSelectedPhoto().getTags().get(tagNameField.getText()).contains(tagValueField.getText()))) || (PhotosLibrary.session.getSelectedPhoto().getTags().containsKey(tagNameField.getText()) && !tagNameField.getText().equals("person"))){
                    duplicateTagAlert((Stage)b.getScene().getWindow());
                }
                else{
                    PhotosLibrary.session.getSelectedPhoto().setTags(tagNameField.getText().toLowerCase(), tagValueField.getText());
                    successAlert((Stage)b.getScene().getWindow());
                    CurrentSession.save();
                    setTagView();
                }

                
            }

        }
        
    }
    /**
     * Allows a user to delete a tag from a photo 
     * @param e
     * @throws IOException
     */

    public void deleteTag(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == removeTagBtn) {
            if(deleteTagNameField.getText().isEmpty()){
                missingTagFieldAlert((Stage)b.getScene().getWindow());
            }
            else{
                try{
                    PhotosLibrary.session.getSelectedPhoto().removeTag(deleteTagNameField.getText(),
                    deleteTagLabelField.getText());
                setTagView();
                successAlert((Stage)b.getScene().getWindow());
                CurrentSession.save();

                }
                catch (Exception NullPointerException){
                    missingTagFieldAlert((Stage)b.getScene().getWindow());
                }
                

            }
            
        }

    }
    /**
     * Allows for user to deletephoto from ALbum, switches scene to albumView
     * @param e
     * @throws IOException
     */

    public void deletePhoto(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == removePhotoBtn) {
            PhotosLibrary.session.getSelectedAlbum().getPhotoSet().remove(PhotosLibrary.session.getSelectedPhoto());
        }
        switchScene("albumView.fxml", b);
        CurrentSession.save();

    }
    /**
     * Allows for user to copy a photo to another album
     * @param e
     * @throws IOException
     */

    public void copyPhoto(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == copyPhotoBtn) {
            String album_name = albumCopyDropdown.getValue();
            if(album_name == null || album_name.isEmpty()){
                missingPhotoFieldAlert((Stage)b.getScene().getWindow());
            }
            else if(PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).get(album_name).getPhotoSet().contains(PhotosLibrary.session.getSelectedPhoto())==false){
                PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).get(album_name).addPhoto(PhotosLibrary.session.getSelectedPhoto());
                successAlert((Stage)b.getScene().getWindow());
                CurrentSession.save();
            }
            else{
                duplicatePhotoAlert((Stage)b.getScene().getWindow());
            }
            
        }
    }
    /**
     * Allows for user to move a photo to another album
     * @param e
     * @throws IOException
     */

    public void movePhoto(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == movePhotoBtn) {
            String album_name = albumMoveDropdown.getValue();
            Photo moved_photo = PhotosLibrary.session.getSelectedPhoto();
            if(album_name == null || album_name.isEmpty()){
                missingPhotoFieldAlert((Stage)b.getScene().getWindow());
            }
            else if(PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).get(album_name).getPhotoSet().contains(PhotosLibrary.session.getSelectedPhoto())==false){
                PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).get(album_name).addPhoto(moved_photo);
                PhotosLibrary.session.getSelectedAlbum().getPhotoSet().remove(PhotosLibrary.session.getSelectedPhoto());
                switchScene("albumView.fxml", b);
                CurrentSession.save();
            }
            else{
                duplicatePhotoAlert((Stage)b.getScene().getWindow());
            }
            
        }
    }
    /**
     * Alert for missing tag field inputs when deleting or adding tags
     * @param mainstage
     */

    private void missingTagFieldAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Missing One or More Fields");
		alert.setHeaderText(
				"Please add a field for Tag Name and/or Tag Value");
		alert.showAndWait();

	}
    /**
     * Alert for missing photo field inputs when copying or moving photos
     * @param mainstage
     */

    private void missingPhotoFieldAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Missing Photo Field");
		alert.setHeaderText(
				"Please choose a Photo to copy/move");
		alert.showAndWait();

	}

       /**
     * Alert for missing photo field inputs when copying or moving photos
     * @param mainstage
     */

    private void successAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Action successful");
		alert.setHeaderText(
				"Action completed");
		alert.showAndWait();

	}
    
    /**
     * Notifies user of duplicate tags being added
     * @param mainstage
     */

    private void duplicateTagAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Duplicate Tag");
		alert.setHeaderText(
				"Tag Already Exists!");
		alert.showAndWait();

	}
    /**
     * Notifies User of duplicate photos being copied or moved
     * @param mainstage
     */
    private void duplicatePhotoAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Duplicate Photo");
		alert.setHeaderText(
				"Photo Already Exists in Album");
		alert.showAndWait();

	}


    /**
     * Start method for the photoView that initalizes all text fields and ListView items to display per photo specifications. 
     */
    
    public void start() {
        Image graphic = new Image(PhotosLibrary.session.getSelectedPhoto().getPath());
        selectedAlbumLabel.setText(PhotosLibrary.session.getSelectedAlbum().getName());
        try {
            setTagView();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dateTextField.setText(PhotosLibrary.session.getSelectedPhoto().dateToString());
        displayTagsField.setItems(tagsList);
        currentImage.setImage(graphic);
        captionField.setText(PhotosLibrary.session.getSelectedPhoto().getCaption());
        currentImage.setFitWidth(394);
        currentImage.setFitHeight(358);
        ArrayList<String> albums =PhotosLibrary.session.getUserAlbums();
        albums.remove(PhotosLibrary.session.getSelectedAlbum().getName());
        albumCopyDropdown.setItems(FXCollections.observableArrayList(albums));
        albumMoveDropdown.setItems(FXCollections.observableArrayList(albums));

        



        // printing tags to listView

    }

}
