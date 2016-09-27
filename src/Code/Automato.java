package Code;

import Exceptions.MalformadoException;
import java.util.ArrayList;

public class Automato {
    
    static String palavrasReservadas[] = {"programa","const","var","funcao","inicio",
                                    "fim","se","entao","senao","enquanto","faca",
                                    "leia","escreva","inteiro","real","booleano",
                                    "verdadeiro", "falso","cadeia","caractere", "nao", "e", "ou"};
    
    public static Token reconhecerToken(String teste) throws MalformadoException{
        int ch = 0, estadoAtual = 0;
        Token t = null;
    
    while(ch<teste.length() && estadoAtual!= -1){
        Character c = teste.charAt(ch);
        ch++;
        
        switch(estadoAtual){
            
            case 0: 
                    if(isLetter(c)){
                        estadoAtual = 1;
                        t = new Token(teste, "identificador");
                    }
                    else if(isDigit(c)){
                        estadoAtual = 3;
                        t = new Token(teste, "numero");
                    }
                    else if(c=='-'){
                        estadoAtual = 2;
                        t = new Token(teste, "op_arit");
                    }
                    else if(c=='"'){
                        estadoAtual = 4;
                        t = new Token(teste, "cadeia");
                    }
                    else if(c=='\''){
                        estadoAtual = 5;
                        t = new Token(teste, "digito");
                    }
                    else if(c=='+'){
                        estadoAtual = 6;
                        t = new Token(teste, "op_arit");
                    }
                    else if(c=='*'){
                        estadoAtual = 6;
                        t = new Token(teste, "op_arit");
                    }
                    else if(c=='/'){
                        estadoAtual = 6;
                        t = new Token(teste, "op_arit");
                    }
                    else if(c==';'){
                        estadoAtual = 7;
                        t = new Token(teste, "delimitador");
                    }
                    else if(c==','){
                        estadoAtual = 7;
                        t = new Token(teste, "delimitador");
                    }
                    else if(c=='('){
                        estadoAtual = 7;
                        t = new Token(teste, "delimitador");
                    }
                    else if(c==')'){
                        estadoAtual = 7;
                        t = new Token(teste, "delimitador");
                    }
                    else if(c=='<'){
                        estadoAtual = 8;
                        t = new Token(teste, "op_relac");
                    }
                    else if(c=='='){
                        estadoAtual = 9;
                        t = new Token(teste, "op_relac");
                    }
                    else if(c=='>'){
                        estadoAtual = 10;
                        t = new Token(teste, "op_relac");
                    }
                    else{
                        estadoAtual = -1;
                        t = new Token(teste, "invalido");
                    }
                    break;
                    
            case 1: 
                    t = new Token(teste, "identificador");
                    if(isLetterOrDigit(c) || c=='_')
                        estadoAtual = 1;
                    else
                        estadoAtual = -1;
                    break;
                    
            case 2:     
                    if(isDigit(c)){
                        estadoAtual = 3;
                        t = new Token(teste, "numero");
                    }
                    else
                        estadoAtual = -1;
                    break;
                    
            case 3: t = new Token(teste, "numero");
                    if(isDigit(c))
                        estadoAtual = 3;
                    else if(c=='.')
                        estadoAtual = 13;
                    else
                        estadoAtual = -1;
                    break;
                    
            case 4: t = new Token(teste, "cadeia");
                    if(isLetter(c))
                        estadoAtual = 14;
                    else
                        estadoAtual = -1;
                    break;
                    
            case 5: t = new Token(teste, "caractere");
                    if(isLetterOrDigit(c))
                        estadoAtual = 15;
                    else
                        estadoAtual = -1;
                    break;
                    
            case 6: t = new Token(teste, "op_arit");
                    estadoAtual = -1;
                    break;
                    
            case 7: t = new Token(teste, "delimitador");
                    estadoAtual = -1;
                    break;
            
            case 8: t = new Token(teste, "op_relac");
                    if (c=='>' || c=='=')
                        estadoAtual = 9;
                    else estadoAtual = -1;    
                    break;
                    
            case 9: t = new Token(teste, "op_relac");
                    estadoAtual = -1;
                    break;
                    
            case 10:t = new Token(teste, "op_relac");
                    if(c=='=')
                        estadoAtual = 9;
                    else
                        estadoAtual = -1;
                    break;
                    
            case 13:
                    if(isDigit(c))
                        estadoAtual = 17;
                    else
                        estadoAtual = -1;
                    break;
            case 14:
                    if(isLetterOrDigit(c) || c==' ')
                        estadoAtual = 14;
                    else if(c=='"')
                        estadoAtual = 18;
                    else estadoAtual = -1;
                    break;
            
            case 15:
                    if(c=='\'')
                        estadoAtual = 16;
                    else estadoAtual = -1;
                    break;
            case 16:
                    estadoAtual = -1;
                    break;
            
            case 17:
                    if(isDigit(c))
                        estadoAtual = 17;
                    else
                        estadoAtual = -1;
                    break;
            
            case 18: 
                    estadoAtual = -1;
                    break;
            
            default: 
                    estadoAtual = -1;
                    break;        
            
        }
    }
    
    //Reconhecendo estados finais
    if(t==null) throw new MalformadoException(new Token(teste, "invalido"));
    
        switch (estadoAtual) {
            case 1:
                for(String a: palavrasReservadas)
                    if(a.equals(teste))
                        return new Token(teste, "pal_reserv");
                break;
                
            case -1:
                throw new MalformadoException(t);
        }
        return t;
    }
    
