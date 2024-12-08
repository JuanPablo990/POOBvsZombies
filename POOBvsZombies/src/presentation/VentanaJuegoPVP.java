package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

public class VentanaJuegoPVP extends JFrame {

    private JLabel puntosSolesLabel;
    private JLabel puntosCerebrosLabel;
    private JLabel contadorSolesJugador1Label;
    private JLabel contadorCerebrosJugador2Label;
    private JLabel labelTemporizador; // Label para el temporizador
    private int puntosSoles = 0;
    private int puntosCerebros = 0;
    private int contadorSolesJugador1 = 0;
    private int contadorCerebrosJugador2 = 0;
    private int tiempoRestante; // Tiempo restante en segundos
    private Timer timer; // Temporizador para cuenta regresiva
    private Timer timerSolesJugador1; // Temporizador para sumar soles a Info 1
    private Timer timerCerebrosJugador2; // Temporizador para sumar cerebros a Info 14
    private JButton[][] botones;
    private Board board; // Tablero lógico
    private Element selectedPlant = null; // Planta seleccionada para colocar

    public VentanaJuegoPVP(String nombreJugador1, String nombreJugador2, int solesIniciales, int cerebrosIniciales, int duracionPartida, List<Element> plantasSeleccionadas, List<Element> zombiesSeleccionados) {
        super("POOBvsZombies - Player vs Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicialización de contadores globales
        tiempoRestante = duracionPartida * 60; // Convertimos duración en minutos a segundos
        contadorSolesJugador1 = solesIniciales;
        contadorCerebrosJugador2 = cerebrosIniciales;
        board = new Board(5, 10); // Inicializamos el tablero lógico

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

        // Panel superior (NORTE)
        JPanel panelNorte = new JPanel(new GridLayout(1, 14));
        panelNorte.setOpaque(false);
        panelNorte.setPreferredSize(new Dimension(0, 150));

        // Info 1: Jugador 1 con imagen y contador
        JLabel[] contadorSolesLabelRef = new JLabel[1];
        JPanel panelJugador1 = createPlayerPanel(
                nombreJugador1, "/presentation/images/images_Plants/sol.png", contadorSolesJugador1, 80, new Color(245, 245, 220), contadorSolesLabelRef
        );
        contadorSolesJugador1Label = contadorSolesLabelRef[0];
        panelNorte.add(panelJugador1);

        // Infos 2 a 6: Plantas seleccionadas
        for (Element planta : plantasSeleccionadas) {
            JButton boton = createButtonWithDynamicImage(planta.getImagePath(), new Color(205, 133, 63));
            boton.addActionListener(e -> selectedPlant = planta);
            panelNorte.add(boton);
        }

        // Info 7: Contador de soles global independiente
        puntosSolesLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Plants/puntos.png",
                String.valueOf(puntosSoles), 150, new Color(0, 0, 0, 128)
        );
        panelNorte.add(puntosSolesLabel);

        // Info 8: Contador de cerebros global independiente
        puntosCerebrosLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Zombies/puntoszombies.png",
                String.valueOf(puntosCerebros), 150, new Color(0, 0, 0, 128)
        );
        panelNorte.add(puntosCerebrosLabel);

        // Infos 9 a 13: Zombies seleccionados
        for (Element zombie : zombiesSeleccionados) {
            JButton boton = createButtonWithDynamicImage(zombie.getImagePath(), new Color(70, 130, 180));
            panelNorte.add(boton);
        }

        // Info 14: Jugador 2 con imagen y contador
        JLabel[] contadorCerebrosLabelRef = new JLabel[1];
        JPanel panelJugador2 = createPlayerPanel(
                nombreJugador2, "/presentation/images/images_Zombies/cerebro.png", contadorCerebrosJugador2, 80, new Color(245, 245, 220), contadorCerebrosLabelRef
        );
        contadorCerebrosJugador2Label = contadorCerebrosLabelRef[0];
        panelNorte.add(panelJugador2);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Panel central (CENTRO)
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
                boton.addActionListener(e -> placePlantOnBoard(f, c));
                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior (SUR)
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setPreferredSize(new Dimension(0, 120));

        JButton info15 = createButtonWithFixedImage("/presentation/images/images_Plants/pala.png", new Color(139, 69, 19), 100);
        panelSur.add(info15, BorderLayout.WEST);

