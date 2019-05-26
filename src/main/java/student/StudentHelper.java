/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static resources.Constant.ALPHA;


public class StudentHelper{
    
    private MongoDatabase dbquiz, dbSRecord ; 
    private MongoCollection<Document>  qCollection , qDetail, s_rec, qQuery; //Collection of Quizes and short description 
    private MongoClient mongoClient ;
    
    
    
    public StudentHelper (){
       
        MongoClientURI uri = new MongoClientURI(ALPHA);
        mongoClient = new MongoClient(uri);
        dbquiz = mongoClient.getDatabase("QUIZ");
        dbSRecord = mongoClient.getDatabase("STUDENT");
        qCollection = dbquiz.getCollection("quizCollection");  // contains all quiz
        qDetail = dbquiz.getCollection("quizDetail");  //contains subjects and topics
        s_rec =  dbSRecord.getCollection("studentRecord");
        qQuery = dbquiz.getCollection("quizQuery");
 }
    
    /**
     *   Get all the subjects present in Quiz 
     * @return list of All the subjects 
     */
    public ArrayList getSubjects(){
     
      ArrayList<String> subjects = new ArrayList<String>();
     FindIterable<Document> findIterable = qDetail.find(new Document());   //finds all documents 
     /**
      * findIterable = qDetail.find(eq(key , value ));  
      * Finds a particular document consisting key value pair 
      * 
      */
     Block<Document>  printBlock = new Block<Document>(){
         
         JSONObject  resultObject;
         @Override
         public void apply(Document t) {
                System.out.println(t.toJson());
             try { 
               resultObject = (JSONObject) new JSONParser().parse(t.toJson());
               String subject = (String) resultObject.get("subject");
               subjects.add(subject);
                //System.out.println(sub);
             } catch (ParseException ex) {
                 System.out.println("Error in parsing ");
             }
         }
 };
     
          findIterable.forEach(printBlock);
          return subjects ; 
 }
   
  
   /*
    *Get all the topics of a prticular subject
    * @return List of All the topics under Subject.
    */ 
  public ArrayList getTopics(String subject){
    
      ArrayList<String> topics = new ArrayList<String>();
     FindIterable<Document> findIterable = qDetail.find(eq("subject" ,subject));   //finds all documents 
     //Bson sort = descending("topics");
     //findIterable.sort(sort);
     /**
      * findIterable = qDetail.find(eq(key , value ));  
      * Finds a particular document consisting key value pair 
      * 
      */
     
     Block<Document>  printBlock = new Block<Document>(){
         
         JSONObject  resultObject; 
         JSONArray topicArray ; 
         
         @Override
         public void apply(Document t) {
                System.out.println(t.toJson());
             try { 
               resultObject = (JSONObject) new JSONParser().parse(t.toJson());
                topicArray = (JSONArray)resultObject.get("topics");
               for(int i = 0 ; i<topicArray.size() ; i++){
                   JSONObject topic = (JSONObject) topicArray.get(i);
                   String topicName = (String) topic.get("name");
                   topics.add(topicName);
               }
               // System.out.println(sub);
             } catch (ParseException ex) {
                 System.out.println("Error in parsing ");
             }
         }
 };
     
          findIterable.forEach(printBlock);
          return topics ; 
      
      
      
      
      
  }
    

 /*
  *Returns only the first occurance of topic ID
  */
  public String  getQuizId(String subject, String topic){
                  
      ArrayList<String> id =new ArrayList<String>() ; 
       FindIterable<Document> findIterable = qDetail.find(and(eq("subject" ,subject), eq("topics.name",topic)));   //finds all documents 
      Block<Document>  printBlock = new Block<Document>(){
          
   
         JSONObject  resultObject; 
         JSONArray topicArray ; 
         
         @Override
         public void apply(Document t) {
                System.out.println(t.toJson());
             try { 
                resultObject = (JSONObject) new JSONParser().parse(t.toJson());
                topicArray = (JSONArray)resultObject.get("topics");
            
                for(int i = 0 ; i<topicArray.size() ; i++){
                   JSONObject Topic = (JSONObject) topicArray.get(i);
                   String topicID = (String)Topic.get("id");
                  // System.out.println(topicID);
                   
                    if(Topic.get("name").equals(topic)){
                      //  System.out.println(topicID);
                      id.add(topicID);
                    }
             }
             }  catch (ParseException ex) {
                 System.out.println("Error in parsing ");
             }
         }
 };
  
           System.out.println("hello");
           findIterable.forEach(printBlock);
          
      
     return id.get(0);
         }
  
  public JSONObject getQuiz(String quizID) throws ParseException {
      
              
           FindIterable<Document> findIterable = qCollection.find(eq("quizid", quizID));
           Block<Document> printBlock = new Block < Document>(){
               @Override
               public void apply(Document t) {
                     System.out.println(t.toJson());
               } };
        Document d   = findIterable.first();
        JSONObject q = (JSONObject) new JSONParser().parse(d.toJson());
         return q;
         //System.out.println(j.get("teachername"));
         // JSONObject quiz =(JSONObject) new JSONParser().parse(q);
        // return quiz;
}
  
  
  public JSONArray getSectionArray(JSONObject q){
      
     JSONArray sections =new JSONArray();
      sections =(JSONArray) q.get("sections");
     //  System.out.println(sections);
      return sections;
      
      
      
  }
  
    public JSONArray getQuestionArray(JSONObject s){
      
   JSONArray questions=new JSONArray();
      questions=(JSONArray) s.get("questions");
     //  System.out.println(sections);
      return questions;
      
  }
    
