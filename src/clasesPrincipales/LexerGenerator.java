package clasesPrincipales;

import java.util.ArrayList;

public class LexerGenerator {
    //este arreglo  contiene todos los ident que se lean en el archivo
    private ArrayList<String> arreglorCharacters = new ArrayList<>();
    private ArrayList<String> arregloContentCharacters = new ArrayList<>();
    private ArrayList<String> arreglorKeywords = new ArrayList<>();
    private ArrayList<String> arreglorRegexCharacters = new ArrayList<>();
    private ArrayList<String> arreglorTokenNames = new ArrayList<>();
    private ArrayList<String> arreglorTokenContent = new ArrayList<>();

    public String textoFinal = "package Pruebas;\n" +
            "import java.io.*;\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Arrays;\n" +
            "\n" +
            "import clasesPrincipales.Automata;\n" +
            "import clasesPrincipales.RegExConverter;\n" +
            "import clasesPrincipales.SuperClaseHiperMegaPro;\n" +
            "\n" +
            "import javax.swing.*;\n" +
            "import javax.swing.filechooser.FileNameExtensionFilter;\n" +
            "\n" +
            "public class Lexer {\n" +
            "\n" +
            "    private static String  textoDeSalida = \"\";\n" +
            "    private static SuperClaseHiperMegaPro laClase = new SuperClaseHiperMegaPro();\n";


