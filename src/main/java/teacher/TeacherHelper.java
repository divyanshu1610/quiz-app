/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacher;

import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static resources.Constant.ALPHA;
import static resources.Constant.KEY;
import security.EncryptionDecryption;

/**
 *
 * @author Divyanshu
 */
public class TeacherHelper {
    
    private MongoDatabase dbquiz, dbteacher;
    private MongoCollection<Document>  qCollection, qQuery ;
    private MongoCollection<Document>  qDetail ;
    private MongoCollection<Document> t_rec;
    private JSONObject question ;  //Stores a particular JSON Question object
    private JSONObject section ;   //Stores a particular JSON Section object ;
    private JSONObject quiz ;//Stores a whole QUiz
    private JSONArray options;
    private MongoClient  mongoClient;
      private    EncryptionDecryption ed;
    public TeacherHelper(){
             ed =  new    EncryptionDecryption(KEY);
            MongoClientURI uri  = new MongoClientURI(ALPHA);
            mongoClient = new MongoClient(uri);
             dbquiz = mongoClient.getDatabase("QUIZ");
             dbteacher = mongoClient.getDatabase("TEACHER");
            qCollection = dbquiz.getCollection("quizCollection");
            qQuery =dbquiz.getCollection("quizQuery");
            t_rec =dbteacher.getCollection("teacherRecord");
             qDetail = dbquiz.getCollection("quizDetail");
            System.out.println("Connected");
            question =new JSONObject();
            section =new JSONObject();
            quiz = new JSONObject();
            options = new JSONArray();
            
}
    
    public JSONObject createQuestion(String qNo, String question, JSONArray options, String answer, String marks){
            
        try {
            this.question.clear();
            this.question.put("question", ed.encryptData(question));
            this.question.put("options",options);
            this.question.put("answer",answer);
            this.question.put("marks", marks);
            this.question.put("qNo",qNo);
            System.out.println("SH : " +question);
          
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TeacherHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(TeacherHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TeacherHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(TeacherHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(TeacherHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.question;
 }
  
   public JSONObject createSection(String sectionName, String time, JSONArray questions){
               
               this.section.clear();
               this.section.put("sectionName", sectionName);
               this.section.put("time", time);
               this.section.put("questions",questions);
               //System.out.println("SH : " +section);
               return this.section;
 }    
   
   public JSONObject createQuiz(String quizid, String teachername, String topicname, String subject, JSONArray sections){
       
                this.quiz.put("quizid",quizid);
                this.quiz.put("teachername", teachername);
                this.quiz.put("topicname", topicname);
                this.quiz.put("subject", subject);
                this.quiz.put("sections",sections);
                return this.quiz;
  }
   
   
     public JSONArray addOptions(String option1, String option2, String option3, String option4){
               
               this.options.clear();
               this.options.add(option1);
                this.options.add(option2);
                 this.options.add(option3);
                  this.options.add(option4);
                 // System.out.println("SH : " +options);
                return options;
        
    }
         
   public void hostQuiz(JSONObject quiz) {
                
                String jString = quiz.toString();
                Document d =Document.parse(jString);
                //Document doc = new Document("quiz", jString);
                qCollection.insertOne(d);
        
   }
      
   public ArrayList<Object> getDetails(String email){
        
       ArrayList<Object> details = new ArrayList<Object>();
       FindIterable<Document> findIterable = t_rec.find(eq("email", email));
       Block<Document> printBlock =new Block<Document>(){
           @Override
           public void apply(Document t) {
               
          
               try {
                     JSONObject q = (JSONObject) new JSONParser().parse(t.toJson());
                       details.add(q.get("firstname"));
                   details.add(q.get("lastname"));
                   details.add(q.get("NQuizhosted"));
                   //details.add(q.get("queries"));
                  // System.out.println(q);
                   //System.out.println(q.get("queries"));
               } catch (ParseException ex) {
                   Logger.getLogger(TeacherHelper.class.getName()).log(Level.SEVERE, null, ex);
               }
                    
           }
           
       };
       findIterable.forEach(printBlock);
       System.out.println(details);
       return details;
         
   }
  
   public String generateId(String subject){
       
       String t= subject.substring(0, 4).toUpperCase()+Integer.toString((int) (Math.random()*1000));
       return t;
       
   }
   
   
   public void addQuizDetail(String subject, String topic, String id ){
       
       
           JSONObject temp =new JSONObject();
           temp.put("name", topic);
           temp.put("id",id);
      long count = qDetail.count(eq("subject", subject));
       if(count==0){
           Document d = new Document("subject",subject);
           System.out.println("Inside count = 0"+ count);
           JSONArray topicArray = new JSONArray();
           topicArray.add(temp);
           d.append("topics", topicArray);
           qDetail.insertOne(d);
           
 }else{
           
           System.out.println("Inside count = 1"+ count);
           
         FindIterable<Document> findIterable = qDetail.find(eq("subject", subject));
           
           Block<Document> printBlock = new Block<Document>() {
               @Override
               public void apply(Document t) {
                    
                   System.out.println(t.toString());
                    
                   
                    ArrayList<JSONObject> topics = (ArrayList<JSONObject>) t.get("topics");
                    topics.add(temp);
                    t.replace("topics", topics);
                   System.out.println( t.get("topics"));
                   qDetail.replaceOne(eq("subject", subject), t);
                  }
        };
           findIterable.forEach(printBlock); 
       }
    }
           
   public void upDateQuizHosted(String email){
          System.out.println("updatequiz");
             ArrayList<Integer> a= new ArrayList<Integer>(); 
            FindIterable<Document> findIterable = t_rec.find(eq("email",email));
            Block<Document> printBlock =new Block<Document>(){
   @Override
           public void apply(Document t) {
                       
                       a.add(Integer.parseInt(t.getString("NQuizhosted"))+1);
                          System.out.println("updateind "+a.get(0));
                          t.remove("NQuizhosted");
                          t.append("NQuizhosted",Integer.toString(a.get(0)));
                          t_rec.deleteOne(eq("email", email));
                          t_rec.insertOne(t);
                        // Integer.toString(a.get(0))
           }
           
       };
             findIterable.forEach(printBlock);  
       
        
    } 
   
   public ArrayList<String> getQuery(String firstname){
       
     ArrayList<String> queries = new ArrayList<String>();
       FindIterable<Document> findIterable = qQuery.find(eq("teacher", firstname));
       Block<Document> printBlock =new Block<Document>(){
           @Override
           public void apply(Document t) {
               
               queries.add(t.getString("query"));
           }
           
       };
       findIterable.forEach(printBlock);
      return queries;
       
       
   }
   
   
   public Integer getHostedQuizNo(String email)
   {   
       ArrayList<Integer> a= new ArrayList<Integer>(); 
            FindIterable<Document> findIterable = t_rec.find(eq("email",email));
            Block<Document> printBlock =new Block<Document>(){
   @Override
           public void apply(Document t) {
                       
                       a.add(Integer.parseInt(t.getString("NQuizhosted")));
                       
           }
           
       };
             findIterable.forEach(printBlock);  
       
        return a.get(0);
       
   }
   
   public void close(){
           mongoClient.close();
       
       
   }
    
       
   }
    
     
