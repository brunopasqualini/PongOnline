package br.com.ponghelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bruno Pasqualini
 */
public class Cmd {
   
    public static final String JOIN = "join";
    public static final String REFRESH_CLIENT = "updateClientFromServer";
    public static final String SET_JOGADOR_ID_CLIENT = "setJogadorIdClient";
    public static final String SET_JOGADOR_POSICAO_FROM_CLIENT = "setJogadorPosicaoFromClient";
    
    public static String extract(String mensagem){
        Matcher matcher = Pattern.compile("\\(([^)]+)\\)").matcher(mensagem);
        while ( matcher.find() ) {
            String resultado = matcher.group(0); 
            return resultado.substring(1, resultado.length() - 1);
        }
        return "";
    }
    
    public static boolean is(String cmd, String mensagem){
        return mensagem.contains(cmd);
    }
    
    public static String formatJoin(){
        return JOIN;
    }
    
    public static String formatRefreshClient(String info){
        return String.format(REFRESH_CLIENT + "(%s)", info);
    }
    
    public static String formatSetJogadorIdClient(String id){
        return String.format(SET_JOGADOR_ID_CLIENT + "(%s)", id);
    }
    
    public static String formatSetJogadorPosicaoFromClient(int posicao){
        return String.format(SET_JOGADOR_POSICAO_FROM_CLIENT + "(%d)", posicao);
    }
}
