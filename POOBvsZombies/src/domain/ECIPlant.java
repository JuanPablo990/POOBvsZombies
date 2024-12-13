package domain;

public class ECIPlant extends Plant {
    public ECIPlant() {
        super(
            "ECIPlant",
            "/presentation/images/images_Plants/eciplant.png",
            "/presentation/images/images_Plants/eciplant_board.gif",
            75,
            150
        );
    }

    @Override
    public void ability() {
        System.out.println("Genera un sol grande de 50 soles.");
        // Lógica para generar un sol grande
    }
}
