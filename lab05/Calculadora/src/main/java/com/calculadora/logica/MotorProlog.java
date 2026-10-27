package com.calculadora.logica;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

import java.io.InputStream;

/** Carga operaciones.pl y le hace consultas del tipo operar('+', 5, 3, R). */
public class MotorProlog implements Operador {

    private final Prolog prolog = new Prolog();

    public MotorProlog() throws Exception {
        try (InputStream archivo = MotorProlog.class.getResourceAsStream("/operaciones.pl")) {
            if (archivo == null) {
                throw new IllegalStateException("No se encontró operaciones.pl en resources");
            }
            prolog.setTheory(new Theory(archivo));
        }
    }

    @Override
    public String operar(char simbolo, String a, String b) throws Exception {
        String consulta = "operar('" + simbolo + "', " + a + ", " + b + ", R).";
        SolveInfo respuesta = prolog.solve(consulta);
        if (!respuesta.isSuccess()) {
            return null;
        }
        return respuesta.getTerm("R").toString();
    }
}
