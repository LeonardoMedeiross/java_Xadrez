package org.example.main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Xadrez");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// para poder fechar a janela do jogo
        window.setResizable(false);// para nao pode redimencionar a tela
        //adicionando GamePanel para o main
        GamePanel gp  = new GamePanel();//aqio chama a classe e instacia ela
        window.add(gp);//em seguida adicionamos ela
        window.pack();//e compactamos ela para seu tamanho

        window.setLocationRelativeTo(null);//aqui para deixar a janela no centro da tela
        window.setVisible(true);//para aparecer a janela

        gp.launchGame();//chamando o launchGame
    }
}