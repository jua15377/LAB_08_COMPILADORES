package clasesPrincipales;

import java.util.ArrayList;


public class AnalizadorSintactico {
    private SuperClaseHiperMegaPro clase = new SuperClaseHiperMegaPro();
    private final String identRegex = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z)(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|0|1|2|3|4|5|6|7|8|9)*";
    private final String NUMBERREGEX = "(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*";
    private Automata automataIdent;
    private Automata automatanumber;

    public AnalizadorSintactico(){
        clase.determinarAlfabeto(identRegex);
        automataIdent = clase.analizador(RegExConverter.infixToPostfix(identRegex));
        automatanumber = clase.analizador(RegExConverter.infixToPostfix(NUMBERREGEX));

    }
    public boolean ident(String string_ident) {
        boolean resutlado = clase.simuladorNFA(automataIdent, string_ident);
        return resutlado;
    }

    public void cocolAnalizer(ArrayList<String> texto){
        //bandera paraver si se cumplen las reglas
        boolean[] banderas = new boolean[3];
        String compilerWord, endWord, identWord, identWord2;
        compilerWord = texto.get(0);
        identWord = texto.get(1);
        identWord2 = texto.get(texto.size()-1);
        endWord = texto.get(texto.size()-2);
        //revisa sintaxis de inicio de cocol
        if (compilerWord.equals("COMPILER") && endWord.equals("END")){
            banderas[0] = true;
        }
        else{
            System.out.println("Syntax error! --> COCOL block: COMPILER or END missing");
        }
        if (!identWord2.endsWith(".")){
            System.out.println("Syntax error! --> COCOL block: close Ident error");
        }
        if (identWord.equals(identWord2.replace(".",""))){
            banderas[1] = true;
            if(ident(identWord)){
                banderas[2] = true;
            }
            else {
                System.out.println("Syntax error! --> COCOL block: Ident, bad expression");
            }
        }
        else{
            System.out.println("Syntax error! --> COCOL block: Ident  mach");
        }
        texto.remove(compilerWord);
        texto.remove(endWord);
        texto.remove(identWord);
        texto.remove(identWord2);
    }
    public void analizador(ArrayList<String> texto){
        int numnOfLine = 1;
        boolean banderaDeCaracter =false;
        boolean banderaDeKeyword = false;
        boolean banderaDEToken = false;
        for (String linea: texto){
            // si no es una linea en blanco
            if(!linea.equals("")){
                //si es una linea de comentario
                if(linea.charAt(0) == '/' && linea.charAt(1) == '/'){
                    System.out.println("Se encontro una linea con comentada\nComentario: ");
                    System.out.print(linea.replace("/", ""));
                    numnOfLine++;
                    continue;
                }
                String[] lineaseparada = linea.split(" ");

                //System.out.println(lineaseparada.length);
                //verifica si son una de las palabras que delimitan el contenido
                if(lineaseparada.length == 1){
                    switch (lineaseparada[0]){
                        case "CHARACTERS":
                            banderaDeCaracter = true;
                            banderaDeKeyword = false;
                            banderaDEToken = false;
                            break;
                        case "KEYWORDS":
                            banderaDeKeyword = true;
                            banderaDeCaracter = false;
                            banderaDEToken = false;
                            break;
                        case "TOKENS":
                            banderaDeKeyword = false;
                            banderaDeCaracter = false;
                            banderaDEToken = true;
                            break;
                        default:
                            //error si no hay separacion entre palabras y no es una palabra reservada de cocol
                            System.out.println("Syntax error! --> can't resolve symbol as a reserved word. line: " + numnOfLine);
                    }
                }
                else if (lineaseparada.length == 2){
                    if(lineaseparada[0].equals("COMPILER") || lineaseparada[0].equals("END")){

                    }
                    else if (!lineaseparada[0].equals("IGNORE")){
                        System.out.println("Syntax error! --> can't resolve symbol as a reserved word. line: " + numnOfLine);
                    }
                    else {
                        if(checkEndLine(lineaseparada[1])){
                            String s = prepareString(lineaseparada[1]);
                            //se debe mejorar
                            if (s.contains("\"")){
                                System.out.println("Syntax error! -->not a set. Line: " + numnOfLine);
                            }
                            else {
                                //no tiene errores.
                                numnOfLine ++;
                                continue;
                            }
                        }
                    }
                }
                //deben ser tres elementos con la estructura ident = alguna otra cosa
                else if (lineaseparada.length == 3){

                    if(checkEndLine(lineaseparada[2])) {
                        //verificar el ident
                        if (ident(lineaseparada[0])) {
                            //verificar el signo
                            if (lineaseparada[1].equals("=")) {
                                if (banderaDeCaracter) {
                                    String s = prepareString(lineaseparada[2]);
                                    if (s.contains("\"") && !s.contains("+")){
                                        System.out.println("Syntax error! -->not a set Line: " + numnOfLine);
                                    }
                                    if (s.contains("+")){
                                        //por ahora continue
                                        numnOfLine ++;
                                        continue;
                                    }
                                }
                                else if (banderaDeKeyword) {
                                    String s = prepareString(lineaseparada[2]);
                                    if (s.contains("\"")){
                                        System.out.println("Syntax error! -->not a set. Line: " + numnOfLine);
                                    }
                                    else {
                                        //no tiene errores.
                                        numnOfLine ++;
                                        continue;
                                    }
                                }
                                else if(banderaDEToken){
                                    System.out.println("token encontrado");
                                }
                                else {
                                    System.out.println("Syntax error! -->Bad Declaration Line: " + numnOfLine);
                                }
                            }
                            else {
                                System.out.println("Syntax error! --> Not a statement Line:" + numnOfLine);
                                //validar el set
                            }
                        }
                        else {
                            System.out.println("Syntax error! --> Not a ident  Line:" + numnOfLine);
                        }
                    }
                    else{
                        System.out.println("Syntax error! --> '.' expected  Line:" + numnOfLine);
                    }
                }
            }

            //pasa sin hacer nada
            else {
                //System.out.println("linea enblacnco");
            }
            numnOfLine ++;
        }
    }
    public boolean checkEndLine(String s){
        if(s.charAt(s.length()-1) == '.'){
            return  true;
        }
        else {return false;}
    }
    public String prepareString(String s){
        String s2 =  s.substring(1,s.length()-2);
        return s2;
    }
}
