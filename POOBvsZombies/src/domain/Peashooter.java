package domain;

public class Peashooter extends Plant {
    public Peashooter() {
        super(
            "Peashooter",
            "/presentation/images/images_Plants/peashooter.png",
            "/presentation/images/images_Plants/peashooter_board.gif",
            100,
            300
        );
    }

    @Override
    public void ability() {
        System.out.println("Dispara un guisante que inflige 20 de daño.");
        // Lógica para atacar zombies
    }
}
