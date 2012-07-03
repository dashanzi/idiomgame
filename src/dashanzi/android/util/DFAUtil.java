package dashanzi.android.util;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

	public class DFAUtil {  
        private static List<String> word = new ArrayList<String>();  
        static int a = 0;  
          
        public static  List<String> searchWord(Node rootNode, String content) { 
        	
        	List<String> words = new ArrayList<String>();
        	a=0;
            char[] chars = content.toCharArray();
            Node node = rootNode;  
            while(a<chars.length) {  
                node = findNode(node,chars[a]);  
                if(node == null) {  
                    node = rootNode;  
                    a = a - word.size();  
                    word.clear();  
                } else if(node.flag == 1) {  
                    word.add(String.valueOf(chars[a]));  
                    StringBuffer sb = new StringBuffer();  
                    for(String str : word) {  
                        sb.append(str);  
                    }  
                    words.add(sb.toString());  
                    //a = a - word.size() + 1;  
                    a = a - word.size() + 1;
                    word.clear();  
                    node = rootNode;  
                } else {  
                    word.add(String.valueOf(chars[a]));  
                }//end if  
                a++;  
            }//end while  
            
            Log.e("dfa", words.size()+"");
            for(String str : words){
            	Log.e("dfa", str);
            }
			return words;
        }  
          
        public static void createTree(String[] arr, Node rootNode) {  
            for(String str : arr) {  
                char[] chars = str.toCharArray();  
                if(chars.length > 0)  
                    insertNode(rootNode, chars, 0);  
            }  
        }  
          
        private static void insertNode(Node node, char[] cs, int index) {  
            Node n = findNode(node, cs[index]);  
            if(n == null) {  
                n = new Node(cs[index]);  
                node.nodes.add(n);  
            }  
              
            if(index == (cs.length-1))  
                n.flag = 1;  
                  
            index++;  
            if(index<cs.length)  
                insertNode(n, cs, index);  
        }  
          
        private static Node findNode(Node node, char c) {  
            List<Node> nodes = node.nodes;  
            Node rn = null;  
            for(Node n : nodes) {  
                if(n.c==c) {  
                    rn = n;  
                    break;  
                }  
            }  
            return rn;  
        }  
    }  