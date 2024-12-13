package domain;

public class PotatoMine extends Plant {
    private boolean isArmed = false;

    public PotatoMine() {
        super(
            "PotatoMine",
            "/presentation/images/images_Plants/potatomine.png",
            "/presentation/images/images_Plants/potatomine_board.gif",
            25,
            100
        );
    }

    @Override
    public void ability() {
        if (!isArmed) {
            System.out.println("La papa está armándose.");
            // Simular tiempo de activación (14 segundos)
            isArmed = true;
        } else {
            System.out.println("¡Explota y elimina al zombie más cercano!");
            // Lógica para eliminar un zombie cercano
        }
    }
}
