package br.com.ponghelper;

/**
 * @author Bruno Pasqualini
 */
public class PongInfoJson {
    
    private Bola bola;
    private Jogador p1;
    private Jogador p2;
    
    public PongInfoJson set(Bola bola, Jogador p1, Jogador p2){
        this.bola = bola;
        this.p1 = p1;
        this.p2 = p2;
        return this;
    }
    
    public Bola getBola(){
        return bola;
    }

    public Jogador getP1(){
        return p1;
    }
    
    public Jogador getP2(){
        return p2;
    }
    
    @Override
    public String toString() {
        return Json.toJson(this);
    }
}
