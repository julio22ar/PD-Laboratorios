package com.calculadora.logica;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Lo que "recuerda" la calculadora entre tecla y tecla.
 * No sabe nada de botones ni ventanas: la interfaz solo le avisa
 * qué tecla se presionó y luego le pregunta qué mostrar.
 */
public class EstadoCalculadora {

    private static final int MAX_DIGITOS = 15;

    private final Operador operador;

    private String entrada = "0";        // número que se ve en grande
    private String expresion = "";       // línea pequeña de arriba, ej: "12 ×"
    private BigDecimal guardado = null;  // primer número de la operación
    private Character pendiente = null;  // operación esperando el segundo número
    private boolean escribiendo = false; // ¿el usuario está tecleando un número nuevo?
    private String error = null;

    public EstadoCalculadora(Operador operador) {
        this.operador = operador;
    }

    // ------------------------------------------------------------------
    //  Teclas
    // ------------------------------------------------------------------

    public void digito(char d) {
        if (error != null) limpiar();
        if (!escribiendo) {
            entrada = String.valueOf(d);
            escribiendo = true;
        } else if (entrada.equals("0")) {
            entrada = String.valueOf(d);
        } else if (entrada.equals("-0")) {
            entrada = "-" + d;
        } else if (contarDigitos() < MAX_DIGITOS) {
            entrada += d;
        }
    }

    public void punto() {
        if (error != null) limpiar();
        if (!escribiendo) {
            entrada = "0.";
            escribiendo = true;
        } else if (!entrada.contains(".")) {
            entrada += ".";
        }
    }

    public void cambiarSigno() {
        if (error != null) return;
        if (!escribiendo && pendiente != null) {
            // acaba de elegir operación: empieza a escribir un negativo
            entrada = "-0";
        } else {
            entrada = entrada.startsWith("-") ? entrada.substring(1) : "-" + entrada;
        }
        escribiendo = true;
    }

    public void borrar() {
        if (error != null || !escribiendo) return;
        entrada = entrada.substring(0, entrada.length() - 1);
        if (entrada.isEmpty() || entrada.equals("-")) {
            entrada = "0";
        }
    }

    public void limpiar() {
        entrada = "0";
        expresion = "";
        guardado = null;
        pendiente = null;
        escribiendo = false;
        error = null;
    }

    public void operacion(char simbolo) {
        if (error != null) return;

        // 5 + 3 ×  -> primero resuelve 5 + 3 y luego deja pendiente la ×
        if (pendiente != null && escribiendo) {
            resolver();
            if (error != null) return;
        }
        guardado = valor(entrada);
        pendiente = simbolo;
        escribiendo = false;
        expresion = formatear(guardado) + " " + visible(simbolo);
    }

    public void igual() {
        if (error != null || pendiente == null) return;
        String antes = formatear(guardado) + " " + visible(pendiente) + " " + formatear(valor(entrada)) + " =";
        resolver();
        if (error != null) return;
        expresion = antes;
        guardado = null;
        pendiente = null;
        escribiendo = false;
    }

    // ------------------------------------------------------------------
    //  Lo que muestra la pantalla
    // ------------------------------------------------------------------

    public String getPantalla() {
        return error != null ? error : entrada;
    }

    public String getExpresion() {
        return expresion;
    }

    public boolean hayError() {
        return error != null;
    }

    // ------------------------------------------------------------------
    //  Internos
    // ------------------------------------------------------------------

    /** Le pide al Operador (Prolog) que calcule: guardado [pendiente] entrada. */
    private void resolver() {
        BigDecimal b = valor(entrada);
        if (pendiente == '/' && b.signum() == 0) {
            fallar("No se puede dividir entre 0");
            return;
        }
        try {
            String r = operador.operar(pendiente, guardado.toPlainString(), b.toPlainString());
            if (r == null) {
                fallar("Sin solución");
                return;
            }
            entrada = formatear(new BigDecimal(r));
            guardado = valor(entrada);
            escribiendo = false;
        } catch (Exception e) {
            fallar("Error");
        }
    }

    private void fallar(String mensaje) {
        limpiar();
        error = mensaje;
    }

    private int contarDigitos() {
        return entrada.replace("-", "").replace(".", "").length();
    }

    private static BigDecimal valor(String texto) {
        return new BigDecimal(texto.endsWith(".") ? texto + "0" : texto);
    }

    /** 8.0 -> 8, 0.333333333333333 -> 0.3333333333 */
    static String formatear(BigDecimal n) {
        BigDecimal r = n;
        if (r.scale() > 10) {
            r = r.setScale(10, RoundingMode.HALF_UP);
        }
        r = r.stripTrailingZeros();
        return r.signum() == 0 ? "0" : r.toPlainString();
    }

    private static String visible(char simbolo) {
        return switch (simbolo) {
            case '+' -> "+";
            case '-' -> "−";
            case '*' -> "×";
            case '/' -> "÷";
            default -> String.valueOf(simbolo);
        };
    }
}
