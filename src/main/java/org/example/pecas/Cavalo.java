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

    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if (isForaDoTabuleiro(targetCol,targetRow)){
        //cavalo anda 2 para vertical em forma de L , 1:2 ou 2: 1
            if (Math.abs(targetCol - preCol)*Math.abs(targetRow - preRow)==2){
                if (isValidSquare(targetCol,targetRow)){
                    return true;
                }

            }
        }

        return false;


 }
}
