package domain;

/**
 * Clase que representa el tablero lógico del juego.
 * Gestiona el estado de las celdas (vacías u ocupadas por plantas/zombis/podadoras).
 */
public class Board {
    private final String[][] cells; // Matriz lógica del tablero
    private final int rows;         // Número de filas
    private final int cols;         // Número de columnas

    /**
     * Constructor de la clase Board.
     *
     * @param rows Número de filas del tablero.
     * @param cols Número de columnas del tablero.
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new String[rows][cols];
        initializeBoard(); // Inicializar todas las celdas como vacías
    }

    /**
     * Inicializa el tablero colocando todas las celdas en "Empty".
     * También coloca las podadoras en la primera columna.
     */
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    cells[i][j] = "Lawnmower"; // Colocar podadoras en la primera columna
                } else {
                    cells[i][j] = "Empty"; // Resto de las celdas comienzan vacías
                }
            }
        }
    }

    /**
     * Verifica si una celda específica está vacía.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return true si la celda está vacía, false si está ocupada.
     */
    public boolean isCellEmpty(int row, int col) {
        return "Empty".equals(cells[row][col]); // Evita NullPointerException
    }

    /**
     * Verifica si una celda específica contiene una podadora.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return true si la celda contiene una podadora, false de lo contrario.
     */
    public boolean isCellLawnmower(int row, int col) {
        return "Lawnmower".equals(cells[row][col]); // Evita NullPointerException
    }

    /**
     * Actualiza el contenido de una celda específica.
     *
     * @param row       Fila de la celda.
     * @param col       Columna de la celda.
     * @param contenido Nombre del elemento que ocupará la celda (o null para vaciarla).
     */
    public void setCellContent(int row, int col, String contenido) {
        if (contenido == null) {
            cells[row][col] = "Empty"; // Asegúrate de que no quede como null
        } else {
            cells[row][col] = contenido;
        }
    }

    /**
     * Obtiene el contenido de una celda específica.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Contenido de la celda (e.g., "Sunflower", "Empty", "Lawnmower").
     */
    public String getCellContent(int row, int col) {
        return cells[row][col];
    }

    /**
     * Imprime el estado actual del tablero en la consola (para depuración).
     */
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
