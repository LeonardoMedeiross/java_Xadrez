package org.example.pecas;

import org.example.main.GamePanel;
import org.example.main.Tipo;

public class Rainha extends Pecas{
    public Rainha(int color, int col, int row) {
        super(color, col, row);
        tipo= Tipo.Rainha;

        if (color == GamePanel.White){
            image = getImage("/pecas/rainhaB.png");
        }else {
            image = getImage("/pecas/rainhaP.png");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isForaDoTabuleiro(targetCol,targetRow)&& isMesmoSquare(targetCol, targetRow)== false){
            //vertical e horizontal
            if (targetCol == preCol || targetRow == preRow){
                if (isValidSquare(targetCol,targetRow) && pieceIsOnStrainghtLine(targetCol,targetRow) == false){
                    return true;
                }
            }
                //diagonal
            if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)){
                if (isValidSquare(targetCol,targetRow)&& pecaNaLinhaDiagonal(targetCol,targetRow)== false){
                    return true;
                }
            }
        }
        return false;
    }
}
