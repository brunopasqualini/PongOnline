package br.com.ponghelper;

/**
 * @author Bruno Pasqualini
 */
public class Jogador extends Forma {
    
    public final int id;
    private int pontos;
    
    public Jogador(int id){
        this.id = id;
    }
    
    public int getPontos(){
        return pontos;
    }
    
    public void addPonto(){
        pontos++;
    }
}