    public JSONObject getSection(ArrayList<JSONObject> s, int i){
        
        return s.get(i);
    }
    
    public JSONObject getQuestion(ArrayList<JSONObject> q, int i){
        
        return q.get(i);
    }
    
   public ArrayList<String> getOptionsArray(JSONObject q){
      
      ArrayList<String> options= new ArrayList<String>();
      options = (ArrayList<String>) q.get("options");
     //  System.out.println(sections);
      return options;
      
  }
   
   public String getMarks(JSONObject q){
       return (String) q.get("marks");
       }
   
   public String getAns(JSONObject q){
       return (String) q.get("answer");
       }
      
   public String getTime(JSONObject s){
       return (String) s.get("time");
       }
   
   
public  JSONArray shuffle (JSONArray array)  {
   
        Random rnd = new Random();
        for (int i = array.size() - 1; i >= 0; i--)
        {
          int j = rnd.nextInt(i + 1);
      
          Object object = array.get(j);
          array.add(j, array.get(i));
          array.add(i, object);
        }
    return array;
}
     
 public JSONArray shufflee(JSONArray q){
           
          JSONArray sArray = new JSONArray();
          ArrayList<Integer> qList = new ArrayList<Integer>();
           for(int i=0; i<q.size(); i++)
             qList.add(i);
            Collections.shuffle(qList);
            for(int i =0 ; i<qList.size(); i++)
                sArray.add(i, q.get(qList.get(i)));
           return sArray;
 }  
 
  public ArrayList<Object> getDetails(String email){
        
       ArrayList<Object> details = new ArrayList<Object>();
       FindIterable<Document> findIterable = s_rec.find(eq("email", email));
       Block<Document> printBlock =new Block<Document>(){
           @Override
           public void apply(Document t) {
               
          
              
               try {
                   JSONObject q = (JSONObject) new JSONParser().parse(t.toJson());
                   details.add(q.get("firstname"));
                   details.add(q.get("lastname"));
                   details.add(q.get("NQuizdone"));
                  
               } catch (ParseException ex) {
                   Logger.getLogger(StudentHelper.class.getName()).log(Level.SEVERE, null, ex);
               }
             
                    
           }
           
       };
       findIterable.forEach(printBlock);
       System.out.println(details);
       return details;
         
   }
   
       public Integer getDoneQuizNo(String email)
   {   
       ArrayList<Integer> a= new ArrayList<Integer>(); 
            FindIterable<Document> findIterable = s_rec.find(eq("email",email));
            Block<Document> printBlock =new Block<Document>(){
   @Override
           public void apply(Document t) {
                       
                       a.add(Integer.parseInt(t.getString("NQuizdone")));
                       
           }
           
       };
             findIterable.forEach(printBlock);  
       
        return a.get(0);
       
   }
  
    public ArrayList<String> getQuery(String email){
       
     ArrayList<String> queries = new ArrayList<String>();
       FindIterable<Document> findIterable = qQuery.find(eq("student", email));
       Block<Document> printBlock =new Block<Document>(){
           @Override
           public void apply(Document t) {
               
               queries.add(t.getString("query"));
           }
           
       };
       findIterable.forEach(printBlock);
      return queries;
       
       
   }
 
    public ArrayList<String> getQueryReplies(String email, String query){
       
     ArrayList<String> queries = new ArrayList<String>();
       FindIterable<Document> findIterable = qQuery.find(and(eq("student", email), eq("query", query)));
       Block<Document> printBlock =new Block<Document>(){
           @Override
           public void apply(Document t) {
               
               queries.add(t.getString("query"));
           }
           
       };
       findIterable.forEach(printBlock);
      return queries;
       
       
   }
    
    public void sendQuery(String q, String sname, String tname){
        JSONObject query = new JSONObject();
        Document d = new Document();
       
       d.append("query", q);
          d.append("student", sname);
              d.append("teacher", tname);
                 d.append("reply", "");
        qQuery.insertOne(d);
        
        
        
        
    }
    
    public void upDateQuizDone(String email, String quizid, String marks){
                     ArrayList<Integer> a= new ArrayList<Integer>(); 
                     
            FindIterable<Document> findIterable = s_rec.find(eq("email",email));
            Block<Document> printBlock =new Block<Document>(){
   @Override
           public void apply(Document t) {
                        JSONObject o = new JSONObject();
                        o.put("id", quizid);
                        o.put("marks",marks);
                       a.add(Integer.parseInt(t.getString("NQuizdone"))+1);
                      
                          t.remove("NQuizdone");
                          t.append("NQuizdone",Integer.toString(a.get(0)));
                          t.append("quiz",o);
                          s_rec.deleteOne(eq("email", email));
                         
                          s_rec.insertOne(t);
                          
                        // Integer.toString(a.get(0))
           }
           
       };
             findIterable.forEach(printBlock);  
       
        
    } 
    
    public ArrayList<JSONObject> viewQueryAnswer(String email){
        
        
       
     ArrayList<JSONObject> queries = new ArrayList<JSONObject>();
       FindIterable<Document> findIterable = qQuery.find(eq("student", email));
       Block<Document> printBlock =new Block<Document>(){
           @Override
           public void apply(Document t) {
               JSONObject temp = new JSONObject();
               temp.put("query", t.get("query"));
               temp.put("reply",t.get("reply"));
               queries.add(temp);
               
           }
           
       };
       findIterable.forEach(printBlock);
      return queries;
       
       

   
    }
    
  public void close(){
      mongoClient.close();
      
      
  }
}


