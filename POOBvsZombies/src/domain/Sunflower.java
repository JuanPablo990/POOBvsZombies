package domain;

public class Sunflower extends Plant {
    public Sunflower() {
        super(
            "Sunflower",
            "/presentation/images/images_Plants/sunflower.png",
            "/presentation/images/images_Plants/sunflower_board.gif",
            50,
            300
        );
    }

    @Override
    public void ability() {
        System.out.println("Genera 25 soles cada 20 segundos.");
        // Lógica para aumentar los soles en el contador del jugador
    }
}

