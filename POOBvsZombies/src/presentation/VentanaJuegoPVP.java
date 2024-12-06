package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

    public VentanaJuegoPVP(String nombreJugador1, String nombreJugador2, int solesIniciales, int cerebrosIniciales, int duracionPartida, List<String> plantasSeleccionadas, List<String> zombiesSeleccionados) {
        super("POOBvsZombies - Player vs Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicialización de contadores globales
        tiempoRestante = duracionPartida * 60; // Convertimos duración en minutos a segundos
        contadorSolesJugador1 = solesIniciales;
        contadorCerebrosJugador2 = cerebrosIniciales;

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
        JPanel panelJugador1 = createPlayerPanel(
                nombreJugador1, "/presentation/images/images_Plants/sol.png", contadorSolesJugador1, 80, new Color(245, 245, 220)
        );
        panelNorte.add(panelJugador1);

        contadorSolesJugador1Label = (JLabel) panelJugador1.getComponent(2);

        // Infos 2 a 6: Plantas seleccionadas en `VentanaPVP`
        for (int i = 0; i < 5; i++) {
            String plantaSeleccionada = i < plantasSeleccionadas.size() ? plantasSeleccionadas.get(i) : null;
            String rutaImagen = plantaSeleccionada != null ? switch (plantaSeleccionada) {
                case "Sunflower" -> "/presentation/images/images_Plants/girasolcarta.png";
                case "Peashooter" -> "/presentation/images/images_Plants/tiracarta.png";
                case "Wall-nut" -> "/presentation/images/images_Plants/nuescarta.png";
                case "Potato Mine" -> "/presentation/images/images_Plants/pumcarta.png";
                case "ECIplant" -> "/presentation/images/images_Plants/ecicarta.png";
                default -> null;
            } : "/presentation/images/images_Plants/slot.png";
            JButton boton = createButtonWithDynamicImage(rutaImagen, new Color(205, 133, 63));
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

        // Infos 9 a 13: Zombis seleccionados en `VentanaPVP`
        for (int i = 0; i < 5; i++) {
            String zombieSeleccionado = i < zombiesSeleccionados.size() ? zombiesSeleccionados.get(i) : null;
            String rutaImagen = zombieSeleccionado != null ? switch (zombieSeleccionado) {
                case "Basic Zombie" -> "/presentation/images/images_Zombies/basicocarta.png";
                case "Conehead" -> "/presentation/images/images_Zombies/conocarta.png";
                case "Buckethead" -> "/presentation/images/images_Zombies/cubetacarta.png";
                case "Brainstein" -> "/presentation/images/images_Zombies/cerebrocarta.png";
                case "ECIZombie" -> "/presentation/images/images_Zombies/bombacarta.png";
                default -> null;
            } : "/presentation/images/images_Zombies/entrada.png";
            JButton boton = createButtonWithDynamicImage(rutaImagen, new Color(70, 130, 180));
            panelNorte.add(boton);
        }

        // Info 14: Jugador 2 con imagen y contador
        JPanel panelJugador2 = createPlayerPanel(
                nombreJugador2, "/presentation/images/images_Zombies/cerebro.png", contadorCerebrosJugador2, 80, new Color(245, 245, 220)
        );
        panelNorte.add(panelJugador2);

        contadorCerebrosJugador2Label = (JLabel) panelJugador2.getComponent(2);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Panel central (CENTRO)
        JPanel panelCentro = new JPanel(new GridLayout(5, 10));
        panelCentro.setOpaque(false);
        JButton[][] botones = new JButton[5][10];

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 10; col++) {
                String imagePath = (col == 0) ?
                        "/presentation/images/images_Plants/pisocasa.png" :
                        (col == 9) ?
                                "/presentation/images/images_Zombies/padimento_zombies.png" :
                                "/presentation/images/images_Plants/pasto.png";

                JButton boton = createButtonWithDynamicImage(imagePath, new Color(0, 100, 0));
                botones[fila][col] = boton;
                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior (SUR)
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setPreferredSize(new Dimension(0, 120));

        // Info 15: Pala
        JButton info15 = createButtonWithFixedImage("/presentation/images/images_Plants/pala.png", new Color(139, 69, 19), 100);
        panelSur.add(info15, BorderLayout.WEST);

        // Botón de menú
        JButton menuButton = createButtonWithFixedImage("/presentation/images/windows/menujuego.png", new Color(245, 245, 220), 100);
        panelSur.add(menuButton, BorderLayout.EAST);

        JPanel panelInfoSur = new JPanel(new GridLayout(2, 1));
        panelInfoSur.setOpaque(false);

        JLabel labelDuracion = new JLabel("Duration: " + duracionPartida + " minutes", SwingConstants.CENTER);
        labelDuracion.setFont(new Font("Arial", Font.BOLD, 16));
        labelDuracion.setForeground(Color.WHITE);

        labelTemporizador = new JLabel("Time left: " + formatTime(tiempoRestante), SwingConstants.CENTER);
        labelTemporizador.setOpaque(false);
        labelTemporizador.setForeground(Color.WHITE);
        labelTemporizador.setFont(new Font("Arial", Font.BOLD, 48));

        panelInfoSur.add(labelDuracion);
        panelInfoSur.add(labelTemporizador);
        panelSur.add(panelInfoSur, BorderLayout.CENTER);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        startTimers(); // Inicia los temporizadores
        setVisible(true);
    }

    private void startTimers() {
        // Temporizador del tiempo de juego
        timer = new Timer(1000, e -> {
            if (tiempoRestante > 0) {
                tiempoRestante--;
                labelTemporizador.setText("Time left: " + formatTime(tiempoRestante));
            } else {
                stopTimers();
                JOptionPane.showMessageDialog(this, "Time's up!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.start();

        // Temporizador de Info 1 (Soles del jugador 1)
        timerSolesJugador1 = new Timer(10000, e -> {
            contadorSolesJugador1 += 25;
            contadorSolesJugador1Label.setText(String.valueOf(contadorSolesJugador1));
        });
        timerSolesJugador1.start();

        // Temporizador de Info 14 (Cerebros del jugador 2)
        timerCerebrosJugador2 = new Timer(10000, e -> {
            contadorCerebrosJugador2 += 50;
            contadorCerebrosJugador2Label.setText(String.valueOf(contadorCerebrosJugador2));
        });
        timerCerebrosJugador2.start();
    }

    private void stopTimers() {
        if (timer != null) timer.stop();
        if (timerSolesJugador1 != null) timerSolesJugador1.stop();
        if (timerCerebrosJugador2 != null) timerCerebrosJugador2.stop();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private JPanel createPlayerPanel(String playerName, String imagePath, int initialCount, int iconSize, Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(backgroundColor);

        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Image not found: " + e.getMessage());
            imageLabel.setText("Image");
        }
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel counterLabel = new JLabel(String.valueOf(initialCount), SwingConstants.CENTER);
        counterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(counterLabel, BorderLayout.SOUTH);

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
            System.out.println("Image not found: " + e.getMessage());
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
            System.out.println("Image not found: " + e.getMessage());
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
            System.out.println("Image not found: " + e.getMessage());
        }
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuegoPVP(
                "Player 1", "Player 2", 200, 300, 5, List.of("Sunflower", "Peashooter"), List.of("Basic Zombie", "Conehead")
        ));
    }
}
