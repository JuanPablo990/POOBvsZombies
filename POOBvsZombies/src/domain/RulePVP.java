package domain;

public class RulePVP extends GameRule {
    private boolean isPlantTurn; // Indica si es el turno de las plantas
    private int remainingPlantTime; // Tiempo restante para la fase de plantar
    private int remainingZombieTime; // Tiempo restante para la fase de zombies
    private boolean isGameOver; // Indica si el juego terminó
    private int currentRound; // Ronda actual

    private static final int PLANT_PHASE_DURATION = 120; // 2 minutos en segundos

    /**
     * Constructor vacío para inicialización por defecto.
     */
    public RulePVP() {
        this(10); // Por defecto, la fase de ataque dura 10 minutos.
    }

    /**
     * Constructor con duración de la fase de zombies.
     *
     * @param zombiePhaseMinutes Duración de la fase de ataque en minutos.
     */
    public RulePVP(int zombiePhaseMinutes) {
        this.isPlantTurn = true; // Inicia en la fase de plantar
        this.remainingPlantTime = PLANT_PHASE_DURATION;
        this.remainingZombieTime = zombiePhaseMinutes * 60; // Convertir minutos a segundos
        this.isGameOver = false;
        this.currentRound = 1; // Comienza en la ronda 1
    }

    /**
     * Avanza el tiempo en la fase actual.
     *
     * @return false si el juego terminó, true en caso contrario.
     */
    public boolean advanceTime() {
        if (isGameOver) return false;

        if (isPlantTurn) {
            if (remainingPlantTime > 0) {
                remainingPlantTime--;
                if (remainingPlantTime == 0) {
                    isPlantTurn = false; // Cambiar a la fase de ataque
                }
            }
        } else {
            if (remainingZombieTime > 0) {
                remainingZombieTime--;
                if (remainingZombieTime == 0) {
                    isGameOver = true; // Fin del juego
                }
            }
        }
        return !isGameOver;
    }

    /**
     * Permite saltar manualmente a la siguiente fase.
     */
    public void skipPhase() {
        if (isPlantTurn) {
            remainingPlantTime = 0;
            isPlantTurn = false;
        }
    }

    /**
     * Indica si hay un cambio de fase.
     *
     * @return true si hay un cambio de fase, false de lo contrario.
     */
    public boolean isPhaseChange() {
        return remainingPlantTime == 0 && isPlantTurn;
    }

    /**
     * Indica si es el turno de las plantas.
     *
     * @return true si es el turno de las plantas, false si es el turno de los zombies.
     */
    public boolean isPlantTurn() {
        return isPlantTurn;
    }

    /**
     * Obtiene los minutos restantes en la fase actual.
     *
     * @return Minutos restantes en la fase actual.
     */
    public int getRemainingMinutes() {
        return isPlantTurn ? remainingPlantTime / 60 : remainingZombieTime / 60;
    }

    /**
     * Obtiene la ronda actual.
     *
     * @return Número de la ronda actual.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Obtiene el tiempo restante formateado como MM:SS.
     *
     * @return Tiempo restante formateado como String.
     */
    public String getFormattedTime() {
        int time = isPlantTurn ? remainingPlantTime : remainingZombieTime;
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Avanza manualmente a la siguiente ronda.
     */
    public void advanceRoundManually() {
        if (isPlantTurn) {
            skipPhase();
        } else {
            currentRound++;
            remainingPlantTime = PLANT_PHASE_DURATION;
            remainingZombieTime = 0; // Reinicia la fase de zombies
            isPlantTurn = true;
        }
    }

    /**
     * Verifica si una posición es válida para colocar una planta.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return true si es válida, false de lo contrario.
     */
    @Override
    public boolean isPlantPlacementValid(int row, int col) {
        return col >= 2 && col <= 9; // Las plantas solo pueden colocarse en columnas 2-9
    }

    /**
     * Verifica si una posición es válida para colocar un zombie.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return true si es válida, false de lo contrario.
     */
    @Override
    public boolean isZombiePlacementValid(int row, int col) {
        return col == 10; // Los zombies solo pueden colocarse en la columna 10
    }

    /**
     * Verifica si los zombies han ganado.
     *
     * @param board Tablero de juego.
     * @return true si los zombies han ganado, false en caso contrario.
     */
    @Override
    public boolean checkVictory(Board board) {
        for (int row = 0; row < boardRows; row++) {
            if (!board.isCellEmpty(row, 0)) { // Zombies llegaron a la casa
                return true;
            }
        }
        return false;
    }

	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return false;
	}
}