    public static ArrayList<String> splitTokens(String s){
      ArrayList<String> strings = new ArrayList<>();
      StringBuilder sb = new StringBuilder();
      int estadoAtual = 0;
      for(Character c: s.toCharArray()){
          //System.out.println("EA: "+estadoAtual);
          switch(estadoAtual){
              case 0:
                    if(isDigit(c)){
                        estadoAtual = 3;
                        sb.append(c);
                    }
                    else if(c=='-'){
                        estadoAtual = 2;
                        sb.append(c);
                    }
                    else if(c=='"'){
                        estadoAtual = 4;
                        sb.append(c);
                    }
                    else if(c=='\''){
                        estadoAtual = 5;
                        sb.append(c);
                    }
                    else if(isOpRel(c)){
                        estadoAtual = 6;
                        sb.append(c);
                    }
                    else if(isAutoDelim(c)){
                        estadoAtual = 0;
                        strings.add(c.toString());
                    }
                    else if(c=='{'){
                        estadoAtual = 7;
                    }
                    else if(c=='}'){
                        estadoAtual = 0;
                    }
                    else if(c==' ');
                    else{
                        estadoAtual = 1;
                        sb.append(c);
                    }
                    break;
                    
              case 1:
                  if(c=='-'){
                        estadoAtual = 2;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                  }else if(isAutoDelim(c)){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        strings.add(c.toString());
                        sb.delete(0, sb.length());
                    } else if(isOpRel(c)){
                        estadoAtual = 6;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c==' '){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='\"'){
                        estadoAtual = 4;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='\''){
                        estadoAtual = 5;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='{'){
                        estadoAtual = 7;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='}'){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {
                        sb.append(c);
                    }
                    break;
              case 2:
                    
                    if(isDigit(c)){
                        estadoAtual = 3;
                        sb.append(c);
                    } else if(c=='-'){
                        estadoAtual = 2;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                  }else if(isAutoDelim(c)){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        strings.add(c.toString());
                        sb.delete(0, sb.length());
                    } else if(isOpRel(c)){
                        estadoAtual = 6;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c==' '){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='\"'){
                        estadoAtual = 4;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='\''){
                        estadoAtual = 5;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='{'){
                        estadoAtual = 7;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='}'){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {
                        estadoAtual = 1;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    }
                    break;
                    
              case 3:
                  if(c=='-'){
                        estadoAtual = 2;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                  }else if(isAutoDelim(c)){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        strings.add(c.toString());
                        sb.delete(0, sb.length());
                    } else if(isOpRel(c)){
                        estadoAtual = 6;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c==' '){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='\"'){
                        estadoAtual = 4;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='\''){
                        estadoAtual = 5;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='{'){
                        estadoAtual = 7;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='}'){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {
                        sb.append(c);
                    }
                    break;
                  
              case 4:
                    if(c=='\"'||c=='\n'){
                        estadoAtual = 0;
                        sb.append(c);
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {
                        sb.append(c);
                    }
                    break;
              case 5:
                    if(c=='\''||c=='\n'){
                        estadoAtual = 0;
                        sb.append(c);
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {
                        sb.append(c);
                    }
                    break;
                    
              case 6:
                  if(isOpRel(c)){
                        estadoAtual = 6;
                        sb.append(c);
                  } else if(isDigit(c)){
                        estadoAtual = 3;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                  } else if(c=='-'){
                        estadoAtual = 2;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                  }else if(isAutoDelim(c)){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        strings.add(c.toString());
                        sb.delete(0, sb.length());
                    } else if(c==' '){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='\"'){
                        estadoAtual = 4;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='\''){
                        estadoAtual = 5;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    } else if(c=='{'){
                        estadoAtual = 7;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else if(c=='}'){
                        estadoAtual = 0;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {
                        estadoAtual = 1;
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(c);
                    }
                    break;
              case 7:
                    if(c=='}'||c=='\n'){
                        estadoAtual = 0;
                    }
                    break;
          }
      }
      if(!sb.toString().trim().equals(""))
        strings.add(sb.toString().trim());
      return strings;
  }
    
    private static boolean isLetter(char c){
        int a = Character.getType(c);
        return a==Character.UPPERCASE_LETTER || a==Character.LOWERCASE_LETTER;
    }
    private static boolean isDigit(char c){
        return Character.isDigit(c);
    }
    private static boolean isLetterOrDigit(char c){
        return (isLetter(c)||isDigit(c));
    }
    private static boolean isOpArit(Character c){
      return c=='+'||c=='*'||c=='/'||c=='-';
  }
    private static boolean isOpRel(Character c){
      return (c=='<'||c=='='||c=='>');
  }
    private static boolean isDel(Character c){
      return (c==';'||c==','||c=='('||c==')');
  }
    private static boolean isAutoDelim(Character c){
      if(c=='-') return false;
      return isOpArit(c)||isDel(c)||c=='}';
  }
}
