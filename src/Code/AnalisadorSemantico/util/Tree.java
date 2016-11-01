/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSemantico.util;

/**
 *
 * @author Leonardo Santana
 */
public class Tree {
    Node root;
   public Tree(){
       root = new Node(null, 0);
   }
   
   public Tree(int valor){
       root = new Node(null, valor);
   }
   
   public void insert(int pai, int filho){
       if(pai==root.elem) root.filhos.add(new Node(root, filho));
       else {
           Node n = nodeSearch(pai);
           if(n!=null)
               n.filhos.add(new Node(n, filho));
       }
   }
   
   public Node nodeSearch(int valor){
       return nodeSearch(root,valor);
   }
   
   public Node nodeSearch(Node node,int valor){
       Node n = null;
       if(node.elem==valor) return node;
       else{
           for(Node no: node.filhos){
               Node k = nodeSearch(no,valor); 
               if(k!=null)
                    n = k;
            }
       }
       return n;
   }
}
