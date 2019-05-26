/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import static resources.Constant.KEY;
import security.EncryptionDecryption;

public class StudentQuizPageController implements Initializable {

//END PANE
 @FXML Button btnRate;
 @FXML Label labelMarks;
 @FXML TextArea textareaQuery;
 @FXML Button btnSendQuery;
 @FXML TextField teachername;
 @FXML  Slider slider;
 @FXML Label rating;

// Top Pane 
@FXML TextField sName;
@FXML ChoiceBox cbSection;
@FXML Button btnGo;
@FXML public TextField timer;

//Question pane 
@FXML TextArea displayQuestion;
@FXML Button btnNext;
@FXML TextField quesNo;
@FXML TextField marks;
@FXML RadioButton op1;
@FXML RadioButton op2;
@FXML RadioButton op3;
@FXML RadioButton op4;
@FXML Button btnSave;
@FXML Button btnEnd;

//StartPane 
@FXML Button btnStart;

//Panes
@FXML Pane startPane;
@FXML Pane quizPane;
@FXML Pane optionPane;
@FXML Pane TFPane;
@FXML Pane EndPane;

@FXML ToggleGroup optionsGroup;

  private Stage dashboard;
  private String quizid;
  private StudentHelper sh ;
  private JSONObject quiz;
  private JSONArray sections;
  private JSONObject currentSection;
  private JSONObject currentQuestion;
  private String Canswer;
    private    EncryptionDecryption ed;
  private ArrayList<JSONObject> questions;
  private ArrayList<String> options;
  static int countS =0, countQ=0;
  private static int countMarks = 0;
    private int flag=0, opted=-1;
 private Timer time;
// private ArrayList<ArrayList<Integer>> secQ=new ArrayList<ArrayList<Integer>>(); // keeps track of section and question
   private ArrayList<Integer> qTrack = new ArrayList<Integer>();  
   private ArrayList<Integer> tTrack = new ArrayList<Integer>();
     private ArrayList<Integer> sTrack = new ArrayList<Integer>();
        StudentDashboardController sc;
        
    public void receiveDetails(Stage dashboard, String quizId, StudentDashboardController sc) throws ParseException{
            sh =new StudentHelper();
            this.sc = sc;
            this.dashboard =dashboard;
            this.quizid = quizId;
            System.out.println("Recieved dashboard and id "+quizId );
             quiz  =sh.getQuiz(quizId);
             sections  = sh.getSectionArray(quiz);
              //System.out.println(sections);
             for(int i=0; i<sections.size(); i++){
                     qTrack.add(i, 0);
                     sTrack.add(i);
                     JSONObject secOb =(JSONObject) sections.get(i);
                     System.out.println(secOb);
                     tTrack.add(i, Integer.parseInt((String) secOb.get("time")));
                    
                     JSONArray q = sh.getQuestionArray(secOb);
                     System.out.println(q);
                     JSONArray t = sh.shufflee(q);
                     System.out.println(t);
                     secOb.replace("questions", t);
                     System.out.println(secOb);
                     sections.set(i, secOb);
                 }
             System.out.println(tTrack);
             
                    currentSection = (JSONObject) sections.get(countS);
     try {
         changeQuestion(currentSection, countQ);
     } catch (IOException ex) {
         Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
     } catch (NoSuchAlgorithmException ex) {
         Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
     } catch (NoSuchPaddingException ex) {
         Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InvalidKeyException ex) {
         Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IllegalBlockSizeException ex) {
         Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
     } catch (BadPaddingException ex) {
         Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
     }
                    
    }
    
    
 
