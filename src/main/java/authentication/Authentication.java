/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authentication;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import static java.util.Collections.list;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bson.Document;
import static resources.Constant.*;
import security.EncryptionDecryption;


/**
 *
 * @author Divyanshu
 */
public class Authentication {
    
     private MongoDatabase auth, Student, Teacher ;
    private MongoCollection student =null, teacher=null, s_rec , t_rec;
     private  String pwd;
     private MongoClient mongoClient ;
     private    EncryptionDecryption ed;
    public Authentication(){
            ed =  new    EncryptionDecryption(KEY);
            MongoClientURI uri = new MongoClientURI(ALPHA);
            mongoClient = new MongoClient(uri);
            auth = mongoClient.getDatabase("authentication");
            Student = mongoClient.getDatabase("STUDENT");
            Teacher = mongoClient.getDatabase("TEACHER");
            
}
    
    public int signUpUser(String firstname, String lastname, String email, String password, String user) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
                   
                    String pwd = ed.encryptData(password) ;  //encrypt here
                    if(user.equals("student")){
                               student = auth.getCollection("student");
                               s_rec = Student.getCollection("studentRecord");
                               System.out.println("Student Connected");
                              Document newUserA =new Document("firstname", firstname).append("lastname",lastname).append("email", email).append("password", pwd);
                                Document newUserR =new Document("firstname", firstname).append("lastname",lastname).append("email", email).append("NQuizdone", "0");
                               student.insertOne(newUserA);
                               s_rec.insertOne(newUserR);
                                return RESULT_OK;
                    }else if(user.equals("teacher")){
                                teacher = auth.getCollection("teacher");
                                t_rec = Teacher.getCollection("teacherRecord");
                                System.out.println("Teacher Connected");
                                Document newUserA =new Document("firstname", firstname).append("lastname",lastname).append("email", email).append("password", pwd);
                                Document newUserR =new Document("firstname", firstname).append("lastname",lastname).append("email", email).append("NQuizhosted", "0");
                                                 
                               teacher.insertOne(newUserA);
                               t_rec.insertOne(newUserR);
                                return RESULT_OK;
                    }
                    return RESULT_NOK;
 }
             
    /**
     *
     * @param email gets the userEmail  
     * @param password
     * @param user IS either student or teacher
     * @return
     */
    public int signInUser(String email, String password, String user) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
                    System.out.println(user);
                    String pwd = ed.encryptData(password) ;  //encrypt here
                    long c =0 ;
                    if(user.equals("student")){
                                student = auth.getCollection("student");
                                 c = student.count(and(eq("email",email), eq("password", pwd)));
                                 System.out.println(c+" Signin");
   }
                               
                     else if(user.equals("teacher")){
                                teacher = auth.getCollection("teacher");
                                 c =teacher.count(and(eq("email",email), eq("password", pwd)));
       }
                   if(c==1)
                       return RESULT_OK;
                   else
                       return RESULT_NOK;
    }
    public void close(){
        mongoClient.close();
        
    }
    
    
   }
