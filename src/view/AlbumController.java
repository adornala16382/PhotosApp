//Aditya Rangavajhala, Aryan Dornala
package view;

/** Controller class for Album page 
* @author Aditya Rangavajhala Aryan Dornala
*/

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Album;
import model.CurrentSession;
import model.Photo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.time.*;

import app.PhotosLibrary;
/**
 * Creates a page that displays photos for all the alnbums
 */
public class AlbumController extends SceneSwitcher {

    @FXML
    private Button logoutBtn;

    @FXML
    private Label selectedCaptionLabel;

    @FXML
    private TextField tagFilter;

    @FXML
    private TextField createAlbumField;

    @FXML
    private TextField tagOneField;

    @FXML
    private TextField tagTwoField;

    @FXML
    private Button backBtn;

    @FXML
    private Button addPhotoBtn;

    @FXML
    private Button openPhotoBtn;

    @FXML
    private Button removePhotoBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private Button createAlbumBtn;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox<String> tagOneDropdown;

    @FXML
    private ChoiceBox<String> tagTwoDropdown;

    @FXML
    private ChoiceBox<String> andOrDropdown;

    @FXML
    private Label selectedAlbumLabel;

    @FXML
    private Label selectedPhotoLabel;

    @FXML
    ListView<Photo> photos;

    public ObservableList<Photo> photosList = FXCollections.observableArrayList();

    /**Create logout functionality
 * @param e
 */

    public void logout(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == logoutBtn) {
            switchScene("login.fxml", logoutBtn);
        }
    }
      /**Create go back functionality
 * @param e
 */

    public void goBack(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == backBtn) {
            switchScene("non_admin.fxml", logoutBtn);
        }
    }

      /**Create opening photo functionality
 * @param e
 */

    public void openPhoto(ActionEvent e){
        Button b = (Button) e.getSource();
        if (b == openPhotoBtn && photos.getSelectionModel().getSelectedItem()!=null) {
            switchScene("photoView.fxml", openPhotoBtn);
        }
    }
  /**Create remove Photo functionality
 * @param e
 */
    public void removePhoto(ActionEvent e) throws IOException{
        Button b = (Button) e.getSource();
        if (b == removePhotoBtn && photos.getSelectionModel().getSelectedItem()!=null) {
            Photo selectedPhoto = photos.getSelectionModel().getSelectedItem();
            PhotosLibrary.session.getSelectedAlbum().getPhotoSet().remove(selectedPhoto);
            CurrentSession.save();
            start();
        }
    }
  /**Create add Photo functionality
 * @param e
 */
    public void addPhoto(ActionEvent e) throws IOException {
        Button b = (Button) e.getSource();
        if (b == addPhotoBtn) {
            try{
                Stage mainStage = (Stage) b.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

                File selectedFile = fileChooser.showOpenDialog(mainStage);
                Boolean duplicateError = false;
                for(Photo p: PhotosLibrary.session.getSelectedAlbum().getPhotoSet()){
                    if(p.getPath().equals("file:"+selectedFile.getPath())){
                        //display error Alert
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.initOwner((Stage)b.getScene().getWindow());
                        alert.setTitle("Duplicate Photo");
                        alert.setHeaderText(
                                "Photo Already Exists in Album");
                        alert.showAndWait();
                        duplicateError = true;
                        break;
                    }
                }
                if(!duplicateError){
                    Date d = new Date(selectedFile.lastModified());
                    Photo insert = new Photo(selectedFile.getName(), "file:"+selectedFile.getPath(), d);
                    insert.setName(selectedFile.getName());
                    PhotosLibrary.session.getSelectedAlbum().addPhoto(insert);
                    CurrentSession.save();
                    start();
                }
            } catch(Exception ex){   }
        }
    }
      /**Create search functionality
 * @param e
 */

    public void search(ActionEvent e){
        Button b = (Button) e.getSource();
        if (b == searchBtn) {
            if((startDatePicker.getValue()!=null || endDatePicker.getValue()!=null) && (tagOneDropdown.getValue()!=null || tagTwoDropdown.getValue()!=null || !tagOneField.getText().equals("") || !tagTwoField.getText().equals(""))){
                disjointAlert((Stage)b.getScene().getWindow());
            }
            else{
                photosList.clear();
                ZoneId defaultZoneId = ZoneId.systemDefault();
                //filter by dates
                if((startDatePicker.getValue()!=null && endDatePicker.getValue()!=null)){
                    Date startdate = Date.from(startDatePicker.getValue().atStartOfDay(defaultZoneId).toInstant());
                    Date enddate = Date.from(endDatePicker.getValue().atStartOfDay(defaultZoneId).toInstant());
                    for(Photo p: PhotosLibrary.session.getSelectedAlbum().getPhotoSet()){
                        
                        Date newDate = new Date();
                        newDate = p.getDate();
                        newDate.setHours(0);
                        newDate.setMinutes(0);
                        newDate.setSeconds(0);

                        if((newDate.compareTo(startdate)>=0) && (newDate.compareTo(enddate)<=0)){
                            photosList.add(p);
                        }
                    }
                }
                //filter by two tags
                else if(andOrDropdown.getValue()!=null && (tagOneDropdown.getValue()!=null && tagTwoDropdown.getValue()!=null && tagOneField.getText()!=null && tagTwoField.getText()!=null)){
                        String operation = andOrDropdown.getValue(); 
                        ArrayList<Photo> photoList = PhotosLibrary.session.getSelectedAlbum().getPhotoSet();
                        if(operation.equals("AND")){
            
                            for(int i =0;i<photoList.size();i++){
                                if((photoList.get(i).getTags().get(tagOneDropdown.getValue())!=null && photoList.get(i).getTags().get(tagOneDropdown.getValue()).contains(tagOneField.getText())) && (photoList.get(i).getTags().get(tagTwoDropdown.getValue())!=null && photoList.get(i).getTags().get(tagTwoDropdown.getValue()).contains(tagTwoField.getText()))){
                                    photoList.add(photoList.get(i));
                                }
                            }
                
                        }
                        else if(operation.equals("OR")){
                            for(int i =0;i<photoList.size();i++){
                                if((photoList.get(i).getTags().get(tagOneDropdown.getValue())!=null && photoList.get(i).getTags().get(tagOneDropdown.getValue()).contains(tagOneField.getText())) || (photoList.get(i).getTags().get(tagTwoDropdown.getValue())!=null && photoList.get(i).getTags().get(tagTwoDropdown.getValue()).contains(tagTwoField.getText()))){
                                    photosList.add(photoList.get(i));
                                }
                            }
                        }
                }
                //filter by tagOne
                else if(tagOneDropdown.getValue()!=null && tagOneField.getText()!=null && (tagTwoDropdown.getValue()==null) && tagTwoField.getText().equals("")){
                    ArrayList<Photo> photoList = PhotosLibrary.session.getSelectedAlbum().getPhotoSet();
                    for(int i =0;i<photoList.size();i++){
                        if((photoList.get(i).getTags().get(tagOneDropdown.getValue())!=null && photoList.get(i).getTags().get(tagOneDropdown.getValue()).contains(tagOneField.getText()))){
                            photosList.add(photoList.get(i));
                        }
                    }
                }
                
                else if(tagTwoDropdown.getValue()!=null && tagTwoField.getText()!=null && (tagOneDropdown.getValue()==null) && tagOneField.getText().equals("")){
                    ArrayList<Photo> photoList = PhotosLibrary.session.getSelectedAlbum().getPhotoSet();
                    for(int i =0;i<photoList.size();i++){
                        if((photoList.get(i).getTags().get(tagTwoDropdown.getValue())!=null && photoList.get(i).getTags().get(tagTwoDropdown.getValue()).contains(tagTwoField.getText()))){
                            photosList.add(photoList.get(i));
                        }
                    }
                }
                //filter by tagTwo
                else{
                    missingFieldAlert((Stage)b.getScene().getWindow());
                }
            }
        }
    }
  /**Create disjoint alert
 * @param mainstage
 */
    private void disjointAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Unable to Filter");
		alert.setHeaderText(
				"Please filter with either date range or tag-values");
		alert.showAndWait();
	}

    /**Create missingFieldAlert
 * @param mainstage
 */
    private void missingFieldAlert(Stage mainstage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Unable to Filter");
		alert.setHeaderText(
				"Missing Field");
		alert.showAndWait();

	}
