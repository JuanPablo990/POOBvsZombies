package domain;

public class BasicZombie extends Zombie {
    public BasicZombie() {
        super(
            "BasicZombie",
            "/presentation/images/images_Zombies/basic_zombie.png",
            "/presentation/images/images_Zombies/basic_zombie_board.gif",
            100,
            100,
            100,
            0.5
        );
    }

    @Override
    public void ability() {
        System.out.println("Avanza en línea recta y ataca plantas con 100 de daño cada 0.5 segundos.");
        // Lógica para moverse hacia adelante y atacar
    }
}
