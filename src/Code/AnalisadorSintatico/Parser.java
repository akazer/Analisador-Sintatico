/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSintatico;

import Code.Token;
import java.io.EOFException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonardo Santana
 */
public class Parser{
    List<Token> list;
    Integer i;
    List<String> erros;
    
    public Parser(List<Token> l){
        list = l;
        list.add(new Token("$","$",l.get(l.size()-1).getLinha()));
        i = 0;
        erros = new ArrayList<>();
    }
    
    public List<String> getErros(){
        return erros;
    }
    
    public void execute(){
        erros = new ArrayList<>();
        i = 0;
        try {
            program();
        } catch (EOFException ex) {
            erros.add("Erro: EOF Inesperado");
        }
    }
    
    public void EOFTeste() throws EOFException    {
        if(i>=list.size())
        {
            erros.add("Fim de arquivo inesperado");
            throw new EOFException();
        }
    }
    
    public boolean accept(String tipoToken) throws EOFException{    
    if(i>=list.size()) throw new EOFException();
    if(list.get(i).getLexema().equals("$")) throw new EOFException();
    if(tipoToken.equals(list.get(i).getTipoCompleto())){
        i++;
        return true;
    } else {
        erros.add("Token "+tipoToken+" esperado na linha "+list.get(i).getLinha());
    }
        return false;
    }
    
