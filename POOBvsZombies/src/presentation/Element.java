package presentation;

public class Element {

    private String name;
    private String imagePath; // Ruta de la imagen de selección
    private String boardImagePath; // Ruta de la imagen para el tablero

    public Element(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
        this.boardImagePath = imagePath; // Por defecto, ambas imágenes son iguales
    }

    public Element(String name, String imagePath, String boardImagePath) {
        this.name = name;
        this.imagePath = imagePath;
        this.boardImagePath = boardImagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBoardImagePath() {
        return boardImagePath;
    }
}
