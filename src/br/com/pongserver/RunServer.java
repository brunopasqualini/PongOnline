package br.com.pongserver;

import java.util.Scanner;

/**
 * @author Bruno Pasqualini
 */
public class RunServer {
    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Porta");
        
        PongServer server = new PongServer(s.nextInt());
        server.init();
    }
    
}