    private String regexAZMayus = "(A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z)";
    private String regexAZMinus = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)";
    private String regex09 = "(0|1|2|3|4|5|6|7|8|9)";
    private String azMayus = "ABCDFGHIJKLMNOPQRSTUVWXYZ";
    private String azMinus = "abcdfghijklmnopqrstuvwxyz";
    private String numero09 = "0123456789";
    public LexerGenerator(){
        //solucion mala pero funciona
        arreglorKeywords.add("palabra reseadaque no hace mach");
    }
    public void genarate(ArrayList<String> linesArray){
        String cojunto = "";
        for (String linea: linesArray) {
            // si no es una linea en blanco
            if (!linea.equals("")) {
                //si es una linea de comentario
                if (linea.charAt(0) == '/' && linea.charAt(1) == '/') {
                    System.out.println("Se encontro una linea con comentada\nComentario: ");
                    System.out.print(linea.replace("/", ""));
                    continue;
                }
                String[] lineaseparada = linea.split("\\s+",0);
                if(lineaseparada.length == 1){
                    switch (lineaseparada[0]){
                        case "CHARACTERS":
                            cojunto = "CHARACTERS";
                            break;
                        case "KEYWORDS":
                            cojunto = "KEYWORDS";
                            break;
                        case "TOKENS":
                            cojunto = "TOKENS";
                            break;
                        case "IGNORE":
                            cojunto = "IGNORE";
                            break;
                        default:
                            continue;
                    }
                }
                //debe
                //System.out.println(lineaseparada.length);
                else if (lineaseparada.length == 3){

                    if (cojunto == "KEYWORDS"){
                        arreglorKeywords.add(lineaseparada[0]);
                    }
                    else if (cojunto == "CHARACTERS"){
                        arreglorCharacters.add(lineaseparada[0]);
                        if (lineaseparada[2].contains("+")){
                            if (lineaseparada[2].equals("\'+\'.")){
                                String contenido = "+";
                                arregloContentCharacters.add(contenido);
                                arreglorRegexCharacters.add(contenido);
                                charactersMethod(lineaseparada[0], contenido);
                                continue;
                            }
                            String[] itemsSeparados = lineaseparada[2].split("\\+");
                            itemsSeparados[itemsSeparados.length-1] = itemsSeparados[itemsSeparados.length-1].substring(0,itemsSeparados[itemsSeparados.length-1].length()-1);
                            String cadenaNueva = "";

                            for (String item: itemsSeparados) {
                                if (item.contains("'A'..'Z'")) {
                                    cadenaNueva += azMayus;
                                } else if (item.contains("'a'..'z'")) {
                                    cadenaNueva += azMinus;
                                } else if (item.contains("'0'..'9'")) {
                                    cadenaNueva += numero09;
                                }
                                else if (item.contains("\'") || item.contains("\"")){
                                    cadenaNueva += prepareString(item);
                                }
                                else {
                                    for (String cojuntoPrevio: arreglorCharacters) {
                                        if (lineaseparada[2].contains(cojuntoPrevio)){
                                            cadenaNueva += arregloContentCharacters.get(arreglorCharacters.indexOf(cojuntoPrevio));
                                        }
                                    }
                                }
                            }
                            arregloContentCharacters.add(cadenaNueva);
                            arreglorRegexCharacters.add(automataRegexGenerator(cadenaNueva));
                            charactersMethod(lineaseparada[0], cadenaNueva);
                        }
                        else{
                            String cadenaNueva = "";
                            if (lineaseparada[2].contains("'A'..'Z'")) {
                                cadenaNueva += azMayus;
                            } else if (lineaseparada[2].contains("'a'..'z'")) {
                                cadenaNueva += azMinus;
                            } else if (lineaseparada[2].contains("'0'..'9'")) {
                                cadenaNueva += numero09;
                            }
                            else if (lineaseparada[2].contains("\'") || lineaseparada[2].contains("\"")){
                                cadenaNueva += prepareString(lineaseparada[2]);
                            }
                            arregloContentCharacters.add(cadenaNueva);
                            arreglorRegexCharacters.add(automataRegexGenerator(cadenaNueva));
                            charactersMethod(lineaseparada[0], cadenaNueva);
                        }
                    }
                    else if (cojunto == "TOKENS"){
                        String cadenaNueva = "";
                        String tokenDec = lineaseparada[2].substring(0, lineaseparada[2].length()-1);
                        String tokenDecTemp = tokenDec;
                        for (String cojuntoPrevio: arreglorCharacters) {
                            if (tokenDec.contains(cojuntoPrevio)){
                                String reemplazo = arreglorRegexCharacters.get(arreglorCharacters.indexOf(cojuntoPrevio));
                                cadenaNueva = tokenDecTemp.replace(cojuntoPrevio, reemplazo);
                                tokenDecTemp = cadenaNueva;
                            }
                        }
                        cadenaNueva = cadenaNueva.replace("{","(");
                        cadenaNueva = cadenaNueva.replace("}",")*");
                        cadenaNueva = cadenaNueva.replace("[","(");
                        cadenaNueva = cadenaNueva.replace("]",")?");
                        cadenaNueva = cadenaNueva.replace("\"","");
                        cadenaNueva = cadenaNueva.replace("\'","");
                        //System.out.println(cadenaNueva);
                        arreglorTokenNames.add(lineaseparada[0]);
                        arreglorTokenContent.add(cadenaNueva);
                    }
                }
            }
        }
        keyuwordMethod();
        principalMethod();
    }


    public boolean checkEndLine(String s){
        if(s.charAt(s.length()-1) == '.'){
            return  true;
        }
        else {return false;}
    }
    //quita las comillas y el punto del final de la linea
    public String prepareString(String s){
        if (checkEndLine(s)){
            String s2 =  s.substring(1,s.length()-2);
            return s2;
        }
        else {
            String s2 = s.substring(1,s.length()-1);
            return s2;
        }
    }

    //CREA CADENAS COMO SERIES CCONSECUTIVAS DE Ors
    public String automataRegexGenerator(String enttrada){
        String output = "(";
        for(int x = 0; x < enttrada.length(); x ++){
            String letra = String.valueOf(enttrada.charAt(x));
            if(x!=enttrada.length()-1){
                if (!letra.equals("(") && !letra.equals(")") && !letra.equals("?") && !letra.equals("*") && !letra.equals("|")){
                    output+= letra+"|";
                }
                else {
                    output+= letra;
                }

            }
            else {
                output+= letra+")";
            }
        }
        return output;
    }
    public void charactersMethod(String nombreDeConjunto, String contenidoDelSet){
         textoFinal += "    private static boolean is" + nombreDeConjunto +"(String entrada) {\n" +
                 "        String tokenName = \""+ nombreDeConjunto + "\";\n" +
                 "        String conjunto = \"" + contenidoDelSet +"\";\n" +
                 "        boolean respuesta = conjunto.contains(entrada);\n" +
                 "        if (respuesta) {\n" +
                 "            //textoDeSalida += String.format(\"<%s, \\\"%s\\\"> \",tokenName, entrada);\n" +
                 "            textoDeSalida += String.format(\"<%s> \", tokenName);\n" +
                 "            return true;\n" +
                 "        } else {\n" +
                 "            return false;\n" +
                 "        }\n" +
                 "    }\n";
    }
    public void keyuwordMethod(){
        String cadenaLista = "{";
        for (String item: arreglorKeywords){
            String item2 = "\"" + item +"\"";
            if(!item.equals(arreglorKeywords.get(arreglorKeywords.size()-1))) {
                cadenaLista += item2 + ",";
            }
            else {
                cadenaLista += item2 + "}";
            }
        }

        textoFinal +="    private static boolean isKeyword(String entrada){\n" +
                "        String tokenName = \"keyword\";\n" +
                "        String[] listaDeKeywords = " + cadenaLista + ";\n" +
                "        ArrayList<String> conjunto = new ArrayList<>(Arrays.asList(listaDeKeywords));\n" +
                "        boolean respuesta = conjunto.contains(entrada);\n" +
                "        if (respuesta){\n" +
                "            int indice = conjunto.indexOf(entrada);\n" +
                "            //textoDeSalida += String.format(\"<%s, \\\"%s\\\"> \",conjunto.get(indice), entrada);\n" +
                "            textoDeSalida += String.format(\"<%s> \",conjunto.get(indice));\n" +
                "            return true;\n" +
                "        }\n" +
                "        else {\n" +
                "            return false;\n" +
                "        }\n" +
                "    }\n";


    }
    public void principalMethod(){
         textoFinal += " public static void main(String[] args){\n" +
                 "        //filechooser para ver el escoger el archivo que se esta buscando\n" +
                 "        JFileChooser chooser = new JFileChooser();\n" +
                 "        chooser.setCurrentDirectory(new java.io.File(\"./src\"));\n" +
                 "        chooser.setDialogTitle(\"Seleccione su archivo\");\n" +
                 "        chooser.setFileFilter(new FileNameExtensionFilter(\"Text files (.txt)\", \"txt\"));\n" +
                 "        int returnVal = chooser.showOpenDialog(null);\n" +
                 "        ArrayList<String> lineas = new ArrayList<>();\n" +
                 "        if (returnVal == JFileChooser.APPROVE_OPTION) {\n" +
                 "            try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile().getAbsolutePath()))) {\n" +
                 "                String line;\n" +
                 "                while ((line = br.readLine()) != null) {\n" +
                 "                    if(!line.equals(\"\"))\n" +
                 "                        lineas.add(line);\n" +
                 "                }\n" +
                 "            }\n" +
                 "            catch (IOException e){\n" +
                 "            }\n" +
                 "        }\n";

                //agrega dianmicamente los automatas de los tokens
                for (int x = 0; x < arreglorTokenNames.size(); x++){
                    textoFinal += "\t\tAutomata is"+arreglorTokenNames.get(x)+" = laClase.analizador(RegExConverter.infixToPostfix(\""+ arreglorTokenContent.get(x)+"\"));\n";
                }

         textoFinal += "for(String linea: lineas){\n" +
                 "            for(String palabra: linea.split(\" \")){\n" +
                 "                if(isKeyword(palabra)){\n" +
                 "                }\n";
        //agrega dianmicamente las pruebas  de los tokens
        for (int x = 0; x < arreglorTokenNames.size(); x++){
            textoFinal += "                else if(laClase.simuladorNFA(is"+arreglorTokenNames.get(x)+",palabra)){\n" +
                    "                    textoDeSalida += \" <"+ arreglorTokenNames.get(x) +"> \";\n" +
                    "                }\n";
        }

        textoFinal += "else {\n" +
                "                    textoDeSalida += \"No reconocida \";\n" +
                "                    System.out.printf(\"ERROR [ %s ] no fue reconocida\\n\",palabra);\n" +
                "                }\n" +
                "            }\n" +
                "            textoDeSalida += \"\\n\";\n" +
                "        }\n" +
                "        System.out.println(textoDeSalida);\n" +
                "    }\n" +
                "\n" +
                "}\n";


    }
}
