/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSemantico.util;

import java.util.ArrayList;

/**
 *
 * @author Leonardo Santana
 */
public class Node {
    public ArrayList<Node> filhos;
    public Node pai;
    public int elem;
    
    public Node(Node pai, int valor){
        filhos = new ArrayList<>();
        this.pai = pai;
        elem = valor;
    }
}
