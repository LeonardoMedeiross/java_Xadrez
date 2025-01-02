package org.example.pecas;

import org.example.main.GamePanel;
import org.example.main.Tipo;

public class Bispo extends Pecas{
    public Bispo(int color, int col, int row) {
        super(color, col, row);
        tipo= Tipo.Bispo;

        if (color == GamePanel.White){
            image = getImage("/pecas/bispoB.png");

        }else {
            image = getImage("/pecas/bispoP.png");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isForaDoTabuleiro(targetCol,targetRow)&& isMesmoSquare(targetCol, targetRow)== false){
            if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)){
                if (isValidSquare(targetCol,targetRow)&& pecaNaLinhaDiagonal(targetCol,targetRow)== false){
                    return true;
                }
            }
        }

        return false;
    }
}
