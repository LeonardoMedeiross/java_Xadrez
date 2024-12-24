package org.example.pecas;

import org.example.main.GamePanel;

public class Peao extends Pecas {
    public Peao(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.White) {
            image = getImage("/pecas/peaoB.png");
        } else {
            image = getImage("/pecas/peaoP.png");
        }
    }
}

