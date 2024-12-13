package domain;

public class WallNut extends Plant {
    public WallNut() {
        super(
            "WallNut",
            "/presentation/images/images_Plants/wallnut.png",
            "/presentation/images/images_Plants/wallnut_board.gif",
            50,
            4000
        );
    }

    @Override
    public void ability() {
        System.out.println("Actúa como un escudo para bloquear zombies.");
        // No requiere lógica activa, solo bloquea
    }
}
