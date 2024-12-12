package presentation;

public class Element {
    private final String name;               // Nombre del elemento (e.g., Sunflower, Peashooter)
    private final String imagePath;          // Ruta de la imagen para la carta
    private final String boardImagePath;     // Ruta de la imagen para el tablero (GIF)

    /**
     * Constructor para elementos que usan la misma imagen para la carta y el tablero.
     *
     * @param name Nombre del elemento.
     * @param imagePath Ruta de la imagen para la carta y el tablero.
     */
    public Element(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
        this.boardImagePath = imagePath; // Usa la misma imagen para ambas
    }

    /**
     * Constructor para elementos que tienen imágenes diferentes para la carta y el tablero.
     *
     * @param name Nombre del elemento.
     * @param imagePath Ruta de la imagen para la carta.
     * @param boardImagePath Ruta de la imagen para el tablero.
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
     * Obtiene la ruta de la imagen de la carta.
     *
     * @return Ruta de la imagen de la carta.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Obtiene la ruta de la imagen para el tablero.
     *
     * @return Ruta de la imagen para el tablero.
     */
    public String getBoardImagePath() {
        return boardImagePath;
    }

    /**
     * Método estático para crear un elemento LawnMower.
     * 
     * @return Una instancia del elemento LawnMower con sus imágenes configuradas.
     */
    public static Element createLawnMower() {
        return new Element(
            "Lawnmower",
            "/presentation/images/images_Plants/podadora.png",
            "/presentation/images/images_Plants/podadora.png"
        );
    }
}
