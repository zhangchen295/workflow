package nesc.workflow.test;

import java.io.FileFilter;
import java.util.Comparator;
import java.util.concurrent.Callable;

public class TestUtil {

    public static void main(String[] args){

        Callable<String> c = () -> "done";
        Comparator<String> c1 = (s1, s2) -> s1.compareToIgnoreCase(s2);
        FileFilter java = f -> f.getName().endsWith(".java");
        System.out.println(java);
    }
}
