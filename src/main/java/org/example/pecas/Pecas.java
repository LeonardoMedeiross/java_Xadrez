package org.example.pecas;

import org.example.main.GamePanel;
import org.example.main.Tabuleiro;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pecas {
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public Pecas hittingP ;

    // Construtor
    public Pecas(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }

    // Carregar a imagem da peça
    public BufferedImage getImage(String imagePatch) {
        BufferedImage image = null;
        try {
            // Verifica se o recurso da imagem foi encontrado
            image = ImageIO.read(getClass().getResourceAsStream(imagePatch));
            if (image == null) {
                System.err.println("Erro: Imagem não encontrada! Caminho: " + imagePatch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    // Calcula a posição X da peça
    public int getX(int col) {
        return col * Tabuleiro.SQUARE_SIZE;
    }

    // Calcula a posição Y da peça
    public int getY(int row) {
        return row * Tabuleiro.SQUARE_SIZE;
    }

    public int getCol(int x) {
        return (x + Tabuleiro.HALF_SQUARE_SIZE)/Tabuleiro.SQUARE_SIZE;
    }

    public int getRow(int y) {
        return (y + Tabuleiro.HALF_SQUARE_SIZE)/Tabuleiro.SQUARE_SIZE;
    }

    public int getIndex(){
        for (int index = 0 ; index < GamePanel.simPecas.size(); index ++ ){
            if (GamePanel.simPecas.get(index)==this){
                return index;
            }
        }return 0;
    }
    public void updatePosition(){
        //ajutando a posição quando solta o botão do mouse
        x=getX(col);
        y=getY(row);

        //aqui quando o movimento é confirmado atualiza as colunas e as linhas anteriores
        preCol=getCol(x);
        preRow=getRow(y);
    }
    public void resetPosition(){

        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public boolean canMove(int targetCol , int targetRow){

        return false;
    }

    public boolean isMesmoSquare (int targetCol , int targetRow ){
        if(targetCol == preCol && targetRow == preRow){
            return true;
        }return false;
    }
    public Pecas getHitting(int targetCol , int targetRow){
        for (Pecas pecas : GamePanel.simPecas){
            if (pecas.col == targetCol && pecas.row ==targetRow && pecas != this){
                return pecas;
            }
        }
        return null ;
    }

    public boolean isValidSquare(int targetCol , int targetRow ){
        hittingP =getHitting(targetCol,targetRow);

        if (hittingP == null)//se nao estiver nenhum hitP entao o quadadro esta vazio , podendo mover a peça para la
        {
            return true ;
        }else {//se o quadadro esta ocupado
            if (hittingP.color != this.color){//se a cor for diferente , a peça será captura
            return true ;
            }else {
                hittingP = null ;
            }
        }

        return false ;
    }

    public boolean pieceIsOnStrainghtLine(int targetCol, int targetRow) {
        // Verificar movimento para a esquerda
        if (targetCol < preCol) { // Movimento para a esquerda
            for (int c = preCol - 1; c > targetCol; c--) {
                for (Pecas p : GamePanel.simPecas) {
                    if (p.col == c && p.row == preRow) { // Checa peça no mesmo nível
                        hittingP = p;
                        return true;
                    }
                }
            }
        }

        // Verificar movimento para a direita
        if (targetCol > preCol) { // Movimento para a direita
            for (int c = preCol + 1; c < targetCol; c++) {
                for (Pecas p : GamePanel.simPecas) {
                    if (p.col == c && p.row == preRow) { // Checa peça no mesmo nível
                        hittingP = p;
                        return true;
                    }
                }
            }
        }

        // Verificar movimento para cima
        if (targetRow < preRow) { // Movimento para cima
            for (int r = preRow - 1; r > targetRow; r--) {
                for (Pecas p : GamePanel.simPecas) {
                    if (p.col == preCol && p.row == r) { // Checa peça na mesma coluna
                        hittingP = p;
                        return true;
                    }
                }
            }
        }

        // Verificar movimento para baixo
        if (targetRow > preRow) { // Movimento para baixo
            for (int r = preRow + 1; r < targetRow; r++) {
                for (Pecas p : GamePanel.simPecas) {
                    if (p.col == preCol && p.row == r) { // Checa peça na mesma coluna
                        hittingP = p;
                        return true;
                    }
                }
            }
        }

        return false; // Nenhuma peça foi encontrada no caminho
    }

    public boolean isForaDoTabuleiro(int targetCol ,int targetRow){
        //aqui significa se o targecol for maior ou igual a 0 e menor ou igual a 7 está dentro do tabuleiro
        //a mesma coisa se aplica para targetRow
        if(targetCol >= 0 && targetCol <= 7 && targetRow >=0 && targetRow <= 7 ){
            return true;
        }return false;
    }

    // Desenha a peça no tabuleiro
    public void draw(Graphics2D g2) {
        if (image != null) {
            // Desenha a imagem da peça, ajustando o tamanho
            g2.drawImage(image, x, y, Tabuleiro.SQUARE_SIZE, Tabuleiro.SQUARE_SIZE, null);
        } else {
            // Caso a imagem seja nula, imprime um erro
            System.err.println("Erro: A imagem da peça não foi carregada corretamente.");
        }
    }
}
