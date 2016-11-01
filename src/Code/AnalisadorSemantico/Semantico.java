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
import java.util.PriorityQueue;
import java.util.Queue;

//Vohayoooo!

public class Semantico {
    
    List<Token> list;
    List<Token> outraList;
    List<String> erros;
    Tree escopos;
    ArrayList<Simbolo> tabelaSimbolos;
    

    public Semantico(List<Token> l){
        list = l;
        tabelaSimbolos = new ArrayList<>();
        list.add(new Token("$","$",l.get(l.size()-1).getLinha()));
        erros = new ArrayList<>();
        this.outraList.addAll(list);
        escopos = new Tree();
    }
    
    public void preRun(){
        int j = 0, jmax = 0; //escopo Atual
        Queue<Integer> q = new PriorityQueue();
        for(int pos = 0; pos < outraList.size();pos++){
            Token t = outraList.get(pos);
            
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
            } else if(t.getLexema().equals("var")){
                String tipo = outraList.get(++pos).getLexema();
                while(!t.getLexema().equals(";")){                    
                    t = outraList.get(++pos);
                    if(t.getTipo().equals("identificador")){
                        tabelaSimbolos.add(new Simbolo(t.getLexema(), tipo, "variavel", j));
                    }
                }
                
            } else if(t.getLexema().equals("const")){
                String tipo = outraList.get(++pos).getLexema();
                while(!t.getLexema().equals(";")){                    
                    t = outraList.get(++pos);
                    if(t.getTipo().equals("identificador")){
                        tabelaSimbolos.add(new Simbolo(t.getLexema(), tipo, "constante", j));
                    }
                }
            } else if(t.getLexema().equals("funcao")){
                String tipo = outraList.get(++pos).getLexema();
                pos++;
                Simbolo novaFuncao = new Simbolo(t.getLexema(), tipo, "funcao", j);
                tabelaSimbolos.add(novaFuncao);
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
    
    public boolean jaExiste(String nome, int escopoAtual)
    {
        int escopoTeste = 0;
        for(int a = 0; a <= tabelaSimbolos.size(); a++)
        {
            if(nome.equals(tabelaSimbolos.get(a).nome))
            {
                escopoTeste = tabelaSimbolos.get(a).escopo;
                while(escopoTeste != 0)
                {
                    if(escopoTeste == escopoAtual) return true;
                    else
                    {
                        escopoTeste = escopos.nodeSearch(escopoTeste).pai.elem;
                    }
                }
                if(escopoTeste == 0)
                {
                    if(escopoTeste == escopoAtual) return true;
                }
            }
            else return false;
        }
        return false;
    }
    
}
