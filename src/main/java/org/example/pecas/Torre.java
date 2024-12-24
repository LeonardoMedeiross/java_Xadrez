package org.example.pecas;

import org.example.main.GamePanel;

public class Torre extends Pecas{
    public Torre(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.White){
            image = getImage("/pecas/TorreB.png");
        }else {
            image = getImage("/pecas/TorreP.png");
        }
    }
}
