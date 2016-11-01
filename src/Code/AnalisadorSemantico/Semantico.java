/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSemantico;

import Code.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

//Vohayoooo!

public class Semantico {
    
    List<Token> list;
    List<Token> outraList;
    Integer i, j;
    List<String> erros;
    LinkedHashMap escopos;
    ArrayList<Simbolo> tabelaSimbolos;
    

    public Semantico(List<Token> l){
        list = l;
        tabelaSimbolos = new ArrayList<>();
        list.add(new Token("$","$",l.get(l.size()-1).getLinha()));
        i = 0;
        erros = new ArrayList<>();
        this.outraList.addAll(list);
    }
    
    public void preRun(){
        int j = 0; //escopo Atual
        for(int pos = 0; pos < outraList.size();pos++){
            Token t = outraList.get(pos);
            
            if(t.getLexema().equals("inicio")){
                j++; //isso ta errado
            }else if(t.getLexema().equals("fim")){
                j--; //isso ta errado
            } else if(t.getTipo().equals("identificador")){
                //TODO 
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
    
}
