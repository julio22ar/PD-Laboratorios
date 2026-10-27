package com.calculadora.ui;

import com.calculadora.logica.EstadoCalculadora;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

/** Ventana con pantalla + teclado. Cada tecla le avisa al estado y luego se refresca la pantalla. */
public class VentanaCalculadora extends JFrame {

    private final EstadoCalculadora estado;
    private final Pantalla pantalla = new Pantalla();

    public VentanaCalculadora(EstadoCalculadora estado) {
        super("Calculadora V2 · Prolog");
        this.estado = estado;

        JPanel raiz = new JPanel(new BorderLayout(0, 14));
        raiz.setBackground(Colores.FONDO);
        raiz.setBorder(new EmptyBorder(18, 18, 18, 18));
        raiz.add(pantalla, BorderLayout.NORTH);
        raiz.add(crearTeclado(), BorderLayout.CENTER);

        setContentPane(raiz);
        conectarTecladoFisico();
        refrescar();

        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    //  Teclado en pantalla
    //    C   ⌫   ±   ÷
    //    7   8   9   ×
    //    4   5   6   −
    //    1   2   3   +
    //    0       .   =
    // ------------------------------------------------------------------

    private JPanel crearTeclado() {
        JPanel t = new JPanel(new GridBagLayout());
        t.setOpaque(false);

        poner(t, funcion("C", estado::limpiar), 0, 0, 1);
        poner(t, funcion("⌫", estado::borrar), 1, 0, 1);
        poner(t, funcion("±", estado::cambiarSigno), 2, 0, 1);
        poner(t, operacion("÷", '/'), 3, 0, 1);

        poner(t, numero('7'), 0, 1, 1);
        poner(t, numero('8'), 1, 1, 1);
        poner(t, numero('9'), 2, 1, 1);
        poner(t, operacion("×", '*'), 3, 1, 1);

        poner(t, numero('4'), 0, 2, 1);
        poner(t, numero('5'), 1, 2, 1);
        poner(t, numero('6'), 2, 2, 1);
        poner(t, operacion("−", '-'), 3, 2, 1);

        poner(t, numero('1'), 0, 3, 1);
        poner(t, numero('2'), 1, 3, 1);
        poner(t, numero('3'), 2, 3, 1);
        poner(t, operacion("+", '+'), 3, 3, 1);

        poner(t, numero('0'), 0, 4, 2); // el 0 ocupa dos columnas
        poner(t, new Tecla(".", Colores.NUMERO, Colores.TEXTO_OSCURO, () -> pulsar(estado::punto)), 2, 4, 1);
        poner(t, new Tecla("=", Colores.IGUAL, Colores.TEXTO_CLARO, () -> pulsar(estado::igual)), 3, 4, 1);
        return t;
    }

    private Tecla numero(char d) {
        return new Tecla(String.valueOf(d), Colores.NUMERO, Colores.TEXTO_OSCURO,
                () -> pulsar(() -> estado.digito(d)));
    }

    private Tecla operacion(String texto, char simbolo) {
        return new Tecla(texto, Colores.OPERADOR, Colores.TEXTO_CLARO,
                () -> pulsar(() -> estado.operacion(simbolo)));
    }

    private Tecla funcion(String texto, Runnable accion) {
        return new Tecla(texto, Colores.FUNCION, Colores.TEXTO_OSCURO, () -> pulsar(accion));
    }

    private static void poner(JPanel panel, Tecla tecla, int col, int fila, int ancho) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = fila;
        c.gridwidth = ancho;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(4, 4, 4, 4);
        panel.add(tecla, c);
    }

    // ------------------------------------------------------------------
    //  Teclado físico: números, + - * /, Enter o =, Backspace, Esc
    // ------------------------------------------------------------------

    private void conectarTecladoFisico() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() != KeyEvent.KEY_TYPED || !isActive()) {
                return false;
            }
            char c = e.getKeyChar();
            if (Character.isDigit(c)) {
                pulsar(() -> estado.digito(c));
            } else if (c == '.' || c == ',') {
                pulsar(estado::punto);
            } else if ("+-*/".indexOf(c) >= 0) {
                pulsar(() -> estado.operacion(c));
            } else if (c == '=' || c == '\n') {
                pulsar(estado::igual);
            } else if (c == '\b') {
                pulsar(estado::borrar);
            } else if (c == KeyEvent.VK_ESCAPE) {
                pulsar(estado::limpiar);
            } else {
                return false;
            }
            return true;
        });
    }

    // ------------------------------------------------------------------

    private void pulsar(Runnable accion) {
        accion.run();
        refrescar();
    }

    private void refrescar() {
        pantalla.actualizar(estado.getExpresion(), estado.getPantalla(), estado.hayError());
    }
}
