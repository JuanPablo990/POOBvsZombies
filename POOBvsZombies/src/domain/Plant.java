package domain;

/**
 * Clase abstracta para las plantas, extendiendo Element.
 */
public abstract class Plant extends Element {
    private int cost;         // Costo en soles para colocar la planta
    private int healthPoints; // Puntos de vida de la planta

    /**
     * Constructor de Plantas.
     *
     * @param name Nombre de la planta.
     * @param imagePath Ruta de la imagen de la carta.
     * @param boardImagePath Ruta de la imagen para el tablero.
     * @param cost Costo de la planta en soles.
     * @param healthPoints Puntos de vida iniciales de la planta.
     */
    public Plant(String name, String imagePath, String boardImagePath, int cost, int healthPoints) {
        super(name, imagePath, boardImagePath);
        this.cost = cost;
        this.healthPoints = healthPoints;
    }

    public int getCost() {
        return cost;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void takeDamage(int damage) {
        this.healthPoints -= damage;
        if (this.healthPoints < 0) {
            this.healthPoints = 0;
        }
    }

    public boolean isAlive() {
        return healthPoints > 0;
    }

    /**
     * Método abstracto para definir la habilidad única de cada planta.
     */
    public abstract void ability();
}
