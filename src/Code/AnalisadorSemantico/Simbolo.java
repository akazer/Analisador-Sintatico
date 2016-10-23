/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSemantico;

import java.util.List;

public class Simbolo {
    
    String nome;
    String tipoId;
    String tipo;
    int escopo;
    List<String> parametros;
    
    public Simbolo(String nome, String tipoId, String tipo, int escopo){
        
        this.nome = nome;
        this.tipoId = tipoId;
        this.tipo = tipo;
        this.escopo = escopo;
    }
    
    public void addParametro(String tipoParametro){
        
        this.parametros.add(tipoParametro);
        
    }
    
}