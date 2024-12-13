package domain;

import java.util.Arrays; // Asegúrate de incluir esto


public class POOBvsZombies {
    private final Board board;
    private final GameRule gameRule;
    private int suns;  // Recursos para el jugador de plantas
    private int brains;  // Recursos para el jugador de zombies

    public POOBvsZombies(String mode) {
        this.board = new Board(5, 10);
        this.suns = 50;  // Soles iniciales
        this.brains = 50;  // Cerebros iniciales

        // Configurar reglas según el modo
        switch (mode) {
            case "PVP":
                this.gameRule = new RulePVP();
                break;
            case "PvsM":
                // Implementar otras reglas
                this.gameRule = new RulePvsM();
                break;
            default:
                throw new IllegalArgumentException("Modo de juego no válido");
        }
    }

    public boolean placePlant(int row, int col, String plantName) {
        if (gameRule.isPlantPlacementValid(row, col) && board.isCellEmpty(row, col)) {
            board.setCellContent(row, col, plantName);
            return true;
        }
        return false;
    }

    public boolean placeZombie(int row, int col, String zombieName) {
        if (gameRule.isZombiePlacementValid(row, col) && board.isCellEmpty(row, col)) {
            board.setCellContent(row, col, zombieName);
            return true;
        }
        return false;
    }

    public boolean checkVictory() {
        return gameRule.checkVictory(board);
    }

    public void addSuns(int amount) {
        this.suns += amount;
    }

    public void addBrains(int amount) {
        this.brains += amount;
    }

    public int getSuns() {
        return suns;
    }

    public int getBrains() {
        return brains;
    }

    public Board getBoard() {
        return board;
    }
}
