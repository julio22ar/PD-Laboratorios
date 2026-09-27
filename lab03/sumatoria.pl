sumatoria(1, 1).

sumatoria(N, Resultado) :- 
    N > 1,
    N_Anterior is N - 1,
    sumatoria(N_Anterior, Res_Previo),
    Resultado is N + Res_Previo.