    public void startQuiz(ActionEvent e){
        startPane.setVisible(false);
        //  currentSection = (JSONObject) sections.get(countS);
                 //  changeSectionNext(currentSection, countS);
       time = new Timer(tTrack.get(0), timer);
                 Thread t = new Thread(time);
        t.start();
  
        quizPane.setVisible(true);
  }
    
    
     public void changeQuestion(JSONObject currentSection , int qno) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
         flag=0;
            opted=-1;
             op1.setSelected(false);
             op2.setSelected(false);
             op3.setSelected(false);
             op4.setSelected(false);
            this.currentSection = currentSection;
            questions = sh.getQuestionArray(this.currentSection);
            currentQuestion = questions.get(qno);
            String ques = (String) currentQuestion.get("question");
            displayQuestion.setText(ed.decryptData(ques));
            options  =sh.getOptionsArray(currentQuestion);
            op1.setText(options.get(0).toString());
            op2.setText(options.get(1).toString());
            op3.setText(options.get(2).toString());
            op4.setText(options.get(3).toString());
            marks.setText(currentQuestion.get("marks").toString());
            
    }
    
    
    public void nextQuestionPressed(ActionEvent e){
            
           
           if(countS<sections.size() )
           {
           
              if(countQ < questions.size()-1){
                     countQ++;
              qTrack.set(countS, countQ);
                  try {
                      changeQuestion(currentSection, countQ);
                  } catch (IOException ex) {
                      Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (NoSuchAlgorithmException ex) {
                      Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (NoSuchPaddingException ex) {
                      Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (InvalidKeyException ex) {
                      Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (IllegalBlockSizeException ex) {
                      Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (BadPaddingException ex) {
                      Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
                  }
           }else
              {
                  
             changeSectionNext(currentSection,countS+1 );     
           }
           }
           else 
           btnNext.setDisable(true);
           
                       
      }
    
    
    
    
    public void changeSectionNext(JSONObject currentSection,  int S ){
        
          if(S<sections.size()) {
             
              try {
                  time.stop();
                  qTrack.set(countS, countQ);
                  tTrack.set(countS, Integer.parseInt(timer.getText()));
                  // timer.setText(" ");
                  System.out.println(tTrack);
                  countS = S;
                  countQ=qTrack.get(countS);
                  time = new Timer(tTrack.get(countS), timer);
                  Thread t = new Thread(time);
                  t.start();
                  
                  
                  this.currentSection = sh.getSection(sections, countS);
                  changeQuestion(this.currentSection , countQ);
              } catch (IOException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (NoSuchAlgorithmException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (NoSuchPaddingException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InvalidKeyException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalBlockSizeException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (BadPaddingException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              }
        
    }else
              btnNext.setDisable(true);
   }
    
    
    
     public void changeSectionPrev(JSONObject currentSection,  int S ){
        
          if(S>-1) {
              btnNext.setDisable(false);
             time.stop();
               tTrack.set(countS, Integer.parseInt(timer.getText()));  
                // timer.setText(" ");
             countS = S;
              time = new Timer(tTrack.get(countS), timer);
                 Thread t = new Thread(time);
              t.start();
          this.currentSection = sh.getSection(sections, countS);
              try {
                  changeQuestion(this.currentSection , qTrack.get(countS) );
              } catch (IOException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (NoSuchAlgorithmException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (NoSuchPaddingException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InvalidKeyException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalBlockSizeException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              } catch (BadPaddingException ex) {
                  Logger.getLogger(StudentQuizPageController.class.getName()).log(Level.SEVERE, null, ex);
              }
        
    }
   }
    
    
    
    
   public void btnPrevSectionPressed(ActionEvent e){
       
       changeSectionPrev(currentSection, countS-1);
       
   }
    
   public void btnNextSectionPressed(ActionEvent e){
       
       changeSectionNext(currentSection, countS+1);
       
   }
    
    
    public void saveButtonPressed(ActionEvent e){
          
        
       
           checkAnswer();
            
    }
   
    public void checkAnswer(){
        
       
        
        Canswer = (String) currentQuestion.get("answer");
        
             //flag=0;
           // System.out.println(Canswer+" cans");
          
            if(optionsGroup.getSelectedToggle().equals(op1) && Canswer.equals(op1.getText())){
                 
                 flag=1;
                  if(opted!=0)
                 opted = 1;
                // System.out.println(countMarks+" "+Integer.parseInt(marks.getText()) );
           } else if(optionsGroup.getSelectedToggle().equals(op2) && Canswer.equals(op2.getText())){
                 
               flag=1;
                if(opted!=0)
               opted = 1;
                 //System.out.println(countMarks);
           }else  if(optionsGroup.getSelectedToggle().equals(op3) && Canswer.equals(op3.getText())){
                 
               flag=1;
                if(opted!=0)
               opted = 1;
               //  System.out.println(countMarks);
           } else if(optionsGroup.getSelectedToggle().equals(op4) && Canswer.equals(op4.getText())){
                 
                 flag=1;
                  if(opted!=0)
                 opted = 1;
              //   System.out.println(countMarks);
           
          } else {
               flag=0;
           }
            //flag=0;
          
            if( opted==1){
                 countMarks = countMarks +Integer.parseInt(marks.getText());
                 opted=0;
                  System.out.println(countMarks);
            }else if(opted==0&&flag==0)
                    {
                 countMarks = countMarks -Integer.parseInt(marks.getText());
                 System.out.println(countMarks);
                opted=-1;
            }
    }
    
    public void sendQueryPressed(ActionEvent e){
        
        if(!textareaQuery.getText().trim().equals(""))
             sh.sendQuery(textareaQuery.getText().trim(), sc.email, quiz.get("teachername").toString());
               textareaQuery.setText("");
               Stage stage =(Stage)((Node)e.getSource()).getScene().getWindow();
                this.dashboard.show();
                stage.close();
    }
    
    
    public void endButtonPressed(ActionEvent e){
        System.out.println(countMarks);
  /// send marks to record  
       sh.upDateQuizDone(sc.email, quizid, Integer.toString(countMarks));
       quizPane.setVisible(false);
       EndPane.setVisible(true);
       labelMarks.setText(Integer.toString(countMarks));
       teachername.setText(quiz.get("teachername").toString());
       slider.setValue(0);
       rating.setText(new Double(0).toString());
       
       rating.textProperty().bindBidirectional(slider.valueProperty(), NumberFormat.getNumberInstance());
       Stage stage =(Stage)((Node)e.getSource()).getScene().getWindow();
       this.dashboard.show();
       stage.close();
        
  }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          
          
          //  System.out.println("Recieved dashboard and id "+this.quizid );
       //    sh =new StudentHelper();
           //while(this.quizid!=null)
          // sh.getQuiz(this.quizid);
          optionsGroup = new ToggleGroup();
         op1.setToggleGroup(optionsGroup);
         op2.setToggleGroup(optionsGroup);
         op3.setToggleGroup(optionsGroup);
         op4.setToggleGroup(optionsGroup);
          
          ed =  new    EncryptionDecryption(KEY);
        
        
        
        
    }    
    
}
