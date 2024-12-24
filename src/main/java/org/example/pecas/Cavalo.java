package org.example.pecas;

import org.example.main.GamePanel;

public class Cavalo extends Pecas{
    public Cavalo(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.White){
            image = getImage("/pecas/cavaloB.png");
        }else {
            image = getImage("/pecas/cavaloP.png");
        }
    }
}
