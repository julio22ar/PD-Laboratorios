package com.calculadora.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Botón redondeado del teclado. */
class Tecla extends JButton {

    private final Color fondo;
    private boolean encima;

    Tecla(String texto, Color fondo, Color colorTexto, Runnable accion) {
        super(texto);
        this.fondo = fondo;

        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
        setForeground(colorTexto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFocusable(false); // así el teclado físico no "presiona" el botón enfocado
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(72, 60));
        addActionListener(e -> accion.run());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                encima = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encima = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c = fondo;
        if (getModel().isPressed()) {
            c = fondo.darker();
        } else if (encima) {
            c = mezclar(fondo, Color.BLACK, 0.08f);
        }
        g2.setColor(c);
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 22, 22);
        g2.dispose();

        super.paintComponent(g);
    }

    private static Color mezclar(Color a, Color b, float t) {
        return new Color(
                Math.round(a.getRed() * (1 - t) + b.getRed() * t),
                Math.round(a.getGreen() * (1 - t) + b.getGreen() * t),
                Math.round(a.getBlue() * (1 - t) + b.getBlue() * t));
    }
}
