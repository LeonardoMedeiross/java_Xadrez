package org.example.main;

import java.awt.*;

public class Tabuleiro {
    //aqui definimos o Tabuleiro 8x8
    final int MAX_COL = 8 ;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100 ;//aqui eu defino o tamanho do Tabuleiro que é 100 qe pe um quadrado unico  , 100x 8 =  800 , entao fica 800x800
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2 ;//esse define a metade do quadrado

    public void draw(Graphics2D g2) {
        int c = 0;
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (c == 0) {
                    g2.setColor(new Color(220, 165, 125)); // Cor clara
                    c = 1;
                } else {
                    g2.setColor(new Color(175, 115, 70)); // Cor escura
                    c = 0;
                }
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            c = (c == 0) ? 1 : 0; // Alterna para a próxima linha
        }
    }




}
