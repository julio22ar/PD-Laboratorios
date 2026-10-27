package com.calculadora;

import com.calculadora.logica.EstadoCalculadora;
import com.calculadora.logica.MotorProlog;
import com.calculadora.ui.VentanaCalculadora;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                EstadoCalculadora estado = new EstadoCalculadora(new MotorProlog());
                new VentanaCalculadora(estado).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo iniciar Prolog:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
