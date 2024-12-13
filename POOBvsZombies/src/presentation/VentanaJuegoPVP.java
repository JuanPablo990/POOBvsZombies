package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import domain.Element; // Clase Element en el paquete domain
import domain.Board;   // Clase Board en el paquete domain
import domain.RulePVP; // Clase RulePVP para las reglas del juego

public class VentanaJuegoPVP extends JFrame {

    private JLabel puntosSolesLabel;
    private JLabel puntosCerebrosLabel;
    private JLabel contadorSolesJugador1Label;
    private JLabel contadorCerebrosJugador2Label;
    private JLabel labelTemporizador; // Label para el temporizador
    private JLabel labelRonda; // Label para la ronda actual
    private int puntosSoles = 0;
    private int puntosCerebros = 0;
    private int contadorSolesJugador1 = 0;
    private int contadorCerebrosJugador2 = 0;
    private int tiempoRestante; // Tiempo restante en segundos
    private int rondaActual = 1; // Ronda inicial
    private final int maxRondas = 5; // Número máximo de rondas
    private boolean primeraRondaDeColocacion = true; // Control de primera ronda
    private Timer timer; // Temporizador para cuenta regresiva
    private JButton[][] botones; // Matriz de botones para el tablero
    private Board board; // Tablero lógico
    private Element selectedPlant = null; // Planta seleccionada para colocar
    private Element selectedZombie = null; // Zombie seleccionado para colocar
    private boolean removeMode = false; // Modo "quitar elemento"

    
    private RulePVP reglas; // Instancia de reglas
    
