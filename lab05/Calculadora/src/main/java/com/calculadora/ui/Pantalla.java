package com.calculadora.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Pantalla con dos líneas: la operación en curso (pequeña) y el número actual (grande). */
class Pantalla extends JPanel {

    private final JLabel lblExpresion = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel lblValor = new JLabel("0", SwingConstants.RIGHT);

    Pantalla() {
        super(new BorderLayout(0, 4));
        setOpaque(false);
        setBorder(new EmptyBorder(18, 20, 18, 20));
        setPreferredSize(new Dimension(320, 120));

        lblExpresion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        lblExpresion.setForeground(Colores.TEXTO_SUAVE);

        lblValor.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        lblValor.setForeground(Colores.TEXTO_CLARO);

        add(lblExpresion, BorderLayout.NORTH);
        add(lblValor, BorderLayout.CENTER);
    }

    void actualizar(String expresion, String valor, boolean error) {
        lblExpresion.setText(expresion.isEmpty() ? " " : expresion);
        lblValor.setText(valor);
        lblValor.setForeground(error ? Colores.ERROR : Colores.TEXTO_CLARO);
        // textos largos: letra más pequeña para que quepan
        float tam = valor.length() > 12 ? 22f : 40f;
        lblValor.setFont(lblValor.getFont().deriveFont(tam));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Colores.PANTALLA);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
        g2.dispose();
        super.paintComponent(g);
    }
}
