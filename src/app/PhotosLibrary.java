//Aditya Rangavajhala, Aryan Dornala
/**
 * Main Java class for application 
 * @author Aditya Rangavajhala Aryan Dornala
 */
package app;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;
import model.CurrentSession;
import view.*;
/**
 * Main class that runs the application
 */
public class PhotosLibrary extends Application {

    public static CurrentSession session;
/**
 * Starts the application by initalizing all values and stock images
 */
	@Override
	public void start(Stage primaryStage) throws Exception {

        session = new CurrentSession();
		//session.writeApp(session);
        session = session.readApp();


		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(
				getClass().getResource("/view/login.fxml"));

		// load the fxml
		AnchorPane root = (AnchorPane) loader.load();

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		LoginController loginController = loader.getController();
		loginController.start();

		Scene scene = new Scene(root, 615, 493);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
/**
 * Main method
 * @param args
 * @throws ClassNotFoundException
 * @throws IOException
 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		launch(args);
	}

}
