package Pruebas;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import clasesPrincipales.Automata;
import clasesPrincipales.RegExConverter;
import clasesPrincipales.SuperClaseHiperMegaPro;
public class Principal {

    private static String  textoDeSalida = "";

    private static boolean isNombreD(String entrada) {
        String tokenName = "letter";
        String conjunto = "abcdfghijklmnopqrstuvwxyz";
        boolean respuesta = conjunto.contains(entrada);
        if (respuesta) {
            //textoDeSalida += String.format("<%s, \"%s\"> ",tokenName, entrada);
            textoDeSalida += String.format("<%s> ", tokenName);
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNombredos(String entrada) {
        String tokenName = "digit";
        String conjunto = "123456789";
        boolean respuesta = conjunto.contains(entrada);
        if (respuesta) {
            //textoDeSalida += String.format("<%s, \"%s\"> ",tokenName, entrada);
            textoDeSalida += String.format("<%s> ", tokenName);
            return true;
        } else {
            return false;
        }
    }
    private static boolean isKeyword(String entrada){
        String tokenName = "keyword";
        String[] listaDeKeywords = {"if","for","while"};
        ArrayList<String> conjunto = new ArrayList<>(Arrays.asList(listaDeKeywords));
        boolean respuesta = conjunto.contains(entrada);
        if (respuesta){
            int indice = conjunto.indexOf(entrada);
            //textoDeSalida += String.format("<%s, \"%s\"> ",conjunto.get(indice), entrada);
            textoDeSalida += String.format("<%s> ",conjunto.get(indice));
            return true;
        }
        else {
            return false;
        }

    }

/*

    public static void main(String[] args){

        System.out.println("Hello word!");
        String regExpr = "(0|1|2|3|4|5|6|7|8|9){0|1|2|3|4|5|6|7|8|9}";
        String strprep = regExpr.replace("{","(");
        strprep = strprep.replace("}",")*");
        SuperClaseHiperMegaPro clase = new SuperClaseHiperMegaPro();
        Automata a =  clase.analizador(RegExConverter.infixToPostfix(strprep));
        if(clase.simuladorNFA(a,"69")){System.out.println("<number>");}
        //isNombreD("b");
       // isKeyword("if");
       // System.out.println(textoDeSalida);

        ArrayList<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("test2.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.equals(""))
                    lineas.add(line);
            }
        }
        catch (IOException e){
        }
        for(String linea: lineas){
            for(String palabra: linea.split(" ")){
                if(!isKeyword(palabra)){
                    for(char caracter: palabra.toCharArray()){
                        if(isNombreD(String.valueOf(caracter))){continue;}
                        if(isNombredos(String.valueOf(caracter))){continue;}
                        else {System.out.printf("ERROR [ %c ] no son caracteres reconocidos\n",caracter);}
                    }
                }
                else {
                    //es una keyword
                }
            }
            textoDeSalida += "\n";
        }
        System.out.println(textoDeSalida);
    }
**/
}

