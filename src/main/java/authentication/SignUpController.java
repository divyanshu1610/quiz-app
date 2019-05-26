/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authentication;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import static resources.Constant.RESULT_OK;
import student.StudentDashboardController;
import teacher.TeacherDashboardController;


public class SignUpController implements Initializable {
    
    private Authentication auth;
    private String user="" ;  
    private int resultCode;
    //Anchor pane
 @FXML private AnchorPane ap;
@FXML AnchorPane signUp;
@FXML AnchorPane signIn;
@FXML Button abtnSignin ;
@FXML Button abtnSignup;
@FXML ImageView close;

//SignUp
@FXML Button btnSignup ;
@FXML TextField firstName;
@FXML TextField lastName;
@FXML TextField suEmail;
@FXML PasswordField suPassword;
@FXML CheckBox cbsuTeacher;
@FXML CheckBox cbsuStudent;

//Sign in
@FXML Button btnSignin ;
@FXML TextField siEmail;
@FXML PasswordField siPassword;
@FXML CheckBox cbsiTeacher;
@FXML CheckBox cbsiStudent;

    /**
     *AnchorPane signIn button FOR UI CHANGES
     */
    public void signInCalled(){
    abtnSignin.setStyle("-fx-background-color:  #aa00ff; -fx-font: 25px System ; -fx-font-weight :bold;");
    abtnSignup.setStyle("-fx-background-color:    #6a1b9a;  -fx-font: 15px System ;");
    signUp.setVisible(false);
        signIn.setVisible(true);
        
}

    /**
     *FOR UI CHANGES
     */
    public void signUpCalled(){
    
        abtnSignup.setStyle("-fx-background-color:  #aa00ff;-fx-font: 25px System ;-fx-font-weight :bold; ");
        abtnSignin.setStyle("-fx-background-color:    #6a1b9a; -fx-font: 15px System;");
        signUp.setVisible(true);
        signIn.setVisible(false);
        
}
    
    
    
    /**
     *Register the user
     */
    public void registerNewUser(){
        
       auth = new Authentication();
        System.out.println(user);
        try {
            resultCode = auth.signUpUser(firstName.getText().trim(), lastName.getText().trim(), suEmail.getText().trim(), suPassword.getText(),user );
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (resultCode==RESULT_OK){
             // IF user signup coorectly all went well
             System.out.println("Sign UP DONE!!!!");
             resultCode=0;
             signInCalled();
             
        }else{
            //  ERROR IN SIGN UP 
            System.out.println("ERROR IN SIGNUP  registerNewUser!!!!");
        }
        auth.close();
        
}

    
    
    /**
     *
     * @param e
     * Sign In user
     */
    public void signInUser(ActionEvent e) throws IOException {
   
           auth = new Authentication();
        try {
            resultCode=  auth.signInUser(siEmail.getText().trim(),  siPassword.getText(), user );
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
          System.out.println(resultCode+user);
           if(resultCode==RESULT_OK && user.equals("student")){
                        /// System.out.println(resultCode);

                        FXMLLoader loader = new FXMLLoader(new File("src/main/java/student/StudentDashboard.fxml").toURI().toURL());
                        //FXMLLoader loader = new FXMLLoader();
                        //loader.setLocation(getClass().getResource("../student/StudentDashboard.fxml"));
                        Parent studentRoot = loader.load();
                        Scene scene = new Scene(studentRoot,1080, 720);     
                        StudentDashboardController sDashboard = loader.getController();
                        sDashboard.recieveEmail(siEmail.getText().trim());
                       //Stage stage =(Stage)((Node)e.getSource()).getScene().getWindow();
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("Student Dashboard");
                        stage.show();
                        closeUI();
                        auth.close();
           }
           else if(resultCode == RESULT_OK && user.equals("teacher")){
                        System.out.println("done");

                        FXMLLoader loader = new FXMLLoader(new File("src/main/java/teacher/TeacherDashboard.fxml").toURI().toURL());
                        
                        //FXMLLoader loader = new FXMLLoader();
                        //loader.setLocation(getClass().getResource("../teacher/TeacherDashboard.fxml"));
                          System.out.println("done");
                        Parent teacherRoot = loader.load();
                          System.out.println("done");
                        Scene scene = new Scene(teacherRoot, 1080, 720);     
                          System.out.println("done");
                        TeacherDashboardController tDashboard = loader.getController();
                          System.out.println("done");
                        tDashboard.receiveEmail(siEmail.getText().trim());
                          System.out.println("done");
                       //Stage stage =(Stage)((Node)e.getSource()).getScene().getWindow();
                         System.out.println("done");
                        Stage stage = new Stage();
                        stage.setScene(scene);
                           stage.setTitle("Teacher Dashboard");
                        stage.show();
                        closeUI();
                        auth.close();
               }
 }
        
    
    
    
  /**
    *Closes the Stage
    */
    public void closeUI(){
    
Stage stage = (Stage) ap.getScene().getWindow();
stage.close();
}

    /**
     *
     * @param cb
     * @param b
     */
    public void buttonControl(CheckBox cb, Button b){
    
    if(cb == cbsuTeacher ||  cb==cbsiTeacher)
        user="student";
    else
        user="teacher";
    
    cb.setSelected(false);
    b.setDisable(false);
    if(cbsuTeacher.isSelected()&& cbsuStudent.isSelected()&& cbsiTeacher.isSelected()&&cbsiStudent.isSelected())
    b.setDisable(true);
    
    
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
             //auth = new Authentication();
              cbsuTeacher.setOnAction(e -> buttonControl(cbsuStudent , btnSignup));
             cbsuStudent.setOnAction(e -> buttonControl(cbsuTeacher , btnSignup));
             cbsiTeacher.setOnAction(e -> buttonControl(cbsiStudent , btnSignin));
              cbsiStudent.setOnAction(e -> buttonControl(cbsiTeacher , btnSignin));
      
        
            signUp.setVisible(true);
            signIn.setVisible(false);
        
             abtnSignin.setOnAction(e -> signInCalled());
             abtnSignup.setOnAction(e -> signUpCalled());       
             close.setOnMousePressed(e -> closeUI());
              btnSignup.setOnAction(e  -> registerNewUser());
}
}
