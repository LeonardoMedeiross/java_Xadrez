package org.example.main;

import org.example.pecas.*;

import javax.swing.*;
import java.awt.*;
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
    Pecas activeP, checkingP;

    //cor das peças
    public static final int White = 0;//0 esta definindo as peças em branco
    public static final int Black = 1;//1 esta definindo as peças em preto
    int currentColor = White;//aqui é para quem joga em primeiro

    //Booleanos
    boolean canMove;
    boolean validSquare;
    boolean gameover;

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
      /*  pecas.add(new Peao(White, 0, 6));
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
        pecas.add(new Rainha(White, 3, 7));*/
        pecas.add(new Rei(White, 4, 7));


        //time preto
       /* pecas.add(new Peao(Black, 0, 1));
        pecas.add(new Peao(Black, 1, 1));
        pecas.add(new Peao(Black, 2, 1));
        pecas.add(new Peao(Black, 3, 1));
        pecas.add(new Peao(Black, 4, 1));
        pecas.add(new Peao(Black, 5, 1));
        pecas.add(new Peao(Black, 6, 1));
        pecas.add(new Peao(Black, 7, 1));*/
        pecas.add(new Torre(Black, 0, 0));
        pecas.add(new Torre(Black, 7, 0));
        pecas.add(new Cavalo(Black, 1, 0));
        pecas.add(new Cavalo(Black, 6, 0));
        pecas.add(new Bispo(Black, 5, 0));
        pecas.add(new Bispo(Black, 2    , 0));
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
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        // Enquanto o jogo não acabou e o thread estiver ativo
        while (!gameover && gameThread != null) {
            currentTime = System.nanoTime(); // Captura o tempo atual
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();   // Atualiza a lógica do jogo
                repaint();  // Redesenha o jogo
                delta--;
            }
        }
    }

    //aqui sera atualização das informações das posiçoes ou do número de peças que tem no Tabuleiro
    private void update() {
        // Se o jogo já terminou, não processa novos movimentos
        if (gameover) {
            return;
        }

        if (mouse.clicar) {
            if (activeP == null) {
                // Seleciona a peça ativa conforme a posição do mouse
                for (Pecas p : simPecas) {
                    if (p.color == currentColor &&
                            p.col == mouse.x / Tabuleiro.SQUARE_SIZE &&
                            p.row == mouse.y / Tabuleiro.SQUARE_SIZE) {
                        activeP = p;
                    }
                }
            } else {
                // Simula o movimento enquanto o jogador arrasta a peça
                simulate();
            }
        } else {
            // Quando o mouse é solto
            if (activeP != null) {
                if (validSquare) {
                    // Se houver peça a ser capturada, remove-a
                    if (activeP.hittingP != null) {
                        simPecas.remove(activeP.hittingP.getIndex());
                        activeP.hittingP = null;
                    }

                    if (castlingP != null) {
                        castlingP.updatePosition();
                    }

                    // Atualiza a posição da peça ativa e copia o estado simulado para o estado real
                    activeP.updatePosition();
                    copyPecas(simPecas, pecas);

                    // Verifica e promove o peão, se aplicável
                    handlePeaoPromotion(activeP);

                    // Troca de turno
                    trocaDeTurno();

                    // Verifica se, após a jogada, o rei está em xeque-mate.
                    if (isCheckmate()) {
                        gameover = true;
                    }else if (isStalemate()) {
                        gameover = true;
                    }

                } else {
                    // Se a jogada não foi válida, reseta a posição da peça
                    copyPecas(simPecas, pecas);
                    activeP.resetPosition();
                }
                activeP = null;
            }
        }
    }


    private boolean isReiEstaEnCheck() {
        Pecas rei = getRei(false); // Obtém o rei do jogador atual
        if (rei == null) return false; // Evita NullPointerException

        for (Pecas p : simPecas) {
            if (p.color != currentColor && p.canMove(rei.col, rei.row)) {
                checkingP = p; // Atualiza a peça que está dando o cheque
                return true;
            }
        }
        checkingP = null;
        return false;
    }

    private boolean isCheckmate() {
        // Se o rei não está em xeque, não é xeque-mate.
        if (!isReiEstaEnCheck()) {
            return false;
        }

        // Obtemos o rei do jogador atual.
        Pecas rei = getRei(false);
        if (rei == null) return true; // Se o rei foi capturado, é xeque-mate.

        // Para cada peça da cor atual, testamos todos os movimentos possíveis.
        for (Pecas p : simPecas) {
            if (p.color == currentColor) {
                // Percorre todas as casas do tabuleiro.
                for (int col = 0; col < 8; col++) {
                    for (int row = 0; row < 8; row++) {
                        // Se a peça pode mover para (col, row)
                        if (p.canMove(col, row)) {
                            // Salva a posição original.
                            int origCol = p.col;
                            int origRow = p.row;

                            // Verifica se há alguma peça na casa destino e simula sua captura.
                            Pecas capturada = getPecaNaPosicao(col, row);
                            if (capturada != null) {
                                simPecas.remove(capturada);
                            }

                            // Simula o movimento: atualiza a posição da peça candidata.
                            p.col = col;
                            p.row = row;

                            // Verifica se o rei ainda está em xeque após essa simulação.
                            boolean aindaEmCheck = isReiEstaEnCheck();

                            // Reverte o movimento: restaura a posição original.
                            p.col = origCol;
                            p.row = origRow;

                            // Se uma peça foi capturada, re-adiciona-a.
                            if (capturada != null) {
                                simPecas.add(capturada);
                            }

                            // Se após o movimento o rei NÃO está em xeque, então ainda há um movimento legal.
                            if (!aindaEmCheck) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        // Se nenhum movimento salvar o rei, é xeque-mate.
        return true;
    }
    private boolean isStalemate() {
        if (isReiEstaEnCheck()) return false;
        for (Pecas p : simPecas) {
            if (p.color == currentColor) {
                for (int col = 0; col < 8; col++) {
                    for (int row = 0; row < 8; row++) {
                        if (p.canMove(col, row)) {
                            return false;
                        }
                    }
                }
            }
        }
        gameover=true ;
        return true;
    }



    private Pecas getPecaNaPosicao(int col, int row) {
        for (Pecas p : simPecas) {
            if (p.col == col && p.row == row) {
                return p;
            }
        }
        return null;
    }


    private Pecas getRei(boolean oponente) {
        Pecas rei = null;
        for (Pecas p : simPecas) {
            if (oponente) {
                if (p.tipo == Tipo.Rei && p.color != currentColor) {
                    rei = p;
                }
            } else {
                if (p.tipo == Tipo.Rei && p.color == currentColor) {
                    rei = p;
                }
            }
        }

        return rei;
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
        if (castlingP != null) {
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

        //se o jogador soltou , atualiza a posição
        activeP.x = mouse.x - Tabuleiro.SQUARE_SIZE;
        activeP.y = mouse.y - Tabuleiro.SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        //checando se a peça foi solta no quadrado
        if (activeP.canMove(activeP.col, activeP.row)) {

            canMove = true;
            validSquare = true;
            checkCastling();

            if (isIllegal(activeP) == false) {
                validSquare = true;
            }
        }
    }

    public boolean isIllegal(Pecas rei) {
        if (rei.tipo == Tipo.Rei) {
            for (Pecas p : simPecas) {
                if (p != rei && p.color != rei.color && p.canMove(rei.col, rei.row)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void trocaDeTurno() {
        if (currentColor == White) {
            currentColor = Black;
            //resetando as peças pretas no en passant
            for (Pecas p : pecas) {
                if (p.color == Black) {
                    p.duasCasas = false;
                }
            }
        } else {
            currentColor = White;

            //resetando as peças brancas no en passant
            for (Pecas p : pecas) {
                if (p.color == White) {
                    p.duasCasas = false;
                }
            }

        }
        activeP = null;
    }

    private void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0) {
                castlingP.col += 3;
            } else if (castlingP.col == 7) {
                castlingP.col -= 2;
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
            if (canMove) {
                if (isIllegal(activeP)) {
                    g2.setColor(Color.GRAY);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * Tabuleiro.SQUARE_SIZE, activeP.row * Tabuleiro.SQUARE_SIZE,
                            Tabuleiro.SQUARE_SIZE, Tabuleiro.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } else {
                    g2.setColor(Color.WHITE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * Tabuleiro.SQUARE_SIZE, activeP.row * Tabuleiro.SQUARE_SIZE,
                            Tabuleiro.SQUARE_SIZE, Tabuleiro.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
            activeP.draw(g2);
        }

        // Exibe mensagem de cheque ou xeque-mate
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua", Font.BOLD, 40));
        g2.setColor(Color.WHITE);
        g2.drawString("Turno  " + (currentColor == White ? "Branco" : "Preto"), 800, 410);

        g2.setFont(new Font("Book Antiqua", Font.BOLD, 25));
        if (isCheckmate()) {
            g2.setColor(Color.RED);
            g2.drawString("XEQUE-MATE!", 800, 450);
            g2.setFont(new Font("Book Antiqua", Font.BOLD, 20));
            g2.drawString((currentColor == White ? "Pretos" : "Brancos") + " vencem!", 800, 475);
        } else if (isReiEstaEnCheck()) {
            g2.setColor(Color.RED);
            g2.drawString("XEQUE!", 800, 450);
            g2.setFont(new Font("Book Antiqua", Font.BOLD, 22));
            g2.drawString("O rei " + (currentColor == White ? "Branco" : "preto") + " está em cheque!", 800, 475);
        }

       /*//marik imagem
        ImageIcon icon = new ImageIcon(getClass().getResource("/pecas/personagens/marik200x200.png"));
        g2.drawImage(icon.getImage(),WIDTH - 250, 20,200, 200 , null);*/
    }
}