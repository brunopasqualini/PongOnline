package br.com.pongserver;;

import br.com.ponghelper.PongProcess;
import br.com.ponghelper.Jogador;
import br.com.ponghelper.Bola;
import br.com.ponghelper.Cmd;
import br.com.ponghelper.PongInfoJson;

/**
 * @author Bruno Pasqualini
 */
public class PongInfo implements Runnable, PongProcess{
    
    private Bola bola;
    private Jogador p1;
    private Jogador p2;
    
    private final PongInfoJson json;
    private final PongServer server;
    private final int altura  = 639;
    private final int largura = 815;
    private int ponto;
    
    public PongInfo(PongServer server){
        initBola(); 
        this.server = server;
        this.json = new PongInfoJson();
    }
    
    public void init(){
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(7);
                if(!isRunning()){
                    continue;
                }
                int posBolaX = bola.x, posBolaY = bola.y, velocidadeBola = bola.velocidade;
                //Todas as verificações para identificar as colisões com da bola com parede
                if(bola.isLeft() && (posBolaX > 10)) {
                    posBolaX -= velocidadeBola;
                }
                else {
                    bola.setRight();
                }
                if(bola.isRight() && (posBolaX < (largura - 60))){
                    posBolaX += velocidadeBola;
                }
                else{
                    bola.setLeft();
                }
                if(bola.isDown() && (posBolaY > 10)) {
                    posBolaY -= velocidadeBola;
                }
                else {
                    bola.setUp();
                }
                if(bola.isUp() && (posBolaY < (altura - 85))){
                    posBolaY += velocidadeBola;
                }
                else{
                    bola.setDown();
                }
                if(posBolaX > 100 && posBolaX < 600){
                    ponto = 0;
                }
                if(posBolaX >= ((largura / 4))){
                    verificaBola(p2);
                }
                else if(posBolaX <= ((largura / 4))){
                    verificaBola(p1);
                }
                else {
                    ponto = 0;
                }
                bola.setX(posBolaX).setY(posBolaY);
                //{"bola":{"sentidoX":0,"sentidoY":0,"velocidade":0,"x":0,"y":0},"p1":{"id":1,"pontos":0,"x":0,"y":0},"p2":{"id":2,"pontos":0,"x":0,"y":0}}
                String cmd = Cmd.formatRefreshClient(json.set(bola, p1, p2).toString());
                server.sendClientes(cmd);
            } 
            catch (InterruptedException ex) {}
        }
    }
    
    private void verificaBola(Jogador jogador) {
        int velocidadeBola = bola.velocidade;
        if(jogador.x != 13){
            if(( bola.x+40 ) >= (jogador.x) ){
                if(( ((bola.y+77) < jogador.y+10) || (bola.y > jogador.y+90) )){
                    if((bola.x+77+velocidadeBola) >= (largura - velocidadeBola)) {
                        if(ponto == 0) {
                            ponto = 1;
                            p2.addPonto();
                            if(velocidadeBola < 5) {
                                velocidadeBola++;
                            }
                        }
                    }
                }
                else{
                    bola.setLeft();
                    if(bola.y+77 < jogador.y+30){
                        bola.setDown();
                    }
                    if(bola.y > jogador.y+70){
                        bola.setUp();
                    }
                }
            }
        }
        else {
            if(bola.x-velocidadeBola <= (jogador.x + 25)){
                if(( ((bola.y+77) < jogador.y+10) || (bola.y > jogador.y+90) )){
                    if(bola.x-velocidadeBola <= 10+velocidadeBola) {
                        if(ponto == 0) {
                            ponto = 1;
                            p1.addPonto();
                            if(velocidadeBola < 5) {
                                velocidadeBola++;
                            }
                        }
                    }
                }
                else{
                    bola.setRight();
                    if(bola.y+77 < jogador.y+30){
                        bola.setDown();
                    }
                    if(bola.y > jogador.y+70){
                        bola.setUp();
                    }
                }
            }
        }
        bola.velocidade = velocidadeBola;
    }
    
    public boolean isRunning(){
        return p1 != null && p2 != null;
    }
    
    public Jogador getPlayer(){
        if(p1 == null){
            p1 = new Jogador(1);
            p1.y = altura / 2;
            p1.x = 13;
            return p1;
        }
        if(p2 == null){
            p2 = new Jogador(2);
            p2.y = altura / 2;
            p2.x = largura - 55;
            return p2;
        }
        return null;
    }
    
    public void updatePlayer(int id, int y){
        if(id == p1.id){
            p1.setY(y);
        }
        else if(id == p2.id){
            p2.setY(y);
        }
    }
    
    private void initBola(){
        bola = new Bola();
        bola.velocidade = 3;
        bola.setY(altura / 2).setX(largura / 2);
    }
    
    public void reset(){
        p1 = null;
        p2 = null;
        initBola();
    }
}