    public VentanaJuegoPVP(String nombreJugador1, String nombreJugador2, int solesIniciales, int cerebrosIniciales, int duracionPartida, List<Element> plantasSeleccionadas, List<Element> zombiesSeleccionados) {
        super("POOBvsZombies - Player vs Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicialización de contadores globales
        tiempoRestante = 2 * 60; // La primera ronda de colocación dura 2 minutos
        contadorSolesJugador1 = solesIniciales;
        contadorCerebrosJugador2 = cerebrosIniciales;
        board = new Board(5, 10); // Inicializamos el tablero lógico

        // Inicialización de las reglas del juego
        reglas = new RulePVP(duracionPartida);
        
        // Crear barra de menús
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("File");
        menuBar.add(menuArchivo);

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuArchivo.add(menuItemExit);

        menuItemExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                stopTimers();
                dispose();
            }
        });

        // Panel principal con imagen de fondo
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon fondo = new ImageIcon(getClass().getResource("/presentation/images/windows/fondojuego.jpg"));
                g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(panelPrincipal);

        // Panel superior para infos y jugadores
        JPanel panelNorte = new JPanel(new GridLayout(1, 14));
        panelNorte.setOpaque(false);
        panelNorte.setPreferredSize(new Dimension(0, 150));

        JLabel[] contadorSolesLabelRef = new JLabel[1];
        JPanel panelJugador1 = createPlayerPanel(
                nombreJugador1, "/presentation/images/images_Plants/sol.png", contadorSolesJugador1, 80, new Color(245, 245, 220), contadorSolesLabelRef
        );
        contadorSolesJugador1Label = contadorSolesLabelRef[0];
        panelNorte.add(panelJugador1);

        for (Element planta : plantasSeleccionadas) {
            JButton boton = createButtonWithDynamicImage(planta.getImagePath(), new Color(205, 133, 63));
            boton.addActionListener(e -> {
                selectedPlant = planta;
                selectedZombie = null;
            });
            panelNorte.add(boton);
        }

        puntosSolesLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Plants/puntos.png",
                String.valueOf(puntosSoles), 150, new Color(0, 0, 0, 128)
        );
        panelNorte.add(puntosSolesLabel);

        puntosCerebrosLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Zombies/puntoszombies.png",
                String.valueOf(puntosCerebros), 150, new Color(0, 0, 0, 128)
        );
        panelNorte.add(puntosCerebrosLabel);

        for (Element zombie : zombiesSeleccionados) {
            JButton boton = createButtonWithDynamicImage(zombie.getImagePath(), new Color(70, 130, 180));
            boton.addActionListener(e -> {
                selectedZombie = zombie;
                selectedPlant = null;
            });
            panelNorte.add(boton);
        }

        JLabel[] contadorCerebrosLabelRef = new JLabel[1];
        JPanel panelJugador2 = createPlayerPanel(
                nombreJugador2, "/presentation/images/images_Zombies/cerebro.png", contadorCerebrosJugador2, 80, new Color(245, 245, 220), contadorCerebrosLabelRef
        );
        contadorCerebrosJugador2Label = contadorCerebrosLabelRef[0];
        panelNorte.add(panelJugador2);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Panel central para el tablero
        JPanel panelCentro = new JPanel(new GridLayout(5, 10));
        panelCentro.setOpaque(false);
        botones = new JButton[5][10];

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 10; col++) {
                String imagePath = (col == 0) ?
                        "/presentation/images/images_Plants/pisocasa.png" :
                        (col == 9) ?
                                "/presentation/images/images_Zombies/padimento_zombies.png" :
                                "/presentation/images/images_Plants/pasto.png";

                JButton boton = createButtonWithDynamicImage(imagePath, new Color(0, 100, 0));
                botones[fila][col] = boton;

                final int f = fila, c = col;
                boton.addActionListener(e -> placeElementOnBoard(f, c));
                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior para controles adicionales
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setPreferredSize(new Dimension(0, 120));

        JButton info15 = createButtonWithFixedImage("/presentation/images/images_Plants/pala.png", new Color(139, 69, 19), 100);
        info15.addActionListener(e -> removeMode = true);
        panelSur.add(info15, BorderLayout.WEST);

        JButton menuButton = createButtonWithFixedImage("/presentation/images/windows/menujuego.png", new Color(245, 245, 220), 100);
        panelSur.add(menuButton, BorderLayout.EAST);

        JButton nextRoundButton = new JButton("Next Round");
        nextRoundButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextRoundButton.setBackground(new Color(0, 128, 0));
        nextRoundButton.setForeground(Color.WHITE);
        nextRoundButton.addActionListener(e -> avanzarRonda());
        panelSur.add(nextRoundButton, BorderLayout.SOUTH);

        JPanel panelInfoSur = new JPanel(new GridLayout(3, 1));
        panelInfoSur.setOpaque(false);

        JLabel labelDuracion = new JLabel("Duration: " + duracionPartida + " minutes", SwingConstants.CENTER);
        labelDuracion.setFont(new Font("Arial", Font.BOLD, 16));
        labelDuracion.setForeground(Color.WHITE);

        labelTemporizador = new JLabel("Time left: " + formatTime(tiempoRestante), SwingConstants.CENTER);
        labelTemporizador.setFont(new Font("Arial", Font.BOLD, 48));
        labelTemporizador.setForeground(Color.WHITE);

        labelRonda = new JLabel("Round: " + rondaActual, SwingConstants.CENTER);
        labelRonda.setFont(new Font("Arial", Font.BOLD, 16));
        labelRonda.setForeground(Color.WHITE);

        panelInfoSur.add(labelDuracion);
        panelInfoSur.add(labelTemporizador);
        panelInfoSur.add(labelRonda);
        panelSur.add(panelInfoSur, BorderLayout.CENTER);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        startTimers();
        setVisible(true);
    }

    private void startTimers() {
        timer = new Timer(1000, e -> {
            if (!reglas.isGameOver()) {
                reglas.advanceTime(); // Avanza el tiempo en las reglas
                labelTemporizador.setText(formatTime(reglas.getRemainingMinutes() * 60)); // Actualiza el temporizador
                labelRonda.setText("Round: " + reglas.getCurrentRound()); // Actualiza la ronda actual

                // Detecta cambio de fase
                if (reglas.isPhaseChange()) {
                    JOptionPane.showMessageDialog(this, "Phase changed to: " + 
                        (reglas.isPlantTurn() ? "Planting Phase" : "Zombie Attack Phase"));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Game Over! All rounds completed.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                timer.stop();
            }
        });
        timer.start();
    }


    private void avanzarRonda() {
        reglas.skipPhase(); // Avanza la fase o ronda manualmente
        labelTemporizador.setText(formatTime(reglas.getRemainingMinutes() * 60));
        labelRonda.setText("Round: " + reglas.getCurrentRound());
    }


    private void stopTimers() {
        if (timer != null) timer.stop();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    
    private void placeElementOnBoard(int fila, int col) {
        if (removeMode) {
            // Eliminar el contenido de la celda si está en modo de eliminación
            if (!board.isCellEmpty(fila, col)) {
                board.setCellContent(fila, col, null);
                botones[fila][col].removeAll();
                botones[fila][col].revalidate();
                botones[fila][col].repaint();
            }
            removeMode = false;
            return;
        }

        Element selectedElement = (selectedPlant != null) ? selectedPlant : selectedZombie;

        if (selectedElement == null) {
            JOptionPane.showMessageDialog(this, "No element selected.");
            return;
        }

        // Verificar la validez de la colocación según el tipo de elemento
        boolean validPlacement = selectedPlant != null 
            ? reglas.isPlantPlacementValid(fila, col)
            : reglas.isZombiePlacementValid(fila, col);

        if (!validPlacement) {
            JOptionPane.showMessageDialog(this, "Invalid placement for this element.");
            return;
        }

        // Colocar el contenido en el tablero lógico
        board.setCellContent(fila, col, selectedElement.getName());

        // Mostrar el GIF en el botón correspondiente
        try {
            ImageIcon elementIcon = new ImageIcon(getClass().getResource(selectedElement.getBoardImagePath()));
            JLabel gifLabel = new JLabel(elementIcon);
            gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gifLabel.setVerticalAlignment(SwingConstants.CENTER);

            int cellWidth = botones[fila][col].getWidth();
            int cellHeight = botones[fila][col].getHeight();
            int newWidth = (int) (cellWidth * 0.75);
            int newHeight = (int) (cellHeight * 0.75);

            Image resizedGif = elementIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
            gifLabel.setIcon(new ImageIcon(resizedGif));

            botones[fila][col].removeAll();
            botones[fila][col].add(gifLabel);
            botones[fila][col].revalidate();
            botones[fila][col].repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying element image.");
        }
    }




    
    private JPanel createPlayerPanel(String playerName, String imagePath, int initialCount, int iconSize, Color backgroundColor, JLabel[] counterLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel.setText("Image");
        }
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel counter = new JLabel(String.valueOf(initialCount), SwingConstants.CENTER);
        counter.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(counter, BorderLayout.SOUTH);

        counterLabel[0] = counter;
        return panel;
    }

    private JButton createButtonWithDynamicImage(String imagePath, Color background) {
        JButton button = new JButton();
        button.setBackground(background);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            button.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (button.getWidth() > 0 && button.getHeight() > 0) {
                        Image scaledImage = icon.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledImage));
                    }
                }
            });
        } catch (Exception e) {
            button.setText("Error");
        }
        return button;
    }

    private JLabel createSquareLabelWithImageAndCenteredText(String imagePath, String text, int size, Color backgroundColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(backgroundColor);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            label.setText("Error");
        }

        label.setPreferredSize(new Dimension(size, size));
        return label;
    }

    private JButton createButtonWithFixedImage(String imagePath, Color background, int size) {
        JButton button = new JButton();
        button.setBackground(background);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            button.setText("Error");
        }
        return button;
    }

  

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Element> plantasSeleccionadas = Arrays.asList(
                        new Element("Sunflower", "/presentation/images/images_Plants/girasolcarta.png", "/presentation/images/images_Plants/girasol1.gif"),
                        new Element("Peashooter", "/presentation/images/images_Plants/tiracarta.png", "/presentation/images/images_Plants/tira1.gif")
                );

                List<Element> zombiesSeleccionados = Arrays.asList(
                        new Element("Basic Zombie", "/presentation/images/images_Zombies/basicocarta.png", "/presentation/images/images_Zombies/basic1.gif")
                );

                new VentanaJuegoPVP("Player 1", "Player 2", 200, 300, 5, plantasSeleccionadas, zombiesSeleccionados);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}