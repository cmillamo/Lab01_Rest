/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.modelo;

import com.proyecto.modelo.exceptions.PrerequisitosException;
import com.proyecto.persistencia.Registromutantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.internal.jpa.transaction.TransactionManagerImpl;
import org.json.JSONObject;

/**
 *
 * @author cmillamo
 */
public class Matriz{

    
    private String[] dna;
    
    /*Esta variable contiene el patron el cual se debe buscar en las cadenas de ADN*/
     String  patron= ".*AAAA.*|.*GGGG.*|.*CCCC.*|.*TTTT.*";
    public Matriz(){
        super();
    }
    /**
     * Constructor Matriz
     */
    public Matriz(String[] dna){
        super();
        this.dna=dna;
    }
    /**
     * @return boolean
     * Objetivo: 
     */
    public boolean isMutant() throws PrerequisitosException{
        boolean band=true;
        int longitud=0;
        List<String> list_dna = Arrays.asList(this.dna);
        longitud=list_dna.size();
        /*Se evalua la longitud de las cadenas*/
        for (int i = 0; i < list_dna.size(); i++) {
           if(longitud!=list_dna.get(i).length()){
               //System.err.println("No cumple con el tamaño requerido - "+longitud+" Cadena "+list_dna.get(i)+" Tamaño "+list_dna.get(i).length());
              // i=list_dna.size();
               throw new PrerequisitosException("No cumple con el tamaño requerido - "+longitud+" Cadena "+list_dna.get(i)+" Tamaño "+list_dna.get(i).length());
               //return false;
           }
        }
        Pattern pat = Pattern.compile("(A|T|C|G)+");
         
        /*Se evalua la longitud de las cadenas*/
        for (int i = 0; i < list_dna.size(); i++) {
            
           Matcher mat = pat.matcher(list_dna.get(i));   
            if (!(mat.matches())) {
               //System.err.println("No cumple con la cadena de ADN");
               throw new PrerequisitosException("No cumple con la base nitrogenda de ADN ");
               //return false;
           } 
        }
              
        band=armarcadenasdna(list_dna,patron);
        //System.out.println("1");
        UserTransaction UserTra = new TransactionManagerImpl();
        //System.out.println("2");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Conexionmysql");
        //System.out.println("3");
        RegistromutantesJpaController rmut = new RegistromutantesJpaController(UserTra,emf);
        //System.out.println("4");
        
        // Si band es true es por que se encontro mas de una secuencia de cuatro letras en la matriz.
        if (band){
             Integer i=rmut.getRegistromutantesCount()+1;
             Registromutantes rmu= new Registromutantes(i,this.getDNACompleta(),1);
             //Guardar la cadena de adn en la base de datos.
             rmut.save(rmu,emf);
            
        }
        else{
            Integer i=rmut.getRegistromutantesCount()+1;
            Registromutantes rmu= new Registromutantes(i,this.getDNACompleta(),0);
            rmut.save(rmu,emf);
        }
        return band;
    }// Fin  public boolean isMutant()
    /**
     * @return JSONObject
     * Objetivo Esta función retorna el objeto JSON con las estadisticas de las verificaciones
     * del ADN.
     */
    public JSONObject getStat(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Conexionmysql");
        UserTransaction UserTra = new TransactionManagerImpl();
        RegistromutantesJpaController rmut = new RegistromutantesJpaController(UserTra,emf);
       
        return(rmut.getMutantStat(emf));
    }
    /**
     * @return boolean
     * @param list_dna contiene las cadenas de dna.
     * @param patron contiene el patron que se debe buscar en las cadenas dna.
     * Obejtivo: Evalua si el patron se cumple mas de una vez en la matriz dna.
     *           Se van a evaluar primero Horizontalmente, Verticalmente, y por ultimo las diagonales
     *           Se realiza un conteo cada vez que se cumple el patron y cuando este es mayor a 1 se retorna true.
     *          
     */
    public boolean armarcadenasdna(List<String> list_dna,String patron){
        List<String> list_cadenas_dna = new ArrayList(list_dna);
        boolean band=false;
        int cont=0;
        char[][] matriz=new char[list_dna.size()][list_dna.size()];
        /*Convertir list a matriz*/
        for (int i=0; i<list_dna.size();i++){
           for (int j=0;j<list_dna.get(i).length();j++){
               matriz[i][j]=list_dna.get(i).charAt(j);
           }  
        }
         boolean encontropatron=false;
        //
        for (int i=0; i<list_cadenas_dna.size();i++){
          encontropatron=patroncadena(patron, list_cadenas_dna.get(i));
            if (encontropatron){
               //System.out.println("OK");
               cont++; 
            }
            if (cont > 1){
                return true;
            }
        }
        
        
        String cadena="";
       /*Extraer cadenas verticales*/
        for (int i=0; i<list_dna.size();i++){
            cadena="";
           for (int j=0;j<list_dna.size();j++){
               cadena=cadena+matriz[j][i];
           } 
            encontropatron=patroncadena(patron, cadena);
            if (encontropatron){
               cont++; 
            }
            if (cont > 1){
                return true;
            }
            list_cadenas_dna.add(cadena);
         //   System.out.println(""+cadena);
        }
        
         
        
        /*Extraer diagonal 1*/
       //  System.out.println("Diagonal ");
         cadena="";
         encontropatron=false;
        for (int i = 1 - list_dna.size(); i < list_dna.size(); i++){
         cadena =""; 
         for (int x = -Math.min(0, i), y = Math.max(0, i); x < list_dna.size() && y < list_dna.size(); x++, y++){
             cadena=cadena+matriz[y][x];
          }
         if (cadena.length()>3){
          
          encontropatron=patroncadena(patron, cadena);
            if (encontropatron){
               cont++; 
            }
            if (cont > 1){
                return true;
            }
            list_cadenas_dna.add(cadena);
         }
        }
         
        
        /*Extraer Diagonal 2*/
         cadena="";
         encontropatron=false;
        for (int i =  1-list_dna.size(); i < list_dna.size(); i++){
         cadena =""; 
     
         for (int x = -Math.min(0, (i<0?i*-1:i*-1)), y = Math.max(0, (i<0?list_dna.size()-1+i:list_dna.size()-1)); x < list_dna.size() && y >= 0; x++, y--){
              cadena=matriz[x][y]+cadena;
          }
          if (cadena.length()>3){
          
          encontropatron=patroncadena(patron, cadena);
            if (encontropatron){
               cont++; 
            }
            if (cont > 1){
                return true;
            }
            list_cadenas_dna.add(cadena);
         }
  
        }
        return band;
    }
    /**
     * @return boolean
     * @param patron
     * @param cadena
     * Objetivo: Esta funcion contiene 2 parametros, donde el primero contiene el patron que debe evaluar, 
     *           y el segundo es la cadena donde debe evaluar el patro. retorna true si cumple el patron.
     */
    public boolean patroncadena(String patron,String cadena){
     boolean band=false;
     
     Pattern pat = Pattern.compile(patron);
     Matcher mat = pat.matcher(cadena); 
      //System.out.println("Patron:"+patron+" cadena:"+cadena);
      if (mat.matches()) {
          //System.out.println("Cumplio patron");
               band=true;
      } 
     return band;
    }

    /**
     * @return the dna
     */
    public String[] getDna() {
        return this.dna;
    }
    /**
     * @return String
     * Obejtivo Esta función solo retona la cadena dna en formato String.
     */
    public String getDNACompleta(){
        String cadena="";
        List<String> list_dna = Arrays.asList(this.dna);
        for (int i=0; i< list_dna.size(); i++){
           cadena=cadena+list_dna.get(i);
        }
        return cadena;
    }

    /**
     * @param dna the dna to set
     */
    public void setDna(String[] dna) {
        this.dna = dna;
    }
}
