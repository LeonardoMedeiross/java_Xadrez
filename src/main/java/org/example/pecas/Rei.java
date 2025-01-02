package org.example.pecas;

import org.example.main.GamePanel;
import org.example.main.Tipo;

public class Rei extends Pecas{
    public Rei(int color, int col, int row) {
        super(color, col, row);
        tipo= Tipo.Rei;

        if (color == GamePanel.White) {
            image = getImage("/pecas/reiB.png");
        }else {
            image = getImage("/pecas/reiRá.png");
        }
    }

    public boolean canMove(int targetCol ,int targetRow){
        //movimento
        if (isForaDoTabuleiro(targetCol,targetRow)){

            if (Math.abs(targetCol-preCol)+Math.abs(targetRow - preRow)==1 ||
                  Math.abs(targetCol-preCol) * Math.abs(targetRow -preRow) ==1
            ){
                if (isValidSquare(targetCol,targetRow)){
                return true;
                    }
            }
            //castling
            if(moved == false){
                //movendo para direita
                if(targetCol == preCol+2 && targetRow == preRow && pieceIsOnStrainghtLine(targetCol,targetRow)== false){
                    for (Pecas p : GamePanel.simPecas){
                        if (p.col == preCol+3 && p.row == preRow && p.moved== false){
                            GamePanel.castlingP = p;
                            return true;
                        }
                    }
                }
                //movendo para esquerda
                if(targetCol == preCol-2 && targetRow == preRow && pieceIsOnStrainghtLine(targetCol,targetRow)== false){
                    Pecas p [] = new Pecas[2] ;
                    for (Pecas pecas : GamePanel.simPecas){
                        if (pecas.col == preCol-3 && pecas.row == targetRow){
                            p[0] = pecas ;
                        }
                        if (pecas.col == preCol-4 && pecas.row == targetRow){
                            p[1] = pecas;
                        }
                        if (p[0] == null && p[1] != null  && p[1].moved == false){
                            GamePanel.castlingP = p[1];
                            return true ;
                        }
                    }
                }


            }

        }
        return false;
    }
}
