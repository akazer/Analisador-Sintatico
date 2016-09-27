package Code;

public class Token {
    private String lexema;
    private String tipo;
    private Integer linha;
    
    public Token(String lexema, String tipo, Integer linha){
        this.lexema = lexema;
        this.tipo = tipo;
        this.linha = linha;
    }
    
    public String getLexema(){
        return lexema;
    }
    
    public String getTipo(){
        return tipo;
    }
    
    public String getTipoCompleto(){
        return tipo+"_"+lexema;
    }
    
    public Integer getLinha(){
        return linha;
    }
}
//teste
//teste de novo