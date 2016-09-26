/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code.AnalisadorSintatico;

import Code.Token;
import Exceptions.TokenEsperadoException;
import java.util.List;

/**
 *
 * @author Leonardo Santana
 */
public class Parser{
    List<Token> list;
    Integer i;
    
    public Parser(List<Token> l){
        list = l;
        i = 0;
    }
    
    public boolean accept(String tipoToken) throws TokenEsperadoException{
        if(tipoToken.equals(list.get(i).getTipoCompleto())){
            i++;
        } else {
            throw new TokenEsperadoException(tipoToken);
        }
        return false;
    }
    
    
    public void valor() throws TokenEsperadoException{
        if("(".equals(list.get(i).getLexema()))
        {
            this.accept("delimitador_(");
            if()
            
            this.expressao_aritmetica(); //adicionar chamada_funcao
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
            this.accept(list.get(i).getTipoCompleto()); //adicionar chamada_matriz
        }
        
        else if("numero".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
        }
        
        else //case default
        {
            this.accept(list.get(i).getTipoCompleto());
        }
    }
    
    public void v1() throws TokenEsperadoException{
        
        this.cat();
        
        if("delimitador".equals(list.get(i).getTipo()))
        {
            if(",".equals(list.get(i).getLexema()))
            {
                accept("delimitador_,");
                
                this.v1();
            }
            else if(";".equals(list.get(i).getLexema()))
            {
                this.cat();
                
                accept("delimitador_;");
            }
            else this.accept(list.get(i).getTipoCompleto());
        }
        else this.accept(list.get(i).getTipoCompleto());
        
    }
    
    public void cat() throws TokenEsperadoException{
        
        if("delimitador".equals(list.get(i).getTipo()))
        {
            this.accept(list.get(i).getTipoCompleto());
            
            if("(".equals(list.get(i).getLexema()))
            {
                this.a();
            }
        }
        else this.accept(list.get(i).getTipoCompleto());
        
    }
    
    public void a() throws TokenEsperadoException{
        
        this.accept("delimitador_(");
        this.accept("delimitador_("); //espera dois ( mesmo
        
        if("numero".equals(list.get(i).getTipo())) // ((numero))A | ((numero))
        {
            this.accept(list.get(i).getTipoCompleto());
            this.accept("delimitador_)");
            this.accept("delimitador_)");
            if("(".equals(list.get(i).getLexema())) this.a();
        }
        
        else if("identificador".equals(list.get(i).getTipo())) // ((id))A | ((id)) | ((acessa_matriz)) | ((acessa_matriz))A
        {
            this.accept(list.get(i).getTipoCompleto());
            if("(".equals(list.get(i).getLexema()))
            {
                this.a();
            }
            if(")".equals(list.get(i).getLexema()))
            {
                this.accept("delimitador_)");
                this.accept("delimitador_)");
            }
        }
        else
        {
            this.accept(list.get(i).getTipoCompleto());
        }
    }
    
    public void expressao_aritmetica() throws TokenEsperadoException{
        
        this.mult_exp();
        this.expressao_aritmeticaRec();
        
    }
    
    public void mult_exp() throws TokenEsperadoException{
        
        this.neg_exp();
        this.mult_expRec();
        
    }
    
    public void expressao_aritmeticaRec() throws TokenEsperadoException{
        
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
    
    public void mult_expRec() throws TokenEsperadoException{
        
        if("*".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_*");
            this.expressao_aritmetica();
        }
        else if("/".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_/");
            this.expressao_aritmetica();
        }
        else //reconhece vazio
        {
            
        }
    }
    
    public void neg_exp() throws TokenEsperadoException{
        
        if("-".equals(list.get(i).getLexema()))
        {
            this.accept("op_arit_-");
        }
        
        this.valor();
    }
    
    public void bloco_de_codigo() throws TokenEsperadoException{
        
        if("inicio".equals(list.get(i).getLexema()))
        {
            this.accept("pal_reserv_inicio");
            this.brx();
            this.accept("pal_reserv_fim");
        }
        else this.bx();
    }
    
    public void bx() throws TokenEsperadoException{
        
        if("se".equals(list.get(i).getLexema()))
        {
            this.se_entao_senao();
        }
        else if("var".equals(list.get(i).getLexema()))
        {
            this.declaracao_var();
        }
        else if("identificador".equals(list.get(i).getTipo()))
        {
            this.teste_id();
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
        else this.accept(list.get(i).getTipoCompleto());
    }
    
    public void se_entao_senao() throws TokenEsperadoException{
        
        this.accept("pal_reserv_se");
        this.accept("delimitador_(");
        this.expressao_booleana();
        this.accept("delimitador_)");
        this.accept("pal_reserv_entao");
        this.bloco_de_codigo();
        this.se();
    }
    
    public void se(){
        
        if("senao".equals(list.get(i).getLexema()))
        {
            this.negacao();
        }
        else //aceita vazio
        {
            
        }
    }
    
    public void declaracao_var(){
        
        
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