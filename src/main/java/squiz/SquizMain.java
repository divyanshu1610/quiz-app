/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squiz;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author Divyanshu
 */
public class SquizMain extends Application {
    public static void main(String args[]){
         launch(args);
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

     FXMLLoader loader = new FXMLLoader(new File("src/main/java/authentication/SignUp.fxml").toURI().toURL());
     Parent root = loader.load();
      //Parent root =FXMLLoader.load(getClass().getResource("../authentication/SignUp.fxml"));
      Scene scene = new Scene(root,1080,720);
      primaryStage.setScene(scene);
      primaryStage.setTitle("SQUIZ");
      primaryStage.initStyle(StageStyle.UNDECORATED);
      MouseMovement.getMouseMovement(root, primaryStage);
      primaryStage.show();
      
      }
}
