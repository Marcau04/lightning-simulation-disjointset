public class CeldaAvanzada implements Celda {                                               // Clase CeldaAvanzada hecha por Marcos Alonso Ulloa

    boolean cortocircuitable;                                                              // Variable utilizada para conocer si la matriz ha producido un cortocircuito o no.
    boolean[][] conductor;                                                                 // Matriz boolana que identifica las casillas conductoras de las que no lo son.
    int[] arrayDJ;                                                                         // Array utilizado para implementar el algoritmo DisjointsSets.
    int totalSize;                                                                         // Variable usada para el numero de las filas y columnas(no necesaria pero utilizada para poder realizar el algoritmo de manera mas visual).
    int size;                                                                              // Numero total de celdas que tendrá el arrayDJ (filas*columnas).
    int ultimoi;                                                                           // Variables usadas para conocer el punto que ha realizado el cortocircuito.
    int ultimoj;

    public void Inicializar(int n) {                                                       // Inicializamos las variables globales.
        int valor;                                                                         // Variable usada para saber que valor del arrayDJ le corresponde a cada casilla de la matriz.
        totalSize = n;                                                                     // totalSize=numero de filas y numero de columnas(Ya que la matriz es cuadrada filas=columnas).
        size = totalSize * totalSize;                                                      // size=fila*columna.
        arrayDJ = new int[n * n];                                                          // Inicializamos los 2 arrays.
        conductor = new boolean[n][n];
        cortocircuitable = false;                                                          // Inicializamos cortocircuitable en falso.

        for (int i = 0; i < conductor.length; i++) {
            for (int j = 0; j < conductor[0].length; j++) {                                // Inicializamos los valores de cada celda de los arrays.
                conductor[i][j] = false;                                                   // La matriz de conductor de inicializa en falso.
                valor = j * n + i;
                arrayDJ[valor] = valor;                                                    // El array de DisjointsSets se inicializa de forma que cada valor se apunte a si mismo
                                                                                            // (al inicio cada valor es la raiz de su propio conjunto).
            }
        }
    }

    public boolean Cortocircuito() {                                                      // Funcion que devuelve el valor de cortocircuitable.
        return this.cortocircuitable;
    }

    public void RayoCosmico(int j, int i) {                                              // Funcion RayoCosmico.
        if (conductor[i][j] == true) {                                                   // Si la posicion ya era conductora de antes no se hace nada.
            return;
        } else {
            conductor[i][j] = true;                                                      // Si no lo era, la ponemos como cortocircuitable y comprobamos los vecinos.
            comprobarvecinos(i, j);
        }
    }

    public void comprobarvecinos(int i, int j) {
        if (cortocircuitable) return;                                                    // Salimos inmediatamente si ya hay cortocircuito

        // Recorremos los 8 vecinos con desplazamientos
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int k = 0; k < dx.length; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];

            // Comprobamos que el vecino esté dentro del tablero y sea conductor
            if (ni >= 0 && ni < conductor.length && nj >= 0 && nj < conductor.length) {
                if (conductor[ni][nj]) {
                    comprobarconjuntos(ni, nj, i, j);
                    if (cortocircuitable) break;                                        // Salimos si se produce cortocircuito
                }
            }
        }
    }

    public void comprobarconjuntos(int vecinosI, int vecinosJ, int i, int j) {           // Funcion que nos permitirá comprobar los conjuntos y operar con ellos dados 2 puntos.
        int vecino = vecinosJ * totalSize + vecinosI;                                     // Calculamos la equivalencia de ambos puntos con su posicion en el arrayDJ.
        int punto = j * totalSize + i;
        int raizPunto = encontrarRaiz(punto);                                             // Encontramos la raiz de ambos conjuntos
                                                                                            // (en mi implementación la raiz es el elemento mas pequeño del conjunto
                                                                                            // y todo valor que no pertenece a ningun conjunto es raiz de su propio conjunto)
        int raizVecino = encontrarRaiz(vecino);
        int raizk;                                                                        // Variable usada para encontrar la raiz de los conjuntos de los elementos
                                                                                            // de la última fila de la matriz para comprobar un camino que cortocircuite

        if (raizPunto != raizVecino) {
            raizPunto = unionconjuntos(raizPunto, raizVecino);                            // Si el punto dado y el punto vecino pertenecen a distintos conjuntos
                                                                                            // (es decir que tienen diferentes raices) entonces unimos ambos conjuntos
                                                                                            // (para unirlos solo cambiamos la raiz es decir el elemento mas pequeño del conjunto)
                                                                                            // y obtenemos la nueva raiz del conjunto punto
            if (raizPunto < totalSize) {                                                  // Si dicha raiz, es decir, el elemento mas pequeño del conjunto esta en la primera fila,
                                                                                            // entonces calculamos la raiz de todos los elementos de la última fila hasta encontrar
                                                                                            // un elemento que tenga la misma raiz es decir que apunte al mismo elemento
                for (int k = size - totalSize; k < size; k++) {
                    raizk = encontrarRaiz(k);                                             // Calculamos la raiz de cada elemento de la ultima fila de la matriz.
                    if (raizk == raizPunto) {                                             // Si coincide con la raiz del punto entonces se ha producido un cortocircuito.
                        this.cortocircuitable = true;                                     // Cambiamos la variable cortocircuitable y guardamos el ultimo punto que lo ha producido.
                        this.ultimoi = i;
                        this.ultimoj = j;
                        return;
                    }
                }
            }
        }
    }

    public int encontrarRaiz(int punto) {                                               // Método que busca la raiz de un conjunto
                                                                                            // es decir el elemento mas pequeño del conjunto al que todos los elementos del mismo conjunto tienen que apuntar
        if (arrayDJ[punto] != punto) {                                                  // Si un elemento no se apunta a si mismo es decir no es la raiz de su conjunto
                                                                                            // hacemos que este apunte al elemento final de su conjunto.
            arrayDJ[punto] = encontrarRaiz(arrayDJ[punto]);                              // Ejemplo funcionamiento: 20->2  2->1 1->1, al buscar en 20 este se verá que apunta al 2
                                                                                            // pero que 2 apunta a 1 por lo que se actualiza 20 para que apunte a 1 que es el elemento mas pequeño del conjunto
        }
        return arrayDJ[punto];                                                          // Si se apunta a si mismo devolvemos ese valor y sino la raiz del conjunto
    }

    public int unionconjuntos(int raizPunto, int raizVecino) {                          // Funcion que une 2 conjuntos operando unicamente con sus raices
                                                                                            // de forma que al cambiar la raiz de un conjunto, la proxima vez que se busque la raiz de un elemento del conjunto,
                                                                                            // este pasara a apuntar a la nueva raiz.
        if (raizPunto > raizVecino) {                                                  // Si la raiz del punto es mayor que la raiz del vecino entonces la raiz del conjunto del punto pasa
                                                                                            // a apuntar a la raiz del conjunto del vecino convirtiendose esta en la nueva raiz del conjunto punto
                                                                                            // (ejemplo: raiz punto=2 raiz vecino =1 2>1, la raiz del punto apunta a 1 ahora (2->1 , 1->1) 
                                                                                            // y como 20 apuntaba a 2, la proxima vez que se realice la busqueda en 20 este apuntara a 1 ya que 20->2->1)
            arrayDJ[raizPunto] = raizVecino;
            return raizVecino;                                                          // Devolvemos la raiz del vecino ya que esta es la nueva raiz del conjunto punto que tendremos que usar para buscar un camino que cortocircuite.
        } else {
            arrayDJ[raizVecino] = raizPunto;                                           // Si la raiz del vecino es mas grande que la del punto, la raiz del vecino pasa a apuntar a la raiz del punto.
            return raizPunto;                                                          // Devolvemos la raiz del punto ya que esta no ha cambiado.
        }
    }

    @Override
    public String toString() {                                                          //Imprime la matriz formateada con los rayos correspondientes
        StringBuilder sb = new StringBuilder();
        int n = this.conductor.length;

        sb.append("   ");
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%2d ", i));
        }
        sb.append("\n");

        sb.append("   ");
        for (int i = 0; i < n; i++) {
            sb.append("---");
        }
        sb.append("\n");

        for (int j = 0; j < n; j++) {
            sb.append(String.format("%2d|", j));
            for (int i = 0; i < n; i++) {
                if (i == this.ultimoi && j == this.ultimoj) {
                    sb.append(" * ");
                } else if (this.conductor[i][j]) {
                    sb.append(" X ");
                } else {
                    sb.append(" . ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
