% ==============================================================
%  Calculadora V2 - base de conocimiento
%  Java solo pregunta:  operar(Simbolo, A, B, R).
%  Prolog decide que operacion es y calcula el resultado.
% ==============================================================

% ---- Hechos: que operacion representa cada simbolo -----------
simbolo('+', suma).
simbolo('-', resta).
simbolo('*', multiplicacion).
simbolo('/', division).

% ---- Reglas: una relacion calcular/4 con una clausula por operacion
calcular(suma, A, B, R) :-
    R is A + B.

calcular(resta, A, B, R) :-
    R is A - B.

calcular(multiplicacion, A, B, R) :-
    R is A * B.

% La division solo tiene solucion si B no es 0.
% A se convierte a decimal para que 7 / 2 de 3.5 y no 3.
calcular(division, A, B, R) :-
    B =\= 0,
    Decimal is A * 1.0,
    R is Decimal / B.

% ---- Punto de entrada que usa Java ----------------------------
% 1. busca la operacion del simbolo, 2. verifica que sean numeros,
% 3. calcula. Si algo falla, la consulta no tiene solucion.
operar(Simbolo, A, B, R) :-
    simbolo(Simbolo, Operacion),
    number(A),
    number(B),
    calcular(Operacion, A, B, R).
