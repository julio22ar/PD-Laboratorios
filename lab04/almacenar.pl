% almacenar(+Numero, -Lista)
% Guarda los digitos del numero en una lista, del ultimo al primero.

almacenar(N, [N]) :-
    N < 10.

almacenar(N, [D|Resto]) :-
    N >= 10,
    D is N mod 10,
    N1 is N // 10,
    almacenar(N1, Resto).