package org.example.pecas;

import org.example.main.GamePanel;

public class Rei extends Pecas{
    public Rei(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.White) {
            image = getImage("/pecas/reiB.png");
        }else {
            image = getImage("/pecas/reiRá.png");
        }
    }

    public boolean canMove(int targetCol ,int targetRow){
        if (isForaDoTabuleiro(targetCol,targetRow)){

            if (Math.abs(targetCol-preCol)+Math.abs(targetRow - preRow)==1 ||
                  Math.abs(targetCol-preCol) * Math.abs(targetRow -preRow) ==1
            ){
                if (isValidSquare(targetCol,targetRow)){
                return true;
                    }
            }

        }
        return false;
    }
}
