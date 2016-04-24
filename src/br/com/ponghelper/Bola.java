package br.com.ponghelper;

/**
 * @author Bruno Pasqualini
 */
public class Bola extends Forma{
    
    public static final int LEFT  = 0;
    public static final int RIGHT = 1;
    public static final int UP    = 1;
    public static final int DOWN  = 0;
    
    private int sentidoX;
    private int sentidoY;
    
    public int velocidade;
    
    public boolean isLeft(){
        return sentidoX == LEFT;
    }
    
    public void setLeft(){
        sentidoX = LEFT;
    }
    
    public boolean isRight(){
        return sentidoX == RIGHT;
    }
    
    public void setRight(){
        sentidoX = RIGHT;
    }
    
    public boolean isUp(){
        return sentidoY == UP;
    }
    
    public void setUp(){
        sentidoY = UP;
    }
    
    public boolean isDown(){
        return sentidoY == DOWN;
    }
    
    public void setDown(){
        sentidoY = DOWN;
    }
}
