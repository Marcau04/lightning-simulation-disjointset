# ⚡ Simulación de rayos entre paneles conductores (Disjoint Set)

Este proyecto simula el comportamiento de **rayos aleatorios entre dos paneles conductores** y determina si se produce un **cortocircuito** (cuando los rayos conectan ambos paneles).  
Para detectar la conexión, se emplea una estructura **Disjoint Set (Union–Find)**.

---

## ⚙️ Descripción general

El sistema genera rayos de forma aleatoria entre los paneles y, tras cada generación, comprueba si existe un camino continuo de rayos que une ambos extremos.

Cuando esto ocurre, se considera que **se ha producido un cortocircuito** y la simulación finaliza.

---

## 🧩 Componentes principales

### `Celda.java`
Interfaz para representar el conjunto de celdas que habra entre los 2 paneles conductores.

### `CeldaAvanzada.java`
Extiende la clase `Celda` y gestiona la lógica de esta.  
Implementa la estructura **Disjoint Set** para determinar si dos celdas pertenecen al mismo conjunto (es decir, si están conectadas eléctricamente).

Principales métodos:
- `union(int x, int y)` – Une dos conjuntos.  
- `find(int x)` – Encuentra la raíz de un conjunto.  
- `connected(int x, int y)` – Comprueba si dos celdas están conectadas.  

### `Lanzador.java`
Archivo **proporcionado por el profesor** para lanzar la simulación y realizar pruebas automáticas.  
No forma parte del desarrollo original del alumno, pero se incluye para permitir la ejecución del proyecto.

---

## 🚀 Compilación y Ejecución

### Compilación
```bash
javac Celda.java CeldaAvanzada.java Lanzador.java
```
### Ejecución
```bash
java Lanzador
```
Durante la ejecución, el programa pedirá varios parámetros de configuración:

1- Validación (S/N):

  - Si respondes S, se ejecutará una sola iteración de la simulación (modo validación).

  - Si respondes N, se te pedirá cuántas veces quieres ejecutar el algoritmo (modo repetido).

2- Tamaño del tablero:

  - La simulación trabaja con matrices cuadradas, por lo que si introduces 2000, se generará una matriz 2000x2000.

  - Cuanto mayor sea el tamaño, más rayos podrán aparecer y mayor será la carga computacional.
