package nesc.workflow.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class Test {


    public static void main(String[] args) throws Exception {

        String a = "2019-10-03";
        Class cls = Class.forName("java.util.Date") ;
        Object o = cls.newInstance();
        if(o instanceof Date){
            SimpleDateFormat  s = new SimpleDateFormat("yyyymmdd");
            Date date = s.parse( a );

            System.out.println(date);
        }


        Map<String,String> map = new HashMap<>();
        map.put("1","A");
        map.put("2","B");
        map.put("3","C");
    String columnsName = "";
    StringBuilder sb = new StringBuilder();
    map.forEach((k,v) ->{
        sb.append(k).append(",");
    });
    System.out.println(sb.toString());
        System.out.println(sb.lastIndexOf(","));
        //columnsName.substring(0,sb.lastIndexOf(","));
        System.out.println(sb.substring(0,sb.lastIndexOf(",")));








    }

}