    public void valor()throws EOFException{
        
        this.EOFTeste();
        if("(".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_(");
            this.expressao_booleana();
            this.accept("delimitador_)");
        }
        
        else if("pal_reserv".equals(list.get(i).getTipo()))
        {
            if("verdadeiro".equals(list.get(i).getLexema()))
            {
                this.accept("pal_reserv_verdadeiro");
            }
            if("falso".equals(list.get(i).getLexema()))
            {
                this.accept("pal_reserv_falso");
            }
        }
        
        else if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
            this.aux_valor1();
        }
        
        else if("numero".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        
        else //case default
        {
            erros.add("Valor esperado na linha " + list.get(i).getLinha());
        }
    }
    
    public void aux_valor1()throws EOFException{
        
        this.EOFTeste();
        if("(".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_(");
            this.aux_valor2();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void aux_valor2()throws EOFException{
        
        this.EOFTeste();
        if("(".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_(");
            this.aux_valor3();
        }
        else 
        {
            this.parametro();
            this.accept("delimitador_)");
        }
    }
    
    public void aux_valor3()throws EOFException{
        
        this.EOFTeste();
        this.valor();
        this.accept("delimitador_)");
        this.accept("delimitador_)");
        this.aux_valor4();
        
    }
    
    public void aux_valor4()throws EOFException{
        
        this.EOFTeste();
        if("(".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_(");
            this.accept("delimitador_(");
            this.valor();
            this.accept("delimitador_)");
            this.accept("delimitador_)");
            this.aux_valor4();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void parametro()throws EOFException{
        
        this.EOFTeste();
        if("cadeia".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        else if( //P(expressao_booleana)
                "-".equals(list.get(i).getLexema())             ||
                "(".equals(list.get(i).getLexema())             ||
                "verdadeiro".equals(list.get(i).getLexema())    ||
                "falso".equals(list.get(i).getLexema())         ||
                "identificador".equals(list.get(i).getLexema()) ||
                "nao".equals(list.get(i).getLexema())           ||
                "numero".equals(list.get(i).getLexema())
               )
        {
            this.expressao_booleana();
            this.r();
        }
        else if("caractere".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
            this.r();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void r()throws EOFException{
        
        this.EOFTeste();
        if(",".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_,");
            this.parametro();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void program()throws EOFException{
       
       this.EOFTeste();
       this.declaracao_var_global();
       this.declaracao_programa();
       this.funcoes();
        
    }
    
    public void declaracao_var_global()throws EOFException{
        
        this.EOFTeste();
        if("const".equals(list.get(i).getLexema()))
        {
            this.dec_const();
            this.declaracao_var_global();
        }
        else if("var".equals(list.get(i).getLexema()))
        {
            this.dec();
            this.declaracao_var_global();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void funcoes()throws EOFException{
        
        this.EOFTeste();
        if("funcao".equals(list.get(i).getLexema()))
        {
            this.funcao();
            this.fx();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void fx()throws EOFException{
        
        this.EOFTeste();
        if("funcao".equals(list.get(i).getLexema()))
        {
            this.funcao();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void funcao()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_funcao");
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.accept("delimitador_(");
        this.d();
        this.accept("delimitador_)");
        this.accept("pal_reserv_inicio");
        this.bxr();
        this.accept("pal_reserv_fim");
        this.accept("delimitador_(");
        this.retorno();
        this.accept("delimitador_)");
        this.accept("delimitador_;");
        
    }
    
    public void d()throws EOFException{
        
        this.EOFTeste();
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.d2();
        
    }
    
    public void d2()throws EOFException{
        
        this.EOFTeste();
        if(",".equals(list.get(i).getLexema()))
        {
            this.q();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void q()throws EOFException{
        
        this.EOFTeste();
        this.accept("delimitador_,");
        this.d();
        
    }
    
    public void retorno()throws EOFException{
        
        this.EOFTeste();
        if("caractere".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        else if("cadeia".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        else if( //P(expressao_booleana)
                "-".equals(list.get(i).getLexema())             ||
                "(".equals(list.get(i).getLexema())             ||
                "verdadeiro".equals(list.get(i).getLexema())    ||
                "falso".equals(list.get(i).getLexema())         ||
                "identificador".equals(list.get(i).getLexema()) ||
                "nao".equals(list.get(i).getLexema())           ||
                "numero".equals(list.get(i).getLexema())
               )
        {
            this.expressao_booleana();
        }
        else //aceita vazio
        {
            
        }
        
    }
    
    public void declaracao_programa()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_programa");
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.accept("delimitador_(");
        this.parametro_programa();
        this.accept("delimitador_)");
        this.accept("pal_reserv_inicio");
        this.bxr();
        this.accept("pal_reserv_fim");
        this.accept("delimitador_(");
        this.retorno();
        this.accept("delimitador_)");
        this.accept("delimitador_;");
        
    }
    
    public void parametro_programa()throws EOFException{
        
        this.EOFTeste();
        if( //P(tipo)
            "booleano".equals(list.get(i).getLexema())   ||
            "cadeia".equals(list.get(i).getTipo())       ||
            "caractere".equals(list.get(i).getTipo())    ||
            "inteiro".equals(list.get(i).getLexema())    ||
            "real".equals(list.get(i).getLexema())
           )
        {
            this.tipo();
            if("identificador".equals(list.get(i).getTipo()))
            {
                this.accept(list.get(i).getTipoCompleto());
            }
            this.parametro2();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void parametro2()throws EOFException{
        this.EOFTeste();
        if(",".equals(list.get(i).getLexema())) //P(parametro3)
        {
            this.parametro3();
        }
    }
    
    public void parametro3()throws EOFException{
        
        this.EOFTeste();
        this.accept("delimitador_,");
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.parametro2();
    }
    
    public void v1()throws EOFException{
        
        if(",".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_,");
            if("identificador".equals(list.get(i).getTipo()))
            {
            this.accept(list.get(i).getTipoCompleto());
            }
            this.a();
            this.v1();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void a()throws EOFException{
        
        this.EOFTeste();
        if("(".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_(");
            this.accept("delimitador_("); //espera dois ( mesmo
            if("numero".equals(list.get(i).getTipo()))
            {
                this.accept(list.get(i).getTipoCompleto());
            }
            this.accept("delimitador_)");
            this.accept("delimitador_)");
            this.a();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void expressao_aritmetica()throws EOFException{
        
        this.EOFTeste();
        this.mult_exp();
        this.expressao_aritmeticaRec();
        
    }
    
    public void mult_exp()throws EOFException{
        
        this.EOFTeste();
        this.neg_exp();
        this.mult_expRec();
        
    }
    
    public void expressao_aritmeticaRec()throws EOFException{
        
        this.EOFTeste();
        if("+".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_+");
            this.expressao_aritmetica();
        }
        else if("-".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_-");
            this.expressao_aritmetica();
        }
        else //reconhece vazio
        {
            
        }
    }
    
    public void mult_expRec()throws EOFException{
        
        this.EOFTeste();
        if("*".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_*");
            this.mult_exp();
        }
        else if("/".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_/");
            this.mult_exp();
        }
        else //reconhece vazio
        {
            
        }
    }
    
    public void neg_exp()throws EOFException{
        
        this.EOFTeste();
        if("-".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_-");
        }
        this.valor();
    }
    
    public void bloco_de_codigo()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_inicio");
        this.bxr();
        this.accept("pal_reserv_fim");

    }
    
    public void bx()throws EOFException{
        
        this.EOFTeste();
        if("se".equals(list.get(i).getLexema()))
        {
            this.se_entao_senao();
        }
        else if("var".equals(list.get(i).getLexema()))
        {
            this.dec();
        }
        else if("identificador".equals(list.get(i).getTipo()))
        {
            this.attr();
        }
        else if("enquanto".equals(list.get(i).getLexema()))
        {
            this.enquanto();
        }
        else if("leia".equals(list.get(i).getLexema()))
        {
            this.leia();
        }
        else if("escreva".equals(list.get(i).getLexema()))
        {
            this.escreva();
        }
        else //case default
        {
            erros.add("Início de bloco de código esperado na linha "+list.get(i).getLinha());
        }
    }
    
    public void escreva()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_escreva");
        this.accept("delimitador_(");
        this.retorno();
        this.accept("delimitador_)");
        this.accept("delimitador_;");
        
    }
    
    public void enquanto()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_enquanto");
        this.accept("delimitador_(");
        this.expressao_booleana();
        this.accept("delimitador_)");
        this.accept("pal_reserv_faca");
        this.bloco_de_codigo();
        
    }
    
    public void leia()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_leia");
        this.accept("delimitador_(");
        this.exp();
        this.accept("delimitador_)");
        this.accept("delimitador_;");
        
    }
    
    public void exp()throws EOFException{
        
        this.EOFTeste();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.aux_valor4();
        this.exp2();
    }
    
    public void exp2()throws EOFException{
        
        this.EOFTeste();
        if(",".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_,");
            this.exp();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void bxr()throws EOFException{
        
        this.EOFTeste();
        this.bx();
        this.bxr2();
        
    }
    
    public void bxr2()throws EOFException{
        
        this.EOFTeste();
        if(
           "enquanto".equals(list.get(i).getLexema())       ||
           "escreva".equals(list.get(i).getLexema())        ||
           "identificador".equals(list.get(i).getTipo())    ||
           "leia".equals(list.get(i).getLexema())           ||
           "se".equals(list.get(i).getLexema())             ||
           "var".equals(list.get(i).getLexema())
          )
        {
            this.bxr();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void se_entao_senao()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_se");
        this.accept("delimitador_(");
        this.expressao_booleana();
        this.accept("delimitador_)");
        this.accept("pal_reserv_entao");
        this.bloco_de_codigo();
        this.se();
    }
    
    public void se()throws EOFException{
        
        this.EOFTeste();
        if("senao".equals(list.get(i).getLexema()))
        {
            this.negacao();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void negacao()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_senao");
        this.bloco_de_codigo();
        
    }
    
    public void expressao_booleana()throws EOFException{
        
        this.EOFTeste();
        this.aux_expression();
        this.expressao_booleanaRec();
        
    }
    
    public void aux_expression()throws EOFException{
        
        this.EOFTeste();
        this.nao_expressao_aritmetica();
        this.aux_expressionRec();
        
    }
    
    public void aux_expressionRec()throws EOFException{
           
        this.EOFTeste();
        if(">".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_>");
            this.aux_expression();
        }
        else if("<".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_<");
            this.aux_expression();
        }
        else if("<=".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_<=");
            this.aux_expression();
        }
        else if(">=".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_>=");
            this.aux_expression();
        }
        else if("=".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_=");
            this.aux_expression();
        }
        else if("<>".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_<>");
            this.aux_expression();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void expressao_booleanaRec()throws EOFException{
        
        this.EOFTeste();
        if("e".equals(list.get(i).getLexema()))
        {
            this.accept("op_logico_e");
            this.expressao_booleana();
        }
        else if("ou".equals(list.get(i).getLexema()))
        {
            this.accept("op_logico_ou");
            this.expressao_booleana();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void nao_expressao_aritmetica()throws EOFException{
        
        this.EOFTeste();
        if("nao".equals(list.get(i).getLexema()))
        {
            this.accept("op_logico_nao");
            this.expressao_aritmetica();
        }
        else this.expressao_aritmetica();
    }
    
    public void dec()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_var");
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.a();
        this.v1();
        this.accept("delimitador_;");
        
    }
    
    public void tipo()throws EOFException{
        
        this.EOFTeste();
        if("inteiro".equals(list.get(i).getLexema()))
        {
            this.accept("pal_reserv_inteiro");
        }
        else if("cadeia".equals(list.get(i).getLexema()))
        {
            this.accept("pal_reserv_cadeia");
        }
        else if("real".equals(list.get(i).getLexema()))
        {
            this.accept("pal_reserv_real");
        }
        else if("booleano".equals(list.get(i).getLexema()))
        {
            this.accept("pal_reserv_booleano");
        }
        else if("caractere".equals(list.get(i).getLexema()))
        {
            this.accept("pal_reserv_caractere");
        }
    }
    
    public void dec_const()throws EOFException{
        
        this.EOFTeste();
        this.accept("pal_reserv_const");
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.accept("op_relac_=");
        
        if("numero".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        else if("caractere".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        else if("cadeia".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        
        this.accept("delimitador_;");
    }
    
    public void attr()throws EOFException{
        
        this.EOFTeste();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.aux_valor1();
        this.attr1();
        
    }
    
    public void attr1()throws EOFException{
        
        this.EOFTeste();
        if("=".equals(list.get(i).getLexema()))
        {
            this.accept("op_relac_=");
            this.attr2();
        }
        else if(";".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_;");
        }
        else //case default
        {
            erros.add("Esperado = ou ; na linha "+list.get(i).getLinha());
        }   
    }
    
    public void attr2()throws EOFException{
        
        this.EOFTeste();
        if //P(expressao_aritmetica)
         (
                "-".equals(list.get(i).getLexema())             ||
                "(".equals(list.get(i).getLexema())             ||
                "verdadeiro".equals(list.get(i).getLexema())    ||
                "falso".equals(list.get(i).getLexema())         ||
                "identificador".equals(list.get(i).getTipo())   ||
                "numero".equals(list.get(i).getTipo())
         )
        {
            this.expressao_aritmetica();
            this.accept("delimitador_;");
        }
        else if("cadeia".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
            this.accept("delimitador_;");
        }
        else if("caractere".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
            this.accept("delimitador_;");
        }
        else //case default
        {
            erros.add("Esperado primeiro de expressão aritmética, uma cadeia ou um caractere na linha "+list.get(i).getLinha());
        }
        
    }
}