/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import Code.Token;

/**
 *
 * @author Leonardo Santana
 */
public class MalformadoException extends Exception{
    Token token;

    public MalformadoException(Token token) {
        this.token = token;
    }
    
    public Token getToken(){
        return token;
    }
    
}
