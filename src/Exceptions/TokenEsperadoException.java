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
public class TokenEsperadoException extends Exception{
     String tipo;

    public TokenEsperadoException(String tipo) {
        this.tipo = tipo;
    }
}
