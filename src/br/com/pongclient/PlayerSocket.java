package br.com.pongclient;

import br.com.ponghelper.PongProcess;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Bruno Pasqualini
 */
public class PlayerSocket implements PongProcess{
    
    private boolean connected;
    
    private final String host;
    private final int    port;
    private Socket socket;
    private PrintStream saida;
    private SocketListener listener;
    
    public PlayerSocket(int port, String host){
        this.port = port;
        this.host = host;
    }
    
    public void setListener(SocketListener listener){
        this.listener = listener;
    }
    
    public void init(){
        try {
            socket = new Socket(host, port);
            new Thread(new ServerListener(socket.getInputStream())).start();
            connected = true;
        } 
        catch (IOException ex) {
            connected = false;
        }
    }
    
    public void send(String msg){
        if(!connected) return;
        try {
            if(saida == null){
                saida = new PrintStream(socket.getOutputStream());
            }
            saida.println(msg);
        } 
        catch (IOException ex) {
            System.out.println("Erro ao enviar mensagem para o servidor " + ex.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }
    
    private class ServerListener implements Runnable{
        
        private InputStream msgServer;
        
        public ServerListener(InputStream msgServer){
            this.msgServer = msgServer;
        }

        @Override
        public void run() {
            Scanner s = new Scanner(msgServer);
            while(s.hasNextLine()){
                String message = s.nextLine();
                if(message.trim().length() > 0 && listener != null){
                    listener.onReadMessage(message);
                }
            }
            s.close();
            saida.close();
            try {
                socket.close();
            } 
            catch (IOException ex) {}
            connected = false;
        }
    }
    
    public interface SocketListener{
        
        public void onReadMessage(String message);
        
    }
}
