
package student;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class StudentDashboardController implements Initializable {

    
    // Main Pane
    @FXML private TextField mStudentName;
    @FXML private Pane mainPane ;
    @FXML private Button btnGo;
    @FXML private Button btnStart;
    @FXML private ChoiceBox cbQuizSubject;
    @FXML private ChoiceBox cbQuizTopic;
    @FXML protected AnchorPane dashboardPane;
    
    private String studentname ;
    String email;
    private  ArrayList<String> subjects;
    private ArrayList<String> topics;
    private String currentSubject;
    private StudentHelper sh;
    private int numQD;
       @FXML Label numqd;
         private ArrayList<Object> details;
       @FXML private TextField userName;
       
    @FXML Pane queryPane;
    @FXML ListView queryList;
    @FXML Button btnquery;
    
    public void recieveEmail(String email){
        this.email = email;
    //     numQD =  sh.getDoneQuizNo(email);
     getDetails();
    numqd.setText((String) details.get(2));
   }
   
    
    public void buttonGoPushed(ChoiceBox<String> cb){
           currentSubject = cb.getValue();
           topics = sh.getTopics(currentSubject);
           cbQuizTopic.getItems().clear();
           cbQuizTopic.getItems().addAll(topics);
     }
    
    public void buttonStartPushed(ActionEvent e) throws IOException, ParseException{
        
           
            
            String quizId = sh.getQuizId(currentSubject, (String) cbQuizTopic.getValue());
            System.out.println(quizId);
            //FXMLLoader loader =new FXMLLoader();
            //loader.setLocation(getClass().getResource("/student/StudentQuizPage.fxml"));
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/student/StudentQuizPage.fxml").toURI().toURL());
            Parent quizRoot = loader.load();
            Scene quizScene = new Scene(quizRoot,1080,720);
            StudentQuizPageController controller = loader.getController();
            Stage dashboard =(Stage) dashboardPane.getScene().getWindow();
            controller.receiveDetails(dashboard, quizId , this);
            Stage stage = new Stage();
            stage.setScene(quizScene);
               stage.setTitle("SQUIZ");
            stage.show();
            dashboard.hide();
        
        
    }
    
    public void buttonViewQueryPushed(ActionEvent e){
        
         queryPane.setVisible(true);
        
        ArrayList<JSONObject> queries = new ArrayList<JSONObject>();
        JSONObject temp = new JSONObject ();
        queries = sh.viewQueryAnswer(email);
        for(int i=0;i<queries.size(); i++){
                     temp = queries.get(i);
                     queryList.getItems().add("Query : "+ temp.get("query"));
                       queryList.getItems().add("Reply : "+ temp.get("reply"));
                         queryList.getItems().add(" ");
        }
        btnquery.setDisable(true);
        queryList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            
            sh = new StudentHelper();
            subjects = sh.getSubjects();
            cbQuizSubject.setValue(subjects.get(0));
            cbQuizSubject.getItems().addAll(subjects);
            btnGo.setOnAction(e -> buttonGoPushed(cbQuizSubject));
        //cbQuizSubject.setItems((ObservableList) subjects);
        
    }    

   public void getDetails() {
      System.out.println("getDEtails called "+email);
      details =  sh.getDetails(email);
      userName.setText((String) details.get(0));
   
   }
    
}
