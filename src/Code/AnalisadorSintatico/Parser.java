/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSintatico;

import Code.Token;
import Exceptions.TokenEsperadoException;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

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
        i = 0;
        erros = new ArrayList<>();
    }
    
    public void EOFTeste()
    {
        if(i>=list.size())
        {
            erros.add("Fim de arquivo inesperado");
            return;
        }
    }
    
    public boolean accept(String tipoToken){    

    if(tipoToken.equals(list.get(i).getTipoCompleto())){
        i++;
        return true;
    } else {
        erros.add("Token "+tipoToken+" esperado na linha "+list.get(i).getLinha());
    }
        return false;
    }
    
    
    public void valor(){
        
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
    
    public void aux_valor1(){
        
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
    
    public void aux_valor2(){
        
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
    
    public void aux_valor3(){
        
        this.EOFTeste();
        this.valor();
        this.accept("delimitador_)");
        this.accept("delimitador_)");
        this.aux_valor4();
        
    }
    
    public void aux_valor4(){
        
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
    
    public void parametro(){
        
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
    
    public void r(){
        
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
    
    public void program(){
       
       this.EOFTeste();
       this.declaracao_var_global();
       this.declaracao_programa();
       this.funcoes();
        
    }
    
    public void declaracao_var_global(){
        
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
    
    public void funcoes(){
        
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
    
    public void fx(){
        
        this.EOFTeste();
        if("funcao".equals(list.get(i).getLexema()))
        {
            this.funcao();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void funcao(){
        
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
    
    public void d(){
        
        this.EOFTeste();
        this.tipo();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.d2();
        
    }
    
    public void d2(){
        
        this.EOFTeste();
        if(",".equals(list.get(i).getLexema()))
        {
            this.q();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void q(){
        
        this.EOFTeste();
        this.accept("delimitador_,");
        this.d();
        
    }
    
    public void retorno(){
        
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
    
    public void declaracao_programa(){
        
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
    
    public void parametro_programa(){
        
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
            if(",".equals(list.get(i).getLexema())) //P(parametro3)
            {
                this.parametro3();
            }
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void parametro3(){
        
        this.EOFTeste();
        this.accept("delimitador_,");
        this.parametro_programa();
        
    }
    
//    public void bloco_declaracao_global(){
//        
//        if("const".equals(list.get(i).getLexema()))
//        {
//            this.declaracao_const();
//            this.bloco_declaracao_global();
//        }
//        
//        else if("var".equals(list.get(i).getLexema()))
//        {
//            this.declaracao_var();
//            this.bloco_declaracao_global();
//        }
//        
//        else //aceita vazio
//        {
//            
//        }
//    }
    
    public void v1(){
        
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
    
//    public void cat(){
//        
//        if("delimitador".equals(list.get(i).getTipo()))
//        {
//            this.accept(list.get(i).getTipoCompleto());
//            
//            if("(".equals(list.get(i).getLexema()))
//            {
//                this.a();
//            }
//        }
//        else this.accept(list.get(i).getTipoCompleto());
//        
//    }
    
    public void a(){
        
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
    
    public void expressao_aritmetica(){
        
        this.EOFTeste();
        this.mult_exp();
        this.expressao_aritmeticaRec();
        
    }
    
    public void mult_exp(){
        
        this.EOFTeste();
        this.neg_exp();
        this.mult_expRec();
        
    }
    
    public void expressao_aritmeticaRec(){
        
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
    
    public void mult_expRec(){
        
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
    
    public void neg_exp(){
        
        this.EOFTeste();
        if("-".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_-");
        }
        this.valor();
    }
    
    public void bloco_de_codigo(){
        
        this.EOFTeste();
        this.accept("pal_reserv_inicio");
        this.bxr();
        this.accept("pal_reserv_fim");

    }
    
    public void bx(){
        
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
    
    public void escreva(){
        
        this.EOFTeste();
        this.accept("pal_reserv_escreva");
        this.accept("delimitador_(");
        this.retorno();
        this.accept("delimitador_)");
        this.accept("delimitador_;");
        
    }
    
    public void enquanto(){
        
        this.EOFTeste();
        this.accept("pal_reserv_enquanto");
        this.accept("delimitador_(");
        this.expressao_booleana();
        this.accept("delimitador_)");
        this.accept("pal_reserv_faca");
        this.bloco_de_codigo();
        
    }
    
    public void leia(){
        
        this.EOFTeste();
        this.accept("pal_reserv_leia");
        this.accept("delimitador_(");
        this.exp();
        this.accept("delimitador_)");
        this.accept("delimitador_;");
        
    }
    
    public void exp(){
        
        this.EOFTeste();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.aux_valor4();
        this.exp2();
    }
    
    public void exp2(){
        
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
    
    public void bxr(){
        
        this.EOFTeste();
        this.bx();
        this.bxr2();
        
    }
    
    public void bxr2(){
        
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
    
    public void se_entao_senao(){
        
        this.EOFTeste();
        this.accept("pal_reserv_se");
        this.accept("delimitador_(");
        this.expressao_booleana();
        this.accept("delimitador_)");
        this.accept("pal_reserv_entao");
        this.bloco_de_codigo();
        this.se();
    }
    
    public void se(){
        
        this.EOFTeste();
        if("senao".equals(list.get(i).getLexema()))
        {
            this.negacao();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void negacao(){
        
        this.EOFTeste();
        this.accept("pal_reserv_senao");
        this.bloco_de_codigo();
        
    }
    
    public void expressao_booleana(){
        
        this.EOFTeste();
        this.aux_expression();
        this.expressao_booleanaRec();
        
    }
    
    public void aux_expression(){
        
        this.EOFTeste();
        this.nao_expressao_aritmetica();
        this.aux_expressionRec();
        
    }
    
    public void aux_expressionRec(){
           
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
    
    public void expressao_booleanaRec(){
        
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
    
    public void nao_expressao_aritmetica(){
        
        this.EOFTeste();
        if("nao".equals(list.get(i).getLexema()))
        {
            this.accept("op_logico_nao");
            this.expressao_aritmetica();
        }
        else this.expressao_aritmetica();
    }
    
//    public void decx(){
//        
//        this.dec();
//        if("var".equals(list.get(i).getLexema()))
//        {
//            this.decx();
//        }
//    }
    
    public void dec(){
        
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
    
    public void tipo(){
        
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
    
    public void dec_const(){
        
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
    
    public void attr(){
        
        this.EOFTeste();
        if("identificador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        this.aux_valor1();
        this.attr1();
        
    }
    
    public void attr1(){
        
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
    
    public void attr2(){
        
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

/*

! -------------------------------------------------
! Character Sets
! -------------------------------------------------

{ID Head}        = {Letter}
{ID Tail}        = {Alphanumeric} + [_]
{IdAlphaNumeric} = {Alphanumeric}
{dot}            =[.]
{quote}          =['']
{String Char}    = {Printable} - ["]    
{ID NUM}         = {Digit} 
! -------------------------------------------------
! Terminals
! -------------------------------------------------

BooleanLiteral   = 'verdadeiro' | 'falso'
Identificador    = {ID Head}{IdAlphaNumeric}*|{ID Head}[_]{IdAlphaNumeric}*
numero           ={Digit}+ |{Digit}+[.]{Digit}+
tipo_cadeia      = ["]{Letter}{Alphanumeric}*["]
tipo_caractere   ={quote}[qazwsxedcrfvtgbyhnujmikolpAZQWSXCDEVFRTGBNHYUJMKILOP0123456789]{quote}
! -------------------------------------------------
! Rules
! -------------------------------------------------

!Configurações Iniciais
<bloco_de_codigo>    ::='inicio'<BXR>'fim'|<BX>
<BX>                 ::= <se_entao_senao>|<declaracao_var>|<TESTE_ID>|<Enquanto>|<Leia>|<Escreva>
<BXR>                ::= <BX><BXR2>
<BXR2>               ::= <BXR> | 

! The grammar starts below

<Program> ::= <BLOCO_DECLARACAO_GLOBAL><declaracao_programa><FX>|<BLOCO_DECLARACAO_GLOBAL><declaracao_programa>|<declaracao_programa>
<FX>      ::=<Funcao><FX>|<Funcao>

!Declaracao de Programa

<declaracao_programa>::='programa'<tipo> <id>'('<parametro_programa>')''inicio'<BXR>'fim'<return>
<parametro_programa> ::=<tipo> <id> <parametro3> |<tipo> <id>
<parametro3>         ::=','<parametro_programa> 
<RETORNO>            ::=<expressao_booleana>|tipo_caractere|tipo_cadeia|

!Correcao de Conflito
<id> ::= Identificador
       
!Expressão Booleana

<expressao_booleana>  ::= <Aux_Expression> <expressao_booleanaR> 
              
<expressao_booleanaR>  ::= 'e' <expressao_booleana>
               |'ou' <expressao_booleana>
               |
               
<Aux_Expression>  ::= <nao_expressao_aritmetica> <Aux_ExpressionR>
                   
<Aux_ExpressionR> ::= '>'  <Aux_Expression>
               | '<'  <Aux_Expression> 
               | '<='  <Aux_Expression>
               | '>='  <Aux_Expression>
               | '='  <Aux_Expression>
               | '<>'  <Aux_Expression>
               |  


<nao_expressao_aritmetica> ::= 'nao' <expressao_aritmetica> 
                            | <expressao_aritmetica> 
                            | '(' <expressao_booleana> ')'
                            
!Expressão Aritmetica

<expressao_aritmetica>     ::= <Mult Exp> <expressao_aritmeticaR>

<expressao_aritmeticaR>    ::= '+' <expressao_aritmetica>
               | '-' <expressao_aritmetica>
               |

<Mult Exp>    ::= <Neg Exp> <Mult ExpR>
               
<Mult ExpR>   ::= '*' <Mult Exp>
               | '/' <Mult Exp>
               |


<Neg Exp>  ::= '-' <Valor> 
               |  <Valor> 

!Add more values to the rule below - as needed

<Valor>       ::= <id>
               | numero
               |  '(' <expressao_aritmetica> ')'
               | BooleanLiteral
               |<chamada_funcao>
               |<acessa_matriz>
               

!Enquanto Faca

 <Enquanto>::='enquanto''('<expressao_booleana>')''faca'<bloco_de_codigo> 

!Se Entao Senao

<se_entao_senao>   ::='se' '('<expressao_booleana>')' 'entao'<bloco_de_codigo><SE>
           <SE>   ::= <NEGACAO>|       
<NEGACAO>          ::='senao'<bloco_de_codigo>

!Chamada de Função

!<chamada_funcao>::= <id>'('<parametro>')'
<chamada_funcao>::='('<parametro>
<parametro>     ::= <Valor><R> |tipo_cadeia<R>|<acessa_matriz><R>|tipo_caractere<R>|')'';'
<R>             ::=','<parametro> | ')'';'

!Atribuicao

!<Attr> ::= <id> '='<AttR2>
<Attr> ::= '='<AttR2>
<declaracao_matriz>::=<A>'='<AttR2>
<acessa_matriz>::=<id><A>
<AttR2>::= numero';'| <id>';' |<expressao_aritmetica>';'|tipo_cadeia';'|tipo_caractere';'|BooleanLiteral';'|<chamada_funcao>';'|<acessa_matriz>';'
          

!Correcao de Conflito
                        
<Teste_ID>::=<id><TESTE_ID2>
<TESTE_ID2>::=<Attr>|<chamada_funcao>|<declaracao_matriz>
            
! Declaração de Variaveis

<DEC>  ::='var'<tipo><CAT>';' | 'var'<tipo><CAT>','<V1> 
<V1>   ::=<CAT>','<V1> | <CAT>';'  !alteracao
<CAT>  ::= <id> | <id><A>
<A>    ::= '(''('numero')'')'<A>|'(''('<id>')'')'<A> |'(''('<acessa_matriz>')'')'<A>|'(''('numero')'')'|'(''('<id>')'')'|'(''('<acessa_matriz>')'')'
<tipo> ::='inteiro' | 'cadeia' |'real'|'booleano'|'caractere'

!declaracao

<declaracao_var>   ::=<DECX>
      <DECX>       ::=<DEC><DECX>|<DEC>
 <declaracao_const>::=<CONSTX>
          <CONSTX> ::=<DEC_CONST><CONSTX>|<DEC_CONST>             
!Declaração de constante
<DEC_CONST>::='const' <tipo> <id> '=' <valor>';'

<BLOCO_DECLARACAO_GLOBAL>::=<declaracao_var><BLOCO_DECLARACAO_GLOBAL>|<declaracao_const><BLOCO_DECLARACAO_GLOBAL>|
                          
!Leia

<Leia>  ::='leia''('<Exp>
<Exp>::=<id>')'';'| <id><FATOR_LEIA> |')'';'
<FATOR_LEIA>  ::=','<Exp>

!Escreva

<Escreva>  ::='escreva''('<EXPRESSAO>
<EXPRESSAO>::=<RETORNO>')'';'|<RETORNO><FATOR_ESCREVA>|<expressao_booleana>')'';'|<expressao_booleana><FATOR_ESCREVA>
<FATOR_ESCREVA>  ::=','<EXPRESSAO>

!Funcoes
      
 <Funcao>      ::='funcao' <tipo> <id> '('<D>')' 'inicio'<BXR> 'fim'<return>
 <return>::='('<RETORNO>')'';'|<RETORNO>
 <D>      ::=<tipo><id><Q> |<tipo><id>
 <Q>      ::=','<D>


*/