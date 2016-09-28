package Code;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Exceptions.MalformadoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

class Main { 
  public static void main(String[] args) {
      Scanner s = new Scanner(System.in);
      File arquivos[];
      System.out.println("Digite o diretorio onde se encontram os arquivos:");
      //Lê todos os arquivos do diretorio. Cuidado.
      File diretorio = new File(s.nextLine());
      arquivos = diretorio.listFiles();
      
      for(File arquivo:arquivos){
          System.out.println(arquivo.getAbsolutePath());
          try {
              FileReader fr = new FileReader(arquivo);
              BufferedReader br = new BufferedReader(fr);
              File arqSaida = new File(arquivo.getPath()+"_compiled.txt");
              if(!arqSaida.exists()){
                  arqSaida.createNewFile();
              }
              FileWriter fw = new FileWriter(arqSaida);
              BufferedWriter bw = new BufferedWriter(fw);
              
              ArrayList<Token> tokens = new ArrayList<Token>();
              ArrayList<Integer> token_line = new ArrayList<Integer>();
              
              
              // Analise Lexica
              bw.newLine();
              
              while(br.ready()){
                    ArrayList<String> lista;
                    int linha=0;
                    while(br.ready()){
                          lista = Automato.splitTokens(br.readLine().trim());
                          int i=0;
                          
                          while(i<lista.size()){
                              try {
                                  tokens.add(Automato.reconhecerToken(lista.get(i).trim(),linha+1));
                                  token_line.add(linha+1);
                              } catch (MalformadoException ex) {
                                  if(ex.getToken().getTipo().equals("invalido")){
                                      bw.write(ex.getToken().getLexema()+"  "+ex.getToken().getTipo()+" na linha "+(linha+1)+"\n");
                                      bw.newLine();
                                  }
                                  else{
                                      bw.write(ex.getToken().getLexema()+": "+ex.getToken().getTipo()+" malformado na linha "+(linha+1)+"\n");
                                      bw.newLine();
                                  }
                              }
                              i++;
                          }
                      linha++;
                    }
                    bw.newLine();
                    bw.newLine();
                    bw.append("\n\n --- Tokens Encontrados ---\n");
                    bw.newLine();
                    for(int i=0;i<tokens.size();i++){
                        bw.write(tokens.get(i).getLexema()+" | Tipo: "+
                                tokens.get(i).getTipo()+"\n"+" | Linha: "+token_line.get(i));
                        bw.newLine();
                    }
              }
          
              // Analise Sintatica
              
              
              
              
              
              br.close();
              fr.close();
              bw.close();
              fw.close();
          
          } catch (FileNotFoundException ex) {
              System.out.println("Erro na leitura do arquivo "+arquivo.getName());
          } catch (IOException ex){
              System.out.println("Erro na leitura do arquivo "+arquivo.getName());
          }
          
          
      }
  }
}
