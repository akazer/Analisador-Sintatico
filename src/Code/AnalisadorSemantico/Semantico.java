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

//Vohayoooo!

public class Semantico {
    
    List<Token> list;
    List<Token> outraList;
    Integer i, j;
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
        
        int escopotemp = 0;
        
        while(!"$".equals(outraList.get(j).getLexema()))
        {
            if("pal_reserv_var".equals(outraList.get(j).getTipoCompleto()))
            {
                j++;
                if("pal_reserv_inteiro".equals(outraList.get(j).getTipoCompleto()))
                {
                    j++;
                    tabelaSimbolos.add(new Simbolo(outraList.get(j).getLexema(), "var", "inteiro", escopotemp));
                    while(!"delimitador_;".equals(outraList.get(j).getTipoCompleto()))
                    {
                        if("delimitador_,".equals(outraList.get(j).getTipoCompleto()))
                        {
                            tabelaSimbolos.add(new Simbolo(outraList.get(j+1).getLexema(), "var", "inteiro", escopotemp));
                        }
                        j++;
                    }
                }
                if("pal_reserv_real".equals(outraList.get(j).getTipoCompleto()))
                {
                    j++;
                    tabelaSimbolos.add(new Simbolo(outraList.get(j).getLexema(), "var", "real", escopotemp));
                }
                while(!"delimitador_;".equals(outraList.get(j).getTipoCompleto()))
                {
                    if("delimitador_,".equals(outraList.get(j).getTipoCompleto()))
                    {
                        tabelaSimbolos.add(new Simbolo(outraList.get(j+1).getLexema(), "var", "real", escopotemp));
                    }
                    j++;
                }
            }
        }
        
    }
    
}