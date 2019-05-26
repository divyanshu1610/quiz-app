package teacher;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.*;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author Kushagra
 */

public class TeacherDashboardController implements Initializable {
    
       private String email;
       @FXML protected AnchorPane dashboardPane;
       private TeacherHelper th;
       private ArrayList<Object> details;
       @FXML private TextField userName;
       private int numQH;
       @FXML Label numqh;
       @FXML Pane queryPane;
       @FXML ListView queryList;
       @FXML Button btnquery;
       
    
    public void receiveEmail(String email){
     this.email = email;
     System.out.println(email);
     getDetails();
     numqh.setText(Integer.toString(numQH));
      
    }
    
    public void hostButtonPushed(ActionEvent e)throws IOException{
           Stage dashboard = (Stage)dashboardPane.getScene().getWindow();
           FXMLLoader loader = new FXMLLoader(new File("src/main/java/teacher/TeacherHost.fxml").toURI().toURL());
            //FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getResource("));
            Parent hostParent = loader.load();
            Scene hostScene = new Scene(hostParent);
            TeacherHostController controller = loader.getController();
            controller.receiveStage(dashboard, (String) details.get(0), email, this);
           Stage hostWindow = new Stage();
            
            dashboard.hide();
            //System.out.println("hidden");
           hostWindow.setScene(hostScene);
           hostWindow.show();
           //dashboard.show();
        
    }
    
    public void queryButtonPushed(ActionEvent e)throws IOException{
        queryPane.setVisible(true);
  
        ArrayList<String> queries = new ArrayList<String>();
        queries = th.getQuery(details.get(0).toString());
        for(int i=0;i<queries.size(); i++){
                     queryList.getItems().add(queries.get(i));
        }
        btnquery.setDisable(true);
        queryList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        
        
    }
    
    public void replyQuery(){
        
        
    }
    
    public void logoutButtonPushed(ActionEvent e)throws IOException{
        th.close();



        FXMLLoader loader = new FXMLLoader(new File("src/main/java/authentication/SignUp.fxml").toURI().toURL());
        Parent hostParent = loader.load();  
        //Parent hostParent = FXMLLoader.load(getClass().getResource("/authentication/SignUp.fxml"));
        Scene hostScene = new Scene(hostParent);
        
        Stage hostWindow = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        hostWindow.setScene(hostScene);
        hostWindow.show();
    }
    
    public void getDetails(){
        System.out.println("getDEtails called "+email);
      details =  th.getDetails(email);
      userName.setText((String) details.get(0));
      numQH =  th.getHostedQuizNo(email);
      }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        th = new TeacherHelper();
        //getDetails();
      
    }    
}
