package com.calculadora.logica;

/**
 * Quien sepa resolver una operación entre dos números.
 * En la app la implementa MotorProlog, pero gracias a esta interfaz
 * la lógica de la calculadora no depende directamente de Prolog.
 */
public interface Operador {

    /**
     * @param simbolo '+', '-', '*' o '/'
     * @return el resultado como texto, o null si no hay solución
     */
    String operar(char simbolo, String a, String b) throws Exception;
}
