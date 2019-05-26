/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import javafx.scene.control.TextField;
import javax.naming.Context;

/**
 *
 * @author Kushagra
 */

public class Timer implements Runnable{
       
    
         int time;
        private volatile boolean end = false;
        private TextField tf;
        
        
        public Timer(int time, TextField tf )
        {
                this.time = time;
                this.tf = tf;
}

        @Override
            public void run(){
                            while(this.time-- > 0 && !end){
                                    try {
                                            Thread.sleep(1000);
                                            this.tf.setText(Integer.toString(this.time));
                                    } catch (InterruptedException ex) {
                                                        System.out.println("error");
                                                }
                            }
            }
            
               public void stop(){
                          end = true;
} 
            
}
