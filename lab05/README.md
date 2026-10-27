# Calculadora V2 (Java + Prolog)

Calculadora de las 4 operaciones básicas con teclado tipo calculadora.
La interfaz está en Java Swing y las operaciones las resuelve Prolog (tuProlog).

## Cómo correrla
Abrir la carpeta `CalculadoraV2` en IntelliJ como proyecto Maven y ejecutar `com.calculadora.App`.

## Cómo está organizada
```
src/main/resources/operaciones.pl      -> reglas de Prolog
src/main/java/com/calculadora/
├── App.java                           -> arranca la app
├── logica/
│   ├── Operador.java                  -> interfaz: "algo que sabe operar"
│   ├── MotorProlog.java               -> implementa Operador consultando a Prolog
│   └── EstadoCalculadora.java         -> memoria de la calculadora (número actual, operación pendiente)
└── ui/
    ├── VentanaCalculadora.java        -> ventana + teclado
    ├── Pantalla.java                  -> display de dos líneas
    ├── Tecla.java                     -> botón redondeado
    └── Colores.java                   -> paleta
```

## Flujo de una operación (ej: 12 ÷ 4 =)
1. Se presiona `1`, `2` → `EstadoCalculadora` arma el número "12".
2. Se presiona `÷` → guarda 12 y deja pendiente la división.
3. Se presiona `4` y `=` → `MotorProlog` consulta `operar('/', 12, 4, R).`
4. Prolog busca `simbolo('/', division)` y aplica `calcular(division, 12, 4, R)` → `R = 3.0`.
5. La pantalla muestra `3` y arriba `12 ÷ 4 =`.

Las operaciones se resuelven de izquierda a derecha, como una calculadora básica:
`2 + 3 × 4 =` da 20.

## Teclado físico
Números, `+ - * /`, `.` o `,`, Enter o `=`, Backspace para borrar y Esc para limpiar.
