package domain;

public class BrainsteinZombie extends Zombie {
    public BrainsteinZombie() {
        super(
            "BrainsteinZombie",
            "/presentation/images/images_Zombies/brainstein_zombie.png",
            "/presentation/images/images_Zombies/brainstein_zombie_board.gif",
            50,
            300,
            0,
            0
        );
    }

    @Override
    public void ability() {
        System.out.println("Genera 25 cerebros cada 20 segundos en lugar de moverse.");
        // Lógica para generar cerebros
    }
}
