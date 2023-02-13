/**
 * Controller class for Login page 
 * @author Aditya Rangavajhala Aryan Dornala
 */

package view;

import java.io.IOException;

import app.PhotosLibrary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.CurrentSession;
/**
 * Controller for the login page
 */
public class LoginController extends SceneSwitcher {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;
/**
 * Functionality for a user to login given a unique username,
 * Sends user to different page based on if they are an admin or not
 * @param e
 */
    public void login(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == loginBtn) {
            String username = usernameField.getText().strip();
            if (username.equals("admin")) {
                PhotosLibrary.session.setCurrentUser("admin");
                switchScene("admin.fxml", loginBtn);
            } else if (PhotosLibrary.session.getUsersToAlbums() != null
                    && PhotosLibrary.session.getUsersToAlbums().containsKey(username)) {
                errorLabel.setText("");
                PhotosLibrary.session.setCurrentUser(username);
                switchScene("non_admin.fxml", loginBtn);
            } else {
                errorLabel.setText("User does not exist");
            }
        }
    }
/**
 * Start method
 * @throws IOException
 */
    public void start() throws IOException {

    }

}