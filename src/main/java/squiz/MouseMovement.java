/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squiz;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class MouseMovement {
    
    
 static     double  yOffset = 0;
static double xOffset = 0;
    public static void getMouseMovement(Parent root, Stage stage){
        
         root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                 xOffset = event.getSceneX();
                 yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                stage.setX(event.getScreenX() - xOffset);
                
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    
    
}
}
