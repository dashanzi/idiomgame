package dashanzi.android.util;

import java.util.ArrayList;
import java.util.List;

public class Node {
	  
    public char c;  
    public int flag; //1：表示终结，0：延续  
    public List<Node> nodes = new ArrayList<Node>();  
      
    public Node(char c) {  
        this.c = c;  
        this.flag = 0;  
    }  
      
    public Node(char c, int flag) {  
        this.c = c;  
        this.flag = flag;  
    }
    public String toString()
    {
    	return "--Node: c = "+ c +",flag = "+flag + ",nodes = " + nodes;            	
    }



}
