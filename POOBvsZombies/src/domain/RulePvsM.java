package domain;

public class RulePvsM extends GameRule {

    @Override
    public boolean isPlantPlacementValid(int row, int col) {
        return row >= 0 && row < boardRows && col >= 1 && col < boardCols;
    }

    @Override
    public boolean isZombiePlacementValid(int row, int col) {
        // En este modo, los zombies son generados automáticamente, así que esto podría no ser necesario
        return false; // No se permite colocar zombies manualmente en este modo
    }

    @Override
    public boolean checkVictory(Board board) {
        // Gana el jugador de plantas si detienen a todos los zombies antes de llegar a la casa de Dave
        for (int row = 0; row < boardRows; row++) {
            if (!board.isCellEmpty(row, boardCols - 1)) {
                return true; // Zombies ganan
            }
        }
        return false; // Las plantas ganan si no hay zombies en la última columna
    }
}
