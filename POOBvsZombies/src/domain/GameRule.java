package domain;

public abstract class GameRule {
    protected final int boardRows = 5;
    protected final int boardCols = 10;

    public abstract boolean isPlantPlacementValid(int row, int col);

    public abstract boolean isZombiePlacementValid(int row, int col);

    public abstract boolean checkVictory(Board board);
}
