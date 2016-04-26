package br.com.pongserver;

import br.com.ponghelper.PongProcess;
import br.com.ponghelper.Jogador;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pasqualini
 */
public class PongServer implements PongProcess{
    
    private final List<SocketClient> clientes;
    private final SocketServer socket;
    private final PongInfo info;
    
    public PongServer(int porta) {
        clientes = new ArrayList();
        info     = new PongInfo(this);
        socket   = new SocketServer(this, porta);
    }
    
    public void init(){
        socket.init();
        info.init();
    }
    
    public void add(SocketClient cliente){
        clientes.add(cliente);
    }
    
    public Jogador addPlayer(){
        return info.getPlayer();
    }
    
    public void updatePlayer(int id, int y){
        if(info.isRunning()){
            info.updatePlayer(id, y);
        }
    }
    
    public void remove(SocketClient cliente, int id){
        //Só para o jogo caso não for espectador
        if(info.isRunning() && id != 0){
            info.reset();
            System.out.println("Jogadores sairam");
        }
        clientes.remove(cliente);
    }
    
    public void sendClientes(final String msg){
        for (SocketClient cliente : clientes) {
            try {
                cliente.getOutput().println(msg);
            } 
            catch (IOException ex) {
                System.out.println("Erro ao enviar mensagem " + ex.getMessage());
            }
        }
    }
}