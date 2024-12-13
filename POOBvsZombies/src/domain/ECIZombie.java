package domain;

public class ECIZombie extends Zombie {
    public ECIZombie() {
        super(
            "ECIZombie",
            "/presentation/images/images_Zombies/ecizombie.png",
            "/presentation/images/images_Zombies/ecizombie_board.gif",
            250,
            200,
            50,
            3
        );
    }

    @Override
    public void ability() {
        System.out.println("Dispara POOmBas que infligen 50 de daño cada 3 segundos.");
        // Lógica para disparar proyectiles
    }
}
