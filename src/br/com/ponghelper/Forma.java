package br.com.ponghelper;

/**
 * @author Bruno Pasqualini
 */
public abstract class Forma {
    
    public int x;
    public int y;
    
    public Forma setX(int x){
        this.x = x;
        return this;
    }
    
    public Forma setY(int y){
        this.y = y;
        return this;
    }
}
