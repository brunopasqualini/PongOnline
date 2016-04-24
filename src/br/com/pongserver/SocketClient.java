package br.com.pongserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Bruno Pasqualini
 */
public class SocketClient {
    
    private final Socket socket;
    private PrintStream saida;
    private Scanner     entrada;
    
    public SocketClient(Socket socket){
        this.socket = socket;
    }
    
    public PrintStream getOutput() throws IOException{
        if(saida == null){
            saida = new PrintStream(socket.getOutputStream());
        }
        return saida;
    }
    
    public Scanner getInput() throws IOException {
        if(entrada == null){
            entrada = new Scanner(socket.getInputStream());
        }
        return entrada;
    }
    
    public void close() throws IOException{
        getInput().close();
        getOutput().close();
        socket.close();
    }
    
    public String getIp() {
        return socket.getInetAddress().getHostAddress();
    }
}
