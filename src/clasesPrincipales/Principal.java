package clasesPrincipales;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import clasesPrincipales.SuperClaseHiperMegaPro;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * @author Jonnathan Juarez
 * @version 1.0 25/09/2017
 */
public class Principal {
    public static void main(String args[]) {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("./src"));
        chooser.setDialogTitle("Seleccione su archivo");
        chooser.setFileFilter(new FileNameExtensionFilter("Text files (.txt)", "txt"));
        int returnVal = chooser.showOpenDialog(null);
        String ruta = "";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
           ruta = chooser.getSelectedFile().getAbsolutePath();
        }
        AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
// lee el archivo
        Scanner s = null;
        ArrayList<String> arreglo = new ArrayList<>();
        try {
            s = new Scanner(new BufferedReader(new FileReader(ruta)));
            while (s.hasNext())
            {
                String str = s.next();
                arreglo.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
            }
        }
        //analiza la estructura de cocol
        analizadorSintactico.cocolAnalizer(arreglo);

        ArrayList<String> arreglodeDeLineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.isEmpty())
                arreglodeDeLineas.add(line);
            }
        }
        catch (IOException e){

        }

        analizadorSintactico.analizador(arreglodeDeLineas);
        System.out.println("Si no hay errores el lexer fue creado correctamente" +
                "de lo contrario, el LEXER FUE GENERARO CON ERRORES");
        // INICA LEXER
        System.out.println("Creando lexer...");
        LexerGenerator lexerGenerator = new LexerGenerator();
        //System.out.println(lexerGenerator.automataRegexGenerator("(abcd)?(abdd)*"));
        lexerGenerator.genarate(arreglodeDeLineas);
        String texto = lexerGenerator.textoFinal;
        System.out.println("Archivo generado!");


        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\src\\Pruebas\\Lexer.java"));  //clears file every time
            output.append(texto);
            output.close();
        }catch (IOException e)
        {            //exception handling left as an exercise for the reader
        }


    }
}
