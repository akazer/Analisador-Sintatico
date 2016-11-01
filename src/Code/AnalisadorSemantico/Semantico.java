/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSemantico;

import Code.AnalisadorSemantico.util.*;
import Code.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

//Vohayoooo!

public class Semantico {
    
    List<Token> list;
    List<Token> outraList;
    List<String> erros;
    Tree escopos;
    Map<String,Simbolo> tabelaSimbolos;
    int j, jmax, pos;
    Token t;
    

    public Semantico(List<Token> l){
        list = l;
        tabelaSimbolos = new LinkedHashMap<>();
        list.add(new Token("$","$",l.get(l.size()-1).getLinha()));
        erros = new ArrayList<>();
        this.outraList.addAll(list);
        escopos = new Tree();
    }
    
    public void preRun(){
        j = 0; jmax = 0; //escopo Atual
        Queue<Integer> q = new LinkedList<>();
        for(pos = 0; pos < outraList.size();pos++){
            t = outraList.get(pos);
            if(t.getLexema().equals("inicio")){
                q.add(j);
                jmax++;
                escopos.insert(j, jmax);
                j = jmax;
            }else if(t.getLexema().equals("fim")){
                j = q.poll();
            } else if(t.getTipo().equals("numero")){
                if(t.getLexema().contains(".")){
                    t.setTipo("numero_real");
                } else {
                    t.setTipo("numero_inteiro");
                }
            } else if(t.getLexema().equals("funcao")){
                String tipo = outraList.get(++pos).getLexema();
                pos++;
                Simbolo novaFuncao = new Simbolo(t.getLexema(), tipo, "funcao", j);
                tabelaSimbolos.put(t.getLexema(),novaFuncao);
                while(!t.getLexema().equals(")"))
                {
                    t = outraList.get(++pos);
                    if(t.getLexema().equals("(") || t.getLexema().equals(","))
                    {
                        pos++;
                        tipo = outraList.get(pos).getLexema();
                        novaFuncao.addParametro(tipo);
                        pos++; //pula o nome do parametro, que nao é usado
                    }
                }
            }
        }
    }
    
    public void run(){
        j = 0; jmax = 0; //escopo Atual
        Queue<Integer> q = new LinkedList<>();
        for(pos = 0; pos < outraList.size();pos++){
            t = outraList.get(pos);
            
            if(t.getLexema().equals("inicio")){
                q.add(j);
                jmax++;
                j = jmax;
            }else if(t.getLexema().equals("fim")){
                Set<String> s = tabelaSimbolos.keySet();
                for(String s1: s){
                    if(tabelaSimbolos.get(s1).escopo==j)
                        tabelaSimbolos.remove(s1);
                }
                j = q.poll();
            } else if(t.getLexema().equals("var")){
                String tipo = outraList.get(++pos).getLexema();
                while(!t.getLexema().equals(";")){
                    t = outraList.get(++pos);
                    if(t.getTipo().equals("identificador")){
                        if(tabelaSimbolos.containsKey(t.getLexema())){
                            erros.add("Nome de variavel já utilizado na linha "+t.getLinha());
                        }else if(outraList.get(pos+1).getLexema().equals("/*")){
                            Token t2 = outraList.get(pos);
                            Simbolo s = new Simbolo(t.getLexema(), tipo, "matriz", j);
                            while(t2.getLexema().equals(",")||t2.getLexema().equals(";")){
                                if(outraList.get(pos).getLexema().equals("/*")){
                                    t2 = outraList.get(++pos);
                                    s.addDimensoes(Integer.parseInt(t2.getLexema()));
                                }
                                t2 = outraList.get(++pos);
                            }
                        } else tabelaSimbolos.put(t.getLexema(),new Simbolo(t.getLexema(), tipo, "variavel", j));
                    }
                }
            } else if(t.getLexema().equals("const")){
                String tipo = outraList.get(++pos).getLexema();
                while(!t.getLexema().equals(";")){                    
                    t = outraList.get(++pos);
                    if(t.getTipo().equals("identificador")){
                        tabelaSimbolos.put(t.getLexema(),new Simbolo(t.getLexema(), tipo, "constante", j));
                    }
                }
            }
        }
    }
    
    public void atribuicao()
    {
        
    }
    
    public void chamadaFuncao()
    {
        
    }
    
    public void acessaMatriz()
    {
        
    }
    
    public void leia()
    {
        
    }
    
    public void escreva()
    {
        
    }
    
    public void expAritmetica()
    {
        
    }
    
//    public void expBooleana()
//    {
//        
//    }
}
