/**
 * SwitchScene abstract class 
 * @author Aditya Rangavajhala Aryan Dornala
 */
package view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * An abstract class for switching scenes 
 */
public abstract class SceneSwitcher {
/**
 * Implements functionality to switch from a current scene to another
 * @param fxmlFile is the given scene that is being switched to 
 * @param btn is the button that informs of the current Scene
 */
    public void switchScene(String fxmlFile, Button btn){
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(fxmlFile));
            AnchorPane root;
            root = (AnchorPane) loader.load();
            Stage window = (Stage) btn.getScene().getWindow();
            Scene newScene = new Scene(root, 615, 493);
            if(fxmlFile.equals("admin.fxml")){
                AdminController adminController = loader.getController();
                adminController.start();
            }
            else if(fxmlFile.equals("non_admin.fxml")){
                NonAdminController nonAdminController = loader.getController();
                nonAdminController.start();
            }
            else if(fxmlFile.equals("albumView.fxml")){
                AlbumController albumController = loader.getController();
                albumController.start();
            }
            else if(fxmlFile.equals("photoView.fxml")){
                PhotoController photoController = loader.getController();
                photoController.start();
            }
            window.setScene(newScene);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}