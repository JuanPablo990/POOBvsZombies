package presentation;

/**
 * Clase Element que representa una planta o un zombie con su nombre y rutas de imagen.
 */
public class Element {
    private String name; // Nombre del elemento (planta o zombie)
    private String imagePath; // Ruta de la imagen para los botones de referencia (en la barra superior)
    private String boardImagePath; // Ruta de la imagen para el tablero (5x10)

    /**
     * Constructor para inicializar un elemento.
     *
     * @param name            Nombre del elemento (ejemplo: "Sunflower")
     * @param imagePath       Ruta de la imagen para la barra superior o referencia.
     * @param boardImagePath  Ruta de la imagen para colocar en el tablero.
     */
    public Element(String name, String imagePath, String boardImagePath) {
        this.name = name;
        this.imagePath = imagePath;
        this.boardImagePath = boardImagePath;
    }

    /**
     * Obtiene el nombre del elemento.
     *
     * @return Nombre del elemento.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la ruta de la imagen de referencia (barra superior).
     *
     * @return Ruta de la imagen de referencia.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Obtiene la ruta de la imagen que se usa al colocar en el tablero.
     *
     * @return Ruta de la imagen para el tablero.
     */
    public String getBoardImagePath() {
        return boardImagePath;
    }

    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", boardImagePath='" + boardImagePath + '\'' +
                '}';
    }
}
