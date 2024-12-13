package domain;

public class ConeheadZombie extends Zombie {
    public ConeheadZombie() {
        super(
            "ConeheadZombie",
            "/presentation/images/images_Zombies/conehead_zombie.png",
            "/presentation/images/images_Zombies/conehead_zombie_board.gif",
            150,
            380,
            100,
            0.5
        );
    }

    @Override
    public void ability() {
        System.out.println("Avanza en línea recta con más resistencia gracias a su cono.");
        // Lógica para moverse hacia adelante y atacar
    }
}
