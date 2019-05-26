/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qna;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class QAStudent {

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private int id;
    private int marks;
    private JSONObject que;
    private JSONArray queArray;
   private  JSONObject section;
    
    
public QAStudent(String path){
    
    
    
}   
    
 
public void getQuestion( int id, String question, int marks){
    this.question  = question;
    this.id = id;
    this.marks =marks;
}

public void getOptions(String option1, String option2, String option3 , String option4){
    this.option1 = option1;
    this.option2 = option2;
    this.option3 = option3;
    this.option4 = option4;
}

public void getAnswer(String answer){
    this.answer =answer;
}



public void  createQuestion(int id, String question, int marks, String option1, String option2, String option3 , String option4, String answer){
   
    //que=null;
    
    que.put("id",id );
    que.put("question",question);
    que.put("option1", option1);
    que.put("option2", option2);
    que.put("option3", option3);
    que.put("option4", option4);
    que.put("answer", answer);
    que.put("marks",marks);
   //System.out.println(que);    
  queArray.add(que);
   System.out.println(queArray);    
  
          //return que;
}    

public void createSection(String sectionName){
    
   
    section.put(sectionName,queArray);
   //return section;
}

public void save(){
    
    try(FileWriter fw  = new FileWriter("Quiz1.json")){
        fw.write(section.toString());
        fw.flush();
        
    }catch(Exception e){
        System.out.println("unable to create file");
    }
    
    
}

public void read(){
    
    
    
}



}
