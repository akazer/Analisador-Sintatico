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
    String tipo;

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
                tipo = outraList.get(++pos).getLexema();
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
                tipo = outraList.get(++pos).getLexema();
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
                tipo = outraList.get(++pos).getLexema();
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
        while(t.getLexema().equals("="))
        {
            t = outraList.get(++pos);
        }
        if(tipo.equals("real"))
        {
            while(!t.getLexema().equals(";"))
            {
                if(t.getTipo().equals("identificador"))
                {
                    if(!tabelaSimbolos.get(t.getLexema()).tipo.equals("real") && !tabelaSimbolos.get(t.getLexema()).equals("inteiro"))
                    {
                        erros.add("Esperado tipo real ou inteiro para atribuicao na linha " + t.getLinha());
                    }
                }
                else if(t.getTipo().equals("numero_real") || t.getTipo().equals("numero_inteiro"))
                {
                    //aceita
                }
                else
                {
                    erros.add("Esperado tipo real ou inteiro para atribuicao na linha " + t.getLinha());
                }
                t = outraList.get(++pos);
            }
        }
        else if(tipo.equals("cadeia"))
        {
            while(!t.getLexema().equals(";"))
            {
                if(t.getTipo().equals("identificador"))
                {
                    if(!tabelaSimbolos.get(t.getLexema()).tipo.equals("cadeia") && !tabelaSimbolos.get(t.getLexema()).equals("caractere"))
                    {
                        erros.add("Esperado tipo cadeia ou caractere para atribuicao na linha " + t.getLinha());
                    }
                }
                else if(t.getTipo().equals("cadeia") || t.getTipo().equals("caractere"))
                {
                    //aceita
                }
                else
                {
                    erros.add("Esperado tipo cadeia ou caractere para atribuicao na linha " + t.getLinha());
                }
                t = outraList.get(++pos);
            }
        }
        else //inteiro caractere ou booleano
        {
            while(!t.getLexema().equals(";"))
            {
                if(t.getTipo().equals("identificador"))
                {
                    if(!tabelaSimbolos.get(t.getLexema()).tipo.equals(tipo))
                    {
                        erros.add("Esperado tipo " + tipo + " para atribuicao na linha " + t.getLinha());
                    }
                }
                else if(tipo.equals("numero_inteiro"))
                {
                    if(!t.getTipo().equals("numero_inteiro")) erros.add("Esperado tipo inteiro para atribuicao na linha " + t.getLinha());
                }
                else if(tipo.equals("booleano"))
                {
                    if(!t.getTipo().equals("booleano")) erros.add("Esperado tipo booleano para atribuicao na linha " + t.getLinha());
                }
                else if(tipo.equals("caractere"))
                {
                    if(!t.getTipo().equals("caractere")) erros.add("Esperado tipo caractere para atribuicao na linha " + t.getLinha());
                }
                t = outraList.get(++pos);
            }
        }
    }
    
    public void chamadaFuncao(String idFuncao)
    {
        Simbolo s = tabelaSimbolos.get(idFuncao);
        if(s==null || !s.tipoId.equals("funcao")){
            erros.add("Função inexistente na linha "+t.getLinha());
            while(!t.getLexema().equals(")")){
                t = outraList.get(++pos);
            }
            return;
        }
        while(!t.getLexema().equals(")")){
           t = outraList.get(++pos);
           int parametro = 0;
           if(t.getTipo().equals("identificador")){
               String l = s.parametros.get(parametro);
               if(l==null){
                   erros.add("Parametro inexistente na linha "+t.getLinha());
               }else if(t.getTipo().equals(l)){
                   erros.add("Parametro com tipo incompativel na linha "+t.getLinha());
               }
               parametro++;
           } else if(t.getTipo().equals("numero_real")){
               String l = s.parametros.get(parametro);
               if(l==null){
                   erros.add("Parametro inexistente na linha "+t.getLinha());
               } else if(!l.equals("real") && !l.equals("inteiro"))
               {
                   erros.add("Parametro incompatível na linha " +t.getLinha() + ". Esperado número real ou inteiro.");
               }
               parametro++;
           }else if(t.getTipo().equals("numero_inteiro")){
               String l = s.parametros.get(parametro);
               if(l==null){
                   erros.add("Parametro inexistente na linha " +t.getLinha());
               }else if(!l.equals("inteiro"))
               {
                   erros.add("Parametro incompatível na linha " +t.getLinha() + ". Esperado número inteiro.");
               }
               parametro++;
           }else if(t.getTipo().equals("cadeia")){
               String l = s.parametros.get(parametro);
               if(l==null){
                   erros.add("Parametro inexistente na linha "+t.getLinha());
               }else if(!l.equals("cadeia") && !l.equals("caractere"))
               {
                   erros.add("Parametro incompatível na linha " +t.getLinha() + ". Esperado cadeia ou caractere.");
               }
               parametro++;
           }else if(t.getTipo().equals("caractere")){
               String l = s.parametros.get(parametro);
               if(l==null){
                   erros.add("Parametro inexistente na linha "+t.getLinha());
               }else if(!l.equals("caractere"))
               {
                   erros.add("Parametro incompatível na linha " +t.getLinha() + ". Esperado caractere.");
               }
               parametro++;
           }else if(t.getTipo().equals("booleano")){
               String l = s.parametros.get(parametro);
               if(l==null){
                   erros.add("Parametro inexistente na linha "+t.getLinha());
               }else if(!l.equals("booleano"))
               {
                   erros.add("Parametro incompatível na linha " +t.getLinha() + ". Esperado booleano.");
               }
               parametro++;
           }
           
        }
    }
    
    public void acessaMatriz()
    {
        while(!t.getLexema().equals("*/")){
           t = outraList.get(++pos);
           if(t.getTipo().equals("identificador")){
               Simbolo s = tabelaSimbolos.get(t.getLexema());
               if(s==null){
                   erros.add("Variavel não declarada na linha "+t.getLinha());
               } else {
                   if(!s.tipo.equals("inteiro")){
                       erros.add("Tipo de variavel incompativel na linha "+t.getLinha());
                   }
               }
           } else if(!t.getTipo().equals("numero_inteiro")){
               erros.add("Valor não esperado na linha "+t.getLinha());
           }
        }
    }
    
    public void leia()
    {
        while(!t.getLexema().equals(";")){
           t = outraList.get(++pos);
           if(t.getTipo().equals("identificador")){
               Simbolo s = tabelaSimbolos.get(t.getLexema());
               if(s==null){
                   erros.add("Variavel não declarada na linha "+t.getLinha());
               } else {
                   if(s.tipo.equals("booleano")){
                       erros.add("Variavel booleana não aceita na linha "+t.getLinha());
                   }
               }
           }
        }   
    }
    
    public void escreva()
    {
        while(!t.getLexema().equals(";")){
           t = outraList.get(++pos);
           if(t.getTipo().equals("identificador")){
               
               if(tabelaSimbolos.containsKey(t.getLexema())){
                   erros.add("Variavel não declarada na linha "+t.getLinha());
               }
           }
        }
    }
    
    public void expAritmetica()
    {
        
    }
    
//    public void expBooleana()
//    {
//        
//    }
}
