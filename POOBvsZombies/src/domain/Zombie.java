package domain;

/**
 * Clase abstracta para los zombies, extendiendo Element.
 */
public abstract class Zombie extends Element {
    private int cost;         // Costo en cerebros para colocar el zombie
    private int healthPoints; // Puntos de vida del zombie
    private int damage;       // Daño que inflige al atacar
    private double attackSpeed; // Velocidad de ataque en segundos

    /**
     * Constructor para los zombies.
     *
     * @param name Nombre del zombie.
     * @param imagePath Ruta de la imagen de la carta.
     * @param boardImagePath Ruta de la imagen para el tablero.
     * @param cost Costo del zombie en cerebros.
     * @param healthPoints Puntos de vida iniciales del zombie.
     * @param damage Daño infligido por el zombie.
     * @param attackSpeed Velocidad de ataque del zombie.
     */
    public Zombie(String name, String imagePath, String boardImagePath, int cost, int healthPoints, int damage, double attackSpeed) {
        super(name, imagePath, boardImagePath);
        this.cost = cost;
        this.healthPoints = healthPoints;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }

    public int getCost() {
        return cost;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDamage() {
        return damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
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
     * Método abstracto para definir el comportamiento único de cada zombie.
     */
    public abstract void ability();
}
