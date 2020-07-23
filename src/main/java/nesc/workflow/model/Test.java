package nesc.workflow.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Test {


    public static void main(String[] args) throws Exception {

        String a = "2019-10-03";
        Class cls = Class.forName("java.util.Date") ;
        Object o = cls.newInstance();
        if(o instanceof Date){
            SimpleDateFormat  s = new SimpleDateFormat("yyyymmdd");
            Date date = s.parse( " 2008-07-10 19:20:00 " );

            System.out.println(date);
        }



    }

}
