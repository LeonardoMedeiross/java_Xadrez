package org.example.pecas;

import org.example.main.GamePanel;

public class Rainha extends Pecas{
    public Rainha(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.White){
            image = getImage("/pecas/rainhaB.png");
        }else {
            image = getImage("/pecas/rainhaP.png");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isForaDoTabuleiro(targetCol,targetRow)&& isMesmoSquare(targetCol, targetRow)== false){

        }
        return false;
    }
}
