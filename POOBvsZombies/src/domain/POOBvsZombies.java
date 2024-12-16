package domain;

import java.util.List;

public class POOBvsZombies {

    // Atributos principales
    private int solesIniciales;
    private int cerebrosIniciales;
    private int duracionPartida;
    private List<String> plantasSeleccionadas;
    private List<String> zombiesSeleccionados;

    // Constructor con parámetros
    public POOBvsZombies(int solesIniciales, int cerebrosIniciales, int duracionPartida, 
                         List<String> plantasSeleccionadas, List<String> zombiesSeleccionados) {
        this.solesIniciales = solesIniciales;
        this.cerebrosIniciales = cerebrosIniciales;
        this.duracionPartida = duracionPartida;
        this.plantasSeleccionadas = plantasSeleccionadas;
        this.zombiesSeleccionados = zombiesSeleccionados;
    }

    // Métodos para depuración (temporal)
    public void mostrarConfiguracion() {
        System.out.println("Soles iniciales: " + solesIniciales);
        System.out.println("Cerebros iniciales: " + cerebrosIniciales);
        System.out.println("Duración partida (minutos): " + duracionPartida);
        System.out.println("Plantas seleccionadas: " + plantasSeleccionadas);
        System.out.println("Zombies seleccionados: " + zombiesSeleccionados);
    }
}
