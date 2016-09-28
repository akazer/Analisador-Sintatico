/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSintatico;

import Code.Token;
import Exceptions.TokenEsperadoException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Leonardo Santana
 */
public class Parser2 {
    List<Token> list;
    Integer i;
    List<String> erros;
    
    public Parser2(List<Token> l){
        list = l;
        i = 0;
        erros = new ArrayList<>();
    }
    
    public boolean accept(String tipoToken) {
        if(tipoToken.equals(list.get(i).getTipoCompleto())){
            i++;
        } else {
            erros.add("Token "+tipoToken+" esperado na linha "+list.get(i).getLinha());
        }
        return false;
    }

    
}
