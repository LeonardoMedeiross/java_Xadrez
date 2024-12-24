package org.example.pecas;

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
    public void updatePosition(){
        //ajutando a posição quando solta o botão do mouse
        x=getX(col);
        y=getY(row);

        //aqui quando o movimento é confirmado atualiza as colunas e as linhas anteriores
        preCol=getCol(x);
        preRow=getRow(y);
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
