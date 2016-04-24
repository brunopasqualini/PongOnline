package br.com.pongclient;
import java.io.IOException;
import javaPlay.GameEngine;

/**
 * Classe que define a incialização do Game Pong
 * @author Bruno Pasqualini
 */
public class Game1 {
    
    public static void main(String[] args) throws IOException {
        GameEngine.getInstance().addGameStateController(0, new Player());
        GameEngine.getInstance().setStartingGameStateController(0);
        GameEngine.getInstance().run();
            
    }
    
}
