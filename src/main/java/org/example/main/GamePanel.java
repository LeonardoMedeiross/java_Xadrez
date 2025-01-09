package org.example.main;

import org.example.pecas.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

//aqui iremos definir o tamanho da tela, cor de fundo
public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    public static Pecas castlingP;
    final int FPS = 60;
    Thread gameThread; //para poder usar o Thread tem que implementar o interface Runnable
    Tabuleiro board = new Tabuleiro();//instanciando o tabuleiro
    Mouse mouse = new Mouse(); //instanciando o Mouse

    //Peças
    public static ArrayList<Pecas> pecas = new ArrayList<>();//essa é mais como uma lista de back up
    public static ArrayList<Pecas> simPecas = new ArrayList<>();//usaremos mais o simPecas
    Pecas activeP ;

    //cor das peças
    public static final int White = 0;//0 esta definindo as peças em branco
    public static final int Black = 1;//1 esta definindo as peças em preto
    int currentColor = White;//aqui é para quem joga em primeiro

    //Booleanos
    boolean canMove;
    boolean validSquare;

    public GamePanel() {
        //tamanho e cor de fundo
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);

        //mouse
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        //peças
        setPecas();
        copyPecas(pecas, simPecas);

    }

    public void launchGame() {
        //isntaciando o Thread
        gameThread = new Thread(this);
        gameThread.start();//chamando o metodo start

    }

    public void setPecas() {
        //time branco
        pecas.add(new Peao(White, 0, 6));
        pecas.add(new Peao(White, 1, 6));
        pecas.add(new Peao(White, 2, 6));
        pecas.add(new Peao(White, 3, 6));
        pecas.add(new Peao(White, 4, 6));
        pecas.add(new Peao(White, 5, 6));
        pecas.add(new Peao(White, 6, 6));
        pecas.add(new Peao(White, 7, 6));
        pecas.add(new Torre(White, 0, 7));
        pecas.add(new Torre(White, 7, 7));
        pecas.add(new Cavalo(White, 1, 7));
        pecas.add(new Cavalo(White, 6, 7));
        pecas.add(new Bispo(White, 5, 7));
        pecas.add(new Bispo(White, 2, 7));
        pecas.add(new Rainha(White, 3, 7));
        pecas.add(new Rei(White, 4, 7 ));


        //time preto
        pecas.add(new Peao(Black, 0, 1));
        pecas.add(new Peao(Black, 1, 1));
        pecas.add(new Peao(Black, 2, 1));
        pecas.add(new Peao(Black, 3, 1));
        pecas.add(new Peao(Black, 4, 1));
        pecas.add(new Peao(Black, 5, 1));
        pecas.add(new Peao(Black, 6, 1));
        pecas.add(new Peao(Black, 7, 1));
        pecas.add(new Torre(Black, 0, 0));
        pecas.add(new Torre(Black, 7, 0));
        pecas.add(new Cavalo(Black, 1, 0));
        pecas.add(new Cavalo(Black, 6, 0));
        pecas.add(new Bispo(Black, 5, 0));
        pecas.add(new Bispo(Black, 2, 0));
        pecas.add(new Rainha(Black, 3, 0));
        pecas.add(new Rei(Black, 4, 0));
    }

    private void copyPecas(ArrayList<Pecas> source, ArrayList<Pecas> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }


    //para poder implementar o Runnable tem que ter o metodo run
    @Override
    public void run() {
        //Game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();// Captura o tempo atual em nanossegundos

            // Calcula o tempo decorrido desde o último ciclo e acumula no delta
            // Esse valor indica o quanto estamos "atrasados" para o próximo quadro
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime; // Atualiza o lastTime para o tempo atual, para a próxima iteração

            // Se o delta for maior ou igual a 1, significa que já é hora de processar o próximo quadro.
            if (delta >= 1) {
                update();//aqui ele atualiza o jogo a cada movimento de peças
                repaint();//aqui desenha o jogo com a autualização

                // Reduz o delta em 1, para ajustar o tempo acumulado
                // Isso garante que o loop fique sincronizado com o FPS
                delta--;
            }
        }
    }

    //aqui sera atualização das informações das posiçoes ou do número de peças que tem no Tabuleiro
    private void update() {
        if (mouse.clicar) {
            if (activeP == null) {
                for (Pecas pecas : simPecas) {
                    if (pecas.color == currentColor &&
                            pecas.col == mouse.x / Tabuleiro.SQUARE_SIZE &&
                            pecas.row == mouse.y / Tabuleiro.SQUARE_SIZE) {
                        activeP = pecas;
                    }
                }
            } else {
                // se um jogador esta segurando a peça , simula o movimento
                //seria uma fase de pensamento antes de soltar para jogar
                simulate();
            }
        }else {
            //soltando o mouse
            if (mouse.clicar == false) {
                if (activeP != null) {
                    if (validSquare) {
                        // captura a peça
                        if (activeP.hittingP !=null){
                            simPecas.remove(activeP.hittingP.getIndex());
                            activeP.hittingP = null ;
                        }

                        if (castlingP !=null ){
                            castlingP.updatePosition();
                        }
                        //atualiza posição da peça ativa
                        activeP.updatePosition();
                        copyPecas(simPecas,pecas);


                        // Verifica e promove o peão, se aplicável
                        handlePeaoPromotion(activeP);

                        //troca de turno
                        trocaDeTurno();

                    } else {
                        //reseta a posição se for invalido
                        copyPecas(simPecas, pecas);
                        activeP.resetPosition();

                    }
                    activeP = null;
                }
            }
        }
    }
    private void handlePeaoPromotion(Pecas peao) {
        if (!(peao instanceof Peao)) return; // Verifica se é um Peão

        // Verifica se o peão chegou na última linha
        if ((peao.color == White && peao.row == 0) || (peao.color == Black && peao.row == 7)) {
            // Cria uma janela de diálogo para o jogador escolher a peça para promoção
            String[] opcoes = {"Rainha", "Cavalo", "Bispo", "Torre"};
            String escolha = (String) JOptionPane.showInputDialog(
                    this,
                    "Escolha a peça para a promoção:",
                    "Promoção de Peão",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            // Promove o peão com base na escolha
            Pecas novaPeca = switch (escolha) {
                case "Cavalo" -> new Cavalo(peao.color, peao.col, peao.row);
                case "Bispo" -> new Bispo(peao.color, peao.col, peao.row);
                case "Torre" -> new Torre(peao.color, peao.col, peao.row);
                default -> new Rainha(peao.color, peao.col, peao.row);
            };

            // Substitui o peão pela nova peça
            simPecas.set(peao.getIndex(), novaPeca);
        }
    }


    public void simulate() {
        canMove = false;
        validSquare = false;

        //copia as peças para simluação
        copyPecas(simPecas, pecas);

        //reseta o castling
        if (castlingP != null){
            castlingP.col = castlingP.preCol;
            castlingP.x =castlingP.getX(castlingP.col);
            castlingP = null ;
        }

        //se o jogador soltou , atualiza a posição
        activeP.x = mouse.x - Tabuleiro.SQUARE_SIZE;
        activeP.y = mouse.y - Tabuleiro.SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        //checando se a peça foi solta no quadrado
        if(activeP.canMove(activeP.col,activeP.row)){

            canMove = true ;
            validSquare = true ;
            checkCastling();
        }
    }

    private void trocaDeTurno (){
        if ( currentColor == White){
            currentColor = Black ;
            //resetando as peças pretas no en passant
            for (Pecas p : pecas){
                if (p.color ==Black){
                    p.duasCasas = false;
                }
            }
        }else {
            currentColor = White;

            //resetando as peças brancas no en passant
            for (Pecas p : pecas){
                if (p.color ==White){
                    p.duasCasas = false;
                }
            }

        }
        activeP =null ;
    }

    private void checkCastling(){
        if (castlingP != null){
            if (castlingP.col == 0){
                castlingP.col +=3 ;
            }
            else if (castlingP.col == 7){
                castlingP.col -= 2 ;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    //o paintComponet lida com tudo que é desenhavel no programa
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Desenha o tabuleiro
        board.draw(g2);

        // Desenha as peças no tabuleiro
        for (Pecas p : simPecas) {
            p.draw(g2);
        }
        if (activeP != null) {
            if (canMove){
                g2.setColor(Color.WHITE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * Tabuleiro.SQUARE_SIZE, activeP.row * Tabuleiro.SQUARE_SIZE,
                        Tabuleiro.SQUARE_SIZE, Tabuleiro.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }


            //
            activeP.draw(g2);
        }
        //marik imagem
        ImageIcon icon = new ImageIcon(getClass().getResource("/pecas/personagens/marik200x200.png"));
        g2.drawImage(icon.getImage(),WIDTH - 250, 20,200, 200 , null);



        //status de mensagem
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book  Antiqua", Font.ROMAN_BASELINE,40));
        g2.setColor(Color.WHITE);

        if (currentColor == White){
            g2.drawString("Turno do Branco ", 800, 410);
        }else {
            g2.drawString("Turno do Preto", 800,410);
        }

    }
}