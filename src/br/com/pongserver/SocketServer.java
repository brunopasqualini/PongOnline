package br.com.pongserver;

import br.com.ponghelper.Cmd;
import br.com.ponghelper.Jogador;
import br.com.ponghelper.PongProcess;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * @author Bruno Pasqualini
 */
public class SocketServer implements Runnable, PongProcess{
    private final PongServer handler;
    private final int port;

    public SocketServer(PongServer server, int port){
        this.handler = server;
        this.port    = port;
    }

    public void init(){
        new Thread(this).start();
    }
    
    public void run(){
        try {
            ServerSocket server = new ServerSocket(port);
            while(true){
                SocketClient cliente = new SocketClient(server.accept());
                handler.add(cliente);
                new Thread(new ClientConnectListener(cliente)).start();
            }
        }
        catch (IOException ex) {
            System.out.println("Erro ao iniciar o socket " + ex.getMessage());
        }
    }

    private class ClientConnectListener implements Runnable{

        private int _id;
        private final SocketClient cliente;

        public ClientConnectListener(SocketClient cliente){
            this.cliente = cliente;
        }

        @Override
        public void run() {
            try {
                Scanner entrada = cliente.getInput();
                while(entrada.hasNextLine()){
                    String mensagem = entrada.nextLine();
                    verificaMensagemSocket(cliente, mensagem);
                }
                System.out.println("Saiu " + cliente.getIp());
                cliente.close();
                handler.remove(cliente, _id);
            }
            catch (IOException ex) {
                System.out.println("Erro ao fazer a leitura " + ex.getMessage());
            }
        }
    
        private void verificaMensagemSocket(SocketClient socket, String mensagem){
            if(Cmd.is(Cmd.JOIN, mensagem)){
                Jogador jogador = handler.addPlayer();
                if(jogador != null){
                    //Seta o ID do jogador no cliente
                    _id = jogador.id;
                }
                try {
                    String id = _id == 0 ? "" : String.valueOf(_id);
                    cliente.getOutput().println(Cmd.formatSetJogadorIdClient(id));
                }
                catch (IOException ex) {}
            }
            else if(Cmd.is(Cmd.SET_JOGADOR_POSICAO_FROM_CLIENT, mensagem) && _id != 0){
                int y = Integer.parseInt(Cmd.extract(mensagem));
                handler.updatePlayer(_id, y);
            }
        }
    }
}