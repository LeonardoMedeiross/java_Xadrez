package org.example.pecas;

import org.example.main.GamePanel;

public class Bispo extends Pecas{
    public Bispo(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.White){
            image = getImage("/pecas/bispoB.png");

        }else {
            image = getImage("/pecas/bispoP.png");
        }
    }
}
