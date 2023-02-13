
/**
 * Controller class for Nonadmin page 
 * @author Aditya Rangavajhala Aryan Dornala
 */
package view;

import java.util.HashMap;
import java.util.Map;

import app.PhotosLibrary;
import model.*;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Album;
import model.CurrentSession;
/**
 * Controller for the NonAdmin Users
 */
public class NonAdminController extends SceneSwitcher{

    @FXML
	private Button logoutBtn;

    @FXML
	private Button openAlbumBtn;

    @FXML
	private Button deleteAlbumBtn;

    @FXML
	private Button createAlbumBtn;

    @FXML
	private Button renameAlbumBtn;

    @FXML
	private TextField createAlbumField;

    @FXML
	private TextField renameAlbumField;

    @FXML
	private Label createErrorLabel;
    
    @FXML
	private Label renameErrorLabel;

    @FXML
	private ListView<Album> albumView;

    @FXML
	private Label selectedAlbumLabel;

    public ObservableList<Album> albumList;
/**
 * Allows user to logout
 * @param e 
 */
    public void logout(ActionEvent e){
        Button b = (Button) e.getSource();
        if(b == logoutBtn){
            switchScene("login.fxml", logoutBtn);
        }
    }
/**
 * Allows for user to open an album after pressing the "Open Album" button
 * @param e
 */
    public void openAlbum(ActionEvent e){
        Button b = (Button) e.getSource();
        if(b == openAlbumBtn && albumView.getSelectionModel().getSelectedItem()!=null){
            switchScene("albumView.fxml", logoutBtn);
        }       
    }
    /**
     * Allows user to create an Album by inputting a string in the textField
     * @param e
     * @throws IOException
     */

    public void createAlbum(ActionEvent e) throws IOException{
        Button b = (Button) e.getSource();
        if(b == createAlbumBtn){
            String input = createAlbumField.getText();
            if(!PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).containsKey(input)){
                Album newAlbum = new Album(input, PhotosLibrary.session.getCurrentUser());
                PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).put(input, newAlbum);
                CurrentSession.save();
                start();
            }
            else{
                createErrorLabel.setText("Album already exists");
            }
        }
    }
    /**
     * Allows user to rename an album by inputting a string in the text field
     * @param e
     * @throws IOException
     */

    public void renameAlbum(ActionEvent e) throws IOException{
        Button b = (Button) e.getSource();
        if(b == renameAlbumBtn && albumView.getSelectionModel().getSelectedItem()!=null){
            String input = renameAlbumField.getText();
            if(PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser())!=null && !PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).containsKey(input)){
                String selectedAlbum = PhotosLibrary.session.getSelectedAlbum().getName();
                PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).get(selectedAlbum).setName(input);
                Album curAlbum = PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).remove(selectedAlbum);
                PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).put(input, curAlbum);
                PhotosLibrary.session.setSelectedAlbum(curAlbum);
                CurrentSession.save();
                start();
            }
            else if(PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser())!=null){
                renameErrorLabel.setText("Album already exists");
            }
        }
    }

    /**
     * Allows user to delete an Album by selecting an album to be deleted
     * @param e
     * @throws IOException
     */
    public void deleteAlbum(ActionEvent e) throws IOException{
        Button b = (Button) e.getSource();
        if(b == deleteAlbumBtn && albumView.getSelectionModel().getSelectedItem()!=null){  
            String selectedAlbum = PhotosLibrary.session.getSelectedAlbum().getName();
            if(PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser())!=null && PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).containsKey(selectedAlbum)){
                PhotosLibrary.session.getUsersToAlbums().get(PhotosLibrary.session.getCurrentUser()).remove(selectedAlbum);
                CurrentSession.save();
                start();
            }
        }   
    }
    /**
     * Sets the global selected album to the album that a User is exploring
     */

	public void setSelectedAlbum() {
		Album selected = albumView.getSelectionModel().getSelectedItem();
		selectedAlbumLabel.setText(selected==null? "N/A" : selected.getName());
        PhotosLibrary.session.setSelectedAlbum(selected);
	}
/**
 * Initalizes all text fields and varaibles to have proper display funcationality on the app
 */
    public void start() {
        albumList = FXCollections.observableArrayList();

        createErrorLabel.setText("");
        createAlbumField.setText("");
        renameErrorLabel.setText("");
        renameAlbumField.setText("");
        String curUser = PhotosLibrary.session.getCurrentUser();
        Map<String, Album> albums; 
        if(PhotosLibrary.session.getUsersToAlbums().size() > 0){
            albums = PhotosLibrary.session.getUsersToAlbums().get(curUser);
            if(albums != null && !albums.isEmpty()){
                for(String albumName: albums.keySet()){
                    albumList.add(albums.get(albumName));
                }
            }
        }
        albumView.setItems(albumList);
        albumView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> setSelectedAlbum());
	
		if (!albumView.getItems().isEmpty()) {
			albumView.getSelectionModel().select(0);
            PhotosLibrary.session.setSelectedAlbum(albumView.getSelectionModel().getSelectedItem());
		}
	}

}