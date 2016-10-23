/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSemantico;

import Code.Token;
import java.util.ArrayList;
import java.util.List;
import sun.reflect.generics.tree.Tree;



public class Semantico {
    
    List<Token> list;
    List<Token> outraList;
    Integer i;
    List<String> erros;
    Tree escopos;
    ArrayList<Simbolo> tabelaSimbolos;
    

    public Semantico(List<Token> l){
        list = l;
        list.add(new Token("$","$",l.get(l.size()-1).getLinha()));
        i = 0;
        erros = new ArrayList<>();
        this.outraList.addAll(list);
        
    }
    
    public void preRun(){
        
        
        
    }
    
}