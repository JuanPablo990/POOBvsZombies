package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class VentanaJuegoPVP extends JFrame {

    private JLabel contadorSolesLabel;
    private JLabel contadorCerebrosLabel;
    private JLabel labelTemporizador; // Label para el temporizador
    private int contadorSoles = 0;
    private int contadorCerebros = 0;
    private int tiempoRestante; // Tiempo restante en segundos
    private Timer timer; // Temporizador

    public VentanaJuegoPVP(String nombreJugador1, String nombreJugador2, int solesIniciales, int cerebrosIniciales, int duracionPartida) {
        super("POOBvsZombies - Player vs Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicialización de contadores
        contadorSoles = solesIniciales;
        contadorCerebros = cerebrosIniciales;
        tiempoRestante = duracionPartida * 60; // Convertimos duración en minutos a segundos

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
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                dispose();
            }
        });

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        setContentPane(panelPrincipal);

        // Panel superior (NORTE)
        JPanel panelNorte = new JPanel(new GridLayout(1, 14));
        panelNorte.setPreferredSize(new Dimension(0, 150)); // Ajustamos altura

        // Info 1: Jugador 1 con imagen y contador
        JPanel panelJugador1 = createPlayerPanel(
                nombreJugador1, "/presentation/images/images_Plants/sol.png", solesIniciales, 80, new Color(245, 245, 220)
        );
        panelNorte.add(panelJugador1);

        // Infos 2 a 6: Botones con imágenes y colores personalizados
        for (int i = 2; i <= 6; i++) {
            Color backgroundColor = (i == 5) ? new Color(139, 69, 19) : new Color(205, 133, 63); // Info5 café
            JButton boton = createButtonWithDynamicImage("/presentation/images/images_Plants/maseta.png", backgroundColor);
            panelNorte.add(boton);
        }

        // Info 7: Contador de soles cuadrado con fondo verde
        contadorSolesLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Plants/puntos.png",
                String.valueOf(contadorSoles),
                150,
                new Color(0, 128, 0) // Fondo verde
        );
        panelNorte.add(contadorSolesLabel);

        // Info 8: Contador de cerebros cuadrado con fondo verde
        contadorCerebrosLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Zombies/puntoszombies.png",
                String.valueOf(contadorCerebros),
                150,
                new Color(0, 128, 0) // Fondo verde
        );
        panelNorte.add(contadorCerebrosLabel);

        // Infos 9 a 13: Botones con imagen de entrada y fondo azul
        for (int i = 9; i <= 13; i++) {
            JButton boton = createButtonWithDynamicImage("/presentation/images/images_Zombies/entrada.png", new Color(70, 130, 180));
            panelNorte.add(boton);
        }

        // Info 14: Jugador 2 con imagen y contador
        JPanel panelJugador2 = createPlayerPanel(
                nombreJugador2, "/presentation/images/images_Zombies/cerebro.png", cerebrosIniciales, 80, new Color(245, 245, 220)
        );
        panelNorte.add(panelJugador2);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Panel central (CENTRO)
        JPanel panelCentro = new JPanel(new GridLayout(5, 10));
        JButton[][] botones = new JButton[5][10];

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 10; col++) {
                JButton boton = new JButton();
                boton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                boton.setBackground(new Color(0, 100, 0)); // Verde oscuro
                boton.setOpaque(true);
                botones[fila][col] = boton;

                // Determinar la imagen según la posición
                String imagePath;
                if (col == 0) {
                    imagePath = "/presentation/images/images_Plants/pisocasa.png";
                } else if (col == 9) {
                    imagePath = "/presentation/images/images_Zombies/padimento_zombies.png";
                } else {
                    imagePath = "/presentation/images/images_Plants/pasto.png";
                }

                boton = createButtonWithDynamicImage(imagePath, new Color(0, 100, 0));
                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior (SUR)
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setPreferredSize(new Dimension(0, 120));

        // Info 15 con imagen de pala (tamaño fijo más grande y fondo café)
        JButton info15 = createButtonWithFixedImage("/presentation/images/images_Plants/pala.png", new Color(139, 69, 19), 100);
        panelSur.add(info15, BorderLayout.WEST);

        // Menú con imagen y fondo beige
        JButton menuButton = createButtonWithFixedImage("/presentation/images/windows/menujuego.png", new Color(245, 245, 220), 100);
        panelSur.add(menuButton, BorderLayout.EAST);

        // Información en el centro del panel inferior
        JPanel panelInfoSur = new JPanel(new GridLayout(2, 1));
        JLabel labelDuracion = new JLabel("Duration: " + duracionPartida + " minutes", SwingConstants.CENTER);
        labelTemporizador = new JLabel("Time left: " + formatTime(tiempoRestante), SwingConstants.CENTER);
        panelInfoSur.add(labelDuracion);
        panelInfoSur.add(labelTemporizador);
        panelSur.add(panelInfoSur, BorderLayout.CENTER);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        // Inicia el temporizador
        startTimer();

        setVisible(true);
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

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (tiempoRestante > 0) {
                tiempoRestante--;
                labelTemporizador.setText("Time left: " + formatTime(tiempoRestante));
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time's up!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuegoPVP("Player 1", "Player 2", 200, 300, 5));
    }
}
