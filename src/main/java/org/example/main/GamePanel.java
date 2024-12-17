package org.example.main;

import javax.swing.*;
import java.awt.*;

//aqui iremos definir o tamanho da tela , cor de fundo
public class GamePanel extends JPanel implements Runnable{
        public static final int WIDTH =1100;
    public static final int HEIGHT =800;
    final int FPS = 60;
    Thread gameThread ; //para poder usar o Thread tem que implementar o interface Runnable
    Tabuleiro board = new Tabuleiro();//instaciando o tabuleiro

    public GamePanel (){
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.GRAY);
    }
    public void launchGame(){
        //isntaciando o Thread
        gameThread = new Thread(this);
        gameThread.start();//chamando o metodo start

    }


    //para poder implementar o Runnable tem que ter o metodo run
    @Override
    public void run() {
        //Game loop
        double  drawInterval = 1000000000/FPS;
        double delta = 0 ;
        long lastTime = System.nanoTime();
        long currentTime ;

        while (gameThread !=null){
            currentTime = System.nanoTime();

            delta+= (currentTime - lastTime)/drawInterval;
            lastTime=currentTime;

            if(delta >=1){
                update();
                repaint();
                delta--;
            }
        }
    }
    //aqui sera atualização das informações das posiçoes ou do numero de peças que tem no Tabuleiro
    private void update(){

    }
    //o paintComponet lida com tudo que é desenhavel no programa
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        board.draw(g2);
    }
}
