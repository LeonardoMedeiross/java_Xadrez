package org.example.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    //aquio adicionaremos 4 moetodos
    public int x ,y ;
    public boolean clicar ;


    @Override
    public void mousePressed(MouseEvent e ){
        clicar = true ;

    }

    @Override
    public void mouseReleased(MouseEvent e ){
        clicar = false ;
    }

    @Override
    public void mouseDragged(MouseEvent e ){
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e ){
        x = e.getX();
        y = e.getY();
    }
}
