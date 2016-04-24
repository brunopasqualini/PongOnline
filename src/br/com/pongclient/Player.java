package br.com.pongclient;

import br.com.ponghelper.Cmd;
import br.com.ponghelper.Json;
import br.com.ponghelper.PongInfoJson;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaPlay.GameEngine;
import javaPlay.GameStateController;
import javaPlay.Keyboard;
import javaPlay.Sprite;
import javax.swing.JOptionPane;
import static java.lang.Thread.sleep;

class Player implements GameStateController, PlayerSocket.SocketListener {
    
    private PlayerSocket socket;
    
    //Inicialização de variaveis de controle
    private int idPlayer;
    private int posBarra1;
    private int posBarra2;
    private int altura;
    private int posBolaY;
    private int posBolaX;
    private int pontuacaoA = 0;
    private int pontuacaoB = 0;
    
    //tela
    private int largura;
    
    //Variáveis utilizadas para os Sprites
    private Sprite figuraBola;
    private Sprite barra1;
    private Sprite barra2;
    private Sprite figuraBackground;
    
    //Inicialização das Classes do Game
    Background background = new Background();
    Bola bola1  = new Bola();
    Barra barraA = new Barra();
    Barra barraB = new Barra();
    
    public Player() throws IOException {
        //inicializando o game com a tamanho padrão do GameEngine
        altura  = GameEngine.getInstance().getGameCanvas().getHeight();
        largura = GameEngine.getInstance().getGameCanvas().getWidth();
             
        //Iniciando a bola no meio da tela
        posBarra1 = altura  / 2;
        posBarra2 = altura  / 2;
        posBolaY  = altura  / 2;
        posBolaX  = largura / 2;
        
        //velocidade inicial da bola em cada eixo (px)
        try {
            //Carregamento dos sprites do game
            figuraBackground = new Sprite("resources/background.png", 1, 800, 800);
            figuraBola       = new Sprite("resources/bola.png", 3, 77, 77);
            barra1           = new Sprite("resources/Pong_pad01.png", 3, 25, 100);
            barra2           = new Sprite("resources/Pong_pad02.png", 3, 25, 100);
        }
        catch(Exception erro) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, erro);
        }
        background.setSprite(figuraBackground);
        bola1.setSprite(figuraBola);
        barraA.setSprite(barra1);
        barraB.setSprite(barra2);
    }
    
    @Override
    public void step(long l) {
        try {
            sleep(3);
        }
        catch(InterruptedException erro) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, erro);
        }
        //Espectador ou servidor desligado
        if(idPlayer == 0 || !socket.isConnected()){
            return;
        }
        //Configração das teclas de controle do Game
        Keyboard teclado = GameEngine.getInstance().getKeyboard();
        if(idPlayer == 2){
            
            if( (teclado.keyDown(Keyboard.UP_KEY) == true) && (posBarra2 > 10) ) {
                posBarra2 -= 3;
            }
            if( (teclado.keyDown(Keyboard.DOWN_KEY) == true) && (posBarra2 < (altura - 150) ) ) {
                posBarra2 += 3;
            }
            socket.send(Cmd.formatSetJogadorPosicaoFromClient(posBarra2));
        }
        else if(idPlayer == 1){
            if( (teclado.keyDown(Keyboard.A_KEY) == true) && (posBarra1 > 10) ) {
                posBarra1 -= 3;
            }
            if( (teclado.keyDown(Keyboard.Z_KEY) == true) && (posBarra1 < (altura - 150) ) ) {
                posBarra1 += 3;
            }
            socket.send(Cmd.formatSetJogadorPosicaoFromClient(posBarra1));
        }
    }
    
    /**
     * Metodo Drown. Executado a cada ciclo de clock para redesenhar a tela do Game
     * @param graphic 
     */
    @Override
    public void draw(Graphics graphic){
        
        //Inicializando a tela de fundo do game
        background.x = -1;
        background.y = 0;
        background.draw(graphic);
        
        //Escrevendo os nomes dos players na tela
        Font font = new Font("arial", Font.BOLD, 18);
        graphic.setFont(font);
        graphic.setColor(Color.GREEN);
        graphic.drawString(String.valueOf(pontuacaoB), largura / 2 - 60, 75);
        graphic.drawString(String.valueOf(pontuacaoA), largura / 2 + 40 , 75);
        graphic.setColor(idPlayer == 1 ? Color.RED : Color.green);
        graphic.drawString("Player A", largura / 2 - 90, 55);
        graphic.setColor(idPlayer == 2 ? Color.RED : Color.green);
        graphic.drawString("Player B", largura / 2 + 10, 55);
        
        //Espectador ou servidor desligado
        if(idPlayer == 0 || !socket.isConnected()){
            graphic.setColor(Color.RED);
            graphic.drawString(!socket.isConnected() ? "Server Off" : "Espectador", largura / 2 - 61, 95);
        }
        //Desenhando a Bola
        bola1.x = posBolaX;
        bola1.y = posBolaY;
        bola1.draw(graphic);
        
        //Desenhando a Barra A
        barraA.x = 13;
        barraA.y = posBarra1;
        barraA.draw(graphic);
        
        //Desenhando a Barra B
        barraB.x = largura-55;
        barraB.y = posBarra2;
        barraB.draw(graphic);
    }

    @Override
    public void load() {
        int porta = Integer.parseInt(JOptionPane.showInputDialog("Porta", "50000"));
        String ip = JOptionPane.showInputDialog("IP", "127.0.0.1");
        socket = new PlayerSocket(porta, ip);
        socket.setListener(this);
    }

    @Override
    public void unload() {
        
    }

    @Override
    public void start() {
        socket.init();
        socket.send(Cmd.formatJoin());
    }

    @Override
    public void stop() {
        
    }

    @Override
    public void onReadMessage(String message) {
        String param = Cmd.extract(message);
        if(Cmd.is(Cmd.SET_JOGADOR_ID_CLIENT, message)){
            if(param.trim().length() > 0){
                idPlayer = Integer.parseInt(param);
                System.out.println("ID " + idPlayer );
            }
        }
        else if(Cmd.is(Cmd.REFRESH_CLIENT, message)){
            PongInfoJson json = Json.fromJson(param, PongInfoJson.class);
            posBolaX = json.getBola().x;
            posBolaY = json.getBola().y;
            pontuacaoA = json.getP1().getPontos();
            pontuacaoB = json.getP2().getPontos();
            
            switch (idPlayer) {
                case 1:
                    posBarra2 = json.getP2().y;
                    break;
                case 2:
                    posBarra1 = json.getP1().y;
                    break;
                default:
                    posBarra2 = json.getP2().y;
                    posBarra1 = json.getP1().y;
                    break;
            }
        }
    }
}
