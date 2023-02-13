//Aditya Rangavajhala, Aryan Dornala
/**
 * Controller class for Admin page 
 * @author Aditya Rangavajhala Aryan Dornala
 */
package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.Set;

import app.PhotosLibrary;
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
import model.Photo;

/**
 * Controller for the Admin page of an application 
 */
public class AdminController extends SceneSwitcher{

    @FXML
	private Button logoutBtn;

    @FXML
	private Button createUserBtn;
    
    @FXML
	private TextField usernameField;

    @FXML
	private ListView<String> userView;

    @FXML
	private Label selectedUserLabel;

    @FXML
	private Label errorLabel;

    public ObservableList<String> userList;
/**
 * Allows for logout functionality of a user
 * @param e
 */
    public void logout(ActionEvent e){
        Button b = (Button) e.getSource();
        if(b == logoutBtn){
            switchScene("login.fxml", logoutBtn);
        }
    }
    /**
     * Allows for an admin to create a user
     * @param e
     * @throws IOException
     */

    public void createUser(ActionEvent e) throws IOException{
        Button b = (Button) e.getSource();
        if(b == createUserBtn){
            String inputText = usernameField.getText();
            if(!inputText.equals("")){
                if(!PhotosLibrary.session.getUsersToAlbums().containsKey(inputText)){
                    PhotosLibrary.session.getUsersToAlbums().put(inputText, new HashMap<String, Album>());
                    CurrentSession.save();
                    start();
                }
                else{
                    errorLabel.setText("User exists");
                }
            }
        }
    }

    /**
     * Allows for an admin to delete a user
     * @param e
     * @throws IOException
     */
    public void deleteUser(ActionEvent e) throws IOException{
        String selectedUser = userView.getSelectionModel().getSelectedItem();
        PhotosLibrary.session.getUsersToAlbums().remove(selectedUser);
        CurrentSession.save();
        start();
    }
    /**
     * Sets the selected user 
     */

    public void setSelectedUser() {
		String selected = userView.getSelectionModel().getSelectedItem();
		selectedUserLabel.setText(selected==null? "N/A" : selected);
	}
/**
 * Initalizes all textfields and Listview values to display on application 
 */
    public void start() {
		errorLabel.setText("");
        usernameField.setText("");
        userList = FXCollections.observableArrayList();
        Set <String> allUsers = null;
        if(PhotosLibrary.session.getUsersToAlbums()!=null && PhotosLibrary.session.getUsersToAlbums().size()>0){
            allUsers = PhotosLibrary.session.getUsersToAlbums().keySet();
            for(String username: allUsers){
                userList.add(username);
            }
        }
        userView.setItems(userList);

        userView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> setSelectedUser());
	
		if (!userView.getItems().isEmpty()) {
			userView.getSelectionModel().select(0);
		}

	}

}