/**
 * create an album
 * @param e
 */
    public void createAlbum(ActionEvent e){
        Button b = (Button) e.getSource();
        if (b == createAlbumBtn) {
            Album newAlbum = new Album(createAlbumField.getText(),PhotosLibrary.session.getCurrentUser());
            for(int i =0; i<photosList.size();i++){
                newAlbum.addPhoto(photosList.get(i));
            }
            PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).put(createAlbumField.getText(),newAlbum);
            }
    }
    /**
     * set labels on the display
     */

    public void setLabels() {
		Photo selected = photos.getSelectionModel().getSelectedItem();
        PhotosLibrary.session.setSelectedPhoto(selected);
		selectedCaptionLabel.setText(selected==null || selected.getCaption()==null? "N/A" : selected.getCaption());
        selectedPhotoLabel.setText(selected==null? "N/A" : selected.getName());
	}
/**
 * Initalizes all variables
 */
    public void start() {
        photosList.clear();
        ArrayList<String> andOr = new ArrayList<>();
        andOr.add("");
        andOr.add("AND");
        andOr.add("OR");
        andOrDropdown.setItems(FXCollections.observableArrayList(andOr));

        ArrayList<String> tagList = new ArrayList<>();
        tagList.add("");
        for(Photo p: PhotosLibrary.session.getSelectedAlbum().getPhotoSet()){
            for(String key: p.getTags().keySet()){
                if(!tagList.contains(key)){
                    tagList.add(key);
                }
            }
        }
        tagOneDropdown.setItems(FXCollections.observableArrayList(tagList));
        tagTwoDropdown.setItems(FXCollections.observableArrayList(tagList));

        Album selectedAlbum = PhotosLibrary.session.getSelectedAlbum();
        selectedAlbumLabel.setText(selectedAlbum == null ? "N/A" : selectedAlbum.getName());

        for(Photo p: PhotosLibrary.session.getSelectedAlbum().getPhotoSet()){
            photosList.add(p);
        }
        photos.setItems(photosList);
        photos.setCellFactory(param -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Photo name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Image graphic = new Image(name.getPath());
                    imageView.setFitWidth(70);
                    imageView.setFitHeight(70);

                    imageView.setImage(graphic);
                    setText(name.toString());
                    setGraphic(imageView);
                }
            }
        });

        // photos.setOnMouseClicked(new EventHandler<MouseEvent>() {
        //     @Override
        //     public void handle(MouseEvent mouseEvent) {
        //         if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
        //             if(mouseEvent.getClickCount() == 2){
        //                 System.out.println("DOUBLE CLICKED");
        //                 switchScene("photoView.fxml", addPhoto);

        //             }
        //         }
        //     }
        // });

        photos.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> setLabels());
		if (!photos.getItems().isEmpty()) {
			photos.getSelectionModel().select(0);
            PhotosLibrary.session.setSelectedPhoto(photos.getSelectionModel().getSelectedItem());
		}
    }
}