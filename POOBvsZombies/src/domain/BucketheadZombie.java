package domain;

public class BucketheadZombie extends Zombie {
    public BucketheadZombie() {
        super(
            "BucketheadZombie",
            "/presentation/images/images_Zombies/buckethead_zombie.png",
            "/presentation/images/images_Zombies/buckethead_zombie_board.gif",
            200,
            800,
            100,
            0.5
        );
    }

    @Override
    public void ability() {
        System.out.println("Avanza en línea recta con gran resistencia gracias a su cubeta.");
        // Lógica para moverse hacia adelante y atacar
    }
}