        JButton menuButton = createButtonWithFixedImage("/presentation/images/windows/menujuego.png", new Color(245, 245, 220), 100);
        panelSur.add(menuButton, BorderLayout.EAST);

        JPanel panelInfoSur = new JPanel(new GridLayout(2, 1));
        panelInfoSur.setOpaque(false);

        JLabel labelDuracion = new JLabel("Duration: " + duracionPartida + " minutes", SwingConstants.CENTER);
        labelDuracion.setFont(new Font("Arial", Font.BOLD, 16));
        labelDuracion.setForeground(Color.WHITE);

        labelTemporizador = new JLabel("Time left: " + formatTime(tiempoRestante), SwingConstants.CENTER);
        labelTemporizador.setFont(new Font("Arial", Font.BOLD, 48));
        labelTemporizador.setForeground(Color.WHITE);

        panelInfoSur.add(labelDuracion);
        panelInfoSur.add(labelTemporizador);
        panelSur.add(panelInfoSur, BorderLayout.CENTER);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        startTimers();
        setVisible(true);
    }

    private void placePlantOnBoard(int fila, int col) {
        if (selectedPlant != null && board.isCellEmpty(fila, col)) {
            board.setCellContent(fila, col, selectedPlant.getName());

            // Escalar la imagen de la planta y centrarla
            try {
                ImageIcon plantIcon = new ImageIcon(getClass().getResource(selectedPlant.getBoardImagePath()));
                Image scaledImage = plantIcon.getImage().getScaledInstance(
                        botones[fila][col].getWidth() / 2,  // La mitad del ancho del botón
                        botones[fila][col].getHeight() / 2, // La mitad de la altura del botón
                        Image.SCALE_SMOOTH
                );

                // Asignar el icono escalado
                botones[fila][col].setIcon(new ImageIcon(scaledImage));
                botones[fila][col].setHorizontalAlignment(SwingConstants.CENTER);
                botones[fila][col].setVerticalAlignment(SwingConstants.CENTER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopTimers() {
        if (timer != null) timer.stop();
        if (timerSolesJugador1 != null) timerSolesJugador1.stop();
        if (timerCerebrosJugador2 != null) timerCerebrosJugador2.stop();
    }

    private void startTimers() {
        timer = new Timer(1000, e -> {
            if (tiempoRestante > 0) {
                tiempoRestante--;
                labelTemporizador.setText(formatTime(tiempoRestante));
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time's up!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.start();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
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
                        new Element("Sunflower", "/presentation/images/images_Plants/girasolcarta.png", "/presentation/images/images_Plants/girasol1.png"),
                        new Element("Peashooter", "/presentation/images/images_Plants/tiracarta.png", "/presentation/images/images_Plants/tira1.png"),
                        new Element("Wall-nut", "/presentation/images/images_Plants/nuescarta.png", "/presentation/images/images_Plants/nuez1.png"),
                        new Element("Potato Mine", "/presentation/images/images_Plants/pumcarta.png", "/presentation/images/images_Plants/pum.png"),
                        new Element("ECIplant", "/presentation/images/images_Plants/ecicarta.png", "/presentation/images/images_Plants/eci.png")
                );

                List<Element> zombiesSeleccionados = Arrays.asList(
                        new Element("Basic Zombie", "/presentation/images/images_Zombies/basicocarta.png", "/presentation/images/images_Zombies/basic1.png"),
                        new Element("Conehead", "/presentation/images/images_Zombies/conocarta.png", "/presentation/images/images_Zombies/conehead1.png"),
                        new Element("Buckethead", "/presentation/images/images_Zombies/cubetacarta.png", "/presentation/images/images_Zombies/bucket1.png"),
                        new Element("Brainstein", "/presentation/images/images_Zombies/cerebrocarta.png", "/presentation/images/images_Zombies/brainstein1.png"),
                        new Element("ECIZombie", "/presentation/images/images_Zombies/bombacarta.png", "/presentation/images/images_Zombies/bomba1.png")
                );

                new VentanaJuegoPVP("Player 1", "Player 2", 200, 300, 5, plantasSeleccionadas, zombiesSeleccionados);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
