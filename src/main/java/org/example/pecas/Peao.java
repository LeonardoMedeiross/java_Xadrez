package org.example.pecas;

import org.example.main.GamePanel;
import org.example.main.Tipo;

public class Peao extends Pecas {
    public Peao(int color, int col, int row) {
        super(color, col, row);
        tipo = Tipo.Peao;
        if (color == GamePanel.White) {
            image = getImage("/pecas/peaoB.png");
        } else {
            image = getImage("/pecas/peaoP.png");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isForaDoTabuleiro(targetCol, targetRow)&& isMesmoSquare(targetCol,targetRow)== false){
            //verifica o valor de movimento baseado na cor
            int moveValue;
            //se a cor for branca ela vai subir
            if (color == GamePanel.White){
                moveValue   = -1 ;
            }else {// se for preta vai descer
                moveValue = 1;
            }
                //chequando hitting peace
            hittingP =getHitting(targetCol,targetRow);

            //1 casa por movimento
            if (targetCol == preCol && targetRow == preRow + moveValue && hittingP == null){
                return true ;
            }
            //movimento de 2 casas
            if (targetCol == preCol && targetRow == preRow + moveValue*2 && hittingP == null && moved == false && pieceIsOnStrainghtLine(targetCol,targetRow)==false){
                return true;
            }
            //movimento em diagonal para comer a peça do adversario e ficar ali
            if (Math.abs(targetCol - preCol)== 1 && targetRow == preRow + moveValue && hittingP !=null && hittingP.color != color){
                return true ;
            }
            //en passant
            if (Math.abs(targetCol - preCol)== 1 && targetRow == preRow + moveValue) {
                for (Pecas pecas : GamePanel.simPecas){
                    if (pecas.col == targetCol && pecas.row == preRow && pecas.duasCasas == true){
                            hittingP = pecas ;
                            return true;
                    }
                }
            }
        }
        return false;
    }
}

